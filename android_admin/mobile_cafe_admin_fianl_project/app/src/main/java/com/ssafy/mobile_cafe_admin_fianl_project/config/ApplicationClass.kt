package com.ssafy.mobile_cafe_admin_fianl_project.config

import android.app.Application
import android.util.Log
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.api.FirebaseTokenApi
import com.ssafy.mobile_cafe_admin_fianl_project.util.SharedPreferencesUtil
import com.ssafy.smartstore.src.main.intercepter.AddCookiesInterceptor
import com.ssafy.smartstore.src.main.intercepter.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_싸피"
class ApplicationClass : Application() {
    companion object{
        const val SERVER_URL = "http://192.168.219.107:9999/"
        const val MENU_IMGS_URL = "${SERVER_URL}imgs/menu/"
        const val IMGS_URL = "${SERVER_URL}imgs/"

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000

        // FCM
        const val channel_id = "brewed_admin_channel"
        var adminToken = "none"

        // 완료 주문 조회할 날짜
        var dateString = ""

        fun uploadToken(token: String) {
            // 새로운 토큰 수신 시 서버로 전송
            val brewedService = retrofit.create(FirebaseTokenApi::class.java)
            brewedService.uploadToken(token).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful){
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }


    }


    override fun onCreate() {
        super.onCreate()

        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS).build()

        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}