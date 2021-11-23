package com.ssafy.smartstore.src.main.api

import com.ssafy.smartstore.src.main.dto.Noti
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.response.NotiListResponse
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import retrofit2.Call
import retrofit2.http.*

interface NotiApi {
    // 알람을 추가한다.
    @POST("rest/user/noti")
    fun insertNoti(@Body body: Noti): Call<Boolean>

    // {id}에 해당하는 알람을 삭제한다.
    @DELETE("rest/user/noti/{id}")
    fun deleteNoti(@Path("id") id: Int): Call<Boolean>

    // 사용자의 알람들을 반환한다.
    @GET("rest/user/noti/{id}")
    fun getNoti(@Path("id") id: String): Call<MutableList<NotiListResponse>>
}