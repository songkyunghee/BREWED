package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil.Companion.orderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "OrderService_싸피"
class OrderService {

    // getDateComOrderList
    fun getDateComOrderList(date: String, completed: String, storeId: String, callback: RetrofitCallback<ArrayList<OrderListResponse>>): List<OrderListResponse> {
        var resData = ArrayList<OrderListResponse>()

        val request: Call<List<OrderListResponse>> = RetrofitUtil.orderService.getDateComOrderList(date,completed,storeId)

        request.enqueue(object: Callback<List<OrderListResponse>> {
            override fun onResponse(call: Call<List<OrderListResponse>>, response: Response<List<OrderListResponse>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        res.forEach {
                            resData.add(it)
                        }
                    }
                    callback.onSuccess(response.code(), resData)
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<OrderListResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
                callback.onError(t)
            }
        })

        return resData
    }

    // getDateComOrderList
    fun getDateNotComOrderList(date: String, storeId: String): LiveData<MutableList<OrderListResponse>> {
        val responseLiveData: MutableLiveData<MutableList<OrderListResponse>> = MutableLiveData()

        val request: Call<MutableList<OrderListResponse>> = orderService.getDateNotComOrderList(date,storeId)

        request.enqueue(object: Callback<MutableList<OrderListResponse>> {
            override fun onResponse(call: Call<MutableList<OrderListResponse>>, response: Response<MutableList<OrderListResponse>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        responseLiveData.value = res
                    }
                    Log.d(TAG, "onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<OrderListResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })

        return responseLiveData
    }

    fun update(order: Order) {
        orderService.update(order).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() == 200) {
                    Log.d(TAG, "onResponse: 주문 수정 성공")
                } else {
                    Log.d(TAG, "onResponse: 주문 수정 실패")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: 통신오류")
            }

        })
    }
}