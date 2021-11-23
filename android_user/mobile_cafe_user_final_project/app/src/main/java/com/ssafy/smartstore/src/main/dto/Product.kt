package com.ssafy.smartstore.src.main.dto

data class Product (
    val id: Int,
    val koname: String,
    val name: String,
    val type: String,
    val price: Int,
    val img: String,
    val salesvolume: Int,
    val comment: ArrayList<Comment> = ArrayList()
) {
    constructor(): this(0, "", "","",0,"",0)
}
