package com.guulpay.customUiComponents

import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View

abstract class CustomSwipeOnTouch() : View.OnTouchListener {

    var x1 = 0.0
    var x2 = 0.0
    var isSwiped = false

    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    abstract fun onClick()

    override fun onTouch(v: View?, motionEvent: MotionEvent?): Boolean {
        if (motionEvent?.action == ACTION_DOWN) {
            isSwiped = false
            Log.e("onTouch","ACTION_DOWN")
            x1 = motionEvent.x.toDouble()
        }
        if (motionEvent?.action == ACTION_UP && !isSwiped){
            Log.e("onTouch","ACTION_UP")
            onClick()
        }
        if (motionEvent?.action == ACTION_CANCEL) {

            Log.e("onTouch","ACTION_CANCEL")
            x2 = motionEvent.x.toDouble()
            if ((x1 - x2) > 80.0 && x1 > x2) {
                isSwiped = true
                onSwipeLeft()
            } else if (x2 > x1) {
                isSwiped = true
                onSwipeRight()
            }
        }
        if (motionEvent?.action == ACTION_MOVE) {
           // To nothing
        }

        return true
    }
}