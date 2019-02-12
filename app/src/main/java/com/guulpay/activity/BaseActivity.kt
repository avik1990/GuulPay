package com.guulpay.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.guulpay.R
import kr.co.namee.permissiongen.PermissionGen


abstract class BaseActivity : AppCompatActivity() {

    var isActivityVisible: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* To change the status bar text color */
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(getLayout())
        initResources()
        initListeners()
        initPermissions()
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    abstract fun initResources()
    abstract fun initListeners()
    abstract fun getLayout(): Int
    abstract fun initPermissions()
    abstract fun globalLogout()
}