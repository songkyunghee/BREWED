package com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse

class OrderListAdapter (val context: Context, var notComOrderList: MutableList<OrderListResponse>) :
    RecyclerView.Adapter<OrderListAdapter.OrderListHolder>() {
    lateinit var clickListener: OnItemClickListener
    var completed = ""
    var process = "N"

    interface OnItemClickListener {
        fun onOrderUpdateClick(view: View, position: Int, commentId: Int)

    }
    inner class OrderListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menuImage = itemView.findViewById<ImageView>(R.id.menuImage)
        var orderIconBack = itemView.findViewById<CardView>(R.id.orderBtn)
        var orderIcon = itemView.findViewById<ImageView>(R.id.orderIcon)
        var makeIconBack = itemView.findViewById<CardView>(R.id.makeBtn)
        var makeIcon = itemView.findViewById<ImageView>(R.id.makeIcon)
        var comIconBack = itemView.findViewById<CardView>(R.id.comBtn)
        var comIcon = itemView.findViewById<ImageView>(R.id.comIcon)

        fun bindInfo(order: OrderListResponse) {
            completed = order.completed
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${order.img}")
                .into(menuImage)

            if(completed == "N") {
                orderIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_blue))
                orderIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_able))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_list, parent, false)
        return OrderListHolder(view)
    }

    override fun onBindViewHolder(holder: OrderListHolder, position: Int) {
        holder.apply {
            bindInfo(notComOrderList[position])

            orderIconBack.setOnClickListener{
                if(process == "N") {
                    orderIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    orderIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    makeIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_blue))
                    makeIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_able))
                    comIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    comIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    clickListener.onOrderUpdateClick(it, position, notComOrderList[position].o_id)
                    //process = "M"
                }
            }

            orderIcon.setOnClickListener{
                if(process == "N") {
                    orderIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    orderIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    makeIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_blue))
                    makeIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_able))
                    comIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    comIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    clickListener.onOrderUpdateClick(it, position, notComOrderList[position].o_id)
                    //process = "M"
                }
            }

            makeIconBack.setOnClickListener{
                if(process == "M") {
                    orderIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    orderIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    makeIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    makeIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    comIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_blue))
                    comIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_able))
                    clickListener.onOrderUpdateClick(it, position, notComOrderList[position].o_id)
                    //process = "N"
                }
            }

            makeIcon.setOnClickListener{
                if(process == "M") {
                    orderIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    orderIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    makeIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_grey))
                    makeIcon.setColorFilter(ContextCompat.getColor(context, R.color.white))
                    comIconBack.background.setTint(ContextCompat.getColor(context, R.color.coffee_blue))
                    comIcon.setColorFilter(ContextCompat.getColor(context, R.color.coffee_able))
                    clickListener.onOrderUpdateClick(it, position, notComOrderList[position].o_id)
                    //process = "N"
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return notComOrderList.size
    }
}

