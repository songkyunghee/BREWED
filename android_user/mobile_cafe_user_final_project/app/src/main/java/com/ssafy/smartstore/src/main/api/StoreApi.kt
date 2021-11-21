package com.ssafy.smartstore.src.main.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    // beacon 주소로 Store Id를 반환한다.
    @GET("rest/store/beacon/{beacon}")
    fun getStoreId(@Path("beacon") beacon: String): Call<HashMap<String, String>>

}