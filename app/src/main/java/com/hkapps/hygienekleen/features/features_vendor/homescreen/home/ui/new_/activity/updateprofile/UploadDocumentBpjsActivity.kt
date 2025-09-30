package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityUploadDocumentBpjsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UploadDocumentBpjsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadDocumentBpjsBinding
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }
    private var openMedia =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.MEDIA_OPENER_BPJS, "")
    private var typeBpjs =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_BPJS, "")
    private var loadingDialog: Dialog? = null
    private var employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var numberBpjs =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_BPJS, "")
    private var numberJamsostek =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NUMBER_JAMSOSTEK, "")
    private var typeBpjsDoc =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_BPJS_DOCUMENT, "")
    private lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDocumentBpjsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        if (typeBpjs == "BPJSKES"){
            binding.tvNumbKesehatan.setText(numberBpjs)
        } else {
            binding.tvNumbKesehatan.setText(numberJamsostek)

        }


        binding.btnUploadDocument.setOnClickListener {
            showLoading("Loading..")
        }

        openMedia()
        setObserver()
    }

    private fun setObserver() {
        viewModel.putUpdateBpjsViewModel().observe(this) {
            if (it.code == 200) {
                dialogSuccessUpdate()
            } else {
                Toast.makeText(this, "Gagal update bpjs", Toast.LENGTH_SHORT).show()
            }
        }
        hideLoading()
    }

    private fun dialogSuccessUpdate() {
        val view = View.inflate(this, R.layout.dialog_succes_update_number_bpjs, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun openMedia(){
        if (openMedia == "gallery"){
            validationTakeGallery()
        } else {
            takeImage()
        }
    }

    private fun validationTakeGallery(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            takeGallery()
        } else {
            takeGalleryLow()
        }
    }

    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivResultSelfie)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangeDocumentActivity.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    // loading profile image from local cache
                    loadProfile(uri.toString())
                    binding.ivResultSelfie.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
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

    //take by gallery
    class OpenDocumentContract(private val context: Context) :
        ActivityResultContract<String, Uri?>() {

        override fun createIntent(context: Context, input: String): Intent {
            return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*" // specify the MIME type of the file(s) to select
                addCategory(Intent.CATEGORY_OPENABLE) // specify that we want to open a file
            }
        }

        private fun isImageFile(uri: Uri): Boolean {
            // Perform validation to check if the file is an image
            val contentResolver: ContentResolver = context.contentResolver
            val mimeType: String? = contentResolver.getType(uri)
            return mimeType?.startsWith("image/") == true
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                val uri: Uri? = intent.data
                // Perform additional validation if needed
                if (uri != null && isImageFile(uri)) {
                    return uri
                }
            }
            return null
        }
    }

    private fun takeGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, launch the file picker intent
            val openDocumentContract = OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    if (typeBpjs == "BPJSKES") {
                        val intent =
                            Intent(applicationContext, UpdateBpjsActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                        startActivity(intent)
                        finish()
                    } else {
                        val intent =
                            Intent(applicationContext, UpdateJamsostekActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                        startActivity(intent)
                        finish()
                    }

                }
            }
            resultLauncher.launch("")
        } else {
            // Permission is denied, show an error message
            Toast.makeText(
                this,
                "Permission denied, please allow external storage in app settings",
                Toast.LENGTH_SHORT
            ).show()
            val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            settingsIntent.data = uri
            startActivity(settingsIntent)
            finish()
        }
    }

    private fun takeGalleryLow() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, launch the file picker intent
            val openDocumentContract = OpenDocumentContract(this)
            val resultLauncher = registerForActivityResult(openDocumentContract) { uri ->
                uri?.let {
                    // handle the selected file URI
                    Glide.with(applicationContext).load(it).into(binding.ivResultSelfie)
                    binding.llResultSelfie.visibility = View.VISIBLE
                } ?: run {
                    // finish activity with FLAG_ACTIVITY_CLEAR_TOP flag
                    if (typeBpjs == "BPJSKES") {
                        val intent =
                            Intent(applicationContext, UpdateBpjsActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                        startActivity(intent)
                        finish()
                    } else {
                        val intent =
                            Intent(applicationContext, UpdateJamsostekActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }
                        startActivity(intent)
                        finish()
                    }

                }
            }
            resultLauncher.launch("")
        } else {
            // Permission is denied, show an error message
            Toast.makeText(
                this,
                "Permission denied, please allow external storage in app settings",
                Toast.LENGTH_SHORT
            ).show()
            val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            settingsIntent.data = uri
            startActivity(settingsIntent)
            finish()
        }
    }


    //take by camera
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    Glide.with(applicationContext).load(uri).into(binding.ivResultSelfie)

                    binding.llResultSelfie.visibility = View.VISIBLE
                }
            } else {
                onBackPressed()
            }
        }
    private var latestTmpUri: Uri? = null
    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    //Buat temporarynya
    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoDcument.jpeg"
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        //bitmap
        val bitmap: Bitmap = (binding.ivResultSelfie.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmap)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

        // load api
        if (typeBpjs == "BPJSKES"){
            viewModel.putUpdateBpjs(employeeId, image, numberBpjs, typeBpjsDoc)
        } else {
            viewModel.putUpdateBpjs(employeeId, image, numberJamsostek, typeBpjsDoc)
        }

    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Agir", Toast.LENGTH_SHORT).show()
        finish()
    }


}