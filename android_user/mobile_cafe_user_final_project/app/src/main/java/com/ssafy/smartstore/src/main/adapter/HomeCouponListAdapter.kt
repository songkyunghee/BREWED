package com.ssafy.smartstore.src.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse

class HomeCouponListAdapter(val context: Context, var list: MutableList<StampWithCouponResponse>) :RecyclerView.Adapter<HomeCouponListAdapter.CouponHolder>(){

    inner class CouponHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cValue: TextView = itemView.findViewById(R.id.textValue)

        fun bindInfo(data : StampWithCouponResponse, pos: Int) {
            cValue.text = data.cValue.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_home_coupon, parent, false)
        return CouponHolder(view)
    }

    override fun onBindViewHolder(holder: CouponHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}