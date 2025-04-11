package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentSubmitRatingBinding
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel.HumanCapitalViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.RatingProfilesViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SubmitRatingFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSubmitRatingBinding

    private val ratingByUserId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val roleUser = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_ROLE, "")
    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.OPERATIONAL_OPS_ID, 0)
    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.OPERATIONAL_OPS_PROJECT_CODE, "")
    private val jobCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.OPERATIONAL_OPS_JOB_CODE, "")
    private var rating = 0

    private val viewModel: HumanCapitalViewModel by lazy {
        ViewModelProviders.of(this)[HumanCapitalViewModel::class.java]
    }
    private val ratingViewModel: RatingProfilesViewModel by lazy {
        ViewModelProviders.of(this)[RatingProfilesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSubmitRatingBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set current month year text
        val currentTime = Calendar.getInstance().time
        binding.tvMonthYearSubmitRating.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentTime)

        // set default rating
        binding.llRateSubmitRating.visibility = View.GONE
        binding.tvDefaultRateSubmitRating.visibility = View.VISIBLE

        // validate on set rating
        binding.ratingBarSubmitRating.setOnRatingBarChangeListener { _, mRating, _ ->
            binding.llRateSubmitRating.visibility = View.VISIBLE
            binding.tvDefaultRateSubmitRating.visibility = View.GONE
            when(mRating) {
                1.0F -> {
                    rating = 1
                    binding.tvRate1SubmitRating.setTextColor(resources.getColor(R.color.red1))
                    binding.tvRate1SubmitRating.text = "Poor "
                    binding.tvRate2SubmitRating.text = "- Needs significant improvement"
                }
                2.0F -> {
                    rating = 2
                    binding.tvRate1SubmitRating.setTextColor(resources.getColor(R.color.primary_color))
                    binding.tvRate1SubmitRating.text = "Needs Improvement "
                    binding.tvRate2SubmitRating.text = "- Requires additional training or support"
                }
                3.0F -> {
                    rating = 3
                    binding.tvRate1SubmitRating.setTextColor(resources.getColor(R.color.cyan))
                    binding.tvRate1SubmitRating.text = "Meets Expectations "
                    binding.tvRate2SubmitRating.text = "- Satisfactory work"
                }
                4.0F -> {
                    rating = 4
                    binding.tvRate1SubmitRating.setTextColor(resources.getColor(R.color.green2))
                    binding.tvRate1SubmitRating.text = "Exceeds Expectations "
                    binding.tvRate2SubmitRating.text = "- Strong performance"
                }
                5.0F -> {
                    rating = 5
                    binding.tvRate1SubmitRating.setTextColor(resources.getColor(R.color.green2))
                    binding.tvRate1SubmitRating.text = "Excellent "
                    binding.tvRate2SubmitRating.text = "- Outstanding performance"
                }
            }
        }

        // set count characters & visibility button submit
        binding.etFeedbackSubmitRating.addTextChangedListener {
            if (it == null) {
                binding.tvCharactersSubmitRating.text = "0/250 characters"
                binding.btnDisableSubmitRating.visibility = View.VISIBLE
                binding.btnSubmitRating.visibility = View.GONE
            } else {
                binding.tvCharactersSubmitRating.text = "${it.length}/250 characters"
                binding.btnDisableSubmitRating.visibility = View.GONE
                binding.btnSubmitRating.visibility = View.VISIBLE
            }
        }
        
        // set on click button submit
        binding.btnSubmitRating.setOnClickListener {
            if (rating == 0) {
                Toast.makeText(requireContext(), "Please input your rate", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(
                    "geik",
                    "onViewCreated: ratingByUserId=$ratingByUserId, roleUser=$roleUser, employeeId=$employeeId, rating=$rating, projectCode=$projectCode, jobCode=$jobCode"
                )
                viewModel.submitRating(ratingByUserId, roleUser, employeeId, rating, binding.etFeedbackSubmitRating.text.toString(), projectCode, jobCode)
            }
        }

        ratingViewModel.getProfileRating(employeeId)
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) {
            if (it != null) {
                if (it) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        ratingViewModel.isLoading?.observe(this) {
            if (it != null) {
                if (it) {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.submitRatingResponse.observe(this) {
            if (it.code == 200) {
                showDialogSubmitResponse()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error ${it.errorCode} ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        ratingViewModel.getProfileRating().observe(this) {
            if (it.code == 200) {
                if (it.data.totalRating == 0) {
                    binding.llEmptySubmitRating.visibility = View.GONE
                    binding.llSubmitRating.visibility = View.VISIBLE
                } else {
                    binding.llEmptySubmitRating.visibility = View.VISIBLE
                    binding.llSubmitRating.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogSubmitResponse() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_custom_response_success_error)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()

        val animationSuccess = dialog.findViewById<LottieAnimationView>(R.id.animationSuccessResponse)
        val animationFailed = dialog.findViewById<LottieAnimationView>(R.id.animationFailedResponse)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccessResponse)
        val tvFailed = dialog.findViewById<TextView>(R.id.tvFailedResponse)
        val tvInfo = dialog.findViewById<TextView>(R.id.tvInfoResponse)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnResponse)

        animationSuccess?.visibility = View.VISIBLE
        animationFailed?.visibility = View.GONE
        tvSuccess?.visibility = View.VISIBLE
        tvFailed?.visibility = View.GONE

        tvSuccess.text = "Submitted"
        tvInfo.text = "Thank you for taking the time to provide a rating and feedback. We will use this valuable input to further support this employee's development."
        button.text = "Back to Profile"
        button.setOnClickListener {
            dialog.dismiss()
            this.dialog?.dismiss()
        }

    }


}