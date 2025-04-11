package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPenilaianKondisiObjectBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.spv.activity.DetailStaffSpvActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.otoritasChecklist.DataOtoritas
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PenilaianKondisiObjectActivity : AppCompatActivity(), NoInternetConnectionCallback {

    private lateinit var binding: ActivityPenilaianKondisiObjectBinding
    private lateinit var listDataOtoritas: ArrayList<DataOtoritas>
    private val jobName: String = "CS-LEADER"
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode: String = "CFHO"
    private val userId: Int = 6751
    private val activityId: Int = 5
    private var employeeId: Int = 6744
    private var plottingId: Int = 1
    private var firstObject: String? = null
    private var secondObject: String? = null
    private var thirdObject: String? = null
    private var fourthObject: String? = null
    private var fifthObject: String? = null
    private var firstValue: String? = null
    private var secondValue: String? = null
    private var thirdValue: String? = null
    private var fourthValue: String? = null
    private var fifthValue: String? = null
    private var note: String = ""
    private lateinit var image: MultipartBody.Part
    private var dataNoInternet: String = "Internet"

    private var latestTmpUri: Uri? = null

    private val previewImage by lazy {
        findViewById<ImageView>(R.id.iv_resultUploadFile)
    }

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenilaianKondisiObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data id
//        activityId = intent.getIntExtra("activityId", 0)
//        employeeId = intent.getIntExtra("staffId", 0)
//        plottingId = intent.getIntExtra("plottingId", 0)
        Log.d("PenilaianObject", "onCreate: employeeId = $employeeId")
        Log.d("PenilaianObject", "onCreate: plottingId = $plottingId")

        // set view appbar
        binding.appbarPenilaianKondisiObj.tvAppbarTitle.text = "Penilaian DAC"
        binding.appbarPenilaianKondisiObj.ivAppbarBack.setOnClickListener {
            if (dataNoInternet == "noInternet") {
                val back = Intent(this, DetailStaffSpvActivity::class.java)
                startActivity(back)
                finishAffinity()
            } else {
                super.onBackPressed()
                finish()
            }
        }

        // set on click upload photo
        binding.btnKirimNilai.isEnabled = false
        binding.rlUploadPhoto.setOnClickListener {
            // set camera permission
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQ)
            } else {
                takeImage()
            }
        }


        // set on click button kirim nilai
        note = binding.etNoteObject.text.toString()
        binding.btnKirimNilai.setOnClickListener {
            if (validateFirstValue() && validateSecondValue() && validateThirdValue() && validateFourthValue() && validateFifthValue()) {
                viewModel.postPenilaianObj(employeeId, projectCode, activityId, userId, plottingId,
                    firstObject, secondObject, thirdObject, fourthObject, fifthObject,
                    firstValue, secondValue, thirdValue, fourthValue, fifthValue,
                    note, image)
            } else {
                Toast.makeText(this, "Penilaian belum selesai", Toast.LENGTH_SHORT).show()
            }

        }

        // set view layout
        binding.llLayoutPenilaianKondisiObj.visibility = View.GONE
        binding.flConnectionTimeoutPenilaianKondisiObj.visibility = View.VISIBLE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(this)
        }

    }

    private fun validateFirstValue(): Boolean {
        if (firstValue != null) {
            if (firstValue == "-- Pilih Penilaian --") {
                return false
            }
        } else {
            firstValue = ""
        }
        return true
    }

    private fun validateSecondValue(): Boolean {
        if (secondValue != null) {
            if (secondValue == "-- Pilih Penilaian --") {
                return false
            }
        } else {
            secondValue = ""
        }
        return true
    }

    private fun validateThirdValue(): Boolean {
        if (thirdValue != null) {
            if (thirdValue == "-- Pilih Penilaian --") {
                return false
            }
        } else {
            thirdValue = ""
        }
        return true
    }

    private fun validateFourthValue(): Boolean {
        if (fourthValue != null) {
            if (fourthValue == "-- Pilih Penilaian --") {
                return false
            }
        } else {
            fourthValue = ""
        }
        return true
    }

    private fun validateFifthValue(): Boolean {
        if (fifthValue != null) {
            if (fifthValue == "-- Pilih Penilaian --") {
                return false
            }
        } else {
            fifthValue = ""
        }
        return true
    }

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file: File = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "_" + "photochecklist.JPEG")
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        // write the bytes in file
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

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.imagePreviewUploadFile.visibility = View.VISIBLE
                previewImage.setImageURI(uri)

                binding.btnKirimNilai.isEnabled = true
                binding.btnKirimNilai.setBackgroundResource(R.drawable.bg_primary_rounded)

                // uri
                val files = File(uri.path)
                val reqFiles = files.asRequestBody("image/*".toMediaTypeOrNull())
                val images: MultipartBody.Part = MultipartBody.Part.Companion.createFormData("file", files.name, reqFiles)

                // bitmap
                val bitmap: Bitmap = (binding.ivResultUploadFile.drawable as BitmapDrawable).bitmap
                val file = createTempFiles(bitmap)
                val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                image = MultipartBody.Part.Companion.createFormData("file", file?.name, reqFile!!)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile
        )
    }

    private fun takeImage() {
        lifecycleScope.launchWhenCreated {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission to use this app", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        //QR cam code Req
        private const val CAMERA_REQ = 101
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            dataNoInternet = "noInternet"
            return true
        }
        return false
    }

    private fun viewIsOnline() {
        binding.shimmerPenilaianKondisiObj.startShimmerAnimation()
        binding.shimmerPenilaianKondisiObj.visibility = View.VISIBLE
        binding.llLayoutPenilaianKondisiObj.visibility = View.INVISIBLE
        binding.flConnectionTimeoutPenilaianKondisiObj.visibility = View.GONE

        setObserver()
        loadData()
    }

    private fun loadData() {
        viewModel.getObjectDac(activityId, projectCode)
        viewModel.getOtoritasChecklist(projectCode)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerPenilaianKondisiObj.stopShimmerAnimation()
                        binding.shimmerPenilaianKondisiObj.visibility = View.GONE
                        binding.llLayoutPenilaianKondisiObj.visibility = View.VISIBLE
                    }, 1500)

                }
            }
        })
        viewModel.objectDacResponseModel().observe(this, {
            if (it.code == 200) {
                firstObject = it.data.objectId
                secondObject = it.data.objectIdSecond
                thirdObject = it.data.objectIdThird
                fourthObject = it.data.objectIdFour
                fifthObject = it.data.objectIdFive

                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    layoutFirstObject(firstObject)
                    layoutSecondObject(secondObject)
                    layoutThirdObject(thirdObject)
                    layoutFourthObject(fourthObject)
                    layoutFifthObject(fifthObject)
                }, 1500)

            }
        })
        viewModel.penilaianObjectResponse().observe(this, Observer {
            if (it.code == 200) {
                val i = Intent(this, HomeVendorActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.otoritasChecklistResponse().observe(this, Observer {
            if (it.code == 200) {
                listDataOtoritas = it.data as ArrayList<DataOtoritas>
                val countJob = listDataOtoritas.count { it.jobCodeOtoritas == jobName }
                when (countJob) {
                    0 -> {
                        binding.btnKirimNilai.isEnabled = false
                    }
                    1 -> {
                        binding.btnKirimNilai.isEnabled = true
                        binding.btnKirimNilai.setBackgroundResource(R.drawable.bg_primary_rounded)
                        binding.btnKirimNilai.text = "Kirim ke Supervisor"
                    }
                    2 -> {
                        binding.btnKirimNilai.isEnabled = true
                        binding.btnKirimNilai.setBackgroundResource(R.drawable.bg_primary_rounded)
                        binding.btnKirimNilai.text = "Berikan Persetujuan"}
                }
                Log.d("PenilaianKondisiObject", "setObserver: countJob = $countJob")
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun layoutFifthObject(fifthObject: String?) {
        if (fifthObject == null) {
            binding.rlObject5.visibility = View.GONE
        } else {
            binding.rlObject5.visibility = View.VISIBLE
            binding.tvFifthObject.text = fifthObject
        }

        // set spinner
        val objectValue = resources.getStringArray(R.array.nilaiPekerjaan)
        val spinner: Spinner = findViewById(R.id.spinner_fifthObject)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                if (position == 0) {
                    binding.spinnerFifthObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                } else {
                    binding.spinnerFifthObject.setBackgroundResource(R.drawable.bg_spinner)
                }
                fifthValue = objectValue[position]
//                    Toast.makeText(this@PenilaianKondisiObjectActivity, getString(R.string.select_item) + " " + valueFirstObj, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p: AdapterView<*>?) {
//                binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
            }

        }
    }

    private fun layoutFourthObject(fourthObject: String?) {
        if (fourthObject == null) {
            binding.rlObject4.visibility = View.GONE
        } else {
            binding.rlObject4.visibility = View.VISIBLE
            binding.tvFourthObject.text = fourthObject

            // set spinner
            val objectValue = resources.getStringArray(R.array.nilaiPekerjaan)
            val spinner: Spinner = findViewById(R.id.spinner_fourthObject)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position == 0) {
                        binding.spinnerFourthObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                    } else {
                        binding.spinnerFourthObject.setBackgroundResource(R.drawable.bg_spinner)
                    }
                    fourthValue = objectValue[position]
//                    Toast.makeText(this@PenilaianKondisiObjectActivity, getString(R.string.select_item) + " " + valueFirstObj, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p: AdapterView<*>?) {
//                binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                }

            }
        }
    }

    private fun layoutThirdObject(thirdObject: String?) {
        if (thirdObject == null) {
            binding.rlObject3.visibility = View.GONE
        } else {
            binding.rlObject3.visibility = View.VISIBLE
            binding.tvThirdObject.text = thirdObject

            // set spinner
            val objectValue = resources.getStringArray(R.array.nilaiPekerjaan)
            val spinner: Spinner = findViewById(R.id.spinner_thirdObject)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position == 0) {
                        binding.spinnerThirdObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                    } else {
                        binding.spinnerThirdObject.setBackgroundResource(R.drawable.bg_spinner)
                    }
                    thirdValue = objectValue[position]
//                    Toast.makeText(this@PenilaianKondisiObjectActivity, getString(R.string.select_item) + " " + valueFirstObj, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p: AdapterView<*>?) {
//                binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                }

            }
        }
    }

    private fun layoutSecondObject(secondObject: String?) {
        if (secondObject == null) {
            binding.rlObject2.visibility = View.GONE
        } else {
            binding.rlObject2.visibility = View.VISIBLE
            binding.tvSecondObject.text = secondObject

            // set spinner
            val objectValue = resources.getStringArray(R.array.nilaiPekerjaan)
            val spinner: Spinner = findViewById(R.id.spinner_secondObject)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position == 0) {
                        binding.spinnerSecondObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                    } else {
                        binding.spinnerSecondObject.setBackgroundResource(R.drawable.bg_spinner)
                    }
                    secondValue = objectValue[position]
//                    Toast.makeText(this@PenilaianKondisiObjectActivity, getString(R.string.select_item) + " " + valueFirstObj, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p: AdapterView<*>?) {
//                binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                }

            }
        }
    }

    private fun layoutFirstObject(firstObject: String?) {
        if (firstObject == null) {
            binding.rlObject1.visibility = View.GONE
        } else {
            binding.rlObject1.visibility = View.VISIBLE
            binding.tvFirstObject.text = this.firstObject

            // set spinner
            val objectValue = resources.getStringArray(R.array.nilaiPekerjaan)
            val spinner: Spinner = findViewById(R.id.spinner_firstObject)
            val adapter = ArrayAdapter(this, R.layout.spinner_item, objectValue)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    if (position == 0) {
                        binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                    } else {
                        binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner)
                    }
                    firstValue = objectValue[position]
//                    Toast.makeText(this@PenilaianKondisiObjectActivity, getString(R.string.select_item) + " " + valueFirstObj, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p: AdapterView<*>?) {
//                binding.spinnerFirstObject.setBackgroundResource(R.drawable.bg_spinner_disable)
                }

            }
        }
    }

    private fun noInternetState() {
        val noInternetState = ConnectionTimeoutFragment.newInstance().also {
            it.setListener(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.flConnectionTimeoutPenilaianKondisiObj, noInternetState, "connectionTimeout")
            .commit()
    }

    override fun onBackPressed() {
        if (dataNoInternet == "noInternet") {
            val back = Intent(this, DetailStaffSpvActivity::class.java)
            startActivity(back)
            finishAffinity()
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onRetry() {
        val i = Intent(this, PenilaianKondisiObjectActivity::class.java)
        startActivity(i)
        finish()
    }
}