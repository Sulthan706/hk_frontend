package com.hkapps.hygienekleen.features.features_client.overtime.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailOvertimeRequestClientBinding
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel

class DetailOvertimeRequestClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOvertimeRequestClientBinding
    private var overtimeId: Int = 0

    private val viewModel: OvertimeClientViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOvertimeRequestClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get overtime id
        overtimeId = intent.getIntExtra("overtimeReqIdClient", 0)

        // change status bar color
        val window: Window = this.window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarDetailOvertimeRequestClient.tvAppbarTitle.text = "Permohonan Lembur Tagih"
        binding.appbarDetailOvertimeRequestClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.detailOvertimeRequestClientModel().observe(this) {
            if (it.code == 200) {
                binding.tvTitleDetailOvertimeRequestClient.text = it.data.title
                binding.tvDateDetailOvertimeRequestClient.text = it.data.atDate
                binding.tvTimeDetailOvertimeRequestClient.text = "${it.data.startAt} - ${it.data.endAt}"
                binding.tvLocDetailOvertimeRequestClient.text = it.data.locationName
                binding.tvSubLocDetailOvertimeRequestClient.text = it.data.subLocationName
                binding.tvWorkerDetailOvertimeRequestClient.text = "${it.data.totalWorker} orang"
                binding.tvDescDetailOvertimeRequestClient.text = it.data.description
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailOvertimeReqClient(overtimeId)
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}