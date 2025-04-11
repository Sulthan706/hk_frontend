package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityListKontrolAreaBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter.ListKontrolAreaAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListKontrolAreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListKontrolAreaBinding
    private lateinit var rvAdapter: ListKontrolAreaAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private var date = ""
    private var typeVisit = ""

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKontrolAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarListKontrolArea.tvAppbarTitle.text = "Kontrol Area"
        binding.appbarListKontrolArea.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set current date
        val today = Calendar.getInstance()
        val day = android.text.format.DateFormat.format("dd", today) as String
        val month = android.text.format.DateFormat.format("MMM", today) as String
        val months = android.text.format.DateFormat.format("MM", today) as String
        val year = android.text.format.DateFormat.format("yyyy", today) as String
        binding.tvDateListKontrolArea.text = "$day $month $year"
        date = "$year-$months-$day"

        // open dialog choose date
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                date = dateParam.format(cal.time)
                binding.tvDateListKontrolArea.text = sdf.format(cal.time)
                loadData()
                setObserver()
            }

        binding.tvDateListKontrolArea.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val objectValue = resources.getStringArray(R.array.tujuanKunjungan)
        val adapter = ArrayAdapter (this, R.layout.spinner_item, objectValue)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerListKontrolArea.adapter = adapter
        binding.spinnerListKontrolArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                typeVisit = when (position) {
                    0 -> "SEMUA"
                    1 -> "KUNJUNGAN RUTIN"
                    2 -> "SIDAK"
                    else -> ""
                }

                loadData()
                setObserver()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListKontrolArea.layoutManager = layoutManager

        // set on click create laporan
        binding.ivCreateListKontrolArea.setOnClickListener {
            startActivity(Intent(this, FormKontrolAreaActivity::class.java))
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listKontrolAreaResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvListKontrolArea.visibility = View.VISIBLE
                        binding.tvEmptyListKontrolArea.visibility = View.GONE
                        rvAdapter = ListKontrolAreaAdapter(
                            this,
                            it.data as ArrayList<Data>
                        )
                        binding.rvListKontrolArea.adapter = rvAdapter
                    }, 1500)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.rvListKontrolArea.adapter = null
                        binding.tvEmptyListKontrolArea.visibility = View.VISIBLE
                    }, 1500)
                }
            } else {
                binding.rvListKontrolArea.adapter = null
                Toast.makeText(this, "Gagal mengambil data list kontrol area", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListKontrolArea(userId, projectCode, date, typeVisit)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}