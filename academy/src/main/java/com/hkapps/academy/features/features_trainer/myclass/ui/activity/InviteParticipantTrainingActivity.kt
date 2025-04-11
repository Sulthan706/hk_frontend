package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityInviteParticipantTrainingBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ContentParcelable
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.InviteParticipantTrainingAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InviteParticipantTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteParticipantTrainingBinding

    private val trainingId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TRAINING_ID_CREATE_CLASS, 0)
    private val trainingName = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_CREATE_CLASS, "")
    private val trainingDate = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_DATE_CREATE_CLASS, "")
    private val trainingStart = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_START_CREATE_CLASS, "")
    private val trainingEnd = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_END_CREATE_CLASS, "")
    private val region = AcademyOperationPref.loadString(AcademyOperationPrefConst.REGION_CREATE_CLASS, "")
    private val levelModule = AcademyOperationPref.loadString(AcademyOperationPrefConst.MODULE_LEVEL_CREATE_CLASS, "")
    private val participant = AcademyOperationPref.loadString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, "")
    private var selectedParticipants = ArrayList<ContentParcelable>()
    private var clickFrom = ""

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteParticipantTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get selected list participant
        clickFrom = intent.getStringExtra("clickFrom").toString()
        if (clickFrom == "selectedParticipant") {
            selectedParticipants = intent.getParcelableArrayListExtra("selectedParticipants")!!

            // set count participant
            binding.tvCountParticipantInviteParticipant.visibility = View.VISIBLE
            binding.tvCountParticipantInviteParticipant.text = "${selectedParticipants.size} Peserta"

            // set recycler view
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvInviteParticipantTraining.layoutManager = layoutManager
            val rvAdapter = InviteParticipantTrainingAdapter(selectedParticipants)
            binding.rvInviteParticipantTraining.adapter = rvAdapter

            // set button submit
            binding.btnSubmitInviteParticipant.setBackgroundResource(R.drawable.bg_second_color)
            binding.btnSubmitInviteParticipant.setOnClickListener {
                val userNuc = ArrayList<String>()
                val length = selectedParticipants.size
                for (i in 0 until length) {
                    userNuc.add(selectedParticipants[i].userNuc)
                }

                viewModel.inviteParticipant(userNuc, trainingId)
            }
        } else {
            binding.tvCountParticipantInviteParticipant.visibility = View.GONE
            binding.btnSubmitInviteParticipant.setBackgroundResource(R.drawable.bg_rounded_disable)
        }

        // set appbar
        binding.appbarInviteParticipant.tvAppbarTitle.text = "Invite Peserta Training"
        binding.appbarInviteParticipant.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set data
        binding.tvStatusInviteParticipant.text = levelModule
        when (levelModule) {
            "BASIC" -> binding.tvStatusInviteParticipant.setBackgroundResource(R.drawable.bg_rounded_orange2)
            "INTERMEDIATE" -> binding.tvStatusInviteParticipant.setBackgroundResource(R.drawable.bg_rounded_purple1)
            "ADVANCE" -> binding.tvStatusInviteParticipant.setBackgroundResource(R.drawable.bg_rounded_blue5)
            "PROFESIONAL" -> binding.tvStatusInviteParticipant.setBackgroundResource(R.drawable.bg_second_color)
        }
        binding.tvTitleInviteParticipant.text = trainingName
        binding.tvTrainerInviteParticipant.text = if (participant == "Semua") {
            "Semua Jabatan"
        } else {
            participant
        }

        val dayOfWeek = getDayOfWeek(trainingDate)
        val dateTxt = getDateTxt(trainingDate)
        binding.tvDateInviteParticipant.text = "$dayOfWeek, $dateTxt"
        binding.tvTimeInviteParticipant.text = "$trainingStart $region - $trainingEnd $region"

        // on click add participant
        binding.btnAddInviteParticipant.setOnClickListener {
            showBottomSheetDialog()
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.inviteParticipantModel.observe(this) {
            if (it.code == 200) {
                val intent = Intent(this, FinishedCreateClassActivity::class.java)
                intent.putExtra("clickFrom", "InviteParticipant")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal invite peserta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDateTxt(date: String): String {
        val sdfBefore = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfAfter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val dateBefore = sdfBefore.parse(date)
        return sdfAfter.format(dateBefore)

    }

    private fun getDayOfWeek(date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val mDate = dateFormat.parse(date) ?: Date()

        val calendar = Calendar.getInstance()
        calendar.time = mDate

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Minggu"
            Calendar.MONDAY -> "Senin"
            Calendar.TUESDAY -> "Selasa"
            Calendar.WEDNESDAY -> "Rabu"
            Calendar.THURSDAY -> "Kamis"
            Calendar.FRIDAY -> "Jumat"
            Calendar.SATURDAY -> "Sabtu"
            else -> ""
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)

        dialog.setContentView(R.layout.bottom_dialog_project_position_invite)
        val btnClose = dialog.findViewById<ImageView>(R.id.btnCloseBottomDialogProjectPositionInvite)
        val btnProject = dialog.findViewById<AppCompatButton>(R.id.btnProjectBottomDialog)
        val btnPosition = dialog.findViewById<AppCompatButton>(R.id.btnPositionBottomDialog)

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }
        btnProject?.setOnClickListener {
            val intent = Intent(this, ProjectPositionInviteActivity::class.java)
            intent.putExtra("clickFrom", "project")
            startActivity(intent)
            dialog.dismiss()
        }
        btnPosition?.setOnClickListener {
            val intent = Intent(this, ProjectPositionInviteActivity::class.java)
            intent.putExtra("clickFrom", "position")
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}