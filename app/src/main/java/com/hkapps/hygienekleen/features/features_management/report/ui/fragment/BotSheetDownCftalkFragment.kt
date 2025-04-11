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
import com.hkapps.hygienekleen.databinding.FragmentBotSheetDownCftalkBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class BotSheetDownCftalkFragment : BottomSheetDialogFragment() {

    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECTCODE_FILTER, "")
    private val startDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STARTDATE_DOWNLOAD, "")
    private val endDate =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ENDDATE_DOWNLOAD, "")

    private lateinit var binding: FragmentBotSheetDownCftalkBinding
    private var selectedItem = ""
    var progressBar = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetDownCftalkBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBtnCloseBottomCftalk.setOnClickListener {
            dismiss()
        }
        binding.rgRadioBtnCftalk.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbInternalCftalk) {
                selectedItem = binding.rbInternalCftalk.text.toString()
            }
            if (checkedId == R.id.rbManajemenCftalk) {
                selectedItem = binding.rbManajemenCftalk.text.toString()
            }
        }
        binding.rbInternalCftalk.isChecked = true
        binding.btnDownloadReport.setOnClickListener {
            if (selectedItem == "Internal area (pengawas)"){
                downloadInternalCftalk()
            } else {
                downloadManajemenCftalk()
            }
        }
    }

    private fun downloadManajemenCftalk() {
        val url = "https://ops.carefast.id/carefast/Detailcomplaint/dataDetailComplaintFilterCftalkManagementHO/$projectCode/$startDate/$endDate"
        Log.d("AGRI","$url")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File CFTalk-Manajemen_${getCurrentDateTime()}")
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

    private fun downloadInternalCftalk() {
        val url = "https://ops.carefast.id/carefast/Detailcomplaint/dataDetailComplaintFilterCftalkInternal/$projectCode/$startDate/$endDate"
        Log.d("AGRI","$url")
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("File CFTalk-Internal_${getCurrentDateTime()}")
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