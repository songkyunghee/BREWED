package com.ssafy.smartstore.src.main.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class NotiListResponse(
    @SerializedName("user_id") val user_id: String,
    @SerializedName("n_msg") val n_msg: String,
    @SerializedName("noti_time") val noti_time: Date,
    @SerializedName("n_id") val n_id: Int
)