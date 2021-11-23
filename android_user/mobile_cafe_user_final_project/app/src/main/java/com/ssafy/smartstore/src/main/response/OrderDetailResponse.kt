package com.ssafy.smartstore.src.main.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class OrderDetailResponse(
    @SerializedName("o_id") val orderId: Int,
    @SerializedName("s_id") val storeId: Int,
    @SerializedName("order_table") val orderTable: String,
    @SerializedName("order_time") val orderDate: Date,
    @SerializedName("completed") val orderCompleted: Char='N',
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("p_id") val productId: Int,
    @SerializedName("name") val productName: String,
    @SerializedName("koname") val koname: String,
    @SerializedName("unitprice") val unitPrice: Int,
    @SerializedName("img") val img: String,
    @SerializedName("stamp") val stampCount: Int,
    @SerializedName("totalprice") val totalPrice: Int,
    @SerializedName("type") val productType: String
)