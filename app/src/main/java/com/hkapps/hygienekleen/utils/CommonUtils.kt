package com.hkapps.hygienekleen.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toDrawable
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hkapps.hygienekleen.databinding.ProgressDialogBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.Calendar
import java.util.concurrent.TimeUnit

object CommonUtils {
    fun showLoadingDialog(
        context: Context,
        loadingText: String,
        cancelable: Boolean = true
    ): Dialog {
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        val progressDialog = Dialog(context)
        progressDialog.setContentView(binding.root)

        binding.tvLoadingText.text = loadingText

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setCancelable(cancelable)
            it.setCanceledOnTouchOutside(false)

//            //set timer cuma 1.5 detik buat loadingnya
//            val progressRunnable = Runnable { it.cancel() }
//            val pdCanceller = Handler()
//            pdCanceller.postDelayed(progressRunnable, 1500)

            return it
        }

    }

    fun showLoadingCancelableDialog(
        context: Context,
        loadingText: String,
        cancelable: Boolean = true
    ): Dialog {
        val binding = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        val progressDialog = Dialog(context)
        progressDialog.setContentView(binding.root)

        binding.tvLoadingText.text = loadingText

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setCancelable(cancelable)
            it.setCanceledOnTouchOutside(true)

//            //set timer cuma 1.5 detik buat loadingnya
//            val progressRunnable = Runnable { it.cancel() }
//            val pdCanceller = Handler()
//            pdCanceller.postDelayed(progressRunnable, 1500)

            return it
        }

    }

    class ResetPreferencesWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
        override fun doWork(): Result {
            val preferences = applicationContext.getSharedPreferences("com.digimaster.carefastoperation", Context.MODE_PRIVATE)
            preferences.edit().remove(CarefastOperationPrefConst.ID_RKB_OPR_ATTENDANCE_GEO).apply()

           
            scheduleResetPreferences(applicationContext)

            return Result.success()
        }
    }

    fun scheduleResetPreferences(context: Context) {
        val currentTime = Calendar.getInstance()
        val dueTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val initialDelay = dueTime.timeInMillis - currentTime.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<com.hkapps.hygienekleen.utils.ResetPreferencesWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "ResetPreferencesWork",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }


}