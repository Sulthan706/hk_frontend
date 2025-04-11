package com.hkapps.hygienekleen.features.features_management.report.ui.fragment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSheetDownCtalkBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class BotSheetDownCtalkFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetDownCtalkBinding

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTCODE_FILTER, "")
    private val startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
    private val endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")
    private var selectedItem = ""
    var progressBar = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetDownCtalkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.rgRadioBtnCtalk.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbInternalCtalk) {
                selectedItem = binding.rbInternalCtalk.text.toString()
            }
            if (checkedId == R.id.rbManajemenCtalk) {
                selectedItem = binding.rbManajemenCtalk.text.toString()
            }
        }
        binding.rbInternalCtalk.isChecked = true
        binding.btnDownloadReport.setOnClickListener {
            if (selectedItem == "Internal client"){
                downloadInternalCtalk()
            } else {
                downloadManajemenCtalk()
            }
        }

    }

    private fun downloadManajemenCtalk() {
        val url = "https://ops.carefast.id/carefast/Detailcomplaint/dataDetailComplaintFilterCtalkClientManagement/$projectCode/$startDate/$endDate"
        Log.d("AGRI","$url")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File CTalk-Manajemen_Client_${getCurrentDateTime()}")
            .setDescription("Downloading file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CFTalk.pdf")
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadId = downloadManager.enqueue(request)
//        var progressBar = 0

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                val bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                if (bytesDownloadedIndex >= 0 && bytesTotalIndex >= 0) {
                    cursor.moveToFirst()
                    val bytesDownloaded = cursor.getLong(bytesDownloadedIndex)
                    val bytesTotal = cursor.getLong(bytesTotalIndex)

                    progressBar = ((bytesDownloaded * 100) / bytesTotal).toInt()
                    // update progress UI here
                    Log.d("Download Progress", "$progressBar%")
                    binding.progressRounded.visibility = View.VISIBLE
                    binding.progressRounded.setProgressPercentage(progressBar.toDouble())

                    val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (statusColumnIndex != -1) {
                        val downloadStatus = cursor.getInt(statusColumnIndex)
                        if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                            // download complete, handle completion here
                            Log.d("Download Complete", "File downloaded successfully!")
                            fadeOutView(binding.progressRounded, 500)
                            Toast.makeText(context, "File downloaded successfully!", Toast.LENGTH_SHORT).show()
                            binding.progressRounded.setProgressPercentage(0.0)
                            handler.removeCallbacks(this)
                        } else if (downloadStatus == DownloadManager.STATUS_FAILED){
                            // download failed, handle failure here
                            Log.d("Download Failed", "File download failed!")
                            Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show()
                            binding.progressRounded.setProgressPercentage(0.0)
                            binding.progressRounded.visibility = View.GONE
                            handler.removeCallbacks(this)
                        }
                    }
                }

                cursor.close()

                if (progressBar < 100) {
                    handler.postDelayed(this, 5) // update progress every half second
                }
            }
        })
    }

    private fun downloadInternalCtalk() {
        val url = "https://ops.carefast.id/carefast/Detailcomplaint/dataDetailComplaintFilterCtalkClient/$projectCode/$startDate/$endDate"
        Log.d("AGRI","$url")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File CTalk-Internal_Client_${getCurrentDateTime()}")
            .setDescription("Downloading file")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "CFTalk.pdf")
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadId = downloadManager.enqueue(request)
//        var progressBar = 0

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor = downloadManager.query(query)

                val bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                if (bytesDownloadedIndex >= 0 && bytesTotalIndex >= 0) {
                    cursor.moveToFirst()
                    val bytesDownloaded = cursor.getLong(bytesDownloadedIndex)
                    val bytesTotal = cursor.getLong(bytesTotalIndex)

                    progressBar = ((bytesDownloaded * 100) / bytesTotal).toInt()
                    // update progress UI here
                    Log.d("Download Progress", "$progressBar%")
                    binding.progressRounded.visibility = View.VISIBLE
                    binding.progressRounded.setProgressPercentage(progressBar.toDouble())

                    val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (statusColumnIndex != -1) {
                        val downloadStatus = cursor.getInt(statusColumnIndex)
                        if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                            // download complete, handle completion here
                            Log.d("Download Complete", "File downloaded successfully!")
                            fadeOutView(binding.progressRounded, 500)
                            Toast.makeText(context, "File downloaded successfully!", Toast.LENGTH_SHORT).show()
                            binding.progressRounded.setProgressPercentage(0.0)
                            handler.removeCallbacks(this)
                        } else if (downloadStatus == DownloadManager.STATUS_FAILED){
                            // download failed, handle failure here
                            Log.d("Download Failed", "File download failed!")
                            Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show()
                            binding.progressRounded.setProgressPercentage(0.0)
                            binding.progressRounded.visibility = View.GONE
                            handler.removeCallbacks(this)
                        }
                    }
                }

                cursor.close()

                if (progressBar < 100) {
                    handler.postDelayed(this, 5) // update progress every half second
                }
            }
        })
    }
    fun fadeOutView(view: View, duration: Long) {
        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = duration

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        view.startAnimation(fadeOut)
    }

    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd_MMMM_yyyy_HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

}