package com.ssafy.smartstore.src.main.dto

import java.util.*

data class Noti (
    var nId: Int,
    var nMsg: String,
    val notiTime: String,
    val userId: String
) {
    constructor(nMsg: String, userId: String) : this(0, nMsg, "", userId)

}