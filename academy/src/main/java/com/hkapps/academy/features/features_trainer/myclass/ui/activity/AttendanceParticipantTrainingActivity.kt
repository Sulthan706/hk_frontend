package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityAttendanceParticipantTrainingBinding
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.Content
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ParticipantAttendanceOnlineAdapter
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ParticipantAttendanceOnsiteAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView

class AttendanceParticipantTrainingActivity : AppCompatActivity(),
    ParticipantAttendanceOnlineAdapter.ParticipantsAttendanceCallBack {

    private lateinit var binding: ActivityAttendanceParticipantTrainingBinding
    private lateinit var rvAdapterOnline: ParticipantAttendanceOnlineAdapter
    private lateinit var rvAdapterOnsite: ParticipantAttendanceOnsiteAdapter

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val trainingId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TRAINING_ID_DETAIL_TRAINING_TRAINER, 0)
    private val trainingDate = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_DATE_FORMATTED_DETAIL, "")
    private val trainingTime = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_TIME_FORMATTED_DETAIL, "")
    private val categoryClass = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_CATEGORY_DETAIL_TRAINING, "")
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
        binding = ActivityAttendanceParticipantTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarAttendanceParticipantTraining.tvAppbarTitle.text = "Absensi Peserta"
        binding.appbarAttendanceParticipantTraining.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set training data
        binding.tvDateAttendanceParticipantTraining.text = trainingDate
        binding.tvTimeAttendanceParticipantTraining.text = trainingTime
        binding.tvParticipantsAttendanceParticipantTraining.text = "$totalParticipant Peserta"
        when (categoryClass) {
            "ONLINE" -> {
                binding.tvBarcodeAttendanceParticipantTraining.visibility = View.GONE
            }
            "ONSITE" -> {
                binding.tvBarcodeAttendanceParticipantTraining.visibility = View.VISIBLE
                binding.tvBarcodeAttendanceParticipantTraining.setOnClickListener {
                    startActivity(Intent(this, QrAttendanceTrainingActivity::class.java))
                }
            }
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvAttendanceParticipantTraining.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvAttendanceParticipantTraining.addOnScrollListener(scrollListener)

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSetAttendance(attendance: String, participantId: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_warning_choices)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val title = dialog.findViewById(R.id.tvTitleDialogWarningChoices) as TextView
        val info = dialog.findViewById(R.id.tvInfoDialogWarningChoices) as TextView
        val btn1 = dialog.findViewById(R.id.btn1DialogWarningChoices) as AppCompatButton
        val btn2 = dialog.findViewById(R.id.btn2DialogWarningChoices) as AppCompatButton

        when(attendance) {
            "HADIR" -> {
                title.text = "Konfirmasi HADIR?"
                title.setTextColor(resources.getColor(R.color.blue5))
                info.text = "Jika benar, peserta dinyatakan HADIR mengikuti kelas"
                btn1.text = "Kembali"
                btn1.setBackgroundResource(R.drawable.bg_rounded_red1)
                btn2.text = "Ya, HADIR"
                btn2.setBackgroundResource(R.drawable.bg_rounded_blue6)
            }
            "TIDAK_HADIR" -> {
                title.text = "Konfirmasi TIDAK HADIR?"
                title.setTextColor(resources.getColor(R.color.red1))
                info.text = "Jika benar, peserta dinyatakan TIDAK HADIR mengikuti kelas"
                btn1.text = "Kembali"
                btn1.setBackgroundResource(R.drawable.bg_rounded_blue6)
                btn2.text = "Ya, TIDAK HADIR"
                btn2.setBackgroundResource(R.drawable.bg_rounded_red1)
            }
        }

        // set on click button
        btn1.setOnClickListener {
            dialog.dismiss()
        }
        btn2.setOnClickListener {
            viewModel.updateAttendanceParticipant(participantId, attendance, userNuc)
            // set observer
            viewModel.updateAttendanceParticipantModel.observe(this) {
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

    private fun setObserver() {
        viewModel.participantsTrainingModel.observe(this) {
            if (it.code == 200) {
                when (categoryClass) {
                    "ONLINE" -> {
                        isLastPage = it.data.last

                        if (page == 0) {
                            rvAdapterOnline = ParticipantAttendanceOnlineAdapter(
                                it.data.content as ArrayList<Content>
                            ).also { it.setListener(this) }
                            binding.rvAttendanceParticipantTraining.adapter = rvAdapterOnline
                        } else {
                            rvAdapterOnline.listParticipant.addAll(it.data.content)
                            rvAdapterOnline.notifyItemRangeChanged(
                                rvAdapterOnline.listParticipant.size - it.data.content.size,
                                rvAdapterOnline.listParticipant.size
                            )
                        }
                    }
                    "ONSITE" -> {
                        isLastPage = it.data.last

                        if (page == 0) {
                            rvAdapterOnsite = ParticipantAttendanceOnsiteAdapter(
                                it.data.content as ArrayList<Content>
                            )
                            binding.rvAttendanceParticipantTraining.adapter = rvAdapterOnsite
                        } else {
                            rvAdapterOnsite.listParticipant.addAll(it.data.content)
                            rvAdapterOnsite.notifyItemRangeChanged(
                                rvAdapterOnsite.listParticipant.size - it.data.content.size,
                                rvAdapterOnsite.listParticipant.size
                            )
                        }
                    }
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

    override fun onClickParticipant(attendance: String, participantId: Int) {
        showDialogSetAttendance(attendance, participantId)
    }
}