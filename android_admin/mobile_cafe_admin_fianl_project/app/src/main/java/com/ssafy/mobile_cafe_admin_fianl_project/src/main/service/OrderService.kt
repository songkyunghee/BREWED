package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderDetailResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil.Companion.orderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "OrderService_싸피"
class OrderService {

    // 주문 상세 내역 가져오는 API
    fun getOrderDetails(orderId: Int): LiveData<List<OrderDetailResponse>> {
        val responseLiveData: MutableLiveData<List<OrderDetailResponse>> = MutableLiveData()
        val orderDetailRequest: Call<List<OrderDetailResponse>> = RetrofitUtil.orderService.getOrderDetail(orderId)

        orderDetailRequest.enqueue(object : Callback<List<OrderDetailResponse>> {
            override fun onResponse(call: Call<List<OrderDetailResponse>>, response: Response<List<OrderDetailResponse>>) {
                val res = response.body()
                Log.d(TAG, "onResponse: $res")
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                    Log.d(TAG, "com order detail onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<OrderDetailResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "주문 상세 내역 받아오는 중 통신오류")
            }
        })

        return responseLiveData
    }


    // getDateComOrderList
    fun getDateComOrderList(date: String, completed: String, storeId: String): LiveData<MutableList<OrderListResponse>> {
        val responseLiveData: MutableLiveData<MutableList<OrderListResponse>> = MutableLiveData()

        val request: Call<List<OrderListResponse>> = RetrofitUtil.orderService.getDateComOrderList(date,completed,storeId)

        request.enqueue(object: Callback<List<OrderListResponse>> {
            override fun onResponse(call: Call<List<OrderListResponse>>, response: Response<List<OrderListResponse>>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res != null) {
                        responseLiveData.value = makeList(res)
                    }
                    Log.d(TAG, "com order onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<OrderListResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "날짜에 해당하는 완료된 주문 내역 받아오는 중 통신오류")
            }
        })

        return responseLiveData
    }


    private fun makeList(orderList: List<OrderListResponse>): MutableList<OrderListResponse> {
        val hm = HashMap<Int, OrderListResponse>()
        orderList.forEach{ order ->
            if(hm.containsKey(order.o_id)) {
                val tmp = hm[order.o_id]!!
                tmp.orderCnt += order.orderCnt
                tmp.totalPrice += order.productPrice * order.orderCnt
                hm[order.o_id] = tmp
            } else {
                order.totalPrice = order.productPrice * order.orderCnt
                hm[order.o_id] = order
            }
        }

        val list = ArrayList<OrderListResponse>(hm.values)
        return list
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
                    Log.d(TAG, "not order onResponse: $res")
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

    fun update(order: Order, callback: RetrofitCallback<Boolean>) {
        orderService.update(order).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(response.code() == 200) {
                    Log.d(TAG, "onResponse: 주문 수정 성공")
                    callback.onSuccess(response.code(), true)
                } else {
                    Log.d(TAG, "onResponse: 주문 수정 실패")
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: 통신오류")
                callback.onError(t)
            }

        })
    }
}