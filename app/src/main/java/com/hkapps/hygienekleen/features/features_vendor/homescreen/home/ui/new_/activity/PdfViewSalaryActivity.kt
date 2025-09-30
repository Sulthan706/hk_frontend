package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.databinding.ActivityPdfViewSalaryBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class PdfViewSalaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewSalaryBinding
    private val salaryUrl = CarefastOperationPref.loadString(CarefastOperationPrefConst.URL_SALARY, "")

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)

        // prevent screen capture on this layout
        window.setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE)

        binding.webViewSalary.settings.apply {
            javaScriptEnabled = true
            displayZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        val i = intent.getStringExtra("file")

//        binding.webViewSalary.webViewClient = object : WebViewClient() {
//            @Deprecated("Deprecated in Java")
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(url!!)
//                return true
//            }
//        }

        binding.webViewSalary.setInitialScale(-10)
        if(i != null){
            binding.webViewSalary.visibility = View.VISIBLE
            binding.webViewSalary.settings.apply {
                javaScriptEnabled = true // Aktifkan JavaScript
                builtInZoomControls = true // Tambahkan kontrol zoom
                displayZoomControls = false // Sembunyikan tombol zoom (agar bisa pinch-to-zoom)
                loadWithOverviewMode = true
                useWideViewPort = true
            }

            val pdfUrl = "https://ops.carefast.id/carefast/assets/rkbreport/rkbdaily-013950110-2024-11-10.pdf"
            val googleDocsUrl = "https://docs.google.com/viewer?url=$i"
            binding.webViewSalary.loadUrl(googleDocsUrl)
//            binding.pdfView.visibility = View.GONE
//            binding.pdfView.initWithUrl(
//                url = "https://ops.hk-indonesia.id/hksite/assets/rkbreport/rkbdaily-09020102-2025-03-12.pdf",
//                lifecycleCoroutineScope = lifecycleScope,
//                lifecycle = lifecycle
//            )
        }else{
            binding.webViewSalary.visibility = View.VISIBLE
            binding.pdfView.visibility = View.GONE
            binding.webViewSalary.loadUrl(salaryUrl)
        }

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}