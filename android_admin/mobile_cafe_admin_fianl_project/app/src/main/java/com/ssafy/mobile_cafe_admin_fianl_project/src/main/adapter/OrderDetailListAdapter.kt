package com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderDetailResponse
import com.ssafy.mobile_cafe_admin_fianl_project.util.CommonUtils


class OrderDetailListAdapter(val context: Context, val orderDetail:List<OrderDetailResponse>) :RecyclerView.Adapter<OrderDetailListAdapter.OrderDetailListHolder>(){

    inner class OrderDetailListHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuImage = itemView.findViewById<ImageView>(R.id.menuImage)
        val textShoppingMenuName = itemView.findViewById<TextView>(R.id.textShoppingMenuName)
        val textShoppingMenuMoney = itemView.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val textShoppingMenuCount = itemView.findViewById<TextView>(R.id.textShoppingMenuCount)
        val textShoppingMenuMoneyAll = itemView.findViewById<TextView>(R.id.textShoppingMenuMoneyAll)

        fun bindInfo(data:OrderDetailResponse){
            Log.d("Adapter_싸피", "bindInfo: $data")
            var type = if(data.productType == "coffee") "잔" else "개"

            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${data.img}")
                .into(menuImage)

            textShoppingMenuName.text = data.productName
            textShoppingMenuMoney.text = CommonUtils.makeComma(data.unitPrice)
            textShoppingMenuCount.text = "${data.quantity} ${type}"
            textShoppingMenuMoneyAll.text = CommonUtils.makeComma(data.unitPrice * data.quantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_detail_list, parent, false)
        return OrderDetailListHolder(view)
    }

    override fun onBindViewHolder(holder: OrderDetailListHolder, position: Int) {
        holder.bindInfo(orderDetail[position])
    }

    override fun getItemCount(): Int {
        return orderDetail.size
    }
}

