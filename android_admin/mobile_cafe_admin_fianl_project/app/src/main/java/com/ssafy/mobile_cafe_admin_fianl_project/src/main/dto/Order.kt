package com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

data class Order (
    var id: Int,
    var userId: String,
    var storeId: Int,
    var orderTable: String,
    var orderTime: String,
    var complited: String,
    val details: ArrayList<OrderDetail> = ArrayList() ){



    constructor(userId: String, storeId: Int, orderTable: String, complited: String, details: ArrayList<OrderDetail>)
            :this(0, userId, storeId, orderTable, "", complited, details)
    @RequiresApi(Build.VERSION_CODES.O)
    constructor():this(0,"",0, "","", "", arrayListOf())
}
