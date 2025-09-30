package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.databinding.ActivityFabSearchAttendanceBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch.DataFabSearch
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab.FabSearchPersonAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class FabSearchAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFabSearchAttendanceBinding
    private val viewModel: AttendanceFixViewModel by viewModels()
    // pref
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    //val
    private var keywords: String = ""
    private var page: Int = 0
    //adapter
    lateinit var adapters: FabSearchPersonAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFabSearchAttendanceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

//        binding.ivBackSearchProjectAttend.setOnClickListener {
//            onBackPressed()
//        }
        binding.appbarSearchFabPerson.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        binding.appbarSearchFabPerson.svAppbarSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvFabSearchPerson.visibility = View.VISIBLE
                loadData(query!!)
                return true
            }

            override fun onQueryTextChange(keywords: String): Boolean {
                binding.rvFabSearchPerson.visibility = View.VISIBLE
                binding.llEmptySearchPerson.visibility = View.GONE
                loadData(keywords)
                return true
            }
        })

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFabSearchPerson.layoutManager = layoutManager


        setObserver()
        //oncreate ^
    }
    //fun
    private fun loadData(keywords: String){
        viewModel.getSearchPerson(projectCode, page , keywords)
    }
    private fun setObserver(){
        viewModel.getSearchPerson().observe(this, Observer {
            if (it.code == 200){
                if (it.data.isNullOrEmpty()){
                    Toast.makeText(this, "data kosong", Toast.LENGTH_SHORT).show()
                }
                adapters = FabSearchPersonAdapter(it.data as ArrayList<DataFabSearch>)
                binding.rvFabSearchPerson.adapter =adapters
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}