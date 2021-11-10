package com.example.imageapi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "FavImage")
data class Image(
    @PrimaryKey()
    @ColumnInfo(name = "ID")
    val ID: String,
//    @ColumnInfo(name = "title")
//    var title: String,
    @ColumnInfo(name = "url")
    var url: String)
