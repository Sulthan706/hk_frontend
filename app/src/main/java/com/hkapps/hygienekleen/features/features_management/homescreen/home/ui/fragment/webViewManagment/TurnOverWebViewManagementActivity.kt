package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment.webViewManagment

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hkapps.hygienekleen.databinding.ActivityTurnOverWebViewManagementBinding

class TurnOverWebViewManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTurnOverWebViewManagementBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTurnOverWebViewManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.webViewTurnOver
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide loading indicator or perform other actions after page loads
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                // Handle web page loading errors
            }
        }
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("https://ops.carefast.id/")

    }
}