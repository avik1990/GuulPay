package com.guulpay.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.crashlytics.android.Crashlytics
import com.guulpay.R
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import io.fabric.sdk.android.Fabric

class SplashActivity : AppCompatActivity() {

    lateinit var handler: Handler
    val waitingTime: Long = 3000
    var appdata: AppData? = null

    companion object {
        val CLASS_NAME = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash_activity)
        appdata = AppData(this, Constants.Keys._KeyCryptoPreference)
      //  Fabric.with(this, Crashlytics())
        if (appdata!!.isLoggedin) {
            Log.d("IsLogged:",""+appdata!!.isLoggedin)
            movetopasscodescreen()
        } else {
            Log.d("IsLogged::",""+appdata!!.isLoggedin)
            waitScreen()
        }
    }

    private fun waitScreen() {
        handler = Handler()
        handler.postDelayed(
                object : Runnable {
                    override fun run() {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }, waitingTime)
    }

    private fun movetopasscodescreen() {
        handler = Handler()
        handler.postDelayed(
                object : Runnable {
                    override fun run() {
                        /*val intent = Intent(this@SplashActivity, DashboardHomeActivity::class.java)
                        intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, DashboardMainContentFragment.CLASS_NAME)
                        startActivity(intent)
                        finish()*/
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    }
                }, waitingTime)
    }
}