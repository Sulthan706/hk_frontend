package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailMeetingInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class DetailMeetingInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMeetingInspeksiBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val mClickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_FROM, "")
    private val branchName = CarefastOperationPref.loadString(CarefastOperationPrefConst.BRANCH_NAME_LAST_VISIT, "")
    private var projectName = ""
    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")
    private val meetingId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.MEETING_ID_INSPEKSI, 0)

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMeetingInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEdgeToEdge(binding.root,binding.statusBarBackground)

        // set app bar
        binding.appbarDetailMeetingInspeksi.tvAppbarTitle.text = "Laporan Meeting"
        binding.appbarDetailMeetingInspeksi.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // validate project name & branch name
        if (clickFrom == "mainInspeksi" || mClickFrom == "mainInspeksi") {
            binding.tvBranchDetailMeetingInspeksi.visibility = View.GONE
            projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_MEETING, "")
        } else if (clickFrom == "listMeeting" || mClickFrom == "listMeeting") {
            binding.tvBranchDetailMeetingInspeksi.visibility = View.VISIBLE
            projectName = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_NAME_LAST_VISIT, "")
        } else {
            binding.tvBranchDetailMeetingInspeksi.visibility = View.GONE
            projectName = ""
        }

        // set default data
        binding.tvBranchDetailMeetingInspeksi.text = branchName
        binding.tvCreatedDetailMeetingInspeksi.text = userName

        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahana", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.detailMeetingResponse.observe(this) {
            if (it.code == 200) {
                binding.tvProjectDetailMeetingInspeksi.text = it.data.projectName
                binding.tvDateDetailMeetingInspeksi.text = it.data.startMeetingDate
                binding.tvTimeDetailMeetingInspeksi.text = "${it.data.startMeetingTime} - ${it.data.endMeetingTime}"
                binding.tvTopikDetailMeetingInspeksi.text = it.data.title
                binding.tvNoteDetailMeetingInspeksi.text = if (it.data.description == null || it.data.description == "null" || it.data.description == "") {
                    "-"
                } else {
                    it.data.description
                }
                if (it.data.meetingFile.isNotEmpty()){
                    binding.rlUploadFotoInspeksi.visibility = View.GONE
                    binding.mvCardResultUploadMeeting.visibility = View.VISIBLE
                } else {
                    binding.rlUploadFotoInspeksi.visibility = View.VISIBLE
                    binding.mvCardResultUploadMeeting.visibility = View.GONE
                }
                setPhotoProfile(it.data.meetingFile, binding.ivResultUri)
                binding.etUploadImage.setText(it?.data?.fileDescription ?: "-")

            } else {
                Toast.makeText(this, "Gagal mengambil data detail meeting", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
//        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        val url = getString(R.string.url) + "assets.admin_master/files/inspection/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, packageName)
            val res = resources.getDrawable(imaResource)
            imageView.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }



    private fun loadData() {
        viewModel.getDetailMeeting(meetingId)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }

    }

}