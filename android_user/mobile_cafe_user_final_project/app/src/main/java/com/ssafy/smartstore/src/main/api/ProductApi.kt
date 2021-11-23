package com.ssafy.smartstore.src.main.api

import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {
    // 전체 상품의 목록을 반환한다
    @GET("rest/product")
    fun getProductList(): Call<MutableList<Product>>

    // 판매량 순으로 상품의 목록을 반환한다.
    @GET("rest/product/hot")
    fun getHotProductList(): Call<MutableList<Product>>

    // 판매량을 갱신한다.
    @PUT("rest/product")
    fun updateSalesProduct(@Body product: Product): Call<Boolean>

    // {productId}에 해당하는 상품의 정보를 comment와 함께 반환한다.
    // comment 조회시 사용
    @GET("rest/product/{productId}")
    fun getProductWithComments(@Path("productId") productId: Int): Call<MutableList<MenuDetailWithCommentResponse>>
}