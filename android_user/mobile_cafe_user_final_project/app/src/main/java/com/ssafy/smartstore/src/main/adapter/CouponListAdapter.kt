package com.ssafy.smartstore.src.main.adapter

import android.content.Context
import android.util.Log
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

class CouponListAdapter(val context: Context, var list: MutableList<StampWithCouponResponse>) :RecyclerView.Adapter<CouponListAdapter.CouponHolder>(){
    lateinit var clickListener: CouponListAdapter.OnItemClickListener
    var isCheck: Boolean = false
    var selectCheck: ArrayList<Int> = arrayListOf()
    var checkMode = 0
    var checkPosition = -1

    init {
        for(i in 0 until list.size) {
            selectCheck.add(0)
        }
    }


    interface OnItemClickListener {
        fun onClick(view: View, position: Int, commentId: Int, mode:Boolean)
    }
    inner class CouponHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: CardView = itemView.findViewById<CardView>(R.id.cardview)
        var cValue: TextView = itemView.findViewById(R.id.textValue)
        var checkIcon: ImageView = itemView.findViewById<ImageView>(R.id.btnCouponCheck)

        fun bindInfo(data : StampWithCouponResponse, pos: Int) {
            cValue.text = data.cValue.toString()

            if(selectCheck[pos] == 1) {
                cValue.setTextColor(ContextCompat.getColor(context, R.color.black))
                item.background.setTint(ContextCompat.getColor(context, R.color.coffee_menu_back))
                checkIcon.visibility = View.VISIBLE
            } else {
                cValue.setTextColor(ContextCompat.getColor(context, R.color.coffee_dark_gray))
                item.background.setTint(ContextCompat.getColor(context, R.color.white))
                checkIcon.visibility = View.INVISIBLE
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_coupon, parent, false)
        return CouponHolder(view)
    }

    override fun onBindViewHolder(holder: CouponHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)

            item.setOnClickListener{
                if(selectCheck[position] == 0 && checkMode == 0) {
                    selectCheck[position] = 1
                    checkMode = 1
                    checkPosition = position
                    clickListener.onClick(it, position, list[position].cId, false)


                } else if(checkMode == 1 && selectCheck[position] == 0) {
                    selectCheck[position] = 1
                    selectCheck[checkPosition] = 0
                    checkPosition = position
                    clickListener.onClick(it, position, list[position].cId, false)


                }
                else if(checkMode == 1 && selectCheck[position] == 1) {
                    if(position == checkPosition) {
                        checkMode = 0
                    }
                    selectCheck[position] = 0
                    clickListener.onClick(it, position, list[position].cId, true)

                }

            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}