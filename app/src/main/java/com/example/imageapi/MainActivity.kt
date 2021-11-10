package com.example.imageapi

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//https://picsum.photos/v2/list
class MainActivity : AppCompatActivity() {
    //xml
    lateinit var myRv: RecyclerView
    lateinit var imageView: ImageView
    lateinit var llBottom: LinearLayout

    // array of images -- Image class
    var images = ArrayList<Image>()
    var Favimages = listOf<Image>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //xml
        imageView = findViewById(R.id.imageView)
        llBottom = findViewById(R.id.llBottom)
        myRv = findViewById(R.id.recyclerView)
        // recycler view
        //context , array
        myRv.adapter = RecyclerViewAdapter(this, images,Favimages)
        myRv.layoutManager = LinearLayoutManager(this)
                fetchData()
        imageView.setOnClickListener { closeImage() }
    }

    fun fetchData() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        // clear previous search
        images.clear()
        //progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        val call: Call<List<ImageDetails.Photos>> = apiInterface!!.getPhoto()
        call?.enqueue(object : Callback<List<ImageDetails.Photos>> {
            override fun onResponse(
                    call: Call<List<ImageDetails.Photos>>,
                    response: Response<List<ImageDetails.Photos>>
            ) {
                progressDialog.dismiss()
               // val resource = response.body()
                val resource: List<ImageDetails.Photos>? = response.body()
                val datumList = resource
                for (i in datumList!!) {
                    images.add(Image(i.id!!, i.url!!))
                }
                myRv.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ImageDetails.Photos>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
            }
        })
    }

    //open image glide
    fun openImage(link: String) {
        Glide.with(this)
                .load(link)
                .into(imageView)
        imageView.isVisible = true
        myRv.isVisible = false
        llBottom.isVisible = false
    }

    // close image
    private fun closeImage() {
        imageView.isVisible = false
        myRv.isVisible = true
        llBottom.isVisible = true
    }

    fun addFav(id:String ,link:String){
        //Favimages.add(Image(id,title,link))
        val s =Image(id,link)
        CoroutineScope(Dispatchers.IO).launch {
            ImageDatabase.getInstance(applicationContext).ImageDao().insertImage(s)
        }
        Toast.makeText(applicationContext, "added successfully! ", Toast.LENGTH_SHORT)
                .show()
        // showFav()
    }

    fun showFav() {
        CoroutineScope(Dispatchers.IO).launch {
            Favimages = ImageDatabase.getInstance(applicationContext).ImageDao()
                    .getImage()
            withContext(Dispatchers.Main) {
                //recycler view
                myRv.adapter = RecyclerViewAdapter2(this@MainActivity, Favimages)
                myRv.layoutManager = LinearLayoutManager(this@MainActivity)
                llBottom.isVisible = false
                imageView.isVisible = false
            }
        }
    }
    fun removeFav(id:String ,link:String){
        //Favimages.remove(Image(id,title,link))
        val del = Image(id,link)
        CoroutineScope(Dispatchers.IO).launch {
            ImageDatabase.getInstance(applicationContext).ImageDao().deleteImage(del)
        }
        Toast.makeText(applicationContext, "removed successfully! ", Toast.LENGTH_SHORT)
                .show()
        //retrieve data and update recycler view
        showFav()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.m1 -> {
                if(llBottom.isVisible==false){
                    myRv.adapter = RecyclerViewAdapter(this, images, Favimages)
                    myRv.layoutManager = LinearLayoutManager(this)
                    myRv.isVisible = true
                    llBottom.isVisible = true
                }else {
                    showFav()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}