package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        supportActionBar?.hide()

        val animation = AnimationUtils.loadAnimation(this, R.anim.top_animationn)
        val logo: ImageView = findViewById(R.id.logo)
        logo.startAnimation(animation)

        val animation1 = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)
        val text : TextView = findViewById(R.id.text)
        text.startAnimation(animation1)

//        Handler().postDelayed({
//
//        }, 3000)

        lifecycleScope.launch {
            delay(2500)
            startActivity(Intent( this@SplashActivity,MainActivity::class.java))
            finish()
        }
    }
}