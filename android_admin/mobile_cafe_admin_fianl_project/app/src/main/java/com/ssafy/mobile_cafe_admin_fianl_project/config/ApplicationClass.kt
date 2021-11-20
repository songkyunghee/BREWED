package com.ssafy.mobile_cafe_admin_fianl_project.config

import android.app.Application
import com.ssafy.mobile_cafe_admin_fianl_project.util.SharedPreferencesUtil
import com.ssafy.smartstore.src.main.intercepter.AddCookiesInterceptor
import com.ssafy.smartstore.src.main.intercepter.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_싸피"
class ApplicationClass : Application() {
    companion object{
        const val SERVER_URL = "http://172.30.1.26:9999/"
        const val MENU_IMGS_URL = "${SERVER_URL}imgs/menu/"
        const val IMGS_URL = "${SERVER_URL}imgs/"

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000



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