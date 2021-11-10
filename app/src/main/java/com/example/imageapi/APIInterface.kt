package com.example.imageapi

import android.provider.ContactsContract
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {
    @GET("list")
    fun getPhoto(): Call<List<ImageDetails.Photos>>

    //another ways
    /*
    @GET("/services/rest/?nojsoncallback=1")
    fun getPhoto(@Query("method")method: String,@Query("api_key")apiKey: String ,
             @Query("tags")tags: String , @Query("format")format: String ): Call<ImageDetails?>?
*/
/*
@GET("/services/rest/?=1")
fun getPhoto(@Query("method")method: String,@Query("api_key")apiKey: String ,
         @Query("tags")tags: String , @Query("format")format: String ,
         @Query("nojsoncallback") nojsoncallback: String): Call<ImageDetails?>?
*/
}