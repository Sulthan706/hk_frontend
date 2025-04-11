package com.hkapps.hygienekleen.features.features_client.overtime.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOvertimeRequestClientBinding
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.HomeClientActivity
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel.CreatePermissionActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OvertimeRequestClientFragment : Fragment() {

    private lateinit var binding: FragmentOvertimeRequestClientBinding
    private var dateText: String = ""
    private var hourStart: Int = 0
    private var minuteStart: Int = 0
    private var hourEnd: Int = 0
    private var minuteEnd: Int = 0

    private val clientId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private var title: String = ""
    private var image: MultipartBody.Part? = null
    private var dateParamm: String = ""
    private var timeStart: String = ""
    private var timeEnd: String = ""
    private var locationId: Int = 0
    private var subLocationId: Int = 0

    private var latestTmpUri: Uri? = null
    private val previewImage by lazy {
        view?.findViewById<ImageView>(R.id.iv_OvertimeRequestClient)
    }

    private val viewModel: OvertimeClientViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeClientViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeRequestClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set spinner title
        val objectValue = resources.getStringArray(R.array.overtimeReqType)
        val spinner: Spinner = view.findViewById(R.id.spinnerOvertimeRequestClient)
        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, objectValue)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                if (position == 0) {
                    binding.spinnerOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner_default)
                } else {
                    binding.spinnerOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner)
                }
                title = when(position) {
                    0 -> ""
                    else -> objectValue[position]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        // validate input text desc
        binding.etDescOvertimeRequestClient.addTextChangedListener {
            if (binding.etDescOvertimeRequestClient.text.isNullOrEmpty()) {
                binding.rlDescOvertimeRequestClient.setBackgroundResource(R.drawable.bg_empty_card)
            } else {
                binding.rlDescOvertimeRequestClient.setBackgroundResource(R.drawable.bg_white_card)
            }
        }

        // set on click upload photo
        binding.cardImageOvertimeRequestClient.setOnClickListener {
            if (validateDate()) {
                setupPermissions()
            } else {
                Toast.makeText(context, "Judul lembur & alasan lembur harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // open dialog choose date
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMMM yyyy" // mention the format you need
                val paramsFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateParam = SimpleDateFormat(paramsFormat, Locale.US)

                dateParamm = dateParam.format(cal.time)
                dateText = sdf.format(cal.time)
                binding.tvDateOvertimeRequestClient.text = dateText
                binding.rlDateOvertimeRequestClient.setBackgroundResource(R.drawable.bg_white_card)
            }

        binding.rlDateOvertimeRequestClient.setOnClickListener {
            if (image != null) {
                DatePickerDialog(
                    requireContext(), R.style.CustomDatePickerDialogTheme, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            } else {
                Toast.makeText(context, "Unggah foto dahulu", Toast.LENGTH_SHORT).show()
            }

        }

        // set on click time start
        binding.rlTime1OvertimeRequestClient.setOnClickListener {
            if (dateParamm == "") {
                Toast.makeText(context, "Pilih tanggal dahulu", Toast.LENGTH_SHORT).show()
            } else {
                openDialogTimeStart()
            }
        }

        // set on click time end
        binding.rlTime2OvertimeRequestClient.setOnClickListener {
            if (timeStart != "") {
                openDialogTimeEnd()
            } else {
                Toast.makeText(context, "Waktu awal harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        // spinner location
        viewModel.getLocationOvertimeClient(projectId)
        viewModel.locationOvertimeClientModel().observe(requireActivity()) {
            val locationValue = ArrayList<String>()
            locationValue.add("Pilih Area")
            val locSize = it.data.size
            for (i in 0 until locSize) {
                locationValue.add(it.data[i].locationName)
            }

            val adapterLoc: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, locationValue)
            adapterLoc.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerLocOvertimeRequestClient.adapter = adapterLoc

            binding.spinnerLocOvertimeRequestClient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position == 0) {
                        binding.spinnerLocOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner_default)
                    } else {
                        binding.spinnerLocOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner)
                        locationId = it.data[position].locationId
                        Log.d("OvertimeReqClientAct", "onItemSelected: locId = $locationId")
                        viewModel.getSubLocOvertimeClient(projectId, it.data[position].locationId)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

            // spinner sub location
            viewModel.subLocOvertimeClientModel().observe(requireActivity()) {
                val subLocValue = ArrayList<String>()
                subLocValue.add("Pilih Sub Area")
                val subLocSize = it.data.size
                for (i in 0 until subLocSize) {
                    subLocValue.add(it.data[i].subLocationName)
                }

                val adapterSubLoc: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, subLocValue)
                adapterSubLoc.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                binding.spinnerSubLocOvertimeRequestClient.adapter = adapterSubLoc

                binding.spinnerSubLocOvertimeRequestClient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        if (position == 0) {
                            binding.spinnerSubLocOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner_default)
                        } else {
                            binding.spinnerSubLocOvertimeRequestClient.setBackgroundResource(R.drawable.bg_spinner)
                            subLocationId = it.data[position].subLocationId
                            Log.d("OvertimeReqClientAct", "onItemSelected: subLocId = $subLocationId")
                        }

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            }
        }

        // validate text input operational
        binding.etOpsOvertimeRequestClient.addTextChangedListener() {
            if (binding.etOpsOvertimeRequestClient.text.isNullOrEmpty()) {
                binding.rlOpsOvertimeRequestClient.setBackgroundResource(R.drawable.bg_empty_card)
            } else {
                binding.rlOpsOvertimeRequestClient.setBackgroundResource(R.drawable.bg_white_card)

                if (locationId != 0) {
                    binding.btnSubmitDisableOvertimeRequestClient.visibility = View.GONE
                    binding.btnSubmitEnableOvertimeRequestClient.visibility = View.VISIBLE
                } else {
                    binding.btnSubmitDisableOvertimeRequestClient.visibility = View.VISIBLE
                    binding.btnSubmitEnableOvertimeRequestClient.visibility = View.GONE
                }
            }
        }

        binding.btnSubmitEnableOvertimeRequestClient.setOnClickListener {

            val bitmap: Bitmap =
                (binding.ivOvertimeRequestClient.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

            viewModel.createOvertimeClient(
                clientId,
                projectId,
                title,
                binding.etDescOvertimeRequestClient.text.toString(),
                locationId,
                subLocationId,
                dateParamm,
                "$timeStart:00",
                "$timeEnd:00",
                image,
                Integer.parseInt(binding.etOpsOvertimeRequestClient.text.toString())
            )
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.createOvertimeReqModel().observe(requireActivity()) {
            if (it.code == 200) {
                val i = Intent(requireContext(), HomeClientActivity::class.java)
                startActivity(i)
                requireActivity().finish()
                Toast.makeText(requireContext(), "Berhasil membuat lembur.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openDialogTimeEnd() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hourEnd = hour
            minuteEnd = minute
            timeEnd = String.format(Locale.getDefault(), "%02d:%02d", hourEnd, minuteEnd)
            binding.tvTime2OvertimeRequestClient.text = timeEnd
            binding.rlTime2OvertimeRequestClient.setBackgroundResource(R.drawable.bg_white_card)
        }
        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(requireContext(), style, timeSetListener, hourEnd, minuteEnd, true)

        timePickerDialog.setTitle("Pilih Waktu")
        timePickerDialog.show()
    }

    private fun openDialogTimeStart() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hourStart = hour
            minuteStart = minute
            timeStart = String.format(Locale.getDefault(), "%02d:%02d", hourStart, minuteStart)
            binding.tvTime1OvertimeRequestClient.text = timeStart
            binding.rlTime1OvertimeRequestClient.setBackgroundResource(R.drawable.bg_white_card)
        }
        val style = AlertDialog.THEME_HOLO_LIGHT
        val timePickerDialog = TimePickerDialog(requireContext(), style, timeSetListener, hourStart, minuteStart, true)

        timePickerDialog.setTitle("Pilih Waktu")
        timePickerDialog.show()
    }

    private fun validateDate(): Boolean {
        if (title == "") {
            return false
        }
        if (binding.etDescOvertimeRequestClient.text.isNullOrEmpty()) {
            return false
        }
        return true
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    // ambil foto
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    previewImage?.setImageURI(uri)
                    //uri
                    val files = File(uri.path)
                    val reqFiles = files.asRequestBody("image/*".toMediaTypeOrNull())
                    image = MultipartBody.Part.createFormData("file", files.name, reqFiles)

                    //bitmap
                    binding.ivDefaultOvertimeRequestClient.visibility = View.GONE
                    binding.ivOvertimeRequestClient.visibility = View.VISIBLE

                }
            }
        }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CreatePermissionActivity.CAMERA_REQ
        )
    }

}