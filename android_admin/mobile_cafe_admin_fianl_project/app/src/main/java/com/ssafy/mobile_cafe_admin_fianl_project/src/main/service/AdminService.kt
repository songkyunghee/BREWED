package com.ssafy.mobile_cafe_admin_fianl_project.src.main.service

import android.util.Log
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Admin
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitCallback
import com.ssafy.mobile_cafe_admin_fianl_project.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminService {

    // 관리자 로그인
    fun login(admin: Admin, callback: RetrofitCallback<Admin>) {
        RetrofitUtil.adminService.login(admin).enqueue(object: Callback<Admin> {
            override fun onResponse(call: Call<Admin>, response: Response<Admin>) {
               val res = response.body()
                if(response.code() == 200) {
                    if(res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                        callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Admin>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun update(admin: Admin) {
        RetrofitUtil.adminService.update(admin).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                Log.d(TAG, "update: $admin")
                Log.d(TAG, "update: ${response.code()}")
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: success")
                } else {
                    Log.d(TAG, "onResponse: Error Code : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }
        })
    }

}