package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.util.Log
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil.Companion.pushService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PushService {

    fun broadcastMsg(title: String, body: String) {
        pushService.broadcastMsg(title, body).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Log.d(TAG, "onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
            }
        })
    }

    fun sendMessageTo(token: String, title: String, body: String) {

        pushService.sendMessageTo(token, title, body).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: success")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
            }

        })
    }
}