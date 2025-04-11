package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityFilterAbsentOprMgmntBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class FilterAbsentOprMgmntActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterAbsentOprMgmntBinding
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var month: Int = 0
    private var projectCode = ""
    private var jobRole = ""
    private var page = 0
    private val perPage = 10

    private val viewModel: AbsentOprMgmntViewModel by lazy {
        ViewModelProviders.of(this).get(AbsentOprMgmntViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterAbsentOprMgmntBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        if (userLevel == "CLIENT") {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // finally change the color
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)
            binding.appbarFilterAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.secondary_color)
        } else {
            binding.appbarFilterAbsentOprMgmnt.llAppbar.setBackgroundResource(R.color.primary_color)
        }
        binding.appbarFilterAbsentOprMgmnt.tvAppbarTitle.text = "Filter Absen"
        binding.appbarFilterAbsentOprMgmnt.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // set spinner month
        val listMont = resources.getStringArray(R.array.listMonth)
        val adapterMonth = ArrayAdapter(this, R.layout.spinner_item, listMont)
        binding.spinnerMonthFilterAbsentOprMgmnt.adapter = adapterMonth
        binding.spinnerMonthFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != 0) {
                    month = p2
                    viewModel.getListBranchAbsentOpr()
                    Log.d("filterAbsentOprMgmnt", "onItemSelected: bulan ${listMont[p2]} = $month ")
                } else {
                    setSpinnerProjectNoData()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // set spinner branch
        val data = ArrayList<String>()
        data.add("Pilih Cabang")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
        binding.spinnerBranchFilterAbsentOprMgmnt.adapter = adapter
        viewModel.listBranchAbsentOprResponseModel.observe(this) {
            val length = it.data.size
            for (i in 0 until  length) {
                data.add(it.data[i].branchName)
            }
//            val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
//            binding.spinnerBranchFilterAbsentOprMgmnt.adapter = adapter
            binding.spinnerBranchFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    val branch = data[position]
                    Log.d("filterAbsentOprMgmnt", "onItemSelected: branch $branch ")

                    // set spinner project
                    if (position == 0) {
                        setSpinnerProjectNoData()
                    } else {
                        viewModel.getListProjectByBranch(it.data[position+1].branchCode, page, perPage)
                        setSpinnerProject()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        // set spinner project
//        val dataProject = ArrayList<String>()
//        dataProject.add("Pilih Project")
//        val adapterProject = ArrayAdapter(this, R.layout.spinner_item, dataProject)
//        binding.spinnerProjectFilterAbsentOprMgmnt.adapter = adapterProject
//        viewModel.listProjectByBranchAbsentOprResponseModel.observe(this) {
//            val length = it.data.content.size
//            for (i in 0 until length) {
//                dataProject.add(it.data.content[i].projectName)
//            }
//            binding.spinnerProjectFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                    projectCode = dataProject[p2]
//                    Log.d("filterAbsentOprMgmnt", "onItemSelected: projectCode $projectCode ")
//
//                    // set spinner job role
//                    val dataJobRole = when(p2) {
//                        0 -> resources.getStringArray(R.array.noListJobRole)
//                        else -> resources.getStringArray(R.array.listJobRole)
//                    }
//                    setSpinnerJobRole(dataJobRole)
//
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//
//                }
//
//            }
//        }

        // set spinner job role
        val dataJobRole = if (projectCode == "" || projectCode == "Pilih Project") {
            resources.getStringArray(R.array.noListJobRole)
        } else {
            resources.getStringArray(R.array.listJobRole)
        }
        val adapterJobRole = ArrayAdapter(this, R.layout.spinner_item, dataJobRole)
        binding.spinnerJobRoleFilterAbsentOprMgmnt.adapter = adapterJobRole
        binding.spinnerJobRoleFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                jobRole = dataJobRole[p2]
                Log.d("filterAbsentOprMgmnt", "onItemSelected: jobRole $jobRole ")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun setSpinnerProjectNoData() {
        val dataProject = ArrayList<String>()
        dataProject.add("Pilih Project")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dataProject)
        binding.spinnerProjectFilterAbsentOprMgmnt.adapter = adapter
    }

    private fun setSpinnerProject() {
        viewModel.listProjectByBranchAbsentOprResponseModel.observe(this) {
            val dataProject = ArrayList<String>()
            dataProject.add("Pilih Project")
            val length = it.data.content.size
            for (i in 0 until length) {
                dataProject.add(it.data.content[i].projectName)
            }
            val adapterProject = ArrayAdapter(this, R.layout.spinner_item, dataProject)
            binding.spinnerProjectFilterAbsentOprMgmnt.adapter = adapterProject
            binding.spinnerProjectFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    projectCode = dataProject[p2]
                    Log.d("filterAbsentOprMgmnt", "onItemSelected: projectCode $projectCode ")

                    // set spinner job role
                    val dataJobRole = when(p2) {
                        0 -> resources.getStringArray(R.array.noListJobRole)
                        else -> resources.getStringArray(R.array.listJobRole)
                    }
//                    setSpinnerJobRole(dataJobRole)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }

    private fun setSpinnerJobRole(dataJobRole: Array<String>) {
        val adapterJobRole = ArrayAdapter(this, R.layout.spinner_item, dataJobRole)
        binding.spinnerJobRoleFilterAbsentOprMgmnt.adapter = adapterJobRole
        binding.spinnerJobRoleFilterAbsentOprMgmnt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                jobRole = dataJobRole[p2]
                Log.d("filterAbsentOprMgmnt", "onItemSelected: jobRole $jobRole ")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}