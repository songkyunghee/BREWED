package com.ssafy.mobile_cafe_admin_fianl_project.src.main.api

import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderDetailResponse
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse
import retrofit2.Call
import retrofit2.http.*

interface OrderApi {
    // order 객체를 통해 완료된 주문을 모두 조회한다.
    @GET("rest/order/dateOrderComList/{date}/{completed}/{storeId}")
    fun getDateComOrderList(@Path("date") date: String, @Path("completed") completed: String, @Path("storeId") storeId: String): Call<List<OrderListResponse>>

    @GET("rest/order/dateOrderNotComList/{date}/{storeId}")
    fun getDateNotComOrderList(@Path("date") date: String, @Path("storeId") storeId: String): Call<MutableList<OrderListResponse>>

    @PUT("rest/order/dateOrderNotComList")
    fun update(@Body order: Order): Call<Boolean>

    @GET("rest/order/{orderId}")
    fun getOrderDetail(@Path("orderId") orderId: Int): Call<List<OrderDetailResponse>>
}