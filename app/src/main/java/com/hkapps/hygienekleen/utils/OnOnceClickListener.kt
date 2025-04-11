package com.hkapps.hygienekleen.utils

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

abstract class OnOneOffClickListener : View.OnClickListener {


    abstract fun onSingleClick(v: View?)
    var isViewClicked = false

    private var mLastClickTime: Long = 0
    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 600
    }

    private fun isOnceClick(): Boolean {
        val currentClickTime: Long = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
//        mLastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return true
        }
        if (!isViewClicked) {
            isViewClicked = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable { isViewClicked = false }, 600)
        }
        mLastClickTime = currentClickTime
        return false
    }

    override fun onClick(v: View) {
        val currentClickTime: Long = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL) {
            return
        }
        if (!isViewClicked) {
            isViewClicked = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable { isViewClicked = false }, 600)
        } else {
            return
        }
        onSingleClick(v)
    }
}