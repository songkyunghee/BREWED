package com.ssafy.smartstore.src.main.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.smartstore.src.main.dto.Comment
import com.ssafy.smartstore.src.main.dto.Product
import com.ssafy.smartstore.src.main.response.MenuDetailWithCommentResponse
import com.ssafy.smartstore.util.RetrofitCallback
import com.ssafy.smartstore.util.RetrofitUtil
import com.ssafy.smartstore.util.RetrofitUtil.Companion.productService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "UserService_싸피"
class ProductService {

    fun getProductList(): LiveData<List<Product>>  {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val menuInfoRequest: Call<MutableList<Product>> = RetrofitUtil.productService.getProductList()
        menuInfoRequest.enqueue(object : Callback<MutableList<Product>> {
            override fun onResponse(call: Call<MutableList<Product>>, response: Response<MutableList<Product>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                        Log.d(TAG, "onResponse: ShoppingListFragment ${res.size}")
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<Product>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })
        return responseLiveData
    }

    fun getHotProductList(): LiveData<List<Product>>  {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val menuInfoRequest: Call<MutableList<Product>> = RetrofitUtil.productService.getHotProductList()
        menuInfoRequest.enqueue(object : Callback<MutableList<Product>> {
            override fun onResponse(call: Call<MutableList<Product>>, response: Response<MutableList<Product>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<Product>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })
        return responseLiveData
    }
    
    fun updateSalesProduct(product: Product) {
        
        productService.updateSalesProduct(product).enqueue(object: Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: 판매량 갱신 완료")
                } else {
                    Log.d(TAG, "onResponse: 판매량 갱신 실패 오류코드 : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message} 통신오류")
            }

        })
    }

    fun getProductWithComments(productId: Int) : LiveData<MutableList<MenuDetailWithCommentResponse>> {
        val responseLiveData : MutableLiveData<MutableList<MenuDetailWithCommentResponse>> = MutableLiveData()
        val menuInfoRequest: Call<MutableList<MenuDetailWithCommentResponse>> = RetrofitUtil.productService.getProductWithComments(productId)

        menuInfoRequest.enqueue(object : Callback<MutableList<MenuDetailWithCommentResponse>> {
            override fun onResponse(call: Call<MutableList<MenuDetailWithCommentResponse>>, response: Response<MutableList<MenuDetailWithCommentResponse>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<MenuDetailWithCommentResponse>>, t: Throwable) {
                Log.d(TAG, t.message ?: "메뉴 상세 내역 받아오는 중 통신오류")
            }
        })
        return responseLiveData
    }

    fun restComment(comment: Comment, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.commentService.insert(comment).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                Log.d(TAG, "onResponse: $comment , $response")
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

    fun deleteComment(id: Int, callback: RetrofitCallback<Boolean>)  {
        RetrofitUtil.commentService.delete(id).enqueue(object : Callback<Boolean> {
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

    fun updateComment(comment: Comment, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.commentService.update(comment).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (res!!) {
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



}