package com.ssafy.smartstore.config

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.nfc.NfcAdapter
import android.util.Log
import com.ssafy.smartstore.src.main.api.FirebaseTokenApi
import com.ssafy.smartstore.src.main.intercepter.AddCookiesInterceptor
import com.ssafy.smartstore.src.main.intercepter.ReceivedCookiesInterceptor
import com.ssafy.smartstore.util.SharedPreferencesUtil
import com.ssafy.smartstore.util.ShoppingSharedPreference
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
        // ipconfig를 통해 ip확인하기
        // 핸드폰으로 접속은 같은 인터넷으로 연결 되어있어야함 (유,무선)
        const val SERVER_URL = "http://172.30.1.26:9999/"
        const val MENU_IMGS_URL = "${SERVER_URL}imgs/menu/"
        const val IMGS_URL = "${SERVER_URL}imgs/"

        lateinit var shoppingSharedPreference: ShoppingSharedPreference
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit
        lateinit var nfcAdapter: NfcAdapter

        // 모든 퍼미션 관련 배열
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000

        // FCM
        const val channel_id = "brewed_channel"
        var userToken = "none"

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
        shoppingSharedPreference = ShoppingSharedPreference(applicationContext)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS).build()

        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

}