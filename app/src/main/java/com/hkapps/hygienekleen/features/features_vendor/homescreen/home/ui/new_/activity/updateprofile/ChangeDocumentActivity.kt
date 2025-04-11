package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUpdateDocumentBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.fragment.BotSheetUploadDocumentFragment
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ChangeDocumentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateDocumentBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var loadingDialog: Dialog? = null
    private val numberNIK =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_NIK, "")
    private var bpjsImg =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_BPJS,"")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateDocumentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layoutAppbarUpdateDocument.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAppbarUpdateDocument.tvAppbarTitle.text = "Dokumen Saya"

        binding.llUploadKTP.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "KTP")
            BotSheetUploadDocumentFragment().show(supportFragmentManager, "bottomsheetktp")
        }
        binding.llUploadIjazah.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "IJAZAH")
            BotSheetUploadDocumentFragment().show(supportFragmentManager, "bottomsheetijazah")
        }
        binding.llUploadSertifikat.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.TYPE_DOCUMENT,
                "SERTIFIKAT KOMPETENSI"
            )
            BotSheetUploadDocumentFragment().show(supportFragmentManager, "bottomsheetsertifikat")
        }
        binding.llUploadSIO.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "SIO")
            BotSheetUploadDocumentFragment().show(supportFragmentManager, "bottomsheetsio")
        }
        binding.llUploadSKCK.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.TYPE_DOCUMENT, "SKCK")
            BotSheetUploadDocumentFragment().show(supportFragmentManager, "bottomsheetskck")
        }
        //bpjs
        binding.llbpjskerja.setOnClickListener {
            startActivity(Intent(this, UpdateJamsostekActivity::class.java))
        }
        binding.llbpjskesehatan.setOnClickListener {
            startActivity(Intent(this, UpdateBpjsActivity::class.java))
        }


        loadData()
        setObserver()
    }

    companion object {
        private val TAG = ChangeDocumentActivity::class.java.simpleName
        const val REQUEST_IMAGE = 100
    }


    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivKTPPlaceImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // You can update this bitmap to your server
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    Log.d(TAG, "onActivityResult: $bitmap")
                    // loading profile image from local cache
                    loadProfile(uri.toString())

                    binding.ivKTPPlaceImage.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadingUploadPhoto(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, "loadingText")

        val bitmaps: Bitmap = (binding.ivKTPPlaceImage.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmaps)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
        viewModel.UploadDocument(employeeId, image, typeDocument = "KTP")

    }

    private fun createTempFiles(bitmap: Bitmap): File? {
//        File file = new File(TahapTigaActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                , System.currentTimeMillis() + "_education.JPEG");
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoselfie.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
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

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    //fun
    private fun loadData() {
        viewModel.getListDocument(employeeId)
    }

    private fun updatePhotoProfile() {
        if (binding.ivKTPPlaceImage.drawable == null) {
            Toast.makeText(this, "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
        } else {
            loadingUploadPhoto(getString(R.string.loading_string2))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.getListDocumentViewModel().observe(this) {
            if (it.code == 200) {
                hideLoading()
                //get nik
                binding.tvNumberNikDokumen.text = if (numberNIK.isNullOrEmpty()) {
                    "Number nik not found"
                } else {
                    numberNIK
                }
                binding.tvUploadedKTP.text = if (it.data.employeeKtp == null) {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_KTP,
                        false
                    )
                    "file belum tersedia"
                } else {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_KTP,
                        true
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_KTP,
                        it.data.employeeKtp
                    )
                    it.data.employeeKtp
                }
                binding.tvUploadedIjazah.text = if (it.data.employeeIjazah == null) {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_IJAZAH,
                        false
                    )
                    "file belum tersedia"
                } else {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_IJAZAH,
                        true
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_IJAZAH,
                        it.data.employeeIjazah
                    )
                    it.data.employeeIjazah
                }
                binding.tvUploadedSertifikat.text =
                    if (it.data.employeeSertifikatKompetensi == null) {
                        CarefastOperationPref.saveBoolean(
                            CarefastOperationPrefConst.CHECK_DOCUMENT_SERTIFIKAT_KOMPETENSI,
                            false
                        )
                        "file belum tersedia"
                    } else {
                        CarefastOperationPref.saveBoolean(
                            CarefastOperationPrefConst.CHECK_DOCUMENT_SERTIFIKAT_KOMPETENSI,
                            true
                        )
                        CarefastOperationPref.saveString(
                            CarefastOperationPrefConst.IMAGE_SERTIFIKAT,
                        it.data.employeeSertifikatKompetensi)
                        it.data.employeeSertifikatKompetensi
                    }
                binding.tvUploadedSio.text = if (it.data.employeeSio == null) {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_SIO,
                        false
                    )
                    "file belum tersedia"
                } else {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_SIO,
                        true
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_SIO,
                        it.data.employeeSio.toString()
                    )
                    it.data.employeeSio.toString()
                }
                binding.tvUploadedSkck.text = if (it.data.employeeSkck == null) {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_SKCK,
                        false
                    )
                    "file belum tersedia"
                } else {
                    CarefastOperationPref.saveBoolean(
                        CarefastOperationPrefConst.CHECK_DOCUMENT_SKCK,
                        true
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.IMAGE_SKCK,
                        it.data.employeeSkck.toString()
                    )
                    it.data.employeeSkck.toString()
                }

                binding.tvBpjsKes.text = it?.data?.employeeBpjsKesehatanNumber ?:"Nomor belum tersedia"
                binding.tvJamsostekNumb.text = it?.data?.employeeBpjsKetenagakerjaanNumber ?:"Nomor belum tersedia"

                binding.tvFileBpjsKes.text = it?.data?.employeeBpjsKesehatanFile ?:"File belum tersedia"
                binding.tvFileJamsostek.text = it?.data?.employeeBpjsKetenagakerjaanFile ?:"File belum tersedia"

                if (it.data.employeeBpjsKesehatanNumber.isNullOrEmpty() || it.data.employeeBpjsKesehatanFile.isNullOrEmpty()){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSKES_LOW, "")
                } else {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSKES_LOW, it.data.employeeBpjsKesehatanNumber)
                }

                if (it.data.employeeBpjsKetenagakerjaanNumber.isNullOrEmpty() || it.data.employeeBpjsKetenagakerjaanFile.isNullOrEmpty()){
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSTK_LOW, "")
                } else {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.NUMBER_BPJSTK_LOW, it.data.employeeBpjsKetenagakerjaanNumber)
                }


            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}