package com.ssafy.smartstore.src.main.service

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.ssafy.smartstore.src.main.dto.*
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.response.OrderDetailResponse
import com.ssafy.smartstore.src.main.response.OrderListResponse
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import com.ssafy.smartstore.util.MainViewModel
import com.ssafy.smartstore.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "OrderService_싸피"
class OrderService{


    // makeOrder
    fun makeOrder(body: Order, owner: ViewModelStoreOwner): Int{
        val mainViewModel = ViewModelProvider(owner).get(MainViewModel::class.java)
        val menuInfoRequest: Call<MutableList<StampWithCouponResponse>> = RetrofitUtil.userService.getInfo(User(body.userId))
        var nowCouponNum = -1
        Log.d(TAG, "makeOrder: $body")
        var result:Int = 0
        RetrofitUtil.orderService.makeOrder(body).enqueue(object:Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                Log.d(TAG, "TEST2:: onResponse: $res")
                Log.d(TAG, "TEST2:: onResponse: ${response.code()}")
                if(response.isSuccessful){
                    if (res != null) {
                        result =  res
                        Log.d(TAG, "TEST2:: onResponse: success $res")

                        // 조회하는 것을 호출...
                        menuInfoRequest.enqueue(object : Callback<MutableList<StampWithCouponResponse>> {
                            override fun onResponse(call: Call<MutableList<StampWithCouponResponse>>, response: Response<MutableList<StampWithCouponResponse>>) {
                                val res = response.body()
                                if(response.code() == 200){
                                    if (res != null) {
                                        nowCouponNum = res.size
                                        mainViewModel.setNowCouponNum(res.size)
                                        Log.d(TAG, "TEST2::사용자 쿠폰 스탬프 받아오는 중 데이터: ${res.size}")
                                        Log.d(TAG, "TEST2::onResponse: ------------")
                                    }
                                } else {
                                    Log.d(TAG, "TEST2::onResponse: Error Code ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<MutableList<StampWithCouponResponse>>, t: Throwable) {
                                Log.d(TAG, t.message ?: "TEST2::사용자 정보 받아오는 중 통신오류")
                            }
                        })

                    }
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })

        Log.d(TAG, "TEST2:: makeOrder: $nowCouponNum")
        return nowCouponNum
    }

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
                    Log.d(TAG, "onResponse: $res")
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

    // 최근 한달간 주문내역 가져오는 API
    fun getLastMonthOrder(userId: String): MutableLiveData<List<LatestOrderResponse>> {
        val responseLiveData: MutableLiveData<List<LatestOrderResponse>> = MutableLiveData()
        val latestOrderRequest: Call<List<LatestOrderResponse>> = RetrofitUtil.orderService.getLastMonthOrder(userId)

        latestOrderRequest.enqueue(object : Callback<List<LatestOrderResponse>> {
            override fun onResponse(call: Call<List<LatestOrderResponse>>, response: Response<List<LatestOrderResponse>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        // 가공 필요 orderDate 를 기준으로 정렬, o_img 하나로 축약 필요
                        //orderId를 기준으로 새로운 리스트 만들어서 넘기기
                        responseLiveData.value = makeLatestOrderList(res)
                    }
                    Log.d(TAG, "onResponse: $res")
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<LatestOrderResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "최근 주문 내역 받아오는 중 통신오류")
            }
        })

        return responseLiveData
    }

    // 최근 주문 목록에서 총가격, 주문 개수 구하여 List로 반환한다.
    // 반환되는 List의 경우 화면에서 보여주는 최근 주문 목록 List이다.
    private fun makeLatestOrderList(latestOrderList: List<LatestOrderResponse>): List<LatestOrderResponse>{
        val hm = HashMap<Int, LatestOrderResponse>()
        latestOrderList.forEach { order ->
            if(hm.containsKey(order.orderId)){
                val tmp = hm[order.orderId]!!
                tmp.orderCnt += order.orderCnt
                tmp.totalPrice  += order.productPrice * order.orderCnt
                hm[order.orderId] = tmp
            } else {
                order.totalPrice = order.productPrice * order.orderCnt
                hm[order.orderId] = order
            }
        }
        val list = ArrayList<LatestOrderResponse>(hm.values)
        list.sortWith { o1, o2 -> o2.orderDate.compareTo(o1.orderDate) }
        return list
    }

    // 모든 주문내역을 반환받는다.
    fun getAllOrderList(): List<OrderListResponse> {
        var orderList = ArrayList<OrderListResponse>()
        val orderListRequest: Call<List<OrderListResponse>> = RetrofitUtil.orderService.getAllOrder()

        orderListRequest.enqueue(object : Callback<List<OrderListResponse>> {
            override fun onResponse(call: Call<List<OrderListResponse>>, response: Response<List<OrderListResponse>>) {
                val res = response.body()
                Log.d(TAG, "onResponse: ${response.body()}")
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 여기입니다... ***")
                    if (res != null) {
                        res.forEach {
                            Log.d(TAG, "onResponse: $it")
                            orderList.add(it)
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<OrderListResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "전체 주문 내역 받아오는 중 통신오류")
            }
        })

        return orderList
    }

}