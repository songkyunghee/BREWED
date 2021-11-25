package com.ssafy.smartstore.util

import androidx.lifecycle.*


class MainViewModel : ViewModel() {
    private val _bannerItemList: MutableLiveData<List<String>> = MutableLiveData()
    private val _currentPosition: MutableLiveData<Int> = MutableLiveData()


    val bannerItemList: LiveData<List<String>>
        get() = _bannerItemList

    val currentPosition: LiveData<Int>
        get() = _currentPosition


    init{
        _currentPosition.value = 0


    }

    fun setBannerItems(list: List<String>) {
        _bannerItemList.value = list
    }

    fun setCurrentPosition(position: Int) {
        _currentPosition.value = position
    }


    fun getcurrentPosition() = currentPosition.value

}