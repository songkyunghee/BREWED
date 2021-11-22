package com.ssafy.smartstore.util

import androidx.lifecycle.*
import com.ssafy.smartstore.src.main.dto.BannerItem
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.service.OrderService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _bannerItemList: MutableLiveData<List<String>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()
    private val _userLastOrderLiveData: MutableLiveData<List<LatestOrderResponse>> = MutableLiveData()

    val bannerItemList: LiveData<List<String>>
        get() = _bannerItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition

    val userLastOrderLiveData: LiveData<List<LatestOrderResponse>>
        get() = _userLastOrderLiveData

    init{
        _currentPosition.value = 0

    }

    fun setBannerItems(list: List<String>) {
        _bannerItemList.value = list
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun setLastOderList(id: String) {
        _userLastOrderLiveData.value = OrderService().getLastMonthOrder(id).value;
    }

    fun setUserLastOrderItems(list: List<LatestOrderResponse>) {
        _userLastOrderLiveData.value = list
    }


    fun getcurrentPosition() = currentPosition.value
}