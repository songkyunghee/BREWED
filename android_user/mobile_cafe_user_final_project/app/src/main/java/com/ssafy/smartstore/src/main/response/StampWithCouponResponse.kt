package com.ssafy.smartstore.src.main.response

import com.google.gson.annotations.SerializedName
import java.util.*

// o_id 기준으로 분류하고, img는 그 중 하나로 사용하기
data class StampWithCouponResponse(
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("id") val userId: String,
    @SerializedName("c_value") val cValue: Int,
    @SerializedName("c_id") val cId: Int,
    @SerializedName("order_id") val orderId: Int,
)