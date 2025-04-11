package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivitySummaryCreateClassBinding
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SummaryCreateClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryCreateClassBinding

    private val userName = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NAME, "")
    private val trainerNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private val trainingName = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_CREATE_CLASS, "")
    private val participant = AcademyOperationPref.loadString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, "")
    private val moduleId = AcademyOperationPref.loadInt(AcademyOperationPrefConst.MODULE_ID_CREATE_CLASS, 0)
    private val assignOnlineTest = AcademyOperationPref.loadString(AcademyOperationPrefConst.ONLINE_TEST_CREATE_CLASS, "")
    private val category = AcademyOperationPref.loadString(AcademyOperationPrefConst.CATEGORY_CREATE_CLASS, "")
    private val quota = AcademyOperationPref.loadInt(AcademyOperationPrefConst.QUOTA_CREATE_CLASS, 0)
    private val projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_CODE_CREATE_CLASS, "")
    private val trainingDate = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_DATE_CREATE_CLASS, "")
    private val trainingStart = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_START_CREATE_CLASS, "")
    private val trainingEnd = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_END_CREATE_CLASS, "")
    private val region = AcademyOperationPref.loadString(AcademyOperationPrefConst.REGION_CREATE_CLASS, "")
    private val jenisKelas = AcademyOperationPref.loadString(AcademyOperationPrefConst.JENIS_KELAS_CREATE_CLASS, "")
    private val trainingLocationCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.TRAINING_LOCATION_CREATE_CLASS, "")
    private val locationDescription = AcademyOperationPref.loadString(AcademyOperationPrefConst.LOCATION_DESC_CREATE_CLASS, "")
    private val alternateLocation = AcademyOperationPref.loadString(AcademyOperationPrefConst.ALTERNATE_LOCATION_CREATE_CLASS, "")
    private val appName = AcademyOperationPref.loadString(AcademyOperationPrefConst.APP_NAME_CREATE_CLASS, "")
    private val appLink = AcademyOperationPref.loadString(AcademyOperationPrefConst.APP_LINK_CREATE_CLASS, "")
    private val levelModule = AcademyOperationPref.loadString(AcademyOperationPrefConst.MODULE_LEVEL_CREATE_CLASS, "")
    private val nameModule = AcademyOperationPref.loadString(AcademyOperationPrefConst.MODULE_NAME_CREATE_CLASS, "")

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarSumCreateClass.tvAppbarTitle.text = "Summary Kelas"
        binding.appbarSumCreateClass.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set data trainer
        binding.tvTrainerSumCreateClass.text = userName
        val dayOfWeek = getDayOfWeek(trainingDate)
        val dateTxt = getDateTxt(trainingDate)
        binding.tvDateSumCreateClass.text = "$dayOfWeek, $dateTxt"
        binding.tvTimeSumCreateClass.text = "$trainingStart $region - $trainingEnd $region"

        // set data module
        binding.tvLevelTrainingSumCreateClass.text = levelModule
        when (levelModule) {
            "BASIC" -> binding.tvLevelTrainingSumCreateClass.setBackgroundResource(R.drawable.bg_rounded_orange2)
            "INTERMEDIATE" -> binding.tvLevelTrainingSumCreateClass.setBackgroundResource(R.drawable.bg_rounded_purple1)
            "ADVANCE" -> binding.tvLevelTrainingSumCreateClass.setBackgroundResource(R.drawable.bg_rounded_blue5)
            "PROFESIONAL" -> binding.tvLevelTrainingSumCreateClass.setBackgroundResource(R.drawable.bg_second_color)
        }
        binding.tvTitleTrainingSumCreateClass.text = trainingName
        binding.tvTrainingForSumCreateClass.text = if (participant == "Semua") {
            "Semua Jabatan"
        } else {
            participant
        }
        binding.tvModuleTrainingSumCreateClass.text = nameModule

        // set data info public or private
        when (category) {
            "PRIVATE" -> {
                binding.tvInfoSumCreateClass.text = getString(R.string.info_private_class)
                binding.tv7SumCreateClass.visibility = View.GONE
                binding.tvKuotaSumCreateClass.visibility = View.GONE
            }
            "PUBLIK" -> {
                binding.tvInfoSumCreateClass.text = getString(R.string.info_public_class)
                binding.tv7SumCreateClass.visibility = View.VISIBLE
                binding.tvKuotaSumCreateClass.visibility = View.VISIBLE
                binding.tvKuotaSumCreateClass.text = quota.toString()
            }
        }

        // set data online or onsite class
        binding.tvTrainingTypeSumCreateClass.text = "$jenisKelas TRAINING"
        when (jenisKelas) {
            "ONLINE" -> {
                binding.ivCopyLinkSumCreateClass.visibility = View.VISIBLE
                binding.tvOnlineMeetingSumCreateClass.visibility = View.VISIBLE
                binding.tvAddressTrainingSumCreateClass.visibility = View.VISIBLE

                binding.tvTrainingTypeSumCreateClass.setBackgroundResource(R.drawable.bg_rounded_blue5)
                binding.tvAddressTrainingSumCreateClass.text = appLink
                binding.tvOnlineMeetingSumCreateClass.text = appName

                binding.ivCopyLinkSumCreateClass.setOnClickListener {
                    copyText(appLink)
                }
            }
            "ONSITE" -> {
                binding.ivCopyLinkSumCreateClass.visibility = View.GONE
                binding.tvOnlineMeetingSumCreateClass.visibility = View.GONE
                binding.tvAddressTrainingSumCreateClass.visibility = View.VISIBLE

                binding.tvTrainingTypeSumCreateClass.setBackgroundResource(R.drawable.bg_rounded_orange)
                binding.tvAddressTrainingSumCreateClass.text = locationDescription
            }
        }

        // set on click create class
        binding.btnSumCreateClass.setOnClickListener {
            viewModel.createClassTrainer(
                trainerNuc,
                trainingName,
                participant,
                moduleId,
                assignOnlineTest,
                category,
                quota,
                projectCode,
                trainingDate,
                trainingStart,
                trainingEnd,
                region,
                jenisKelas,
                trainingLocationCode,
                locationDescription,
                alternateLocation,
                appName,
                appLink
            )
        }

        setObserver()
    }

    private fun copyText(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Link Copied", Toast.LENGTH_SHORT).show()
    }

    private fun setObserver() {
        viewModel.createClassResponseModel.observe(this) {
            if (it.code == 200) {
                AcademyOperationPref.saveInt(AcademyOperationPrefConst.TRAINING_ID_CREATE_CLASS, it.data.trainingId)

                val intent = Intent(this, FinishedCreateClassActivity::class.java)
                intent.putExtra("clickFrom", "SummaryCreateClass")
                startActivity(intent)
            } else {
                when(it.errorCode) {
                    "01" -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    "02" -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this, "Gagal membuat kelas", Toast.LENGTH_SHORT).show()
                }
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}