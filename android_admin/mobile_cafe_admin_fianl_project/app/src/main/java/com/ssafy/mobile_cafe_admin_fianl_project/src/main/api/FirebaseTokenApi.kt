package com.ssafy.mobile_cafe_admin_fianl_project.src.main.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseTokenApi {

    // Token 정보 서버로 전송
    @POST("token")
    fun uploadToken(@Query("token") token: String): Call<String>

    // 소유하고 있는 모든 Token에 BroadCasting.
    @POST("/broadcast")
    fun broadcastMsg(@Query("title") title: String, @Query("body") body: String): Call<Int>

    // Target Token에 Send Message.
    @POST("/sendMessageTo")
    fun sendMessageTo(@Query("token") token: String, @Query("title") title: String, @Query("body") body: String): Call<Unit>

}