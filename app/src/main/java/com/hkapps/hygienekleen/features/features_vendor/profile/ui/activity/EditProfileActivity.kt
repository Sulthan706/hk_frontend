package com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity

//import kotlinx.android.synthetic.main.activity_edit_profile_management.*
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
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityEditProfileBinding
import com.hkapps.hygienekleen.features.features_vendor.profile.ui.activity.ImagePickerActivity.PickerOptionListener
import com.hkapps.hygienekleen.features.features_vendor.profile.viewmodel.ProfileViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.setupEdgeToEdge
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


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private var loadingDialog: Dialog? = null
    private var dateParamm: String = ""
    private var dateText: String = "Pilih Tanggal"
    private var gender: String = ""
    private var maritalStatus: String = ""
    private var userDateBirth: String = ""
    private var employeeBirthDate: String = ""

    private var selectedItem: String = ""
    private var addressKtp: String = ""
    private var selectedReligion: String = ""
    private var selectedChild: String = ""
    private var selectAddress =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.SELECTED_ADDRESS,"")
    private val requestImageCode = 100
    private val requestCameraCode = 200

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        if (jobLevel == "BOD" || jobLevel == "CEO") {
            binding.llEditProfile.visibility = View.GONE

        } else {
            binding.llEditProfile.visibility = View.VISIBLE
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.appBarDefault.tvAppbarTitle.text = "Edit Profile"
        binding.appBarDefault.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        binding.appBarDefault.ivAppbarCheck.setOnClickListener {
            if (onEmptyDataCheck()) {
                Toast.makeText(this, "Harap mengisi semua data", Toast.LENGTH_SHORT).show()
            } else {
                submitForm()
            }
        }

        binding.ivPlus.setOnClickListener {
            onProfileImageClick()
        }
        binding.ivUploadPhoto.setOnClickListener {
            updatePhotoProfile()
        }

        //no hp
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

        binding.etBirthdateProfile.setOnClickListener {
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

                    binding.tvProfileBirthdate.text = dateText
                    binding.tvProfileBirthdate.setTextColor(resources.getColor(R.color.neutral100))
                }
            DatePickerDialog(
                this, R.style.CustomDatePickerDialogTheme, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // dropdown gender
        val itemsGender = resources.getStringArray(R.array.gender)
        val adapterGender = ArrayAdapter(this, R.layout.spinner_item, itemsGender)
        binding.tvGender.adapter = adapterGender
        binding.tvGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = itemsGender[position]
            }
        }


        // dropdown marital status
        val itemsMarital = resources.getStringArray(R.array.maritalStatus)
        val adapterMarital = ArrayAdapter(this, R.layout.spinner_item, itemsMarital)
        binding.tvMarital.adapter = adapterMarital
        binding.tvMarital.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                maritalStatus = itemsMarital[position]
            }
        }

        showLoading(getString(R.string.loading_string))
        ImagePickerActivity.clearCache(this)

        binding.tvAddress.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.tvAlamatKtp.text = s
            }

        })
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

        }




        //dropdown religion
        val data = arrayOf("ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, data)

        binding.acomFieldAdapterReligion.setAdapter(adapter)
        binding.acomFieldAdapterReligion.setOnItemClickListener { adapterView, _, i, _ ->
            selectedReligion = adapterView.getItemAtPosition(i).toString()
        }

        //dropdown children
        val dataChild = arrayOf(0, 1, 2, 3)
        val adapterChild = ArrayAdapter(this, R.layout.spinner_item, dataChild)

        binding.acomFieldAdapterChildren.setAdapter(adapterChild)
        binding.acomFieldAdapterChildren.setOnItemClickListener { adapterView, _, i, _ ->
            selectedChild = adapterView.getItemAtPosition(i).toString()
        }

        profileViewModel.getProfile(employeeId)


        setObserver()
    }

    private fun onEmptyDataCheck(): Boolean {
        if (binding.tvEmail.text.toString().isNullOrEmpty() ||
            binding.tvPhone.text.toString().isNullOrEmpty() ||
            binding.tvAddress.text.toString().isNullOrEmpty() ||
            binding.tvProfileBirthdate.text.toString().isNullOrEmpty() ||
            binding.tvMotherName.text.toString().isNullOrEmpty() ||
            binding.tvProfileBirthdate.text == "Pilih tanggal" ||
            selectedReligion == "" ||
            binding.tvGender.selectedItemPosition == 0 ||
            binding.tvMarital.selectedItemPosition == 0 ||
            selectedChild == "") {
            return true
        }
        return false
    }

    private fun updatePhotoProfile() {
        if (binding.ivProfile.drawable == null) {
            Toast.makeText(this, "Gagal mengambil foto", Toast.LENGTH_SHORT).show()
        } else {
            loadingUploadPhoto(getString(R.string.loading_string2))
        }
    }

    //function
    private fun checkEmail(): String? {
        val emailText = binding.tvEmail.text.toString().trim()
        val validateEmail = Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        if (emailText.isEmpty()) {
            return "email tidak boleh kosong"
        } else if (validateEmail == false) {
            return "email tidak sesuai"
        }
        return null
    }

    private fun checkValidAlamatKtp(): String? {
        val alamatKtp = binding.tvAlamatKtp.text.toString()
        if (alamatKtp.isEmpty()){
            return "alamat ktp tidak boleh kosong"
        }
        return null
    }

    private fun checkValidMarriage(): Boolean {
        val maritalStats = binding.tvMaritalStatusProfile.text.toString()
        return maritalStats.isEmpty()
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

    private fun checkValidNikNumber():String? {
        val nikText = binding.tvNikValueEmployee.text.toString()
        if (nikText.length < 16) {
            return "Minimal harus 16 angka"
        } else if (nikText.isEmpty()){
            return "Nik tidak boleh kosong"
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    private fun submitForm() {
        binding.etUsernameProfile.helperText = checkEmail()
        binding.etNoHpProfile.helperText = checkValidPhoneNumber()
        binding.etAlamatKtp.helperText = checkValidAlamatKtp()
        binding.etNikEmployee.helperText = checkValidNikNumber()

        val checkPhoneIsValid = binding.etNoHpProfile.helperText == null
        val checkEmailIsValid = binding.etUsernameProfile.helperText == null
        val checkAddressKtpValid = binding.etAlamatKtp.helperText == null
        val checkNikValid = binding.etNikEmployee.helperText == null

        Log.d(TAG, "submitForm: $checkPhoneIsValid $checkEmailIsValid $checkAddressKtpValid ${checkValidMarriage()}")

        // convert date format
        employeeBirthDate = if (userDateBirth == "") {
            dateParamm
        } else {
            if (dateParamm == "") {
                val sdfBefore = SimpleDateFormat("dd-MM-yyyy")
                val dateParamBefore = sdfBefore.parse(userDateBirth)
                val sdfAfter = SimpleDateFormat("yyyy-MM-dd")
                val dateParamAfter = sdfAfter.format(dateParamBefore)
                dateParamAfter
            } else {
                dateParamm
            }
        }

        if (checkPhoneIsValid && checkEmailIsValid && checkAddressKtpValid && checkNikValid && checkValidMarriage() != null
            && employeeBirthDate != "" && gender != "" && maritalStatus != "" && selectedReligion != "" && selectedChild != ""
            && binding.tvMotherName.text.toString() != "" && binding.tvPlaceProfile.text.toString() != ""
            && binding.tvProfileBirthdate.text.toString() != "Pilih tanggal"
            ) {
            showLoading(getString(R.string.loading_string2))
            profileViewModel.updateProfile(
                employeeId,
                binding.tvEmail.text.toString(),
                binding.tvPhone.text.toString(),
                binding.tvPhone2.text.toString(),
                binding.tvAddress.text.toString(),
                binding.tvAlamatKtp.text.toString(),
                employeeBirthDate,
                binding.tvPlaceProfile.text.toString(),
                gender,
                maritalStatus,
                binding.tvMotherName.text.toString(),
                selectedReligion,
                selectedChild,
                binding.tvNikValueEmployee.text.toString()
            )

        } else {
            Toast.makeText(this, "Harap lengkapi data Anda", Toast.LENGTH_SHORT).show()
        }
    }

    // function
    private fun loadProfile(url: String) {
        Glide.with(this).load(url).into(binding.ivProfile)
    }

    private fun loadProfileDefault(img: String) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        Log.d(TAG, "loadProfileDefault: $url $img")
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
        ImagePickerActivity.showImagePickerOptions(this, object : PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
//        val intent = Intent(this@EditProfileActivity, ImagePickerActivity::class.java)
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
//        startActivityForResult(intent, REQUEST_IMAGE)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, requestCameraCode)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestImageCode)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_IMAGE) {
//            if (resultCode == RESULT_OK) {
//                val uri = data!!.getParcelableExtra<Uri>("path")
//                try {
//                    // You can update this bitmap to your server
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                    Log.d(TAG, "onActivityResult: $bitmap")
//                    // loading profile image from local cache
//                    loadProfile(uri.toString())
//                    binding.ivUploadPhoto.visibility = View.VISIBLE
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
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

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@EditProfileActivity)
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

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    companion object {
        private val TAG = EditProfileActivity::class.java.simpleName
        const val REQUEST_IMAGE = 100
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        profileViewModel.profileResponseModel.observe(this, Observer {
            if (it.code == 200) {
                hideLoading()

                loadProfileDefault(it.data.employeePhotoProfile)

                //nik
                if (it.data.employeeNik == "" || it.data.employeeNik.isNullOrEmpty()) {
                    binding.tvNikValueEmployee.setText("")
                } else {
                    binding.tvNikValueEmployee.setText(it.data.employeeNik)
                }

                if (it.data.employeeEmail == "null" || it.data.employeeEmail == null) {
                    binding.tvEmail.setText("")
                } else {
                    binding.tvEmail.setText("" + it.data.employeeEmail)
                }

                if (it.data.employeePhoneNumber.isNullOrEmpty() || it.data.employeePhoneNumber == null) {
                    binding.tvPhone.setText("")
                    binding.tvPhone2.setText("")
                } else {
                    binding.tvPhone.setText("" + it.data.employeePhoneNumber[0].employeePhone)
                    binding.tvPhone2.setText("" + it.data.employeePhoneNumber[1].employeePhone)
                    if (it.data.employeePhoneNumber[1].employeePhone != "") {

                        binding.etNoHpProfileSecond.visibility = View.VISIBLE
                        binding.llAddNumberPhoneProfile.visibility = View.GONE
                        binding.llMinNumberPhoneProfile.visibility = View.VISIBLE
                    } else {
                        binding.etNoHpProfileSecond.visibility = View.GONE
                        binding.llAddNumberPhoneProfile.visibility = View.VISIBLE
                        binding.llMinNumberPhoneProfile.visibility = View.GONE
                    }
                }
                addressKtp = it?.data?.employeeAddress ?: ""
                binding.tvAddress.setText(it?.data?.employeeAddress ?: "")
                binding.tvAlamatKtp.setText(it?.data?.employeeKtpAddress ?: "")


                if (it?.data?.employeeReligion.isNullOrEmpty()){
                    binding.acomFieldAdapterReligion.setText("")
                } else {
                    binding.acomFieldAdapterReligion.setText(it.data.employeeReligion)
                }

                if (it?.data?.employeeReligion.isNullOrEmpty()) {
                    binding.acomFieldAdapterReligion.isEnabled = false
                } else {
                    binding.acomFieldAdapterReligion.isEnabled = true

                    selectedReligion = it.data.employeeReligion

                    val datas = arrayOf("ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU")
                    val adapters = ArrayAdapter(this, R.layout.spinner_item, datas)
                    binding.acomFieldAdapterReligion.setAdapter(adapters)
                }



                if (it?.data?.employeeCountChildren.isNullOrEmpty()) {
                    binding.acomFieldAdapterChildren.setText("")
                    Log.d("saduk","hehse")

                } else {

                    binding.acomFieldAdapterChildren.setText(it.data.employeeCountChildren)

                    selectedChild = it.data.employeeCountChildren

                    val data = arrayOf("0", "1", "2", "3")
                    val adapter = ArrayAdapter(this, R.layout.spinner_item, data)
                    binding.acomFieldAdapterChildren.setAdapter(adapter)
                    Log.d("saduk","hehe")

                }
                Log.d("saduk","${it.data.employeeCountChildren}")






                if (it.data.employeeAddress.isNotEmpty()){
                    binding.tvAlamatKtp.setText(it.data.employeeAddress)
                }


                if (it.data.employeeBirthDate == "null" || it.data.employeeBirthDate == null) {
                    binding.tvProfileBirthdate.text = "Pilih tanggal"
                    userDateBirth = ""
                } else {
                    userDateBirth = it.data.employeeBirthDate
                    binding.tvProfileBirthdate.text = it.data.employeeBirthDate
                    binding.tvProfileBirthdate.setTextColor(resources.getColor(R.color.neutral100))
                }
                if (it.data.employeePlaceOfBirth == "null" || it.data.employeePlaceOfBirth == null) {
                    binding.tvPlaceProfile.setText("")
                } else {
                    binding.tvPlaceProfile.setText("" + it.data.employeePlaceOfBirth)
                }

                if (it.data.employeeGender == "null" || it.data.employeeGender == null) {
                    binding.tvGender.setSelection(0)
                } else {
                    when(it.data.employeeGender) {
                        "PRIA", "Pria", "Laki-laki", "LAKI-LAKI" -> binding.tvGender.setSelection(1)
                        "WANITA", "Wanita", "Perempuan", "PEREMPUAN" -> binding.tvGender.setSelection(2)
                        else -> binding.tvGender.setSelection(0)
                    }
                }

                if (it.data.employeeMarriageStatus == "null" || it.data.employeeMarriageStatus == null) {
                    binding.tvMarital.setSelection(0)
                } else {
                    when(it.data.employeeMarriageStatus) {
                        "Nikah", "Menikah", "MENIKAH", "Kawin", "KAWIN" -> binding.tvMarital.setSelection(1)
                        "Belum nikah", "Belum Nikah",
                        "belum nikah", "BELUM NIKAH",
                        "Belum Kawin", "Belum kawin",
                        "belum kawin", "BELUM KAWIN",
                        "Belum Menikah", "Belum menikah",
                        "belum menikah", "BELUM MENIKAH", -> binding.tvMarital.setSelection(2)
                        "Cerai", "cerai" -> binding.tvMarital.setSelection(3)
                        else -> binding.tvMarital.setSelection(0)
                    }
                }

                if (it.data.employeeMotherName == "null" || it.data.employeeMotherName == null) {
                    binding.tvMotherName.setText("")
                } else {
                    binding.tvMotherName.setText("" + it.data.employeeMotherName)
                }
            }
        })
        profileViewModel.updateProfileObs().observe(this, Observer {
            when (it.code) {
                200 -> {
                    hideLoading()
                    startActivity(Intent(this, EditProfileActivity::class.java))
                    finish()
                    Toast.makeText(this, "Anda berhasil update.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    hideLoading()
                    startActivity(Intent(this, EditProfileActivity::class.java))
                    finish()
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                }
            }
            hideLoading()
        })
        profileViewModel.updatePhotoProfileModel.observe(this) {
            if (it.code == 200) {
                hideLoading()
                binding.ivUploadPhoto.visibility = View.GONE
                Toast.makeText(this, "Berhasil update foto profil", Toast.LENGTH_SHORT).show()
            } else {
                hideLoading()
                binding.ivUploadPhoto.visibility = View.GONE
                if (it.errorCode == "96") {
                    Toast.makeText(this, "Foto lebih dari 1mb", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Gagal update foto profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadingUploadPhoto(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        val bitmaps: Bitmap = (binding.ivProfile.drawable as BitmapDrawable).bitmap
        val file = createTempFiles(bitmaps)
        val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
        val image: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
        profileViewModel.updatePhotoProfile(employeeId, image)
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
}