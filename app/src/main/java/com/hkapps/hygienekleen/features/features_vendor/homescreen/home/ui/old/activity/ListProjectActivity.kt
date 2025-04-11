package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityListProjectBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject.ListProjectData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject.ListProjectModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.old.adapter.ListProjectAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.ListProjectViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListProjectActivity : AppCompatActivity(), ListProjectAdapter.ListProjectCallBack {

    private lateinit var binding: ActivityListProjectBinding
    private lateinit var projectAdapter: ListProjectAdapter
    private lateinit var listProjectModel: ListProjectModel
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    var projectName: String = ""
    var projectCode: String = ""

    private val viewModel: ListProjectViewModel by lazy {
        ViewModelProviders.of(this).get(ListProjectViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListProject.layoutManager = layoutManager

        binding.ivBackListProject.setOnClickListener {
            onBackPressed()
        }

        binding.ivDoneListProject.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.USER_PRO_PROJECT_NAME,
                projectName)

            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.USER_PRO_PROJECT_ID,
                projectCode
            )

            val intent = Intent(this, HomeVendorActivity::class.java)
            startActivity(intent)
        }

        setObserver()
        loadData()

    }

    private fun loadData() {
        viewModel.getListProject(userId)
    }

    private fun setObserver() {
        viewModel.getListProject().observe(this, { it ->
            if (it.code == 200) {
                listProjectModel = it
                projectAdapter = ListProjectAdapter(
                    this,
                    it.data.projects as ArrayList<ListProjectData>
                ).also { it.setListener(this) }
                binding.rvListProject.adapter = projectAdapter
            }
        })
    }

    override fun onClickListProject(projectName: String, projectCode: String) {
        this.projectName = projectName
        this.projectCode = projectCode

    }
}