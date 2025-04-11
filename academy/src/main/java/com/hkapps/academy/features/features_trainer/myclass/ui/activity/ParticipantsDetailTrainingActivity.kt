package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityParticipantsDetailTrainingBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ParticipantsDetailTrainingAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst

class ParticipantsDetailTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParticipantsDetailTrainingBinding
    private lateinit var rvAdapter: ParticipantsDetailTrainingAdapter

    private val trainingId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TRAINING_ID_DETAIL_TRAINING_TRAINER, 0)
    private val levelModule = AcademyOperationPref.loadString(AcademyOperationPrefConst.LEVEL_MODULE_DETAIL_TRAINING, "")
    private val trainingName = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_NAME_DETAIL_TRAINING, "")
    private val trainingFor = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_FOR_DETAIL_TRAINING, "")
    private val trainingDate = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_DATE_FORMATTED_DETAIL, "")
    private val trainingTime = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_TIME_FORMATTED_DETAIL, "")
    private val participantsTotal = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TOTAL_PARTICIPANT_DETAIL_TRAINING, 0)
    private val page = 0
    private val size = 10

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParticipantsDetailTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarParticipantsDetailTraining.tvAppbarTitle.text = "Lihat Peserta Training"
        binding.appbarParticipantsDetailTraining.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set data detail training
        binding.tvStatusParticipantsDetailTraining.text = levelModule
        when (levelModule) {
            "BASIC" -> binding.tvStatusParticipantsDetailTraining.setBackgroundResource(R.drawable.bg_rounded_orange2)
            "INTERMEDIATE" -> binding.tvStatusParticipantsDetailTraining.setBackgroundResource(R.drawable.bg_rounded_purple1)
            "ADVANCE" -> binding.tvStatusParticipantsDetailTraining.setBackgroundResource(R.drawable.bg_rounded_blue5)
            "PROFESIONAL" -> binding.tvStatusParticipantsDetailTraining.setBackgroundResource(R.drawable.bg_second_color)
        }
        binding.tvTitleParticipantsDetailTraining.text = trainingName
        binding.tvTrainerParticipantsDetailTraining.text = if (trainingFor == "Semua") {
            "Semua Jabatan"
        } else {
            trainingFor
        }
        binding.tvDateParticipantsDetailTraining.text = trainingDate
        binding.tvTimeParticipantsDetailTraining.text = trainingTime
        binding.tvCountParticipantParticipantsDetailTraining.text = "$participantsTotal Peserta"

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvParticipantsDetailTraining.layoutManager = layoutManager

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.participantsTrainingModel.observe(this) {
            if (it.code == 200) {
                rvAdapter = ParticipantsDetailTrainingAdapter(it.data.content as ArrayList<Content>)
                binding.rvParticipantsDetailTraining.adapter = rvAdapter
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getParticipantsTraining(trainingId, page, size)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}