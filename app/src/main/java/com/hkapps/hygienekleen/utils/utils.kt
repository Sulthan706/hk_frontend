package com.hkapps.hygienekleen.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}

fun Activity.showKeyboard() {
    showKeyboard(currentFocus ?: View(this))
}

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun TextView.empty() {
    this.text = ""
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun AppCompatActivity.setupEdgeToEdge(rootView: View, statusBarBackground: View? = null) {
    enableEdgeToEdge()

    ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            systemBars.left,
            if(statusBarBackground != null) 0 else systemBars.top,
            systemBars.right,
            systemBars.bottom
        )

        statusBarBackground?.updateLayoutParams {
            height = systemBars.top
        }

        insets
    }
}

class ResetPreferencesWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val preferences = applicationContext.getSharedPreferences("com.digimaster.carefastoperation", Context.MODE_PRIVATE)
        preferences.edit().remove(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO).apply()
        return Result.success()
    }
}

