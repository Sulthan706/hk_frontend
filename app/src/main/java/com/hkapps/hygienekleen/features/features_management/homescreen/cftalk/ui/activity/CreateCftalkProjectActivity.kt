package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.activity

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.BuildConfig
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityCreateCftalkProjectBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.Content
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.ui.adapter.DialogChooseProjectAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CreateCftalkProjectActivity : AppCompatActivity(),
    DialogChooseProjectAdapter.ProjectsAllCallBack {

    private lateinit var binding: ActivityCreateCftalkProjectBinding
    private lateinit var rvAdapter: DialogChooseProjectAdapter
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_CFTALK_MANAGEMENT, "")
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private var loadingDialog: Dialog? = null
    var dateParamm: String = ""
    var dateText: String = "Pilih Tanggal"
    private var titleCtlak: Int = 0
    private var page = 0
    private var isLastPage = false
    private var keywords: String = ""

    companion object {
        const val CAMERA_REQ = 101
    }

    private val viewModel: CftalkManagementViewModel by lazy {
        ViewModelProviders.of(this).get(CftalkManagementViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCftalkProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        binding.appbarCreateCftalkProject.tvAppbarTitle.text = "Komplain"
        binding.appbarCreateCftalkProject.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // validate click from
        when(clickFrom) {
            "project saya" -> {
                binding.llProjectCreateCftalkProject.visibility = View.GONE

                // load data area
                viewModel.getLocationsComplaint(projectId)
                // set spinner area
                viewModel.locationsCftalkManagementModel.observe(this) {
                    val data = ArrayList<String>()
                    val length = it.data.size
                    for (i in 0 until length) {
                        data.add(it.data[i].locationName)
                    }

                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item,
                        data
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    binding.etInputAreaCreateCftalkProject.adapter = adapter

                    binding.etInputAreaCreateCftalkProject.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>,
                            selectedItemView: View?,
                            position: Int,
                            id: Long
                        ) {
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.AREA_COMPLAINT,
                                it.data[position].locationId
                            )
                            viewModel.getSubLocComplaint(projectId, it.data[position].locationId)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
                }
            }
            "project lain" -> {
                binding.llProjectCreateCftalkProject.visibility = View.VISIBLE
                binding.llProjectCreateCftalkProject.setOnClickListener {
                    showDialogProject()
                    page = 0
                }
            }
        }

        binding.ivPickPhotoCreateCftalkProject.setOnClickListener {
            setupPermissions()
        }

        // show photo
        binding.ivResultPhotoCreateCftalkProject.setOnClickListener {
            showDialog(binding.ivResultPhotoCreateCftalkProject.drawable)
        }
        binding.ivResultPhoto2CreateCftalkProject.setOnClickListener {
            showDialog(binding.ivResultPhoto2CreateCftalkProject.drawable)
        }
        binding.ivResultPhoto3CreateCftalkProject.setOnClickListener {
            showDialog(binding.ivResultPhoto3CreateCftalkProject.drawable)
        }
        binding.ivResultPhoto4CreateCftalkProject.setOnClickListener {
            showDialog(binding.ivResultPhoto4CreateCftalkProject.drawable)
        }

        // set title spinner
        viewModel.getTitleComplaint()
        viewModel.titlesCftalkManagementModel.observe(this) {
            val data = ArrayList<String>()
            val length = it.data.size
            for (i in 0 until length) {
                data.add(it.data[i].title)
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, data
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerTitleCreateCftalkProject.adapter = adapter
            binding.spinnerTitleCreateCftalkProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                    titleCtlak = it.data[position].complaintTitleId
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }

        binding.etInputDescCreateCftalkProject.addTextChangedListener {
            checkNullInput()
        }

        // set spinner sub area
        viewModel.subLocationCftalkManagementModel.observe(this) {
            val dataSub = ArrayList<String>()
            val lengthSub = it.data.size
            for (i in 0 until lengthSub) {
                dataSub.add(it.data[i].subLocationName)
            }

            val adapterSub: ArrayAdapter<String> = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                dataSub
            )
            adapterSub.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.etInputSubAreaCreateCftalkProject.adapter = adapterSub

            binding.etInputSubAreaCreateCftalkProject.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.SUB_AREA_COMPLAINT,
                        it.data[position].subLocationId
                    )
                    checkNullInput()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        // button submit
        var flag = 1
        binding.btnSubmitEnableCreateCftalkProject.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableCreateCftalkProject.isEnabled = false
                showLoading(getString(R.string.loading_string))
            }
            flag = 0
        }

        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setObserver() {
        viewModel.createCftalkManagementModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                onBackPressed()
                finish()
                Toast.makeText(this, "Berhasil mengirim CFTalk", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                Toast.makeText(this, "Gagal mengirim CFTalk", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            takeImage()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
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
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    if (binding.ivResultPhotoCreateCftalkProject.isInvisible && binding.ivResultPhoto2CreateCftalkProject.isInvisible && binding.ivResultPhoto3CreateCftalkProject.isInvisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
                        previewImage.setImageURI(uri)
                        binding.llResultsPhotoCreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhotoCreateCftalkProject.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isInvisible && binding.ivResultPhoto3CreateCftalkProject.isInvisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
                        previewImage2.setImageURI(uri)

                        binding.ivResultPhotoCreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateCftalkProject.visibility = View.VISIBLE

                    } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isVisible && binding.ivResultPhoto3CreateCftalkProject.isInvisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
                        previewImage3.setImageURI(uri)

                        binding.ivResultPhotoCreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto3CreateCftalkProject.visibility = View.VISIBLE
                    } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isVisible && binding.ivResultPhoto3CreateCftalkProject.isVisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
                        previewImage4.setImageURI(uri)
                        binding.ivPickPhotoCreateCftalkProject.visibility = View.GONE

                        binding.ivResultPhotoCreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto2CreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto3CreateCftalkProject.visibility = View.VISIBLE
                        binding.ivResultPhoto4CreateCftalkProject.visibility = View.VISIBLE
                    }
                }
            }
        }

    private fun createTempFiles(bitmap: Bitmap?): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    private fun createTempFiles2(bitmap: Bitmap?): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint2.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    private fun createTempFiles3(bitmap: Bitmap?): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint3.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    private fun createTempFiles4(bitmap: Bitmap?): File? {
        val file: File = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photocomplaint4.JPEG"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 10, bos)
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

    private var latestTmpUri: Uri? = null

    private val previewImage by lazy {
        findViewById<ImageView>(R.id.ivResultPhotoCreateCftalkProject)
    }
    private val previewImage2 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto2CreateCftalkProject)
    }
    private val previewImage3 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto3CreateCftalkProject)
    }
    private val previewImage4 by lazy {
        findViewById<ImageView>(R.id.ivResultPhoto4CreateCftalkProject)
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

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

    private fun checkNullInput() {
        viewModel.checkNull(binding.etInputDescCreateCftalkProject.text.toString())
        viewModel.getDesc().observe(this) {
            val area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
            val subArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

            if (it == true) {
                binding.btnSubmitEnableCreateCftalkProject.visibility = View.GONE
                binding.btnSubmitDisableCreateCftalkProject.visibility = View.VISIBLE
            } else {
                if (binding.ivResultPhotoCreateCftalkProject.isVisible && area != 0 && subArea != 0) {
                    binding.btnSubmitEnableCreateCftalkProject.visibility = View.VISIBLE
                    binding.btnSubmitDisableCreateCftalkProject.visibility = View.GONE
                } else {
                    binding.btnSubmitEnableCreateCftalkProject.visibility = View.GONE
                    binding.btnSubmitDisableCreateCftalkProject.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showDialogProject() {
        val dialog = this.let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_list_project)
        val close = dialog.findViewById(R.id.ivCloseDialogProject) as ImageView
        val search = dialog.findViewById(R.id.svDialogProject) as androidx.appcompat.widget.SearchView
        val rvProject = dialog.findViewById(R.id.rvListProject) as RecyclerView
        val button = dialog.findViewById(R.id.btnListProject) as AppCompatButton
        val noData = dialog.findViewById(R.id.tvNoDataListProject) as TextView

        close.setOnClickListener {
            dialog.dismiss()
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvProject.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (keywords != "") {
                        viewModel.getOtherProjectsCftalk(userId, page)
                    } else {
                        viewModel.getSearchOtherProjects(userId, page, keywords)
                    }
                }
            }
        }
        rvProject.addOnScrollListener(scrollListener)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchOtherProjects(userId, page, query!!)
                keywords = query
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.getSearchOtherProjects(userId, page, query!!)
                keywords = query
                return true
            }

        })

        viewModel.searchOtherProjectsModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    noData.visibility = View.GONE
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvAdapter = DialogChooseProjectAdapter(
                            this,
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        rvProject.adapter = rvAdapter
                    } else {
                        rvAdapter.listAllProject.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listAllProject.size - it.data.content.size,
                            rvAdapter.listAllProject.size
                        )
                    }
                } else {
                    rvProject.adapter = null
                    noData.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getOtherProjectsCftalk(userId, page)
        viewModel.otherProjectsCftalkModel.observe(this) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    rvAdapter = DialogChooseProjectAdapter(
                        this,
                        it.data.content as ArrayList<Content>
                    ).also { it.setListener(this) }
                    rvProject.adapter = rvAdapter
                } else {
                    rvAdapter.listAllProject.addAll(it.data.content)
                    rvAdapter.notifyItemRangeChanged(
                        rvAdapter.listAllProject.size - it.data.content.size,
                        rvAdapter.listAllProject.size
                    )
                }
            }
        }

        button.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialog(img: Drawable) {
        val dialog = this.let { Dialog(it) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_image_zoom)
        val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
        val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

        ivZoom.setImageDrawable(img)

        close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
        loadData()
    }

    private fun loadData() {
        val complaintType = "COMPLAINT_MANAGEMENT"
        val area = CarefastOperationPref.loadInt(CarefastOperationPrefConst.AREA_COMPLAINT, 0)
        val subArea = CarefastOperationPref.loadInt(CarefastOperationPrefConst.SUB_AREA_COMPLAINT, 0)

        if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isInvisible && binding.ivResultPhoto3CreateCftalkProject.isInvisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateCftalkProject.drawable as BitmapDrawable).bitmap

            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())

            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            val image2: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileTwo",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )
            val image3: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileThree",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )
            val image4: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileFourth",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )

            viewModel.createComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateCftalkProject.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isVisible && binding.ivResultPhoto3CreateCftalkProject.isInvisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateCftalkProject.drawable as BitmapDrawable).bitmap

            val file = createTempFiles(bitmap)
            val file2 = createTempFiles2(bitmap2)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())

            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            val image2: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)

            val image3: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileThree",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )
            val image4: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileFourth",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )

            viewModel.createComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateCftalkProject.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isVisible && binding.ivResultPhoto3CreateCftalkProject.isVisible && binding.ivResultPhoto4CreateCftalkProject.isInvisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap3: Bitmap =
                (binding.ivResultPhoto3CreateCftalkProject.drawable as BitmapDrawable).bitmap

            val file = createTempFiles(bitmap)
            val file2 = createTempFiles2(bitmap2)
            val file3 = createTempFiles3(bitmap3)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile3 = file3?.asRequestBody("image/*".toMediaTypeOrNull())

            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            val image2: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)
            val image3: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileThree", file3?.name, reqFile3!!)

            val image4: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "fileFourth",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )

            viewModel.createComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateCftalkProject.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        } else if (binding.ivResultPhotoCreateCftalkProject.isVisible && binding.ivResultPhoto2CreateCftalkProject.isVisible && binding.ivResultPhoto3CreateCftalkProject.isVisible && binding.ivResultPhoto4CreateCftalkProject.isVisible) {
            val bitmap: Bitmap =
                (binding.ivResultPhotoCreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap2: Bitmap =
                (binding.ivResultPhoto2CreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap3: Bitmap =
                (binding.ivResultPhoto3CreateCftalkProject.drawable as BitmapDrawable).bitmap
            val bitmap4: Bitmap =
                (binding.ivResultPhoto4CreateCftalkProject.drawable as BitmapDrawable).bitmap

            val file = createTempFiles(bitmap)
            val file2 = createTempFiles2(bitmap2)
            val file3 = createTempFiles3(bitmap3)
            val file4 = createTempFiles4(bitmap4)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile2 = file2?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile3 = file3?.asRequestBody("image/*".toMediaTypeOrNull())
            val reqFile4 = file4?.asRequestBody("image/*".toMediaTypeOrNull())

            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            val image2: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileTwo", file2?.name, reqFile2!!)
            val image3: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileThree", file3?.name, reqFile3!!)
            val image4: MultipartBody.Part =
                MultipartBody.Part.createFormData("fileFourth", file4?.name, reqFile4!!)

            viewModel.createComplaintManagement(
                userId,
                projectId,
                titleCtlak,
                binding.etInputDescCreateCftalkProject.text.toString(),
//                dateParamm,
                area,
                subArea,
                image,
                image2,
                image3,
                image4,
                complaintType
            )
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickProject(projectCode: String, projectName: String) {
        projectId = projectCode
        binding.tvProjectCreateCftalkProject.text = projectName

        // load data area
        viewModel.getLocationsComplaint(projectId)
        // set spinner area
        viewModel.locationsCftalkManagementModel.observe(this) {
            if (it.data.isEmpty()) {
//                Toast.makeText(this, "Tidak ada kode plotting di project ini", Toast.LENGTH_SHORT).show()
            } else {
                val data = ArrayList<String>()
                val length = it.data.size
                for (i in 0 until length) {
                    data.add(it.data[i].locationName)
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,
                    data
                )

                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                binding.etInputAreaCreateCftalkProject.adapter = adapter

                binding.etInputAreaCreateCftalkProject.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        CarefastOperationPref.saveInt(
                            CarefastOperationPrefConst.AREA_COMPLAINT,
                            it.data[position].locationId
                        )
                        viewModel.getSubLocComplaint(projectId, it.data[position].locationId)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
        }
    }
}