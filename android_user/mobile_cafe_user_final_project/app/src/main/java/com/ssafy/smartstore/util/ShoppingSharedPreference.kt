package com.ssafy.smartstore.util

import android.content.Context
import android.content.SharedPreferences

class ShoppingSharedPreference (context: Context) {
    private val SHARED_PREFERENCES_NAME = "shoppingList_preference"

    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun putItem(id:Int, cnt:Int){
        val isIn = preferences.getInt(id.toString(), 0)
        val editor = preferences.edit()
        if(isIn!=0){
            editor.putInt(id.toString(), cnt+isIn)
            editor.apply()
        }else{
            editor.putInt(id.toString(), cnt)
            editor.apply()
        }
    }

    fun getList(): MutableList<Int>{
        var list = mutableListOf<Int>()
        list.add(0)
        for(i in 1..12){
            val item = preferences.getInt(i.toString(), 0)
            list.add(item)
        }
        return list
    }

    fun deleteItem(id:Int){
        //preference 지우기
        val editor = preferences.edit()
        editor.putInt(id.toString(), 0)
        editor.apply()
    }

    fun deleteList(){
        //preference 지우기
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}