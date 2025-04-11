package com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.FragmentMyClassBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.Content
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.adapter.AllClassesTrainerAdapter
import com.hkapps.academy.features.features_trainer.homescreen.home.viewmodel.HomeTrainerViewModel
import com.hkapps.academy.features.features_trainer.myclass.ui.activity.CreateClassActivity
import com.hkapps.academy.features.features_trainer.myclass.ui.activity.DetailTrainingTrainerActivity
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import java.util.Date
import java.util.TimeZone

class MyClassFragment : Fragment(), AllClassesTrainerAdapter.ListClassCallBack {

    private lateinit var binding: FragmentMyClassBinding
    private lateinit var rvAdapter: AllClassesTrainerAdapter

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val date = ""
    private var region = ""
    private var page = 0
    private val size = 10
    private var isLastPage = false

    private val viewModel: HomeTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(HomeTrainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyClassBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set appbar
        binding.appbarMyClass.tvAppbarTitle.text = "Daftar Kelas Saya"

        // set time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListMyClass.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object  : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvListMyClass.addOnScrollListener(scrollListener)

        binding.fabCreateClassMyClass.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateClassActivity::class.java))
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.listAllClassModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.tvEmptyListMyClass.visibility = View.VISIBLE
                    binding.rvListMyClass.visibility = View.GONE
                    binding.rvListMyClass.adapter = null
                } else {
                    binding.tvEmptyListMyClass.visibility = View.GONE
                    binding.rvListMyClass.visibility = View.VISIBLE

                    isLastPage = it.data.last

                    if (page == 0) {
                        rvAdapter = AllClassesTrainerAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        binding.rvListMyClass.adapter = rvAdapter
                    } else {
                        rvAdapter.listClass.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listClass.size - it.data.content.size,
                            rvAdapter.listClass.size
                        )
                    }
                }
            } else {
                binding.rvListMyClass.adapter = null
                binding.tvEmptyListMyClass.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Gagal mengambil data list class", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getAllClassTrainer(userNuc, date, region, page, size)
    }

    override fun onClickClass(trainingId: Int) {
        AcademyOperationPref.saveInt(AcademyOperationPrefConst.TRAINING_ID_DETAIL_TRAINING_TRAINER, trainingId)
        startActivity(Intent(requireContext(), DetailTrainingTrainerActivity::class.java))
    }

}