package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFormKontrolAreaBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.ui.activity.LaporanKondisiAreaActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import java.util.*

class FormKontrolAreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormKontrolAreaBinding
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, "")
    private val projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private var tujuanKunjungan = ""
    private var date = ""

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormKontrolAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarFormKontrolArea.tvAppbarTitle.text = "Form Kontrol Area"
        binding.appbarFormKontrolArea.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set default data
        binding.tvBranchFormKontrolArea.text = branchName
        binding.tvProjectFormKontrolArea.text = projectName
        binding.tvCreatedFormKontrolArea.text = userName

        // set current date
        val today = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", today) as String
        val month = android.text.format.DateFormat.format("MMMM", today) as String
        val months = android.text.format.DateFormat.format("MM", today) as String
        val year = android.text.format.DateFormat.format("yyyy", today) as String
        binding.tvDateFormKontrolArea.text = "$day $month $year"
        date = "$year-$months-$day"

        // set spinner
        val objectValue = resources.getStringArray(R.array.tujuanKunjunganArea)
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerFormKontrolArea.adapter = spinnerAdapter
        binding.spinnerFormKontrolArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                tujuanKunjungan = objectValue[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // set on click manpower
        binding.rlManpowerFormKontrolArea.setOnClickListener {
            startActivity(Intent(this, PenilaianManpowerInspeksiActivity::class.java))
        }
        // set on click kondisi area
        binding.rlKondisiAreaFormKontrolArea.setOnClickListener {
            startActivity(Intent(this, LaporanKondisiAreaActivity::class.java))
        }
    }

    fun showDialogSubmit() {
        val dialog = BottomSheetDialog(this)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_form_kontrol_area)
        val btnBack = dialog.findViewById<ImageView>(R.id.btnBackDialogFormKontrolArea)
        val btnYes = dialog.findViewById<ImageView>(R.id.btnYesDialogFormKontrolArea)

        btnBack?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}