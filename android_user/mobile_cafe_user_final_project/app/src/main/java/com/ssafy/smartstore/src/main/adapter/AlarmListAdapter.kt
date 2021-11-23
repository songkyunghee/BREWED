package com.ssafy.smartstore.src.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore.R
import com.ssafy.smartstore.src.main.dto.Noti
import com.ssafy.smartstore.src.main.response.NotiListResponse
import com.ssafy.smartstore.src.main.response.StampWithCouponResponse
import com.ssafy.smartstore.util.CommonUtils

class AlarmListAdapter(val context: Context, var list: MutableList<NotiListResponse>) :RecyclerView.Adapter<AlarmListAdapter.AlarmHolder>(){

    inner class AlarmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nDate: TextView = itemView.findViewById(R.id.textDateContent)
        var nMsg: TextView = itemView.findViewById<TextView>(R.id.textMsgContent)
        val btnDelete = itemView.findViewById<ImageView>(R.id.btnAlarmDelete)

        fun bindInfo(data : NotiListResponse, position: Int) {
            nDate.text = CommonUtils.getFormattedString2(data.noti_time)
            nMsg.text = data.n_msg

            btnDelete.setOnClickListener{
                itemClickListner.onClick(it, position, list[position].n_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notice, parent, false)
        return AlarmHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmHolder, position: Int) {
        holder.apply {
            bindInfo(list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun onClick(view: View,  position: Int, productId:Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: AlarmListAdapter.ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: AlarmListAdapter.ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}