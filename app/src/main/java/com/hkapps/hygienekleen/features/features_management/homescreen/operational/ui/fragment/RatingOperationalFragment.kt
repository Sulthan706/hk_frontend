package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentRatingOperationalBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter.StarAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.RatingProfilesViewModel
import com.hkapps.hygienekleen.utils.CommonUtils

import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody.Companion.toRequestBody

class RatingOperationalFragment : BottomSheetDialogFragment() {

    private val viewModel: RatingProfilesViewModel by viewModels()
    lateinit var binding: FragmentRatingOperationalBinding
    var ratingByUserId : Int = 0
    var employeeId: Int = 0
    var jobCode: String = ""
    var projectCode: String = ""
    var rating: Int = 0
    private var loadingDialog: Dialog? = null


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatingOperationalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabTitles = arrayListOf("5", "4", "3", "2", "1")
        val adapter= StarAdapter(requireContext(),tabTitles)
        binding.spinnerRating.adapter = adapter
        binding.spinnerRating.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("agri","$position")
                rating = tabTitles[position].toInt()
            }

        }
        var employeeId = arguments?.getInt("employeeId",0)!!.toInt()

        binding.btnGiveRating.setOnClickListener {

            showLoading("Loading...")
            binding.btnGiveRating.isEnabled = false
            Log.d("rating", "$rating")
            var ratingByUserIdRequestBody = ratingByUserId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            var employeeIdRequestBody = employeeId.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            var projectCodeRequestBody = projectCode.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            var jobCodeRequestBody = jobCode.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            var ratingRequestBody = rating.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
            viewModel.giveRatingEmployee(ratingByUserIdRequestBody,employeeIdRequestBody,projectCodeRequestBody,jobCodeRequestBody,ratingRequestBody)
        }
        viewModel.getProfileRating(employeeId)
        setObserver()

    }






    //set observer
    private fun setObserver(){

        viewModel.getProfileRating().observe(requireActivity(), Observer {
            Log.d("observe", "response = $it")
            binding.tvNameProfileRating.text = it.data.employeeName
            if (it.data.resultRating.toString().equals("NaN")){
                binding.tvRatingResult.text = "-"
            }
            else {
                binding.tvRatingResult.text = it.data.resultRating
            }

            val img = it.data.employeePhotoProfile
            val url =
                this.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

            if (img == "null" || img == null || img == "") {
                val uri =
                    "@drawable/profile_default" // where myresource (without the extension) is the file
                val imaResource =
                    this.resources.getIdentifier(uri, null, requireContext().packageName)
                val res = this.resources.getDrawable(imaResource)
                this.binding.ivProfile.setImageDrawable(res)
            } else {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_error_image)

                Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(this.binding.ivProfile)
            }
             projectCode = it.data.projectCode
             ratingByUserId = it.data.ratingByUserId.toInt()
             jobCode = it.data.jobCode ?: ""
             employeeId = it.data.employeeId.toInt()



        })
        //success give rating
        viewModel.giveEmployeeRating().observe(requireActivity(), Observer {
            binding.btnGiveRating.text = it.status
            binding.btnGiveRatingSuccess.setOnClickListener {
                Toast.makeText(requireContext(), "Rating sudah diberikan", Toast.LENGTH_SHORT).show()

            }
            if (it.status == "SUCCESS"){
                binding.btnGiveRating.visibility = View.GONE
                binding.btnGiveRatingSuccess.visibility = View.VISIBLE
                binding.animationView.visibility = View.VISIBLE

            } else {
                Toast.makeText(requireContext(), "Already insert Rating or something wrong", Toast.LENGTH_SHORT).show()

            }
            hideLoading()
        })
        //give rating error
        viewModel.isErrorGiveRatingEmployee().observe(requireActivity(), Observer {
//            binding.btnGiveRating.text = it.message
            val view = View.inflate(requireActivity(), R.layout.dialog_custom_rating_error_month, null)
            val builder = AlertDialog.Builder(requireActivity())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            hideLoading()
        })
    }
    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }
    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(requireContext(), loadingText)
    }


}