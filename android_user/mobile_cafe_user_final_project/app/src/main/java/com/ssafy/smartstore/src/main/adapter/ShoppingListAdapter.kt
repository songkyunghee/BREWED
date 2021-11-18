package com.ssafy.smartstore.src.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore.R
import com.ssafy.smartstore.config.ApplicationClass
import com.ssafy.smartstore.src.main.dto.Product


class ShoppingListAdapter(val context: Context, var prodList:MutableList<Product>, var prodCntList:MutableList<Int>)
    :RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>(){


    inner class ShoppingListHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val menuName = itemView.findViewById<TextView>(R.id.textShoppingMenuName)
        val menuMoney = itemView.findViewById<TextView>(R.id.textShoppingMenuMoney)
        val menuCount = itemView.findViewById<TextView>(R.id.textShoppingMenuCount)
        val menuMoneyAll = itemView.findViewById<TextView>(R.id.textShoppingMenuMoneyAll)

        val menuImage = itemView.findViewById<ImageView>(R.id.menuImage)
        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)

        fun bindInfo(product : Product, cnt:Int, position: Int){
            menuName.text = product.name

//            var img = context.resources.getIdentifier("${ApplicationClass.MENU_IMGS_URL}${product.img}", "drawable", context.packageName)
////            Glide.with(this).load("${ApplicationClass.MENU_IMGS_URL}${product.img}").into(menuImage)
//
//            menuImage.setImageResource(img)

            menuMoney.text = product.price.toString()
            menuCount.text = cnt.toString()
            menuMoneyAll.text = (product.price*cnt).toString()

            btnDelete.setOnClickListener{
                itemClickListner.onClick(it, position, prodList[position].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_shopping_list, parent, false)
        return ShoppingListHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
        holder.bindInfo(prodList[position], prodCntList[position], position)
        //Glide.with(this).load("${ApplicationClass.MENU_IMGS_URL}${product.img}").into(menuImage)
        Glide.with(holder.itemView.getContext())
            .load("${ApplicationClass.MENU_IMGS_URL}${prodList[position].img}")
            .into(holder.menuImage);
    }

    override fun getItemCount(): Int {
        return prodList.size
    }
    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View,  position: Int, productId:Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}

