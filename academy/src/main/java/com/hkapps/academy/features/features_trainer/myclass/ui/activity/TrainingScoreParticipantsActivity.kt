package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityTrainingScoreParticipantsBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ParticipantTrainingScoreAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

class TrainingScoreParticipantsActivity : AppCompatActivity(),
    ParticipantTrainingScoreAdapter.ParticipantScoreCallBack {

    private lateinit var binding: ActivityTrainingScoreParticipantsBinding
    private lateinit var rvAdapter: ParticipantTrainingScoreAdapter

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val trainingId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TRAINING_ID_DETAIL_TRAINING_TRAINER, 0)
    private val trainingDate = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_DATE_FORMATTED_DETAIL, "")
    private val trainingTime = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_TIME_FORMATTED_DETAIL, "")
    private val totalParticipant = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TOTAL_PARTICIPANT_DETAIL_TRAINING, 0)
    private var page = 0
    private val size = 10
    private var isLastPage = false

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingScoreParticipantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarTrainingScoreParticipants.tvAppbarTitle.text = "Penilaian Peserta"
        binding.appbarTrainingScoreParticipants.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set training data
        binding.tvDateTrainingScoreParticipants.text = trainingDate
        binding.tvTimeTrainingScoreParticipants.text = trainingTime
        binding.tvParticipantsTrainingScoreParticipants.text = "$totalParticipant Peserta"

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTrainingScoreParticipants.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvTrainingScoreParticipants.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.participantsTrainingModel.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last

                if (page == 0) {
                    rvAdapter = ParticipantTrainingScoreAdapter(
                        it.data.content as ArrayList<Content>
                    ).also { it.setListener(this) }
                    binding.rvTrainingScoreParticipants.adapter = rvAdapter
                } else {
                    rvAdapter.listParticipant.addAll(it.data.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listParticipant.size - it.data.content.size,
                        rvAdapter.listParticipant.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data list peserta", Toast.LENGTH_SHORT).show()
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

    override fun onClickParticipant(
        participantId: Int,
        participantName: String,
        participantPosition: String
    ) {
        showBottomDialog(participantId, participantName, participantPosition)
    }

    private fun showBottomDialog(
        participantId: Int,
        participantName: String,
        participantPosition: String)
    {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_dialog_scoring_participant)
        dialog.setCancelable(false)

        val ivClose = dialog.findViewById<ImageView>(R.id.btnCloseBottomDialogScoringParticipant)
        val tvName = dialog.findViewById<TextView>(R.id.tvNameBottomDialogScoringParticipant)
        val tvPosition = dialog.findViewById<TextView>(R.id.tvPositionBottomDialogScoringParticipant)
        val etScore = dialog.findViewById<AppCompatEditText>(R.id.etScoreBottomDialogScoringParticipant)
        val etComment = dialog.findViewById<AppCompatEditText>(R.id.etCommentBottomDialogScoringParticipant)
        val btnSubmit = dialog.findViewById<AppCompatButton>(R.id.btnBottomDialogScoringParticipant)

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        // set data
        tvName?.text = participantName
        tvPosition?.text = participantPosition

        // set enable button when etScore is not null
        var nilai = ""
        etScore?.addTextChangedListener {
            btnSubmit?.isEnabled = etScore.text.toString() != ""
            nilai = etScore.text.toString()
        }

        btnSubmit?.setOnClickListener {
            viewModel.updateScoreTraining(participantId, nilai.toInt(), etComment?.text.toString(), userNuc)
            // set observer
            viewModel.updateScoreTrainingModel.observe(this) {
                if (it.code == 200) {
                    loadData()
                    setObserver()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, it.message ?: "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.show()
    }
}