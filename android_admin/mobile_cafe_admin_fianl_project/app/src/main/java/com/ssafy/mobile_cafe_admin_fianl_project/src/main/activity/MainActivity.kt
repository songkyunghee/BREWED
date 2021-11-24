package com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.adminToken
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment.OrderComFragment
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment.OrderDetailFragment
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment.OrderFragment
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.service.AdminService

private const val TAG = "MainActivity_싸피"
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!adminToken.equals("none")) {
            var admin = sharedPreferencesUtil.getAdmin()
            admin.aToken = adminToken
            Log.d(TAG, "onCreate: token 갱신...")
            AdminService().update(admin)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, OrderFragment())
            .commit()

        bottomNavigation = findViewById(R.id.tab_layout_bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_page_1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, OrderFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, OrderComFragment())
                        .commit()
                    true
                }
                else -> false
            }

        }
        bottomNavigation.setOnNavigationItemReselectedListener { item ->
            // 재선택시 다시 랜더링 하지 않기 위해 수정
            if(bottomNavigation.selectedItemId != item.itemId){
                bottomNavigation.selectedItemId = item.itemId
            }
        }
    }

    fun openFragment(index:Int, key:String, value:Int){
        moveFragment(index, key, value)
    }

    fun openFragment(index: Int) {
        moveFragment(index, "", 0)
    }

    private fun moveFragment(index: Int, key: String, value: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when(index) {
            1 -> transaction.replace(R.id.frame_layout_main, OrderDetailFragment.newInstance(key, value))
                .addToBackStack(null)
            2->
                logout()
        }
        transaction.commit()
    }

    fun hideBottomNav(state : Boolean){
        if(state) bottomNavigation.visibility =  View.GONE
        else bottomNavigation.visibility = View.VISIBLE
    }

    fun logout(){
        //preference 지우기
        sharedPreferencesUtil.deleteAdmin()

        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent)
    }

}