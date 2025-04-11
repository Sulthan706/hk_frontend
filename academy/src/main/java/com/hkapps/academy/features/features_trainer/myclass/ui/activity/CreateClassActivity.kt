package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityCreateClassBinding
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CreateClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateClassBinding

    private val userName = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NAME, "")
    private val trainerNuc = AcademyOperationPref.loadString(AcademyOperationPrefConst.USER_NUC, "")
    private var trainingName = ""
    private var participant = ""
    private var moduleId = 0
    private var assignOnlineTest = ""
    private var category = ""
    private var quota = 0
    private var projectCode = ""
    private var projectName = ""
    private var trainingDate = ""
    private var trainingStart = ""
    private var trainingEnd = ""
    private var region = ""
    private var jenisKelas = ""
    private var trainingLocationCode = ""
    private var locationDescription = ""
    private var alternateLocation = ""
    private var appName = ""
    private var appLink = ""
    private var levelModule = ""
    private var nameModule = ""

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarCreateClass.tvAppbarTitle.text = "Buat Kelas Training"
        binding.appbarCreateClass.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set trainer name
        binding.tvTrainerNameCreateClass.text = userName

        // set time zone
        val timeZone = TimeZone.getDefault().getOffset(Date().time) / 3600000.0
        region = when(timeZone.toString()) {
            "7.0" -> "WIB"
            "8.0" -> "WITA"
            "9.0" -> "WIT"
            else -> ""
        }

        // set title training
        binding.etClassNameCreateClass.addTextChangedListener {
            trainingName = binding.etClassNameCreateClass.text.toString()
        }

        // set spinner training for
        val jobListValue = resources.getStringArray(R.array.jobList)
        val spinnerAdapterJobList = ArrayAdapter(this, R.layout.spinner_item, jobListValue)
        spinnerAdapterJobList.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerTrainingForCreateClass.adapter = spinnerAdapterJobList
        binding.spinnerTrainingForCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                participant = if (position != 0) {
                    if (jobListValue[position] == "Semua Jabatan") {
                        "Semua"
                    } else {
                        jobListValue[position]
                    }
                } else {
                    ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        // set spinner module training
        val moduleCategory = ArrayList<String>()
        moduleCategory.add("Pilih Kategori")
        val adapterModuleCategory = ArrayAdapter(this, R.layout.spinner_item, moduleCategory)
        adapterModuleCategory.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerModuleTrainingCreateClass.adapter = adapterModuleCategory

        viewModel.getListModuleCreateClass()
        viewModel.listModuleCreateClassModel.observe(this) {
            if (it.code == 200) {
                val length = it.data.size
                for (i in 0 until length) {
                    moduleCategory.add(it.data[i].moduleName)
                }

                binding.spinnerModuleTrainingCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position == 0) {
                            moduleId = 0
                        } else {
                            moduleId = it.data[position-1].moduleId
                            levelModule = it.data[position-1].moduleLevel
                            nameModule = moduleCategory[position]
                            binding.tvLevelModuleTrainingCreateClass.text = "Level : ${it.data[position-1].moduleLevel}"
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }
            } else {
                Toast.makeText(this, "Gagal mengambil data list module", Toast.LENGTH_SHORT).show()
            }
        }

        // set spinner assign online test?
        val assignOnline = resources.getStringArray(R.array.yesOrNo)
        val adapterAssignOnline = ArrayAdapter(this, R.layout.spinner_item, assignOnline)
        adapterAssignOnline.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerAssignOnlineCreateClass.adapter = adapterAssignOnline
        binding.spinnerAssignOnlineCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                assignOnlineTest = if (position == 0) {
                    ""
                } else {
                    assignOnline[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        // set spinner kategori kelas
        val classCategory = resources.getStringArray(R.array.publicOrPrivate)
        val adapterClassCategory = ArrayAdapter(this, R.layout.spinner_item, classCategory)
        adapterClassCategory.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerClassCategoryCreateClass.adapter = adapterClassCategory
        binding.spinnerClassCategoryCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    category = classCategory[position]
                } else {
                    category = ""
                }

                if (classCategory[position] == "PUBLIK") {
                    binding.etKuotaCreateClass.setBackgroundResource(R.drawable.bg_field)
                    binding.etKuotaCreateClass.isEnabled = true

                    // set participant quota
                    binding.etKuotaCreateClass.addTextChangedListener {
                        val etQuota = binding.etKuotaCreateClass.text.toString()
                        quota = if (etQuota == "") {
                            0
                        } else {
                            etQuota.toInt()
                        }
                    }
                } else {
                    binding.etKuotaCreateClass.setBackgroundResource(R.drawable.bg_field_disable)
                    binding.etKuotaCreateClass.isEnabled = false
                    binding.etKuotaCreateClass.text = null
                    quota = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        // set spinner project
        viewModel.getListProjectCreateClass("")
        viewModel.listProjectCreateClassModel.observe(this) {
            if (it.code == 200) {
                val projectObj = ArrayList<String>()
                projectObj.add("SEMUA PROJECT")
                val length = it.data.size
                for (i in 0 until length) {
                    projectObj.add(it.data[i].projectName)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, R.layout.spinner_item, projectObj
                )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.spinnerProjectCreateClass.adapter = adapter
                binding.spinnerProjectCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        if (projectObj[position] == "SEMUA PROJECT") {
                            projectCode = "Semua"
                            projectName = "Semua Project"
                        } else {
                            projectCode = it.data[position-1].projectCode
                            projectName = it.data[position-1].projectName
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }
            } else {
                Toast.makeText(applicationContext, "Gagal mengambil data list project", Toast.LENGTH_SHORT).show()
            }
        }

        // open date dialog to set date
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateTxtFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
            val dateApiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            trainingDate = dateApiFormat.format(cal.time)
            binding.tvDateTrainingCreateClass.text = dateTxtFormat.format(cal.time)
            binding.tvDateTrainingCreateClass.setTextColor(resources.getColor(R.color.black2))
        }
        binding.tvDateTrainingCreateClass.setOnClickListener {
            DatePickerDialog(
                this, R.style.DatePickerDialogAcademyTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // open dialog time training
        binding.tvStartTimeCreateClass.setOnClickListener {
            openDialogStartTime()
        }
        binding.tvEndTimeCreateClass.setOnClickListener {
            openDialogEndTime()
        }

        // set spinner jenis kelas
        val classType = resources.getStringArray(R.array.onlineOrOnsite)
        val adapterClassType = ArrayAdapter(this, R.layout.spinner_item, classType)
        adapterClassType.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerClassTypeCreateClass.adapter = adapterClassType
        binding.spinnerClassTypeCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                jenisKelas = classType[position]
                when (classType[position]) {
                    "ONLINE" -> {
                        binding.llOnlineCreateClass.visibility = View.VISIBLE
                        binding.llOnsiteCreateClass.visibility = View.GONE
                        setOnlineFormLayout()
                    }
                    "ONSITE" -> {
                        binding.llOnlineCreateClass.visibility = View.GONE
                        binding.llOnsiteCreateClass.visibility = View.VISIBLE
                        setOnsiteFormLayout()
                    }
                    else -> {
                        binding.llOnlineCreateClass.visibility = View.GONE
                        binding.llOnsiteCreateClass.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.llOnlineCreateClass.visibility = View.GONE
                binding.llOnsiteCreateClass.visibility = View.GONE
            }

        }

        // button lanjut
        binding.btnNextCreateClass.setOnClickListener {
            AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_CREATE_CLASS, trainingName)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, participant)
            AcademyOperationPref.saveInt(AcademyOperationPrefConst.MODULE_ID_CREATE_CLASS, moduleId)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.ONLINE_TEST_CREATE_CLASS, assignOnlineTest)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.CATEGORY_CREATE_CLASS, category)
            AcademyOperationPref.saveInt(AcademyOperationPrefConst.QUOTA_CREATE_CLASS, quota)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_CODE_CREATE_CLASS, projectCode)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_NAME_CREATE_CLASS, projectName)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_DATE_CREATE_CLASS, trainingDate)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_START_CREATE_CLASS, trainingStart)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_END_CREATE_CLASS, trainingEnd)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.REGION_CREATE_CLASS, region)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.JENIS_KELAS_CREATE_CLASS, jenisKelas)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.TRAINING_LOCATION_CREATE_CLASS, trainingLocationCode)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.LOCATION_DESC_CREATE_CLASS, locationDescription)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.ALTERNATE_LOCATION_CREATE_CLASS, alternateLocation)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.APP_NAME_CREATE_CLASS, appName)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.APP_LINK_CREATE_CLASS, appLink)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.MODULE_LEVEL_CREATE_CLASS, levelModule)
            AcademyOperationPref.saveString(AcademyOperationPrefConst.MODULE_NAME_CREATE_CLASS, nameModule)

            startActivity(Intent(this, SummaryCreateClassActivity::class.java))
        }

    }

    private fun setOnsiteFormLayout() {
        // set null online data
        appName = ""
        appLink = ""

        // set spinner location project
        viewModel.getLocationProjectCreateClass("")
        viewModel.locationProjectCreateClassModel.observe(this) {
            if (it.code == 200) {
                val locProjectObj = ArrayList<String>()
                val length = it.data.size
                for (i in 0 until length) {
                    locProjectObj.add(it.data[i].projectName)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, R.layout.spinner_item, locProjectObj
                )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding.spinnerLocationTrainingCreateClass.adapter = adapter
                binding.spinnerLocationTrainingCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        trainingLocationCode = it.data[position].projectCode
                        locationDescription = it.data[position].projectAddress

                        binding.tvLocationTrainingCreateClass.text = locationDescription
                        binding.tvLocationTrainingCreateClass.setTextColor(resources.getColor(R.color.grey2_client))
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        binding.tvLocationTrainingCreateClass.text = "Alamat Lokasi"
                    }

                }
            } else {
                Toast.makeText(this, "Gagal mengambil data location project", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // set another location
        var isChecked = false
        binding.ivCheckOtherLocCreateClass.visibility = View.GONE
        binding.ivUncheckOtherLocCreateClass.visibility = View.VISIBLE
        binding.etLocationTrainingCreateClass.setBackgroundResource(R.drawable.bg_field_disable)
        binding.etLocationTrainingCreateClass.text = null
        binding.etLocationTrainingCreateClass.isEnabled = false

        binding.llCheckOtherLocCreateClass.setOnClickListener {
            if (trainingLocationCode == "") {
                Toast.makeText(
                    this,
                    "Pilih lokasi project training terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!isChecked) {
                    isChecked = true
                    binding.ivCheckOtherLocCreateClass.visibility = View.VISIBLE
                    binding.ivUncheckOtherLocCreateClass.visibility = View.GONE

                    binding.tvLocationTrainingCreateClass.setTextColor(resources.getColor(R.color.smsp_hint_color))
                    binding.etLocationTrainingCreateClass.isEnabled = true
                    binding.etLocationTrainingCreateClass.setBackgroundResource(R.drawable.bg_field)
                    binding.etLocationTrainingCreateClass.addTextChangedListener {
                        alternateLocation = binding.etLocationTrainingCreateClass.text.toString()
                    }
                } else {
                    isChecked = false
                    binding.ivCheckOtherLocCreateClass.visibility = View.GONE
                    binding.ivUncheckOtherLocCreateClass.visibility = View.VISIBLE

                    binding.tvLocationTrainingCreateClass.setTextColor(resources.getColor(R.color.grey2_client))
                    binding.etLocationTrainingCreateClass.isEnabled = false
                    binding.etLocationTrainingCreateClass.setBackgroundResource(R.drawable.bg_field_disable)
                    binding.etLocationTrainingCreateClass.text = null
                }
            }
        }
    }

    private fun setOnlineFormLayout() {
        // set null onsite data
        trainingLocationCode = ""
        locationDescription = ""
        alternateLocation = ""

        // set spinner link training
        val meetingAppsObj = resources.getStringArray(R.array.meetingApps)
        val adapterMeetingApp = ArrayAdapter(this, R.layout.spinner_item, meetingAppsObj)
        adapterMeetingApp.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerLinkTrainingCreateClass.adapter = adapterMeetingApp
        binding.spinnerLinkTrainingCreateClass.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                if (position == 0) {
                    appName = ""
                    binding.etLinkTrainingCreateClass.text = null
                } else {
                    appName = meetingAppsObj[position]

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        // set edit text link online training
        binding.etLinkTrainingCreateClass.addTextChangedListener {
            appLink = binding.etLinkTrainingCreateClass.text.toString()
        }

    }

    private fun openDialogEndTime() {
        var hours = 0
        var minutes = 0
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hours = hour
            minutes = minute
            trainingEnd = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            binding.tvEndTimeCreateClass.text = trainingEnd
            binding.tvEndTimeCreateClass.setTextColor(resources.getColor(R.color.black2))
        }

        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(this, style, timeSetListener, hours, minutes, true)
        timePickerDialog.setTitle("Pilih Waktu")
        timePickerDialog.show()
    }

    private fun openDialogStartTime() {
        var hours = 0
        var minutes = 0
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hours = hour
            minutes = minute
            trainingStart = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
            binding.tvStartTimeCreateClass.text = trainingStart
            binding.tvStartTimeCreateClass.setTextColor(resources.getColor(R.color.black2))
        }

        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(this, style, timeSetListener, hours, minutes, true)
        timePickerDialog.setTitle("Pilih Waktu")
        timePickerDialog.show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
}