package com.hkapps.hygienekleen.features.features_client.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSheetComplaintBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.DashboardCtalkClientActivity
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.visitor.DashboardComplaintVisitorClientActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSheetComplaintFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBotSheetComplaintBinding
    private val logoPref =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.LOGO_CLIENT, "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentBotSheetComplaintBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBottomComplaintProjectClient.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardCtalkClientActivity::class.java))
            dismiss()
        }

        binding.btnBottomComplaintVisitorClient.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardComplaintVisitorClientActivity::class.java))
            dismiss()
        }

        //setlogo
        if (logoPref.isNullOrEmpty()){
            binding.llImageLogoClient.visibility = View.GONE
        } else {
            setPhotoProfile(logoPref, binding.ivLogoClient)
        }
    }


    private fun setPhotoProfile(img: String?, imageView: ImageView) {
//        val url = getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        val url = getString(R.string.url) + "assets.admin_master/images/logo_project/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                resources.getIdentifier(uri, null, requireContext().packageName)
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

}