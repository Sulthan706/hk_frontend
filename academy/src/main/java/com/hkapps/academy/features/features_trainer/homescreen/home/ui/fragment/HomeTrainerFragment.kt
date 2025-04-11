package com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.FragmentHomeTrainerBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.Content
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.adapter.ClassesHomeTrainerAdapter
import com.hkapps.academy.features.features_trainer.homescreen.home.viewmodel.HomeTrainerViewModel
import com.hkapps.academy.features.features_trainer.myclass.ui.activity.CreateClassActivity
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class HomeTrainerFragment : Fragment() {

    private lateinit var binding: FragmentHomeTrainerBinding
    private lateinit var rvAdapter: ClassesHomeTrainerAdapter
    private var isExpanded = false

    private val fromBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab)
    }
    private val toBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab)
    }
    private val rotateClockWiseAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWiseAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise)
    }

    private val userName = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NAME, "")
    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val userPosition = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_POSITION, "")
    private var isLastPage = false
    private var page = 0
    private val size = 10
    private var currentDate = ""
    private var dayString = ""
    private var monthString = ""
    private var region = ""

    private val viewModel: HomeTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(HomeTrainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeTrainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // floating button
        binding.fabMainHomeTrainer.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        // set user data
        greetingText()
        binding.tvNameHomeTrainer.text = userName
        binding.tvNucJobHomeTrainer.text = "$userNuc | $userPosition"

        // get time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // current date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())

        val calendar = Calendar.getInstance()
        val dayInt = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        daysToString(dayInt)
        monthToString(month+1)

        binding.tvDateHomeTrainer.text = "$dayString, $dayOfMonth $monthString $year"

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHomeTrainer.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvHomeTrainer.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.listClassHomeTrainerModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.llTimeTrainingHomeTrainer.visibility = View.GONE
                    binding.rvHomeTrainer.visibility = View.GONE
                    binding.rvHomeTrainer.adapter = null

                    binding.tvTotalClassHomeTrainer.text = "Anda tidak memiliki jadwal training hari ini"
                    binding.tvTotalClassHomeTrainer.setTextColor(resources.getColor(R.color.red1))
                    binding.tvTotalClassHomeTrainer.setBackgroundResource(R.color.red3)
                } else {
                    binding.llTimeTrainingHomeTrainer.visibility = View.VISIBLE
                    binding.rvHomeTrainer.visibility = View.VISIBLE

                    binding.tvTotalClassHomeTrainer.text = "Anda memiliki ${it.data.content.size} jadwal training hari ini"
                    binding.tvTotalClassHomeTrainer.setTextColor(resources.getColor(R.color.blue6))
                    binding.tvTotalClassHomeTrainer.setBackgroundResource(R.color.light_blue)

                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = ClassesHomeTrainerAdapter(
                            it.data.content as ArrayList<Content>
                        )
                        binding.rvHomeTrainer.adapter = rvAdapter
                    } else {
                        rvAdapter.listClass.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listClass.size - it.data.content.size,
                            rvAdapter.listClass.size
                        )
                    }
                }
            } else {

                binding.llTimeTrainingHomeTrainer.visibility = View.GONE
                binding.rvHomeTrainer.visibility = View.GONE
                binding.rvHomeTrainer.adapter = null

                binding.tvTotalClassHomeTrainer.text = "Anda tidak memiliki jadwal training hari ini"
                binding.tvTotalClassHomeTrainer.setTextColor(resources.getColor(R.color.red1))
                binding.tvTotalClassHomeTrainer.setBackgroundResource(R.color.red3)

                Toast.makeText(requireContext(), "Gagal mengambil data kelas", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListClassHomeTrainer("DIGI111", currentDate, region, page, size)
    }

    private fun monthToString(i: Int) {
        when (i) {
            1 -> monthString = "Januari"
            2 -> monthString = "Februari"
            3 -> monthString = "Maret"
            4 -> monthString = "April"
            5 -> monthString = "Mei"
            6 -> monthString = "Juni"
            7 -> monthString = "Juli"
            8 -> monthString = "Agustus"
            9 -> monthString = "September"
            10 -> monthString = "Oktober"
            11 -> monthString = "November"
            12 -> monthString = "Desember"
        }
    }

    private fun daysToString(dayInt: Int) {
        when (dayInt) {
            1 -> dayString = "Minggu"
            2 -> dayString = "Senin"
            3 -> dayString = "Selasa"
            4 -> dayString = "Rabu"
            5 -> dayString = "Kamis"
            6 -> dayString = "Jumat"
            7 -> dayString = "Sabtu"
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun greetingText() {
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val morningEndTime = "10:59"
        val dayEndTime = "14:59"
        val afternoonEndTime = "17:59"
        val nightEndTime = "05:00"

        when {
            time < morningEndTime -> binding.tvGreetingHomeTrainer.text = "Selamat Pagi, "
            time < dayEndTime -> binding.tvGreetingHomeTrainer.text = "Selamat Siang, "
            time < afternoonEndTime -> binding.tvGreetingHomeTrainer.text = "Selamat Sore, "
            time < nightEndTime -> binding.tvGreetingHomeTrainer.text = "Selamat Malam, "
        }
    }

    private fun expandFab() {
        binding.fabMainHomeTrainer.startAnimation(rotateClockWiseAnim)
        binding.fabCreateHomeTrainer.startAnimation(fromBottomFabAnim)
        binding.fabPeopleHomeTrainer.startAnimation(fromBottomFabAnim)

        binding.fabCreateHomeTrainer.setOnClickListener {
            startActivity(Intent(requireActivity(), CreateClassActivity::class.java))
        }

        isExpanded = !isExpanded
    }

    private fun shrinkFab() {
        binding.fabMainHomeTrainer.startAnimation(rotateAntiClockWiseAnim)
        binding.fabCreateHomeTrainer.startAnimation(toBottomFabAnim)
        binding.fabPeopleHomeTrainer.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

}