package com.ssafy.smartstore.src.main.response

import com.google.gson.annotations.SerializedName

data class OrderListResponse(
    @SerializedName("o_id") val o_id: Int,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("order_table") val order_table: String,
    @SerializedName("order_time") val order_time: String,
    @SerializedName("completed") val completed: String
)