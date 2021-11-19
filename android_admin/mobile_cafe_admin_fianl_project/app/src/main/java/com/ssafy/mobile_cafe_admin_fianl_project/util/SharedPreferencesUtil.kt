package com.ssafy.mobile_cafe_admin_fianl_project.util

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.dto.Admin

class SharedPreferencesUtil (context: Context) {
    val SHARED_PREFERENCES_NAME = "smartstore_preference"
    val COOKIES_KEY_NAME = "cookies"

    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

//    fun addAdmin(admin: Admin){
//        val editor = preferences.edit()
//        editor.putString("id", admin.id)
//        editor.putString("name", admin.name)
//        editor.apply()
//    }
//
//    fun getAdmin(): Admin{
//        val id = preferences.getString("id", "")
//        if (id != ""){
//            val name = preferences.getString("name", "")
//            return Admin(id!!, name!!, "",0)
//        }else{
//            return Admin()
//        }
//    }

    fun deleteAdmin(){
        //preference 지우기
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun addAdminCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

    fun getAdminCookie(): MutableSet<String>? {
        return preferences.getStringSet(COOKIES_KEY_NAME, HashSet())
    }

    fun deleteAdminCookie() {
        preferences.edit().remove(COOKIES_KEY_NAME).apply()
    }


}