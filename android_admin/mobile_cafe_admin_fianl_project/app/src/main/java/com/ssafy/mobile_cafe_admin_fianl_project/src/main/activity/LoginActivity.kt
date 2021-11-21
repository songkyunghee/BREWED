package com.ssafy.mobile_cafe_admin_fianl_project.src.main.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ssafy.mobile_cafe_admin_fianl_project.R
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.adminToken
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.channel_id
import com.ssafy.mobile_cafe_admin_fianl_project.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.mobile_cafe_admin_fianl_project.src.main.fragment.LoginFragment


private const val TAG = "LoginActivity_싸피"
class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "onCreate: ")

        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log
            adminToken = task.result ?: "task.result is null"
            Log.d(TAG, "token: ${adminToken}")
        })
        createNotificationChannel(channel_id, "brewed_admin")

        //로그인 된 상태인지 확인
        var admin = sharedPreferencesUtil.getAdmin()

        Log.d(TAG, "onCreate: admin ${admin}")

        //로그인 상태 확인. id가 있다면 로그인 된 상태
        if (admin.aId != ""){
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

    // Notification 수신 채널 추가
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}