package com.ssafy.smartstore.src.main.dto

data class User (
    val id: String,
    val name: String,
    val pass: String,
    val stamps: Int,
    var token: String,
    val stampList: ArrayList<Stamp> = ArrayList()
) {
    constructor() : this("", "", "", 0, "")
    constructor(id: String, pass: String) : this(id, "", pass, 0, "")
    constructor(id: String) : this(id, "", "", 0, "")
}