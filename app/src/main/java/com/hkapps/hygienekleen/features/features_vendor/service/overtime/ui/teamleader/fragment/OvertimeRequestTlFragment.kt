package com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOvertimeRequestTlBinding
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel.CreatePermissionActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OvertimeRequestTlFragment : Fragment() {

    private lateinit var binding: FragmentOvertimeRequestTlBinding
    private var title: String = ""
    private var dateParamm: String = ""
    private var dateText: String = ""
    private var hourStart: Int = 0
    private var minuteStart: Int = 0
    private var timeStart: String = ""
    private var timeEnd: String = ""
    private var hourEnd: Int = 0
    private var minuteEnd: Int = 0

    private var latestTmpUri: Uri? = null
    private val previewImage by lazy {
        view?.findViewById<ImageView>(R.id.iv_OvertimeRequestTl)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeRequestTlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set spinner title
        val objectValue = resources.getStringArray(R.array.overtimeReqType)
        val spinner: Spinner = view.findViewById(R.id.spinnerOvertimeRequestTl)
        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, objectValue)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                if (position == 0) {
                    binding.spinnerOvertimeRequestTl.setBackgroundResource(R.drawable.bg_spinner_default)
                } else {
                    binding.spinnerOvertimeRequestTl.setBackgroundResource(R.drawable.bg_spinner)
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
        binding.etDescOvertimeRequestTl.addTextChangedListener {
            if (binding.etDescOvertimeRequestTl.text.isNullOrEmpty()) {
                binding.rlDescOvertimeRequestTl.setBackgroundResource(R.drawable.bg_empty_card)
            } else {
                binding.rlDescOvertimeRequestTl.setBackgroundResource(R.drawable.bg_white_card)
            }
        }

        // set on click upload photo
        binding.cardImageOvertimeRequestTl.setOnClickListener {
            setupPermissions()
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
                binding.tvDateOvertimeRequestTl.text = dateText
                binding.rlDateOvertimeRequestTl.setBackgroundResource(R.drawable.bg_white_card)
            }

        binding.rlDateOvertimeRequestTl.setOnClickListener {
            if (validateDate()) {
                DatePickerDialog(
                    requireContext(), R.style.CustomDatePickerDialogTheme, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            } else {
                Toast.makeText(context, "Judul lembur & alasan lembur harus diisi", Toast.LENGTH_SHORT).show()
            }

        }

        // set on click time start
        binding.rlTime1OvertimeRequestTl.setOnClickListener {
            openDialogTimeStart()
        }

        // set on click time end
        binding.rlTime2OvertimeRequestTl.setOnClickListener {
            openDialogTimeEnd()
        }

        // spinner area
//        val objectValue = resources.getStringArray(R.array.overtimeReqType)
//        val spinner: Spinner = view.findViewById(R.id.spinnerOvertimeRequestTl)
//        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, objectValue)
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
//                if (position == 0) {
//                    binding.spinnerOvertimeRequestTl.setBackgroundResource(R.drawable.bg_spinner_default)
//                } else {
//                    binding.spinnerOvertimeRequestTl.setBackgroundResource(R.drawable.bg_spinner)
//                }
//                title = when(position) {
//                    0 -> ""
//                    else -> objectValue[position]
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//
//        }

    }

    private fun openDialogTimeEnd() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            hourEnd = hour
            minuteEnd = minute
            timeEnd = String.format(Locale.getDefault(), "%02d:%02d", hourEnd, minuteEnd)
            binding.tvTime2OvertimeRequestTl.text = timeEnd
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
            binding.tvTime1OvertimeRequestTl.text = timeStart
        }
        val style = AlertDialog.THEME_HOLO_DARK
        val timePickerDialog = TimePickerDialog(requireContext(), style, timeSetListener, hourStart, minuteStart, true)

        timePickerDialog.setTitle("Pilih Waktu")
        timePickerDialog.show()
    }

    private fun validateDate(): Boolean {
        if (title == "") {
            return false
        }
        if (binding.etDescOvertimeRequestTl.text.isNullOrEmpty()) {
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
                    val images: MultipartBody.Part =
                        MultipartBody.Part.createFormData("file", files.name, reqFiles)

                    //bitmap
                    binding.ivDefaultOvertimeRequestTl.visibility = View.GONE
                    binding.ivOvertimeRequestTl.visibility = View.VISIBLE

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