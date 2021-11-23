package com.ssafy.smartstore.src.main.api

import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    // 사용자 정보를 추가한다.
    @POST("rest/user")
    fun insert(@Body body: User): Call<Boolean>

    // 사용자의 정보와 함께 사용자의 스탬프 정보, 쿠폰 정보를 반환한다.
    @POST("rest/user/info")
    fun getInfo(@Body body: User): Call<MutableList<StampWithCouponResponse>>

    // request parameter로 전달된 id가 이미 사용중인지 반환한다.
    @GET("rest/user/isUsed")
    fun isUsedId(@Query("id") id: String): Call<Boolean>

    // 로그인 처리 후 성공적으로 로그인 되었다면 loginId라는 쿠키를 내려준다.
    @POST("rest/user/login")
    fun login(@Body body: User): Call<User>

    @PUT("rest/user/update")
    fun update(@Body body: User): Call<Int>

    // store Id에 해당하는 admin token 값을 반환한다.
    @GET("rest/admin/{sId}")
    fun selectAdminToken(@Path("sId") sId: String): Call<HashMap<String, String>>

    @GET("rest/user/stamp/{id}")
    fun selectStamp(@Path("id") id: String ): Call<String>

    // {id}에 해당하는 coupon을 삭제한다.
    @DELETE("rest/user/coupon/{id}")
    fun delete(@Path("id") id: Int): Call<Boolean>

    // {id}에 해당하는 user의 정보를 조회한다.
    @GET("rest/user/select/{id}")
    fun selectUser(@Path("id") id: String): Call<User>

    // {productId}에 해당하는 Comment를 작성한 user의 name을 조회한다.
    @GET("rest/user/selectcomment/{productId}")
    fun selectWithCommentUserName(@Path("productId") productId: Int): Call<List<String>>

}