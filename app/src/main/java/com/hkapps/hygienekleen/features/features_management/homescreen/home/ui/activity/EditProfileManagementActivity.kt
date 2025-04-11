package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityEditProfileManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileManagementBinding
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var loadingDialog: Dialog? = null

    private var dateParamm: String = ""
    private lateinit var adminBirthDate: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private lateinit var userPhone2: String
    private lateinit var userAddress: String
    private lateinit var userBornDate: String
    private lateinit var userBornPlace: String
    private lateinit var userGender: String
    private lateinit var userMaritalStatus: String
    private lateinit var userMotherName: String
    private var selectedItem: String = ""
    private var addressKtp: String = ""
    private var selectedReligion: String = ""
    private var selectedChild: String = ""
    private var selectAddress =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.SELECTED_ADDRESS,"")
    private val requestImageCode = 100
    private val requestCameraCode = 200

    private val homeManagementViewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // validate layout by job level
        if (jobLevel == "BOD" || jobLevel == "CEO") {
            binding.llEditProfile.visibility = View.GONE
            binding.appBarDefault.ivAppbarCheck.setOnClickListener {
                submitFormManagement()
            }
        } else {
            binding.llEditProfile.visibility = View.VISIBLE
            binding.llAddNumberPhoneProfile.visibility = View.VISIBLE
            binding.llAddNumberPhoneProfile.visibility = View.VISIBLE

            binding.appBarDefault.ivAppbarCheck.setOnClickListener {
                if (emptyDataCheck()) {
                    Toast.makeText(this, "Harap mengisi semua data", Toast.LENGTH_SHORT).show()
                } else {
                    submitFormManagement()
                }
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // set appbar
        binding.appBarDefault.tvAppbarTitle.text = "Edit Profile"
        binding.appBarDefault.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // on click upload image
        binding.ivPlus.setOnClickListener {
            onProfileImageClick()
        }
        binding.ivUploadPhoto.setOnClickListener {
            updatePhotoProfile()
        }

        binding.llAddNumberPhoneProfile.setOnClickListener {
            binding.etNoHpProfileSecond.visibility = View.VISIBLE
            binding.llAddNumberPhoneProfile.visibility = View.GONE
            binding.llMinNumberPhoneProfile.visibility = View.VISIBLE

        }
        binding.llMinNumberPhoneProfile.setOnClickListener {
            binding.etNoHpProfileSecond.visibility = View.GONE
            binding.llAddNumberPhoneProfile.visibility = View.VISIBLE
            binding.llMinNumberPhoneProfile.visibility = View.GONE
        }

        // set calendar
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
                val dateText = sdf.format(cal.time)
                binding.tvProfileBirthdate.text = dateText
                binding.tvProfileBirthdate.setTextColor(resources.getColor(R.color.neutral100))
            }
        binding.etBirthdateProfile.setOnClickListener {
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // set spinner gender
        val gender = resources.getStringArray(R.array.gender)
        val adapterGender = ArrayAdapter(this, R.layout.spinner_item, gender)
        binding.tvGender.adapter = adapterGender
        binding.tvGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    long: Long
                ) {
                    userGender = gender[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        // set spinner martial status
        val maritalStatus = resources.getStringArray(R.array.maritalStatus)
        val adapterMaritalStatus = ArrayAdapter(this, R.layout.spinner_item, maritalStatus)
        binding.tvMarital.adapter = adapterMaritalStatus
        binding.tvMarital.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    long: Long
                ) {
                    userMaritalStatus = maritalStatus[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        //select ktp
        // Slide-down animation
        val slideDownAnimator = ObjectAnimator.ofFloat(binding.etAlamatKtp, "translationY", -binding.etAlamatKtp.height.toFloat(), 0f)
        slideDownAnimator.duration = 500
        // Fade-in animation
        val fadeInAnimator = ObjectAnimator.ofFloat(binding.etAlamatKtp, "alpha", 0f, 1f)
        fadeInAnimator.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(slideDownAnimator, fadeInAnimator)
        animatorSet.start()

        binding.rbEditProfileY.isChecked = true
        binding.rgEditProfile.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbEditProfileY) {
                selectAddress = "Y"
                selectedItem = binding.rbEditProfileY.text.toString()
                binding.etAlamatKtp.visibility = View.GONE
                binding.tvAlamatKtp.setText(addressKtp)

            }
            if (checkedId == R.id.rbEditProfileN) {
                selectAddress = "N"
                selectedItem = binding.rbEditProfileN.text.toString()
                binding.tvAlamatKtp.setText("")
                binding.etAlamatKtp.visibility = View.VISIBLE
                slideDownAnimator.start()
                fadeInAnimator.start()
            }
            CarefastOperationPref.saveString(CarefastOperationPrefConst.SELECTED_ADDRESS, selectAddress)
            Log.d("agri"," inside $selectAddress")
        }
        binding.tvAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.tvAlamatKtp.text = s
            }

        })

        // Set the default value to "Y" if the saved value is empty
        val defaultAddress = if (selectAddress.isNullOrEmpty()) {
            "Y"
        } else {
            selectAddress
        }

        if (defaultAddress == "Y"){
            binding.rbEditProfileY.isChecked = true
        } else {
            binding.rbEditProfileN.isChecked = true
            binding.etAlamatKtp.visibility = View.VISIBLE
        }

        //dropdown religion
        val data = arrayOf("ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, data)

        binding.acomFieldAdapterReligionMgmnt.setAdapter(adapter)
        binding.acomFieldAdapterReligionMgmnt.setOnItemClickListener { adapterView, _, i, _ ->
            selectedReligion = adapterView.getItemAtPosition(i).toString()
        }

        //dropdown children
        val dataChild = arrayOf(0, 1, 2, 3)
        val adapterChild = ArrayAdapter(this, R.layout.spinner_item, dataChild)

        binding.acomFieldAdapterChildren.setAdapter(adapterChild)
        binding.acomFieldAdapterChildren.setOnItemClickListener { adapterView, _, i, _ ->
            selectedChild = adapterView.getItemAtPosition(i).toString()
        }


        showLoading(getString(R.string.loading_string))
        ImagePickerActivity.clearCache(this)

//        homeManagementViewModel.getProfileManagement(userId)
        homeManagementViewModel.getProfileManagement(userId)
        setObserver()

    }


    private fun emptyDataCheck(): Boolean {
        if (binding.tvEmail.text.toString().isEmpty() ||
            binding.tvPhone.text.toString().isEmpty() ||
            binding.tvAddress.text.toString().isEmpty() ||
            binding.tvAlamatKtp.text.toString().isEmpty() ||
            binding.tvPlaceProfile.text.toString().isEmpty() ||
            binding.tvMotherName.text.toString().isEmpty() ||
            binding.tvProfileBirthdate.text == "Pilih tanggal" ||
            binding.tvGender.selectedItemPosition == 0 ||
            binding.tvMarital.selectedItemPosition == 0 ||
            selectedReligion == "") {
            return true
        }
        return false
    }

    private fun updatePhotoProfile() {
        if (binding.ivProfile.drawable == null) {
            Toast.makeText(this, "gagal mengambil foto", Toast.LENGTH_SHORT).show()
        } else {
            val bitmaps: Bitmap = (binding.ivProfile.drawable as BitmapDrawable).bitmap
            val file = createTempFiles(bitmaps)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)

            showLoading(getString(R.string.loading_string2))
            homeManagementViewModel.updateProfilePicture(userId, image)
        }
    }

    private fun checkValidEmail(): String? {
        val emailText = binding.tvEmail.text.toString()
        if (emailText.isEmpty()){
            return "Email tidak boleh kosong"
        }
        return null
    }

    private fun checkValidAddressKtp(): String? {
        val addressKtpMgmnt = binding.tvAlamatKtp.text.toString()
        if (addressKtpMgmnt.isEmpty()){
            return "Alamat tidak boleh kosong"
        }
        return null
    }

    private fun checkValidPhoneNumber(): String? {
        val phoneText = binding.tvPhone.text.toString()
        if (!phoneText.matches(".*[0-9].*".toRegex())) {
            return "Nomer tidak boleh kosong"
        }
        if (phoneText.length < 10) {
            return "Minimal harus 10 angka"
        }
        return null
    }

    private fun checkNikNumber(): String? {
        val nikText = binding.tvNikValueEmployee.text.toString()
        if (nikText.isEmpty()){
            return "NIK tidak boleh kosong"
        } else if (nikText.length < 16){
            return "Minimal harus 16 angka"
        }
        return null
    }
    private fun checkValidPhoneNumber2(): String? {
        val phoneText = binding.tvPhone2.text.toString()
//        if (!phoneText.matches(".*[0-9].*".toRegex())) {
//            return "Nomer tidak boleh kosong"
//        }
        if (phoneText.length < 10) {
            return "Minimal harus 10 angka"
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun submitFormManagement() {
        binding.etNoHpProfile.helperText = checkValidPhoneNumber()
//        binding.etNoHpProfileSecond.helperText = checkValidPhoneNumber2()
        binding.etAlamatKtp.helperText = checkValidAddressKtp()
        binding.etNikEmployee.helperText = checkNikNumber()

        val checkPhoneIsValid = binding.etNoHpProfile.helperText == null
//        val checkPhone2IsValid = binding.etNoHpProfileSecond.helperText == null
        val checkAddressKtpMgmntValid = binding.etAlamatKtp.helperText == null
        val checkNikNumberValid = binding.etNikEmployee.helperText == null

        // convert date format
        adminBirthDate = if (userBornDate == "") {
            dateParamm
        } else {
            if (dateParamm == "") {
                val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                val dateParamBefore = sdfBefore.parse(userBornDate)
                val sdfAfter = SimpleDateFormat("yyyy-MM-dd")
                val dateParamAfter = sdfAfter.format(dateParamBefore)
                dateParamAfter
            } else {
                dateParamm
            }
        }

        if (checkPhoneIsValid && checkAddressKtpMgmntValid
            && binding.tvAddress.text.toString().isNotEmpty()
            && binding.tvPlaceProfile.text.toString().isNotEmpty()
            && binding.tvMotherName.text.toString().isNotEmpty()
            && binding.tvProfileBirthdate.text != "Pilih tanggal"
            && binding.tvGender.selectedItemPosition != 0
            && binding.tvMarital.selectedItemPosition != 0
            && selectedReligion != ""
            && selectedChild.toString().isNotEmpty()
            && checkNikNumberValid
        ){
            if (jobLevel == "BOD" || jobLevel == "CEO") {
                showLoading(getString(R.string.loading_string2))
                homeManagementViewModel.updateProfileManagement(
                    userId,
                    binding.tvEmail.text.toString(),
                    binding.tvPhone.text.toString(),
                    binding.tvPhone2.text.toString()
                )
            } else {
                showLoading(getString(R.string.loading_string2))
                homeManagementViewModel.updateProfileFmGmOm(
                    userId,
                    binding.tvEmail.text.toString(),
                    binding.tvPhone.text.toString(),
                    binding.tvPhone2.text.toString(),
                    binding.tvAddress.text.toString(),
                    adminBirthDate,
                    binding.tvPlaceProfile.text.toString(),
                    userGender,
                    userMaritalStatus,
                    binding.tvMotherName.text.toString(),
                    selectedReligion,
                    binding.tvAlamatKtp.text.toString(),
                    selectedChild,
                    binding.tvNikValueEmployee.text.toString()
                )
            }
        } else {
            Toast.makeText(this, "Harap lengkapi data Anda", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivProfile)
    }

    private fun loadProfileDefault(img: String) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imageResource)
            binding.ivProfile.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(binding.ivProfile)
        }
    }

    private fun onProfileImageClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        } else {
            Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object :
            ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
//        val intent = Intent(this@EditProfileManagementActivity, ImagePickerActivity::class.java)
//        intent.putExtra(
//            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
//            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
//        )
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
//
//        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
//        startActivityForResult(intent, EditProfileActivity.REQUEST_IMAGE)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCameraCode)
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        try {
            val file = File(context.externalCacheDir, "temp_image.jpg")
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream)
            fileOutputStream.close()
            return Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == EditProfileActivity.REQUEST_IMAGE) {
//            if (resultCode == RESULT_OK) {
//                val uri = data!!.getParcelableExtra<Uri>("path")
//                try {
//                    // You can update this bitmap to your server
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                    Log.d("bitmaptes", "onActivityResult: $bitmap")
//                    // loading profile image from local cache
//                    loadProfile(uri.toString())
//                    binding.ivUploadPhoto.visibility = View.VISIBLE
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
        if (requestCode == requestCameraCode && resultCode == RESULT_OK) {
            val capturedImage = data?.extras?.get("data") as? Bitmap
            if (capturedImage != null) {
                val imageUri = saveBitmapToFile(this, capturedImage)
                loadProfile(imageUri.toString())
                binding.ivUploadPhoto.visibility = View.VISIBLE
            }
        }
        if (requestCode == requestImageCode && resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                loadProfile(selectedImageUri.toString())
                binding.ivUploadPhoto.visibility = View.VISIBLE
            }
        }
    }

    //Buat temporarynya
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

    private fun launchGalleryIntent() {
//        val intent = Intent(this@EditProfileManagementActivity, ImagePickerActivity::class.java)
//        intent.putExtra(
//            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
//            ImagePickerActivity.REQUEST_GALLERY_IMAGE
//        )
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
//        startActivityForResult(intent, EditProfileActivity.REQUEST_IMAGE)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImageCode)
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@EditProfileManagementActivity)
        builder.setTitle("Title")
        builder.setMessage("Message")
        builder.setPositiveButton("yes") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    @SuppressLint("SetTextI8n", "SetTextI18n")
    private fun setObserver() {
        homeManagementViewModel.getProfileManagementModel().observe(this) {
            if (it.code == 200) {
                hideLoading()


                loadProfileDefault(it.data.adminMasterImage ?: "")

                //nik
                if (it.data.adminMasterNik == "" || it.data.adminMasterNik.isNullOrEmpty()) {
                    binding.tvNikValueEmployee.setText("")
                } else {
                    binding.tvNikValueEmployee.setText(it.data.adminMasterNik)
                }


                // set email
                if (it.data.adminMasterEmail == "null" || it.data.adminMasterEmail == null || it.data.adminMasterEmail == "") {
                    userEmail = ""
                    binding.tvEmail.setText("")
                } else {
                    userEmail = it.data.adminMasterEmail
                    binding.tvEmail.setText("" + it.data.adminMasterEmail)
                }

                // set phone number
                if (it.data.adminMasterPhone.isEmpty() || it.data.adminMasterPhone == null) {
                    userPhone = ""
                    userPhone2 = ""
                    binding.tvPhone.setText("")
                    binding.tvPhone2.setText("")
                } else {
                    userPhone = it.data.adminMasterPhone[0].adminMasterPhone
                    userPhone2 = it.data.adminMasterPhone[1].adminMasterPhone
                    binding.tvPhone.setText(userPhone)
                    binding.tvPhone2.setText(userPhone2)

                    if (userPhone2 != "") {
                        binding.etNoHpProfileSecond.visibility = View.VISIBLE
                        binding.llAddNumberPhoneProfile.visibility = View.GONE
                        binding.llMinNumberPhoneProfile.visibility = View.VISIBLE
                    } else {
                        binding.etNoHpProfileSecond.visibility = View.GONE
                        binding.llAddNumberPhoneProfile.visibility = View.VISIBLE
                        binding.llMinNumberPhoneProfile.visibility = View.GONE
                    }
                }
                if (it.data.adminMasterReligion == null){
                    binding.acomFieldAdapterReligionMgmnt.setText("")
                } else {
                    binding.acomFieldAdapterReligionMgmnt.setText(it.data.adminMasterReligion)
                }

                if (it?.data?.adminMasterReligion.isNullOrEmpty()) {
                    binding.acomFieldAdapterReligionMgmnt.isEnabled = false
                } else {
                    binding.acomFieldAdapterReligionMgmnt.isEnabled = true

                    selectedReligion = it.data.adminMasterReligion

                    val data = arrayOf("ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU")
                    val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
                    binding.acomFieldAdapterReligionMgmnt.setAdapter(adapter)
                }


                if (it?.data?.adminMasterCountChildren.isNullOrEmpty()) {
                    binding.acomFieldAdapterChildren.setText("")
                    Log.d("saduk","hehse")

                } else {
                    binding.acomFieldAdapterChildren.setText(it.data.adminMasterCountChildren)
                    selectedChild = it.data.adminMasterCountChildren

                    val data = arrayOf("0", "1", "2", "3")
                    val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
                    binding.acomFieldAdapterChildren.setAdapter(adapter)
                }



                // set address
                if (it.data.adminMasterAddress == "" || it.data.adminMasterAddress == null || it.data.adminMasterAddress == "null") {
                    userAddress = ""
                    binding.tvAddress.setText("")
                } else {
                    userAddress = it.data.adminMasterAddress
                    binding.tvAddress.setText(userAddress)
                }

                if (it.data.adminMasterAddress.isNotEmpty()){
                    binding.tvAlamatKtp.setText(it.data.adminMasterAddress)
                }


                //ktp
                if (it.data.adminMasterAddressKtp == null || it.data.adminMasterAddressKtp == ""){
                    binding.tvAlamatKtp.setText("")
                } else {
                    binding.tvAlamatKtp.setText(it.data.adminMasterAddressKtp)
                }

                // set address ktp follow address
                addressKtp = it?.data?.adminMasterAddress ?: ""

                // set born date
                if (it.data.adminMasterBirthDate == "" || it.data.adminMasterBirthDate == null || it.data.adminMasterBirthDate == "null") {
                    userBornDate = ""
                    binding.tvProfileBirthdate.text = "Pilih tanggal"
                } else {
                    userBornDate = it.data.adminMasterBirthDate.toString()
                    binding.tvProfileBirthdate.text = userBornDate
                    binding.tvProfileBirthdate.setTextColor(resources.getColor(R.color.neutral100))
                }

                // set born place
                if (it.data.adminMasterPlaceOfBirth == "" || it.data.adminMasterPlaceOfBirth == null || it.data.adminMasterPlaceOfBirth == "null") {
                    userBornPlace = ""
                    binding.tvPlaceProfile.setText("")
                } else {
                    userBornPlace = it.data.adminMasterPlaceOfBirth
                    binding.tvPlaceProfile.setText(userBornPlace)
                }

                // set gender
                if (it.data.adminMasterGender == "" || it.data.adminMasterGender == null || it.data.adminMasterGender == "null") {
                    userGender = ""
                    binding.tvGender.setSelection(0)
                } else {
                    userGender = it.data.adminMasterGender
                    when(userGender) {
                        "PRIA", "Pria", "Laki-laki", "LAKI-LAKI" -> binding.tvGender.setSelection(1)
                        "WANITA", "Wanita", "Perempuan", "PEREMPUAN" -> binding.tvGender.setSelection(2)
                        else -> binding.tvGender.setSelection(0)
                    }
                }

                // set marital status
                if (it.data.adminMasterMarriageStatus == "" || it.data.adminMasterMarriageStatus == null || it.data.adminMasterMarriageStatus == "null") {
                    userMaritalStatus = ""
                    binding.tvMarital.setSelection(0)
                } else {
                    userMaritalStatus = it.data.adminMasterMarriageStatus
                    when(userMaritalStatus) {
                        "Nikah", "Menikah", "MENIKAH", "Kawin", "KAWIN" -> binding.tvMarital.setSelection(1)
                        "Belum nikah", "Belum Nikah",
                        "belum nikah", "BELUM NIKAH",
                        "Belum Kawin", "Belum kawin",
                        "belum kawin", "BELUM KAWIN",
                        "Belum Menikah", "Belum menikah",
                        "belum menikah", "BELUM MENIKAH" -> binding.tvMarital.setSelection(2)
                        "cerai", "Cerai" -> binding.tvMarital.setSelection(3)
                        else -> binding.tvMarital.setSelection(0)
                    }
                }

                // set mother name
                if (it.data.adminMasterMother == "" || it.data.adminMasterMother == null || it.data.adminMasterMother == "null") {
                    userMotherName = ""
                    binding.tvMotherName.setText("")
                } else {
                    userMotherName = it.data.adminMasterMother
                    binding.tvMotherName.setText(userMotherName)
                }

            }
        }
        homeManagementViewModel.updateProfileManagementObs().observe(this) {
            when (it.code) {
                200 -> {
                    hideLoading()
                    startActivity(Intent(this, EditProfileManagementActivity::class.java))
                    finish()
                    Toast.makeText(this, "Berhasil update data", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    startActivity(Intent(this, EditProfileManagementActivity::class.java))
                    finish()
                    Toast.makeText(this, "Gagal update data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        homeManagementViewModel.updateProfileFmGmOmModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                startActivity(Intent(this, EditProfileManagementActivity::class.java))
                finish()
                Toast.makeText(this, "Berhasil update data", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                startActivity(Intent(this, EditProfileManagementActivity::class.java))
                finish()
                Toast.makeText(this, "Gagal update data", Toast.LENGTH_SHORT).show()
            }
        }
        homeManagementViewModel.updateProfilePictureModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                binding.ivUploadPhoto.visibility = View.GONE
                Toast.makeText(this, "Berhasil update foto profile", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                binding.ivUploadPhoto.visibility = View.GONE
                if (it.errorCode == "96") {
                    Toast.makeText(this, "Foto lebih dari 1mb", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal update foto profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
