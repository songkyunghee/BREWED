package com.ssafy.smartstore.src.main.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.util.RetrofitCallback
import com.ssafy.smartstore.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "StoreService_싸피"
class StoreService {
    fun getStoreId(beacon: String, callback: RetrofitCallback<String>) {
        Log.d(TAG, "getStoreId: start")
        RetrofitUtil.storeService.getStoreId(beacon).enqueue(object : Callback<HashMap<String, String>> {
            override fun onResponse(
                call: Call<HashMap<String, String>>,
                response: Response<HashMap<String, String>>
            ) {
                Log.d(TAG, "getStoreId: ${response.code()}")
                Log.d(TAG, "getStoreId: ${response.body()}")
                val res = response.body()

                if (response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res.get("s_id") ?: "0")
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        callback.onFailure(response.code())
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }

            }

            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                Log.d(TAG, t.message ?: "주문 상세 내역 받아오는 중 통신오류")
                callback.onError(t)
            }
        })
    }

    fun getBannerList(): LiveData<List<String>> {
        val responseLiveData: MutableLiveData<List<String>> = MutableLiveData()
        val request: Call<MutableList<String>> = RetrofitUtil.storeService.getBannerList()
        request.enqueue(object : Callback<MutableList<String>> {
            override fun onResponse(
                call: Call<MutableList<String>>,
                response: Response<MutableList<String>>
            ) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        responseLiveData.value = res
                        Log.d(TAG, "onResponse: $res")
                    }
                    Log.d(TAG, "banner onResponse: success")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })
        return responseLiveData
    }

}