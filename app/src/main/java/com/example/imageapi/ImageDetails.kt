package com.example.imageapi

import com.google.gson.annotations.SerializedName

class ImageDetails {

    var photos: ArrayList<Photos>? = null

    class Photos {
        @SerializedName("id")
        var id: String? = null
        @SerializedName("download_url")
        var url: String? = null
    }
}