package com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto

data class OrderDetail (
    var id: Int,
    var orderId: Int,
    var productId: Int,
    var quantity: Int ) {


    constructor(productId: Int, quantity: Int) :this(0, 0, productId, quantity)
    constructor():this(0,0)

}
