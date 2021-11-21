package com.ssafy.smartstore.src.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.src.main.dto.BannerItem

class ViewPageAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val ITEM_COUNT = 3
    }

    private var bannerItemList: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_banner, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bannerItemList?.let{ bannerItemList ->
            (holder as BannerViewHolder).bind(bannerItemList[position])

        }
    }

    fun submitList(list: List<String>?) {
        bannerItemList = list
        notifyDataSetChanged()
    }

    class BannerViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bannerItem: String) {
            var imageView = itemView.findViewById<ImageView>(R.id.iv_banner_image)
            Glide.with(itemView)
                .load("${ApplicationClass.IMGS_URL}${bannerItem}")
                .into(imageView)

            //itemView.findViewById<ImageView>(R.id.iv_banner_image).setImageResource(bannerItem.image)
        }
    }

}