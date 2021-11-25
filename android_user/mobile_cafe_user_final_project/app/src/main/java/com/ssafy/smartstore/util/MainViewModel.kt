package com.ssafy.smartstore.util

import androidx.lifecycle.*


class MainViewModel : ViewModel() {
    private val _bannerItemList: MutableLiveData<List<String>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()
    private var _nowCouponNum: MutableLiveData<Int> = MutableLiveData()

    val bannerItemList: LiveData<List<String>>
        get() = _bannerItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition

    val nowCouponNum: LiveData<Int>
        get() = _nowCouponNum

    init{
        _currentPosition.value = 0
        _nowCouponNum.value = 0

    }

    fun setBannerItems(list: List<String>) {
        _bannerItemList.value = list
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }

    fun setNowCouponNum(num: Int) {
        _nowCouponNum.value = num
    }

    fun getcurrentPosition() = currentPosition.value

}