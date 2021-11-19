package com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.mobile_cafe_admin_fianl_project.R

class OrderListAdapter (val context: Context, var orderList: MutableList<Int>) :
    RecyclerView.Adapter<OrderListAdapter.OrderListHolder>() {

    inner class OrderListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuImage = itemView.findViewById<ImageView>(R.id.menuImage)
        fun bindInfo(img: Int) {
            menuImage.setImageResource(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_list, parent, false)
        return OrderListHolder(view)
    }

    override fun onBindViewHolder(holder: OrderListHolder, position: Int) {
        holder.apply {
            bindInfo(orderList[position])
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}

