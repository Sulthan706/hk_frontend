package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ActivityWeeklyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.adapter.WeeklyProgressAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeeklyProgressActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityWeeklyProgressBinding

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private var page = 0

    private var isLastPage = false

    private var size = 10

    private lateinit var weeklyProgressAdapter : WeeklyProgressAdapter

    private val weeklyProgressViewModel by lazy {
        ViewModelProviders.of(this)[WeeklyProgressViewModel::class.java]
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeeklyProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        val appBar = "Daily Progress"
        binding.appbarWeeklyProgress.tvAppbarTitle.text = appBar
        binding.appbarWeeklyProgress.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                recreate()
            }
        }

        binding.fabAdd.setOnClickListener {
            val i = Intent(this,FormWeeklyProgressActivity::class.java)
            resultLauncher.launch(i)
        }
        binding.tvDate.text = getCurrentDateFormatted()
        binding.shimmerLayout.startShimmerAnimation()
        binding.shimmerLayout.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
             loadData()
            getDataWeeklyProgress()
        }, 1500)
        setRecycler()
        binding.tvSeeAll.setOnClickListener {
            startActivity(Intent(this,ListWeeklyProgressActivity::class.java))
        }
    }

    private fun loadData(){ weeklyProgressViewModel.getListWeeklyProgress(projectCode,userId,getCurrentDateRequest(),getCurrentDateRequest(),"All",page,size) }

    private fun setRecycler(){
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListWeeklyProgress.layoutManager = layoutManager
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }
        binding.rvListWeeklyProgress.addOnScrollListener(scrollListener)
    }

    private fun getDataWeeklyProgress(){
        weeklyProgressViewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi Kesalahan pada saat pengambilan data", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmerLayout.stopShimmerAnimation()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvListWeeklyProgress.visibility = View.VISIBLE
                }
            }
        }

        weeklyProgressViewModel.listWeeklyProgressResponse.observe(this){
            if(it != null && it.code == 200){
                val filteredData = filterByToday(it.data.content)
                Log.d("DATAX1","${it.data.content}")
                Log.d("DATAXX1","$filteredData")
                if(filteredData.isNotEmpty()){
                    binding.rvListWeeklyProgress.visibility = View.VISIBLE
                    isLastPage = it.data.last
                    if (page == 0) {
                        weeklyProgressAdapter = WeeklyProgressAdapter(userName,filteredData as MutableList<Content>,object  : WeeklyProgressAdapter.OnClickWeeklyProgressAdapter{
                            override fun onDetail(data: Content) {
                                val i = Intent(this@WeeklyProgressActivity,DetailWeeklyProgressActivity::class.java).also{
                                    it.putExtra("id_weekly",data.idWeekly)
                                }
                                resultLauncher.launch(i)

                            }
                        })
                        binding.rvListWeeklyProgress.adapter = weeklyProgressAdapter
                        binding.tvSeeAll.visibility = View.VISIBLE
                        binding.tvEmpty.visibility = View.GONE
                    } else {
                        weeklyProgressAdapter.data.addAll(it.data.content)
                        weeklyProgressAdapter.notifyItemRangeChanged(
                            weeklyProgressAdapter.data.size - it.data.content.size,
                            weeklyProgressAdapter.data.size
                        )
                    }
                }else{
                    binding.tvSeeAll.visibility = View.GONE
                    binding.rvListWeeklyProgress.adapter = null
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }else{
                Toast.makeText(this, "Gagal mengambil data weekly progress", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterByToday(contents: List<Content>): List<Content> {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        return contents.filter { content ->
            val date = format.parse(content.createdAt)
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
            }
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.time == today.time
        }
    }
    private fun getCurrentDateFormatted(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        return formatter.format(currentDate)
    }

    private fun getCurrentDateRequest(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        return formatter.format(currentDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}