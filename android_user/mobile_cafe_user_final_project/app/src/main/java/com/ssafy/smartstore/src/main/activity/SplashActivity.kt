package com.ssafy.smartstore.src.main.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.ssafy.smartstore.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 2000 // 2 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Glide.with(this)
            .load(R.raw.splashgif)
            .into(findViewById(R.id.splash_icon))

        Handler().postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()  // close this activity
        }, SPLASH_TIME_OUT)
    }
}
