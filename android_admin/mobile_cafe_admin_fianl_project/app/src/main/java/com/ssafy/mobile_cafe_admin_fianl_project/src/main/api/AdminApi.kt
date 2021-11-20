package com.ssafy.mobile_cafe_admin_fianl_project.src.main.api

import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Admin
import retrofit2.Call
import retrofit2.http.*

interface AdminApi {

    // 관리자 로그인이 성공적으로 되었다면 loginAdminId라는 쿠키를 내려준다.
    @POST("rest/admin/login")
    fun login(@Body body: Admin): Call<Admin>
}