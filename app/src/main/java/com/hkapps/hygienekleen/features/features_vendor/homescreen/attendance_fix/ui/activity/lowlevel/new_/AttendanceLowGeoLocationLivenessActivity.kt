package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.lowlevel.new_

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityAttendanceLowGeoLocationLivenessBinding
import com.hkapps.hygienekleen.features.facerecog.model.request.Images
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import com.hkapps.liveness.detection.FaceAnalyzer
import com.hkapps.liveness.detection.LivenessDetector
import com.hkapps.liveness.tasks.DetectionTask
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class AttendanceLowGeoLocationLivenessActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAttendanceLowGeoLocationLivenessBinding

    private lateinit var cameraController: LifecycleCameraController
    private var isRegister: Boolean = true
    private var isManagement: Boolean = false
    private var isDone = false
    private var countDownTimer: CountDownTimer? = null

    private val faceRecognitionViewModel by lazy {
        ViewModelProvider(this)[FaceRecogViewModel::class.java]
    }

    private val detector : LivenessDetector = LivenessDetector()

    private var currentTaskIndex = 0

    private val imageFiles = mutableListOf<String>()

    private val images = mutableListOf<Images>()

    private val countDownDuration = 30 * 1000L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    var monthss = ""
    var pass: String = ""

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceLowGeoLocationLivenessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)

        if(isInternetAvailable(this)){
            getGestured()
            permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            lifecycleScope.launch {
                delay(1000)
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }

            isRegister = intent.getBooleanExtra("is_register",false)
            isManagement = intent.getBooleanExtra("is_management",false)

            binding.overlayView.apply {
                init(
                    borderColorRes = R.color.red,
                    progressColorRes = R.color.colorAccent,
                    progressMarginRes = R.dimen._10ssp
                )
                invalidate()
            }
            binding.imgClose.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)

            binding.btnStart.setOnClickListener {
                binding.btnStart.visibility = View.GONE
                binding.cardError.visibility = View.GONE
                binding.cardProgress.visibility = View.VISIBLE
                startCountDownTimer()
            }
        }else{
            Toast.makeText(this, "Tidak ada koneksi internet.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun startCamera() {
        cameraController = LifecycleCameraController(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(this),
            FaceAnalyzer(buildLivenessDetector())
        )
        cameraController.bindToLifecycle(this)
        binding.cameraPreview.controller = cameraController

    }

    private fun startCountDownTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(countDownDuration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val countdown = "$secondsRemaining detik"
                binding.tvCountdown.text = countdown
            }

            override fun onFinish() {
                binding.cardError.visibility = View.VISIBLE
                isDone = true
                resetLivenessCheck()
            }
        }.start()
    }

    private fun buildLivenessDetector(): LivenessDetector {
        val listener = object : LivenessDetector.Listener {
            @SuppressLint("SetTextI18n")
            override fun onTaskStarted(task: DetectionTask) {
                binding.tvGesture.text = detector.randomizedTasks[currentTaskIndex]
            }

            override fun onTaskCompleted(task: DetectionTask, isLastTask: Boolean) {
                takePhoto(
                    File(
                        cacheDir,
                        "${task.taskName() + "_" + System.currentTimeMillis()}.jpg"
                    )
                ) {
                    imageFiles.add(it.absolutePath)
                    if(currentTaskIndex != 0){
                        images.add(Images(task.taskName(),it.absolutePath))
                    }

                    if (isLastTask) {
                        binding.cardFinish.visibility = View.VISIBLE
                        binding.cardProgress.visibility = View.GONE
                        binding.btnStart.visibility = View.GONE
                        Handler().postDelayed({
                            finishForResult()
                            isDone = true
                        },2000)
                    }
                }
                // LAST INDEX
                if (isLastTask) {
                    binding.overlayView.setProgress(100F)
                    binding.overlayView.setBorderColor(Color.parseColor("#219653"))
                    binding.apply {
                        countDownTimer?.cancel() }
                } else {
                    // NOT LAST INDEX || PROCESS
                    binding.cardError.visibility = View.GONE
                    binding.overlayView.setBackgroundProgressVisibility(true)
                    if(currentTaskIndex == 0){
                        startCountDownTimer()
                        binding.overlayView.setProgress(0F)
                    }
                    if(currentTaskIndex != 0){
                        binding.overlayView.setProgress(binding.overlayView.progressValue + 35)
                    }
                    binding.tvInfo.visibility = View.GONE
                    binding.cardError.visibility = View.GONE
                    binding.cardProgress.visibility = View.VISIBLE
                    binding.overlayView.setBorderColor(Color.parseColor("#0064FB"))
                    currentTaskIndex++
//                    binding.tvGesture.text = randomizedTasks[currentTaskIndex]
                }
            }

            override fun onTaskFailed(task: DetectionTask, code: Int) {
                isDone = false
                failedVisibility()
                getGestured()
                when (code) {
                    LivenessDetector.ERROR_MULTI_FACES -> {
                        binding.tvInfo.visibility = View.VISIBLE
                        binding.tvInfo.text = resources.getString(R.string.text_only_one_face)
                    }
                    LivenessDetector.ERROR_NO_FACE -> {
                        binding.tvInfo.visibility = View.VISIBLE
                        binding.tvInfo.text = resources.getString(R.string.text_no_face)
                    }
                    LivenessDetector.ERROR_OUT_OF_DETECTION_RECT -> {
                        binding.tvInfo.visibility = View.VISIBLE
                        binding.tvInfo.text = resources.getString(R.string.text_face_is_out)

                    }
                    else -> {
                        Toast.makeText(
                            this@AttendanceLowGeoLocationLivenessActivity,
                            "${task.taskName()} Failed.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        return detector.also { it.setListener(listener) }
    }

    private fun takePhoto(file: File, onSaved: (File) -> Unit) {
        cameraController.takePicture(
            ImageCapture.OutputFileOptions.Builder(file).build(),
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    e.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    onSaved(file)
                }
            }
        )
    }

    private fun failedVisibility(){
        currentTaskIndex = 0
        imageFiles.clear()
        images.clear()
        binding.overlayView.setProgress(0F)
        binding.btnStart.visibility = View.GONE
        binding.cardProgress.visibility = View.GONE
        binding.overlayView.setBackgroundProgressVisibility(false)
        binding.overlayView.setBorderColor(Color.parseColor("#FF0000"))
    }

    private fun resetLivenessCheck() {
        countDownTimer?.cancel()
        currentTaskIndex = 0
        imageFiles.clear()
        images.clear()
        binding.overlayView.setBorderColor(Color.parseColor("#FF0000"))
        binding.cardProgress.visibility = View.GONE
        Handler().postDelayed({
            finish()
        },500)
    }

    private fun getGestured(){
        faceRecognitionViewModel.getListGesture()
        faceRecognitionViewModel.listGesture.observe(this) {
            if (it != null && it.code == 200) {
                detector.listTaskDetection.clear()
                detector.listTaskDetection.addAll(it.data)
                detector.resetTasks()
            } else {
                Toast.makeText(this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun finishForResult(){
        val result = ArrayList(imageFiles.takeLast(detector.getTaskSize()))
        val results = ArrayList(images.takeLast(detector.getTaskSize()))
        startActivity(Intent(this,AttendanceLowGeoLocationPhoto::class.java).also{
            it.putExtra("image",result)
            it.putExtra("images",results)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}