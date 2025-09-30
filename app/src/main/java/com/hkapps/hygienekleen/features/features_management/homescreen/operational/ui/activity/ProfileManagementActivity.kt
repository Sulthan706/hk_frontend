package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityProfileManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.PhoneNumberAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListAllProjectManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.project.ui.activity.ListBranchProjectManagementActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class ProfileManagementActivity : AppCompatActivity(), PhoneNumberAdapter.ListPhoneNumberCallBack {

    private lateinit var binding : ActivityProfileManagementBinding
    private var adminMasterId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OPERATIONAL_MANAGEMENT_ADMIN_MASTER_ID,0)
    private val userLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")
    private val phoneNumbers = ArrayList<String>()

    companion object {
        const val CALL_REQ = 101
    }

    private val viewModel : OperationalManagementViewModel by lazy {
        ViewModelProviders.of(this).get(OperationalManagementViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,null)

        // set layout client or management
        if (userLevel == "CLIENT") {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

            // icon call
            binding.ivAppbarCall.visibility = View.GONE

            binding.clProfileManagement.setBackgroundResource(R.drawable.bg_profile_operational_secondary)
        } else {
            // icon call
            binding.ivAppbarCall.visibility = View.VISIBLE

            binding.clProfileManagement.setBackgroundResource(R.drawable.bg_profile_operational_primary)
        }

        // set shimmer
        binding.clProfile.visibility = View.GONE
        binding.clShimmer.visibility = View.VISIBLE
        binding.shimmerFotoProfile.startShimmerAnimation()
        binding.shimmerJobProfile.startShimmerAnimation()
        binding.shimmerNameProfile.startShimmerAnimation()
        binding.shimmerNucProfile.startShimmerAnimation()
        binding.shimmerProjectName.startShimmerAnimation()

        binding.ivAppbarCall.setOnClickListener {
            setupPermissionCall()
        }

        binding.ivAppbarBack.setOnClickListener {
            CarefastOperationPref.saveString(
                CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT,
                ""
            )
            CarefastOperationPref.saveInt(
                CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT,
                0
            )
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT, "")
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT, 0)
            super.onBackPressed()
            finish()
        }

        viewModel.getDetailManagement(adminMasterId)
        setObserver()
    }

    private fun setupPermissionCall() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            openDialogPhoneNumber()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CALL_PHONE),
            CALL_REQ
        )
    }

    private fun openDialogPhoneNumber() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_phone_number)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val recyclerView = dialog.findViewById(R.id.rvDialogPhoneNumber) as RecyclerView
        val btnClose = dialog.findViewById(R.id.ivCloseDialogPhoneNumber) as ImageView
        val tvEmpty = dialog.findViewById<TextView>(R.id.tvEmptyDialogPhoneNumber)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        // validate no phone number
        when (phoneNumbers.size) {
            1 -> {
                if (phoneNumbers[0] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            2 -> {
                if (phoneNumbers[0] == "" && phoneNumbers[1] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
            3 -> {
                if (phoneNumbers[0] == "" && phoneNumbers[1] == "" && phoneNumbers[2] == "") {
                    tvEmpty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }

        // set rv layout
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // set rv adapter
        val rvPhoneAdapter = PhoneNumberAdapter(
            this,
            phoneNumbers
        ).also { it.setListener(this) }
        recyclerView.adapter = rvPhoneAdapter

        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler().postDelayed({
                        binding.clProfile.visibility = View.VISIBLE
                        binding.clShimmer.visibility = View.GONE
                        binding.shimmerFotoProfile.stopShimmerAnimation()
                        binding.shimmerJobProfile.stopShimmerAnimation()
                        binding.shimmerNameProfile.stopShimmerAnimation()
                        binding.shimmerNucProfile.stopShimmerAnimation()
                        binding.shimmerProjectName.stopShimmerAnimation()
                    }, 1500)
                }
            }
        })
        viewModel.getDetailManagementResponse().observe(this){
            if (it.code == 200){
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT,
                    it.data.levelJabatan
                )
                CarefastOperationPref.saveInt(
                    CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT,
                    it.data.adminMasterId
                )

                // get phone numbers
                val length = it.data.adminMasterPhone.size
                for (i in 0 until length) {
                    if (it.data.adminMasterPhone[i].adminMasterPhone == null ||
                        it.data.adminMasterPhone[i].adminMasterPhone == "null" ||
                        it.data.adminMasterPhone[i].adminMasterPhone == "") {
                        phoneNumbers.add("")
                    } else {
                        phoneNumbers.add(it.data.adminMasterPhone[i].adminMasterPhone)
                    }
                }

                // menu my team
                binding.llTimku.setOnClickListener {
                    startActivity(Intent(this, MyTeamManagementProfileMgmntActivity::class.java))
                }

                val position = it.data.levelJabatan
                // menu project
                binding.llProject.setOnClickListener {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "profileManagement")
                    if (position == "BOD" || position == "CEO") {
                        startActivity(Intent(this, ListBranchProjectManagementActivity::class.java))
                    } else {
                        startActivity(Intent(this, ListAllProjectManagementActivity::class.java))
                    }
                }

                binding.clShimmer.visibility = View.GONE
                binding.clProfile.visibility = View.VISIBLE
                binding.tvNameUserProfile.text = it.data.adminMasterName
                binding.tvJobUser.text = it.data.adminMasterJabatan
                binding.tvNucProfile.text = it.data.adminMasterNUC

                //set user image
                val img = if (it.data.adminMasterImage == null || it.data.adminMasterImage == "" || it.data.adminMasterImage == "null") {
                    ""
                } else {
                    it.data.adminMasterImage
                }
                val url =
                    this.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

                if (img == "null" || img == null || img == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource =
                        this.resources.getIdentifier(uri, null, this.packageName)
                    val res = this.resources.getDrawable(imaResource)
                    this.binding.ivProfile.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)

                    Glide.with(this)
                        .load(url)
                        .apply(requestOptions)
                        .into(this.binding.ivProfile)
                }
            }
        }
    }

    override fun onBackPressed() {
        CarefastOperationPref.saveString(
            CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT,
            ""
        )
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT,
            0
        )
        CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "")
        CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_LEVEL_PROFILE_MANAGEMENT, "")
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.USER_ID_PROFILE_MANAGEMENT, 0)
        super.onBackPressed()
        finish()
    }

    override fun onClickPhone(phoneNumber: String) {
        val call = Intent(Intent.ACTION_CALL)
        call.data = Uri.parse("tel:$phoneNumber")
        startActivity(call)
    }
}