package com.example.flo.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.view.MainTabActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 2000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, MainTabActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
}