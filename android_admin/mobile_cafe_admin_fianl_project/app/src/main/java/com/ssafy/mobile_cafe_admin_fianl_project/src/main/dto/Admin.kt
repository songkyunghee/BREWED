package com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto

data class Admin(
    val aId: String,
    val sId: Int,
    val aName: String,
    val aPass: String,
    var aToken: String,
) {
    constructor(): this("", 0, "", "","")
    constructor(id:String, pass:String):this(id, 0,"", pass,"")
}
