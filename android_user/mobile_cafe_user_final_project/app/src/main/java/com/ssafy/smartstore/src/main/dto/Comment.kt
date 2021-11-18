package com.ssafy.smartstore.src.main.dto

data class Comment(
    var id: Int = -1,
    var userId: String,
    var productId: Int,
    var rating: Float,
    var comment: String
)