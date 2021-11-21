package com.ssafy.mobile_cafe_admin_fianl_project.src.main.response

import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    @SerializedName("o_id") var o_id: Int,
    @SerializedName("s_id") var s_id: Int,
    @SerializedName("user_id") var user_id: String,
    @SerializedName("order_table") var order_table: String,
    @SerializedName("order_time") var order_time: String,
    @SerializedName("completed") var completed: String,
    @SerializedName("img") var img: String,
    @SerializedName("quantity") var orderCnt: Int,
    @SerializedName("price") val productPrice: Int,
    @SerializedName("type") val type: String,
    @SerializedName("token") val token: String,
    var totalPrice: Int = 0
)