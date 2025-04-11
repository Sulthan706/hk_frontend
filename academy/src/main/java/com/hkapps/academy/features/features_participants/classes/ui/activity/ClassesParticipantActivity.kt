package com.hkapps.academy.features.features_participants.classes.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ActivityClassesParticipantBinding
import com.hkapps.academy.features.features_participants.classes.model.listClass.Content
import com.hkapps.academy.features.features_participants.classes.ui.adapter.ClassesParticipantAdapter
import com.hkapps.academy.features.features_participants.classes.viewmodel.ClassParticipantViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import java.util.Date
import java.util.TimeZone

class ClassesParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassesParticipantBinding
    private lateinit var rvAdapter: ClassesParticipantAdapter

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_PROJECT_CODE, "")
    private val levelJabatan = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_LEVEL_POSITION, "")
    private var isLastPage = false
    private var page = 0
    private var size = 10
    private var date = ""
    private var region = ""

    private val viewModel: ClassParticipantViewModel by lazy {
        ViewModelProviders.of(this).get(ClassParticipantViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassesParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarClassesParticipant.tvAppbarTitle.text = "Daftar Kelas"
        binding.appbarClassesParticipant.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.appbarClassesParticipant.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchClassesActivity::class.java))
        }

        // get time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvClassesParticipant.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    viewModel.getListClassParticipant(userNuc, projectCode, levelJabatan, date,region, page, size)
                }
            }
        }
        binding.rvClassesParticipant.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.classesParticipantModel.observe(this) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.tvEmptyClassesParticipant.visibility = View.VISIBLE
                    binding.rvClassesParticipant.visibility = View.GONE
                    binding.rvClassesParticipant.adapter = null
                } else {
                    binding.tvEmptyClassesParticipant.visibility = View.GONE
                    binding.rvClassesParticipant.visibility = View.VISIBLE

                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ClassesParticipantAdapter(
                            it.data.content as ArrayList<Content>
                        )
                        binding.rvClassesParticipant.adapter = rvAdapter
                    } else {
                        rvAdapter.listClass.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listClass.size - it.data.content.size,
                            rvAdapter.listClass.size
                        )
                    }
                }
            } else {
                binding.tvEmptyClassesParticipant.visibility = View.VISIBLE
                binding.rvClassesParticipant.visibility = View.GONE
                binding.rvClassesParticipant.adapter = null

                when (it.errorCode) {
                    "02" -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Gagal mengambil data kelas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListClassParticipant(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}