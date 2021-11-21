package com.ssafy.mobile_cafe_admin_fianl_project.src.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Order
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.response.OrderListResponse

class OrderComListAdapter(val context: Context, var orderComList: MutableList<OrderListResponse>) : RecyclerView.Adapter<OrderComListAdapter.OrderComListHolder>(){
    

    inner class OrderComListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId = itemView.findViewById<TextView>(R.id.tv_order_id)
        val totalPrice = itemView.findViewById<TextView>(R.id.tv_total_price)

        fun bindInfo(order: OrderListResponse) {
            orderId.text = order.o_id.toString()
            totalPrice.text = order.totalPrice.toString()

            //클릭연결
            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, order.o_id)
            }

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderComListAdapter.OrderComListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_order_com_list, parent, false)
        return OrderComListHolder(view)
    }

    override fun onBindViewHolder(holder: OrderComListAdapter.OrderComListHolder, position: Int) {
        holder.apply {
            bindInfo(orderComList[position])
        }
    }

    override fun getItemCount(): Int {
        return orderComList.size
    }

    interface ItemClickListener {
        fun onClick(view: View,  position: Int, orderid:Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }


}