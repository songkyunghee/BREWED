package com.ssafy.smartstore.src.main.response

import com.google.gson.annotations.SerializedName

data class MenuDetailWithCommentResponse (
    @SerializedName("img") var productImg: String,
    @SerializedName("avg") var productRatingAvg: Double,
    @SerializedName("user_id") var userId: String?,
    @SerializedName("sells") var productTotalSellCnt: Int,
    @SerializedName("price") var productPrice: Int,
    @SerializedName("name") var productName: String,
    @SerializedName("rating") var productRating: Float,
    @SerializedName("commentId") var commentId: Int = -1,
    @SerializedName("comment") var commentContent: String?,
    @SerializedName("userName") var commentUserName: String?,
    @SerializedName("commentCnt") var productCommentTotalCnt: Int,
    @SerializedName("type") var type: String
)
