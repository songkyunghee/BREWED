package com.ssafy.mobile_cafe_admin_fianl_project.util

import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.api.AdminApi
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.api.OrderApi

class RetrofitUtil {
    companion object {
        val orderService = ApplicationClass.retrofit.create(OrderApi::class.java)
        val adminService = ApplicationClass.retrofit.create(AdminApi::class.java)
    }
}