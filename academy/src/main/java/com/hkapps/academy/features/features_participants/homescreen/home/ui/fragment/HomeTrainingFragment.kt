package com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment

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
import com.hkapps.academy.R
import com.hkapps.academy.databinding.FragmentHomeTrainingBinding
import com.hkapps.academy.features.features_participants.classes.ui.activity.ClassesParticipantActivity
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.Content
import com.hkapps.academy.features.features_participants.homescreen.home.ui.adapter.TrainingsHomeAdapter
import com.hkapps.academy.features.features_participants.homescreen.home.viewmodel.HomeParticipantViewModel
import com.hkapps.academy.features.features_participants.training.ui.activity.HistoryTrainingScheduleActivity
import com.hkapps.academy.features.features_participants.homescreen.notification.ui.activity.NotificationTrainingActivity
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.activity.HomeTrainerActivity
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class HomeTrainingFragment : Fragment() {

    private lateinit var binding: FragmentHomeTrainingBinding
    private lateinit var rvAdapter: TrainingsHomeAdapter

    private val userName = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NAME, "")
    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val userPosition = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_POSITION, "")
    private val userLevelPosition = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_LEVEL_POSITION, "")
    private val projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_PROJECT_CODE, "")
    private var isLastPage = false
    private var page = 0
    private var currentDate = ""
    private var dayString = ""
    private var monthString = ""
    private var region = ""

    private val viewModel: HomeParticipantViewModel by lazy {
        ViewModelProviders.of(this).get(HomeParticipantViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set user data
        greetingText()
        binding.tvNameHomeTraining.text = userName
        binding.tvNucJobHomeTraining.text = "$userNuc | $userPosition"

        // to trainer layout
        if (userLevelPosition == "Operator") {
            binding.tvTrainerHome.visibility = View.INVISIBLE
        } else {
            binding.tvTrainerHome.visibility = View.VISIBLE
            binding.tvTrainerHome.setOnClickListener {
                startActivity(Intent(requireActivity(), HomeTrainerActivity::class.java))
            }
        }

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

        binding.tvDateHomeTraining.text = "$dayString, $dayOfMonth $monthString $year"

        // set recycler view
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHomeTraining.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    viewModel.getListTrainingHome(
                        userNuc, "DIGITESTING_123", userLevelPosition, currentDate, region, page, 10
                    )
                }
            }

        }
        binding.rvHomeTraining.addOnScrollListener(scrollListener)

        // notification
        binding.ivNotif.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationTrainingActivity::class.java))
        }

        // daftar kelas
        binding.tvSeeAllClassesHomeTraining.setOnClickListener {
            startActivity(Intent(requireActivity(), ClassesParticipantActivity::class.java))
        }

        // schedule training
        binding.tvSeeAllScheduleHomeTraining.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryTrainingScheduleActivity::class.java))
        }

        loadData()
        setObserver()
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
    private fun setObserver() {
        viewModel.listClassHomeModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.tvEmptyClassesHomeTraining.visibility = View.VISIBLE
                    binding.rlClassHomeTraining.visibility = View.GONE
                } else {
                    binding.tvEmptyClassesHomeTraining.visibility = View.GONE
                    binding.rlClassHomeTraining.visibility = View.VISIBLE

                    // reformat created date
                    val sdfBefore = SimpleDateFormat("yyyy-MM-dd")
                    val dateParamBefore = sdfBefore.parse(it.data.content[0].trainingDate)
                    val sdfAfter = SimpleDateFormat("dd MMM yyyy")
                    val dateParamAfter = sdfAfter.format(dateParamBefore)

                    binding.layoutClassHomeTraining.tvDaysTraining.visibility = View.GONE
                    binding.layoutClassHomeTraining.ivNextTrainingSchedule.visibility = View.INVISIBLE
                    binding.layoutClassHomeTraining.tvApplyTraining.visibility = View.VISIBLE

                    when (it.data.content[0].jenisKelas) {
                        "ONLINE" -> {
                            binding.layoutClassHomeTraining.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_blue5)
                            binding.layoutClassHomeTraining.tvStatusTraining.text = "Online"
                        }
                        "ONSITE" -> {
                            binding.layoutClassHomeTraining.tvStatusTraining.setBackgroundResource(R.drawable.bg_rounded_orange)
                            binding.layoutClassHomeTraining.tvStatusTraining.text = "Onsite"
                        }
                    }

                    binding.layoutClassHomeTraining.tvTimeTraining.text = "${it.data.content[0].durationInMinute} menit"
                    binding.layoutClassHomeTraining.tvTitleTraining.text = it.data.content[0].trainingName
                    binding.layoutClassHomeTraining.tvMateriTraining.text = it.data.content[0].moduleName
                    binding.layoutClassHomeTraining.tvScheduleTraining.text =
                        "$dateParamAfter | ${it.data.content[0].trainingStart} - ${it.data.content[0].trainingEnd}"
                    binding.layoutClassHomeTraining.tvPeopleTraining.text = it.data.content[0].trainerName

                }
            } else {
                when (it.errorCode) {
                    "02" -> Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(requireContext(), "Gagal mengambil data kelas", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listTrainingHomeModel.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.tvCountScheduleHomeTraining.text = "Anda tidak memiliki jadwal training hari ini"
                    binding.tvCountScheduleHomeTraining.setTextColor(resources.getColor(R.color.red1))
                    binding.tvCountScheduleHomeTraining.setBackgroundResource(R.color.red3)
                    binding.llTimeTrainingHomeTraining.visibility = View.GONE
                    binding.rvHomeTraining.visibility = View.GONE
                } else {
                    binding.tvCountScheduleHomeTraining.text = "Anda memiliki ${it.data.content.size} jadwal training hari ini"
                    binding.tvCountScheduleHomeTraining.setTextColor(resources.getColor(R.color.blue6))
                    binding.tvCountScheduleHomeTraining.setBackgroundResource(R.color.light_blue)
                    binding.llTimeTrainingHomeTraining.visibility = View.VISIBLE
                    binding.rvHomeTraining.visibility = View.VISIBLE

                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = TrainingsHomeAdapter(
                            it.data.content as ArrayList<Content>
                        )
                        binding.rvHomeTraining.adapter = rvAdapter
                    } else {
                        rvAdapter.listTraining.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listTraining.size - it.data.content.size,
                            rvAdapter.listTraining.size
                        )
                    }
                }
            } else {
                binding.tvCountScheduleHomeTraining.text = "Anda tidak memiliki jadwal training hari ini"
                binding.tvCountScheduleHomeTraining.setTextColor(resources.getColor(R.color.red1))
                binding.tvCountScheduleHomeTraining.setBackgroundResource(R.color.red3)
                binding.llTimeTrainingHomeTraining.visibility = View.GONE
                binding.rvHomeTraining.visibility = View.GONE
                binding.rvHomeTraining.adapter = null
                Toast.makeText(
                    requireContext(),
                    "Gagal mengambil data training",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListClassHome(
            userNuc, projectCode, userLevelPosition, "", region, page, 1
        )
        viewModel.getListTrainingHome(
            userNuc, projectCode, userLevelPosition, currentDate, region, page, 10
        )
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun greetingText() {
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date())

        val morningEndTime = "10:59"
        val dayEndTime = "14:59"
        val afternoonEndTime = "17:59"
        val nightEndTime = "05:00"

        when {
            time < morningEndTime -> binding.tvGreetingHomeTraining.text = "Selamat Pagi, "
            time < dayEndTime -> binding.tvGreetingHomeTraining.text = "Selamat Siang, "
            time < afternoonEndTime -> binding.tvGreetingHomeTraining.text = "Selamat Sore, "
            time < nightEndTime -> binding.tvGreetingHomeTraining.text = "Selamat Malam, "
        }
    }

}