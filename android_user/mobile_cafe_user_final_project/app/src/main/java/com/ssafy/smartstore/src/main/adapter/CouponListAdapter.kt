package com.ssafy.smartstore.src.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse

class CouponListAdapter(var list: MutableList<StampWithCouponResponse>) :RecyclerView.Adapter<CouponListAdapter.CouponHolder>(){

    inner class CouponHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: CardView = itemView.findViewById<CardView>(R.id.cardview)
        var cValue: TextView = itemView.findViewById(R.id.textValue)

        fun bindInfo(data : StampWithCouponResponse) {
            cValue.text = data.cValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_coupon, parent, false)
        return CouponHolder(view)
    }

    override fun onBindViewHolder(holder: CouponHolder, position: Int) {
        holder.apply {
            bindInfo(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}