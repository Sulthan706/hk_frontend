package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityClosingManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.fragment.GenerateFileManagementFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.fragment.SendEmailClosingManagementFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter.ClosingSpvAdapter

class ClosingManagementActivity : AppCompatActivity() {

    private lateinit var binding : ActivityClosingManagementBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showDialogNotClosing()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosingManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()

    }

    private fun setViewPager(){
        val fragmentList = arrayListOf(
            GenerateFileManagementFragment(),
            SendEmailClosingManagementFragment()
        )

        val adapter = ClosingSpvAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )

        val appBarName = "Generate Laporan Harian"
        binding.appBarGenerateFile.tvAppbarTitle.text = appBarName

        binding.appBarGenerateFile.ivAppbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.viewPagerClosing.adapter = adapter
        binding.viewPagerClosing.isUserInputEnabled = false
        binding.viewPagerClosing.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.tvGenerateFile.setTextColor(ContextCompat.getColor(binding.tvGenerateFile.context,R.color.green))
                        binding.tvSendEmail.setTextColor(ContextCompat.getColor(binding.tvGenerateFile.context,R.color.grey2))
                        binding.appBarGenerateFile.ivAppbarBack.setOnClickListener {
                            finish()
                        }
                    }
                    1 -> {
                        binding.tvGenerateFile.setTextColor(ContextCompat.getColor(binding.tvGenerateFile.context,R.color.green))
                        binding.tvSendEmail.setTextColor(ContextCompat.getColor(binding.tvSendEmail.context,R.color.green))
                        binding.appBarGenerateFile.ivAppbarBack.setOnClickListener {
                            showDialogNotClosing()
                        }
                    }
                }
            }
        })
    }

    private fun showDialogNotClosing() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_closing)

        val yesBtn = dialog.findViewById<AppCompatButton>(R.id.btn_check_and_closing)
        val tvInformation = dialog.findViewById<TextView>(R.id.tv_not_closing_information)
        val tvDesc = dialog.findViewById<TextView>(R.id.tv_not_closing_desc)
        tvInformation.text = "Harap selesaikan proses pengiriman\n email terlebih dahulu"
        tvDesc.visibility = View.GONE
        yesBtn.text = "Kembali"
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}