package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityDetailTrainingTrainerBinding
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DetailTrainingTrainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTrainingTrainerBinding

    private val userNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val trainingId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.TRAINING_ID_DETAIL_TRAINING_TRAINER, 0)
    private var region = ""

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTrainingTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar
        binding.appbarDetailTrainingTrainer.tvAppbarTitle.text = "Detail Kelas Training"
        binding.appbarDetailTrainingTrainer.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // get data
        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getDetailTraining(trainingId, region)
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

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.detailTrainingModel.observe(this) {
            if (it.code == 200) {
                // set validate layout level module
                binding.tvLevelDetailTrainingTrainer.text = it.data.moduleLevel
                when (it.data.moduleLevel) {
                    "BASIC" -> binding.tvLevelDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_orange2)
                    "INTERMEDIATE" -> binding.tvLevelDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_purple1)
                    "ADVANCE" -> binding.tvLevelDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_blue5)
                    "PROFESIONAL" -> binding.tvLevelDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_second_color)
                }

                binding.tvTitleDetailTrainingTrainer.text = it.data.trainingName
                binding.tvModuleDetailTrainingTrainer.text = it.data.moduleName

                // set on click btn read module
                binding.btnReadDetailTrainingTrainer.setOnClickListener {_ ->
                    val intent = Intent(this, ReadModuleActivity::class.java)
                    intent.putExtra("moduleName", it.data.moduleName)
                    startActivity(intent)
                }

                // set date formatted and time training
                val dayOfWeek = getDayOfWeek(it.data.trainingDate)
                val dateTxt = getDateTxt(it.data.trainingDate)
                val dateTxtFormatted = "$dayOfWeek, $dateTxt"
                val timeTxtFormatted = "${it.data.trainingStart} ${it.data.region} - ${it.data.trainingEnd} ${it.data.region}"

                binding.tvDateDetailTrainingTrainer.text = dateTxtFormatted
                binding.tvTimeDetailTrainingTrainer.text = timeTxtFormatted

                AcademyOperationPref.saveString(AcademyOperationPrefConst.LEVEL_MODULE_DETAIL_TRAINING, it.data.moduleLevel)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_NAME_DETAIL_TRAINING, it.data.trainingName)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_FOR_DETAIL_TRAINING, it.data.participant)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_DATE_FORMATTED_DETAIL, dateTxtFormatted)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_TIME_FORMATTED_DETAIL, timeTxtFormatted)
                AcademyOperationPref.saveInt(AcademyOperationPrefConst.TOTAL_PARTICIPANT_DETAIL_TRAINING, it.data.jumlahPeserta)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_CATEGORY_DETAIL_TRAINING, it.data.jenisKelas)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.STATUS_DETAIL_TRAINING, it.data.status)

                // sync data detail training and create training
                AcademyOperationPref.saveInt(AcademyOperationPrefConst.TRAINING_ID_CREATE_CLASS, it.data.trainingId)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_CREATE_CLASS, it.data.trainingName)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_DATE_CREATE_CLASS, it.data.trainingDate)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_START_CREATE_CLASS, it.data.trainingStart)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_END_CREATE_CLASS, it.data.trainingEnd)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.REGION_CREATE_CLASS, it.data.region)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.MODULE_LEVEL_CREATE_CLASS, it.data.moduleLevel)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, it.data.participant)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_CODE_CREATE_CLASS, it.data.projectCode)
                AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_NAME_CREATE_CLASS, it.data.projectName ?: "Semua Project")

                // set training type layout validate
                when(it.data.jenisKelas) {
                    "ONLINE" -> {
                        binding.tvTrainingTypeDetailTrainingTrainer.text = "ONLINE TRAINING"
                        binding.tvTrainingTypeDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_blue5)
                        binding.tvProjectAppDetailTrainingTrainer.text = it.data.appName
                        binding.tvAddressDetailTrainingTrainer.text = it.data.appLink
                        binding.ivCopyLinkDetailTrainingTrainer.visibility = View.VISIBLE
                        binding.ivCopyLinkDetailTrainingTrainer.setOnClickListener { _ ->
                            copyText(it.data.appLink)
                        }
                    }
                    "ONSITE" -> {
                        binding.tvTrainingTypeDetailTrainingTrainer.text = "ONSITE TRAINING"
                        binding.tvTrainingTypeDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_orange2)
                        binding.tvProjectAppDetailTrainingTrainer.text = it.data.trainingLocationName
                        binding.tvAddressDetailTrainingTrainer.text = if (it.data.alternateLocation == "") {
                            it.data.locationDescription
                        } else {
                            it.data.alternateLocation
                        }
                        binding.ivCopyLinkDetailTrainingTrainer.visibility = View.GONE
                    }
                }

                // set data training for
                binding.tvParticipantDetailTrainingTrainer.text = if (it.data.participant == "Semua") {
                    "SEMUA JABATAN"
                } else {
                    it.data.participant
                }
                binding.tvProjectDetailTrainingTrainer.text = if (it.data.projectCode == "Semua") {
                    "SEMUA PROJECT"
                } else {
                    it.data.projectName
                }
                binding.tvCategoryDetailTrainingTrainer.text = it.data.category

                // validate layout by category
                when(it.data.category) {
                    "PUBLIK" -> {
                        binding.tvInfoDetailTrainingTrainer.text = getString(R.string.info_public_class)

                        binding.tvParticipantsDetailTrainingTrainer.text = "Kuota Peserta :  "
                        binding.tvCountParticipantDetailTrainingTrainer.text = it.data.quota.toString()

                        binding.llPublicDetailTrainingTrainer.visibility = View.VISIBLE
                        binding.tvCountKuotaDetailTrainingTrainer.text = "${it.data.quota - it.data.jumlahPeserta}"

                        binding.btnInviteDetailTrainingTrainer.text = "Lihat"
                        binding.btnInviteDetailTrainingTrainer.setOnClickListener {
                            startActivity(Intent(this, ParticipantsDetailTrainingActivity::class.java))
                        }
                    }
                    "PRIVATE" -> {
                        binding.tvInfoDetailTrainingTrainer.text = getString(R.string.info_private_class)

                        binding.tvParticipantsDetailTrainingTrainer.text = "Jumlah Peserta :  "
                        binding.tvCountParticipantDetailTrainingTrainer.text = it.data.jumlahPeserta.toString()

                        binding.llPublicDetailTrainingTrainer.visibility = View.GONE

                        binding.btnInviteDetailTrainingTrainer.text = "Invite"
                        binding.btnInviteDetailTrainingTrainer.setOnClickListener {
                            startActivity(Intent(this, InviteParticipantTrainingActivity::class.java))
                        }
                    }
                }

                // set layout class activity & scoring
                if (it.data.status == "Selesai") {
                    binding.btnDoneDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_grey_disable)
                    binding.tvStatusClassDetailTrainingTrainer.setTextColor(resources.getColor(R.color.green2))
                    binding.tvStatusClassDetailTrainingTrainer.text = "Selesai"
                } else {
                    binding.btnDoneDetailTrainingTrainer.setBackgroundResource(R.drawable.bg_rounded_green2)
                    binding.tvStatusClassDetailTrainingTrainer.setTextColor(resources.getColor(R.color.red1))
                    binding.tvStatusClassDetailTrainingTrainer.text = "Selesaikan untuk beri nilai"

                    binding.btnDoneDetailTrainingTrainer.setOnClickListener {
                        showBottomDialog()
                    }
                }

                binding.tvAttendanceDetailTrainingTrainer.text = "${it.data.absensiPeserta}/${it.data.jumlahPeserta} peserta"
                binding.tvScoringDetailTrainingTrainer.text = "${it.data.penilaianPeserta}/${it.data.jumlahPeserta} peserta"

                // on click layout attendance & scoring participant
                binding.clAbsensiDetailTrainingTrainer.setOnClickListener {
                    startActivity(Intent(this, AttendanceParticipantTrainingActivity::class.java))
                }
                binding.clPenilaianDetailTrainingTrainer.setOnClickListener {
                    startActivity(Intent(this, TrainingScoreParticipantsActivity::class.java))
                }

            } else {
                Toast.makeText(this, "Gagal mengambil data detail training", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showBottomDialog() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_dialog_set_status_training)
        dialog.setCancelable(false)

        val btnYes = dialog.findViewById<AppCompatButton>(R.id.btnYesBottomDialogSetStatusTraining)
        val btnNo = dialog.findViewById<AppCompatButton>(R.id.btnNoBottomDialogSetStatusTraining)

        btnNo?.setOnClickListener {
            dialog.dismiss()
        }

        btnYes?.setOnClickListener {
            viewModel.updateEndClassTraining(trainingId, userNuc)
            // set observer
            viewModel.updateEndClassTrainingModel.observe(this) {
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

    private fun copyText(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Link Copied", Toast.LENGTH_SHORT).show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}