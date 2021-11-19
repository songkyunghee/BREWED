package com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto

data class Admin(
    val id: String,
    val storeId: Int,
    val name: String,
    val pass: String
) {
    constructor(): this("", 0, "", "")
    constructor(id:String, pass:String):this(id, 0,"", pass)
}
