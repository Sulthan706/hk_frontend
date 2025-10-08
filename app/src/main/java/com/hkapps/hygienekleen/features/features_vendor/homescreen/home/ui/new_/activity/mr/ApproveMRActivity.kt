package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityApproveMractivityBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.MRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.mr.DashboardMRActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.MrAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.TableViewAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.Calendar
import kotlin.getValue

class ApproveMRActivity : AppCompatActivity() {
    private lateinit var binding : ActivityApproveMractivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApproveMractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}