package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivitySearchProjectAttendanceBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.ui.adapter.ProjectsAllAttendanceAdapter
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class SearchProjectAttendanceActivity : AppCompatActivity(), ProjectsAllAttendanceAdapter.ProjectAllCallBack {

    private lateinit var binding: ActivitySearchProjectAttendanceBinding
    private lateinit var rvAdapter: ProjectsAllAttendanceAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var page = 0
    private var isLastPage = false
    private var searchQuery: String = ""

    private val viewModel: AttendanceManagementViewModel by lazy {
        ViewModelProviders.of(this).get(AttendanceManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProjectAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)

        // set back button
        binding.ivBackSearchProjectAttend.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        setFirstLayout()

        binding.svBackSearchProjectAttend.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query == "") {
                    binding.shimmerBackSearchProjectAttend.visibility = View.GONE
                    binding.llEmptyBackSearchProjectAttend.visibility = View.GONE
                    binding.llDefaultBackSearchProjectAttend.visibility = View.VISIBLE
                    binding.rvBackSearchProjectAttend.adapter = null
                } else {
                    binding.shimmerBackSearchProjectAttend.startShimmerAnimation()
                    binding.shimmerBackSearchProjectAttend.visibility = View.VISIBLE
                    binding.rvBackSearchProjectAttend.visibility = View.GONE
                    binding.llDefaultBackSearchProjectAttend.visibility = View.GONE
                    binding.llEmptyBackSearchProjectAttend.visibility = View.GONE
                    binding.rvBackSearchProjectAttend.adapter = null
                    loadData(query)
                    searchQuery = query
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query == null || query == "") {
                    binding.shimmerBackSearchProjectAttend.visibility = View.GONE
                    binding.llEmptyBackSearchProjectAttend.visibility = View.GONE
                    binding.llDefaultBackSearchProjectAttend.visibility = View.VISIBLE
                    binding.rvBackSearchProjectAttend.adapter = null
                }
                return true
            }

        })

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvBackSearchProjectAttend.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(searchQuery)
                }
            }

        }
        binding.rvBackSearchProjectAttend.addOnScrollListener(scrollListener)

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun loadData(string: String) {
        if (userLevel == "BOD" || userLevel == "CEO") {
            viewModel.getSearchProjectAll(page, string)
        } else {
            viewModel.getSearchProjectManagement(userId, page, string)
        }
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.shimmerBackSearchProjectAttend.stopShimmerAnimation()
                        binding.shimmerBackSearchProjectAttend.visibility = View.GONE
                    }, 1500)
                }
            }
        }
        viewModel.searchAllProjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvBackSearchProjectAttend.visibility = View.VISIBLE
                        binding.llEmptyBackSearchProjectAttend.visibility = View.GONE
                    }, 1500)
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsAllAttendanceAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvBackSearchProjectAttend.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.llEmptyBackSearchProjectAttend.visibility = View.VISIBLE
                        binding.rvBackSearchProjectAttend.visibility = View.GONE
                        binding.rvBackSearchProjectAttend.adapter = null
                    }, 1500)
                }
            } else {
                binding.llDefaultBackSearchProjectAttend.visibility = View.VISIBLE
                binding.rvBackSearchProjectAttend.adapter = null
                Toast.makeText(this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.searchManagementProjectModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvBackSearchProjectAttend.visibility = View.VISIBLE
                        binding.llEmptyBackSearchProjectAttend.visibility = View.GONE
                    }, 1500)
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ProjectsAllAttendanceAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it1 -> it1.setListener(this) }
                        binding.rvBackSearchProjectAttend.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.llEmptyBackSearchProjectAttend.visibility = View.VISIBLE
                        binding.rvBackSearchProjectAttend.visibility = View.GONE
                        binding.rvBackSearchProjectAttend.adapter = null
                    }, 1500)
                }
            } else {
                binding.llDefaultBackSearchProjectAttend.visibility = View.VISIBLE
                binding.rvBackSearchProjectAttend.adapter = null
                Toast.makeText(this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFirstLayout() {
        binding.llDefaultBackSearchProjectAttend.visibility = View.VISIBLE
        binding.shimmerBackSearchProjectAttend.visibility = View.GONE
        binding.rvBackSearchProjectAttend.visibility = View.GONE
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickProjectAll(
        projectCode: String,
        projectName: String,
        latitude: String,
        longitude: String,
        radius: Int
    ) {
        if (latitude == "" || latitude == "null" || latitude == null ||
            longitude == "" || longitude == "null" || longitude == null ||
            radius == 0 || radius == null) {
            Toast.makeText(this, "Silahkan pilih project lain", Toast.LENGTH_SHORT).show()
        } else {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.PROJECT_ID_ATTENDANCE_GEO, projectCode)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.LATITUDE_ATTENDANCE_GEO, latitude)
            CarefastOperationPref.saveString(CarefastOperationPrefConst.LONGITUDE_ATTENDANCE_GEO, longitude)
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.RADIUS_ATTENDANCE_GEO, radius)

            Toast.makeText(this, "Project yang dipilih $projectCode", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AttendanceGeoManagementActivity::class.java))
            finish()
        }
    }
}