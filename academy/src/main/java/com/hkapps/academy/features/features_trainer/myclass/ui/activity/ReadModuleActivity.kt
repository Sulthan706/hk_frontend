package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.hkapps.academy.databinding.ActivityReadModuleBinding
import com.hkapps.academy.features.features_trainer.myclass.data.service.PdfApi
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ReadModuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadModuleBinding

    private var moduleName = ""

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moduleName = intent.getStringExtra("moduleName").toString()

        // set appbar
        binding.appbarReadModule.tvAppbarTitle.text = moduleName
        binding.appbarReadModule.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set pdf view
//        val pdfView = binding.pdfViewReadModule
//
//        GlobalScope.launch(Dispatchers.Main) {
//            val pdfUrl = "http://54.251.83.205/assets.admin_master/academy/pdf/modules/MODULE_tesss_tes.pdf" // URL of the PDF file on the server
//            val pdfContent = fetchPdfFile(pdfUrl)
//
//            try {
//                pdfView.fromBytes(pdfContent.toByteArray())
//                    .load()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
    }

    private fun fetchPdfFile(url: String): String {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.251.83.205/") // Base URL of the server
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(PdfApi::class.java)
        return api.getPdf(url)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}