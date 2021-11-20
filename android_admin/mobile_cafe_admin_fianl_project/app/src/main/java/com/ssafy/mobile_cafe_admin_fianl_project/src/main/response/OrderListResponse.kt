package com.ssafy.mobile_cafe_admin_fianl_project.src.main.response

import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    @SerializedName("o_id") val o_id: Int,
    @SerializedName("s_id") val s_id: Int,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("order_table") val order_table: String,
    @SerializedName("order_time") val order_time: String,
    @SerializedName("completed") val completed: String,
    @SerializedName("img") val img: String
)