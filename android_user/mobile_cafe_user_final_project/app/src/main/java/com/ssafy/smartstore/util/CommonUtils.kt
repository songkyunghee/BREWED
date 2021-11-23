package com.ssafy.smartstore.util

import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.src.main.response.LatestOrderResponse
import com.ssafy.smartstore.src.main.response.OrderDetailResponse
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    //천단위 콤마
    fun makeComma(num: Int): String {
        var comma = DecimalFormat("#,###")
        return "${comma.format(num)}원"
    }


    fun getFormattedString(date:Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH시 mm분")
        dateFormat.timeZone = TimeZone.getTimeZone("Seoul/Asia")

        return dateFormat.format(date)
    }

    fun getFormattedString2(date:Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd\nHH시 mm분")
        dateFormat.timeZone = TimeZone.getTimeZone("Seoul/Asia")

        return dateFormat.format(date)
    }
}