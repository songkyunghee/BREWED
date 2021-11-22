package com.ssafy.smartstore.src.main.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.smartstore.src.main.dto.User
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import com.ssafy.smartstore.util.RetrofitCallback
import com.ssafy.smartstore.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LoginService_싸피"
class UserService {

    fun login(user:User, callback: RetrofitCallback<User>)  {
        RetrofitUtil.userService.login(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    // 회원가입
    fun rest(user:User, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.userService.insert(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(res!!){
                    callback.onSuccess(response.code(), true)
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    // 아이디 중복 체크
    fun isUsedID(id: String, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userService.isUsedId(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(res!!){
                    callback.onSuccess(response.code(), true)

                } else {
                    callback.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    // 유저 정보 갱신
    fun update(user: User) {
        RetrofitUtil.userService.update(user).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                Log.d(TAG, "update: $user")
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

    // store id 값으로 관리자 token값 획득
    fun selectAdminToken(storeId: String, callback: RetrofitCallback<String>) {
        Log.d(TAG, "selectAdminToken start: ")
        var adminTokenRequest: Call<HashMap<String, String>> = RetrofitUtil.userService.selectAdminToken(storeId)
        var adminToken = "none"
        adminTokenRequest.enqueue(object : Callback<HashMap<String, String>> {
            override fun onResponse(call: Call<HashMap<String, String>>, response: Response<HashMap<String, String>>) {
                val res = response.body()

                if (response.code() == 200) {
                    if (res != null) {
                        adminToken = res.get("a_token") ?: ""
                        callback.onSuccess(response.code(), adminToken)
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        callback.onFailure(response.code())
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<HashMap<String, String>>, t: Throwable) {
                Log.d(TAG, t.message ?: "주문 상세 내역 받아오는 중 통신오류")
                callback.onError(t)
            }
        })
    }

    fun getUserStampWithCoupon(user:User) : LiveData<MutableList<StampWithCouponResponse>> {
        val responseLiveData : MutableLiveData<MutableList<StampWithCouponResponse>> = MutableLiveData()
        val menuInfoRequest: Call<MutableList<StampWithCouponResponse>> = RetrofitUtil.userService.getInfo(user)

        menuInfoRequest.enqueue(object : Callback<MutableList<StampWithCouponResponse>> {
            override fun onResponse(call: Call<MutableList<StampWithCouponResponse>>, response: Response<MutableList<StampWithCouponResponse>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<StampWithCouponResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "사용자 정보 받아오는 중 통신오류")
            }
        })
        return responseLiveData
    }

    fun getUserStamp(id: String, callback: RetrofitCallback<Int>){
        RetrofitUtil.userService.selectStamp(id).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
}
