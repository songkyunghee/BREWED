package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.util.Log
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil
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
    fun getDateNotComOrderList(date: String, storeId: String, callback: RetrofitCallback<ArrayList<OrderListResponse>>): List<OrderListResponse> {
        var resData = ArrayList<OrderListResponse>()

        val request: Call<List<OrderListResponse>> = RetrofitUtil.orderService.getDateNotComOrderList(date,storeId)

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
}