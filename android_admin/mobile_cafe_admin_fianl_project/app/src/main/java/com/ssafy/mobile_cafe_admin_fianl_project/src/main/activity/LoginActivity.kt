package com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment.LoginFragment


private const val TAG = "LoginActivity_싸피"
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //로그인 된 상태인지 확인
        var admin = sharedPreferencesUtil.getAdmin()

        Log.d(TAG, "onCreate: admin ${admin}")

        //로그인 상태 확인. id가 있다면 로그인 된 상태
        if (admin.id != ""){
            openFragment(1)
        }
        else {
            // 가장 첫 화면은 홈 화면의 Fragment로 지정
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_login, LoginFragment())
                .commit()
        }
    }

    fun openFragment(int: Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(int){
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }

            2 -> transaction.replace(R.id.frame_layout_login, LoginFragment())
                .addToBackStack(null)
        }
        transaction.commit()
    }

}