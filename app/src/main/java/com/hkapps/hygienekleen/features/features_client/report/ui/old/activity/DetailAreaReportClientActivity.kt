package com.hkapps.hygienekleen.features.features_client.report.ui.old.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailAreaReportClientBinding
import com.hkapps.hygienekleen.features.features_client.report.model.ListOperatorSpinnerReportModel
import com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter.SpinnerOpsReportAdapter
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.util.*

class DetailAreaReportClientActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAreaReportClientBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val plottingId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)

    private val viewModel: ReportClientViewModel by lazy {
        ViewModelProviders.of(this).get(ReportClientViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAreaReportClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set app bar
        binding.appbarDetailAreaReportClient.tvAppbarTitle.text = "Checklist"
        binding.appbarDetailAreaReportClient.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
            CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)
        }

        loadData()
        setObserver()
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver() {
        viewModel.detailAreaResponseModel.observe(this) {
            if (it.code == 200) {
                // set data pengawas
                binding.tvPengawasDetailAreaReportClient.text = "${it.data.pengawas.employeeName} (${it.data.pengawas.employeeNuc})"
                setPhotoProfile(it.data.pengawas.employeePhotoProfile, binding.ivPengawasDetailAreaReportClient)

                // set dropdown operational
                val listOps: MutableList<ListOperatorSpinnerReportModel> = ArrayList()
                val operational = ListOperatorSpinnerReportModel("operational", "operational", "operational")
                listOps.add(operational)
                val length = it.data.operational.size
                if (length != 0) {
                    for (i in 0 until length) {
                        val name = it.data.operational[i].employeeName
                        val nuc = it.data.operational[i].employeeNuc
                        val image = if (it.data.operational[i].employeePhotoProfile == "" ||
                            it.data.operational[i].employeePhotoProfile == null ||
                            it.data.operational[i].employeePhotoProfile == "null") {
                            ""
                        } else {
                            it.data.operational[i].employeePhotoProfile
                        }

                        val m = ListOperatorSpinnerReportModel(name, nuc, image)
                        listOps.add(m)
                    }
                }
                val spinnerAdapter = SpinnerOpsReportAdapter(this, listOps)
                binding.spinnerOpsDetailAreaReportClient.adapter = spinnerAdapter

                binding.spinnerOpsDetailAreaReportClient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }

                // detail plotting area
                binding.tvCodePlottingDetailAreaReportClient.text = it.data.codePlottingArea
                binding.tvAreaDetailAreaReportClient.text = it.data.locationName
                binding.tvSubAreaDetailAreaReportClient.text = it.data.subLocationName
                binding.tvShiftDetailAreaReportClient.text = if (it.data.shiftDescription == "Week Days (Hari Sabtu dan Hari Minggu Office)") {
                    "Week Days"
                } else {
                    it.data.shiftDescription
                }

            }
        }
        viewModel.detailPlottingResponseModel.observe(this) {
            if (it.code == 200) {
                binding.llProgressDetailAreaReportClient.visibility = View.GONE
                binding.llReviewDetailAreaReportClient.visibility = View.VISIBLE

                // set data penilaian
                binding.tvCheckByDetailAreaReportClient.text = it.data.submitByName
                binding.tvPenilaianObjectDetailAreaReportClient.text = it.data.checklistReviewName
                binding.tvNoteReviewDetailAreaReportClient.text =
                    if (it.data.notes == "" || it.data.notes == null || it.data.notes == "null") {
                        "Tidak ada catatan"
                    } else {
                        it.data.notes
                    }

                // set photo penilaian
                val img = it.data.image
                val url = getString(R.string.url) + "assets.admin_master/images/checklist_image/$img"
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)
                    .centerCrop()
                    .error(R.drawable.ic_error_image)
                Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(binding.ivReviewDetailAreaReportClient)

                // pop up photo
                binding.cardReviewDetailAreaReportClient.setOnClickListener {
                    val dialog = this.let { Dialog(it) }
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_custom_image_zoom)
                    val close = dialog.findViewById(R.id.iv_close_img_zoom) as ImageView
                    val ivZoom = dialog.findViewById(R.id.iv_img_zoom) as ImageView

                    ivZoom.setImageDrawable(binding.ivReviewDetailAreaReportClient.drawable)

                    close.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            } else {
                binding.llProgressDetailAreaReportClient.visibility = View.VISIBLE
                binding.llReviewDetailAreaReportClient.visibility = View.GONE
            }
        }
    }

    private fun loadData() {
        viewModel.getDetailAreaReport(projectCode, shiftId, plottingId)
        viewModel.getDetailPlottingReport(plottingId)
    }

    private fun setPhotoProfile(img: String?, imageView: ImageView) {
        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, 0)
    }
}