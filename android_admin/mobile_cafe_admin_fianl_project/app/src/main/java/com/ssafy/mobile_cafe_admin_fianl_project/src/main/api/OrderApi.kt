package com.ssafy.mobile_cafe_admin_fianl_project.src.main.api

import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import retrofit2.Call
import retrofit2.http.*

interface OrderApi {
    // order 객체를 통해 완료된 주문을 모두 조회한다.
    @GET("rest/order/dateOrderComList/{date}/{completed}/{storeId}")
    fun getDateComOrderList(@Path("date") date: String, @Path("completed") completed: String, @Path("storeId") storeId: String): Call<List<OrderListResponse>>

}