package com.ssafy.smartstore.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG = "Repository_싸피"
class Repository {

    companion object{
        fun getUserLastOrder(id: String) : MutableLiveData<List<LatestOrderResponse>>? {
            val responseLiveData: MutableLiveData<List<LatestOrderResponse>> = MutableLiveData()

            CoroutineScope(Dispatchers.Default).launch {
                launch(Dispatchers.IO) {

                        val latestOrderRequest: Call<List<LatestOrderResponse>> = RetrofitUtil.orderService.getLastMonthOrder(id)

                        latestOrderRequest.enqueue(object : Callback<List<LatestOrderResponse>> {
                            override fun onResponse(call: Call<List<LatestOrderResponse>>, response: Response<List<LatestOrderResponse>>) {
                                val res = response.body()
                                if(response.code() == 200){
                                    if (res != null) {
                                        // 가공 필요 orderDate 를 기준으로 정렬, o_img 하나로 축약 필요
                                        //orderId를 기준으로 새로운 리스트 만들어서 넘기기

                                        responseLiveData.value = makeLatestOrderList(res)
                                        responseLiveData.postValue(res)
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
                }
            }
            return responseLiveData
        }

        private fun makeLatestOrderList(latestOrderList: List<LatestOrderResponse>): List<LatestOrderResponse> {
            val hm = HashMap<Int, LatestOrderResponse>()
            latestOrderList.forEach { order ->
                if (hm.containsKey(order.orderId)) {
                    val tmp = hm[order.orderId]!!
                    tmp.orderCnt += order.orderCnt
                    tmp.totalPrice += order.productPrice * order.orderCnt
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

    }
}