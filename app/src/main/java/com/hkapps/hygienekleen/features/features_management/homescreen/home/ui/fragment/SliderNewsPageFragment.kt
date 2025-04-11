package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentSliderNewsPageBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.ZoomNewsActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class SliderNewsPageFragment : Fragment() {
    private lateinit var binding: FragmentSliderNewsPageBinding
    private var zoomImageDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSliderNewsPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlData = arguments?.getString("image_url")
        Log.d("agrikece","asd $urlData")

        loadImage(urlData, binding.imageViewNews, binding.progressBarNews)
        binding.imageViewNews.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.IMAGE_NEWS, urlData!!)
            startActivity(Intent(requireContext(), ZoomNewsActivity::class.java))
        }

    }

    private fun zoomImage(img: String?) {
        zoomImageDialog = Dialog(requireContext())
        zoomImageDialog?.setContentView(R.layout.dialog_zoom_image)
        zoomImageDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val zoomImg = zoomImageDialog?.findViewById<ImageView>(R.id.ivImageZoomRkb)
        val closeZoom = zoomImageDialog?.findViewById<ImageView>(R.id.ivCloseZoomRkb)

        val requestOptions = RequestOptions()
            .centerCrop()
            .error(R.drawable.ic_error_image)


        Glide.with(this)
            .load(getString(R.string.url) + "assets.admin_master/announcement/image/$img")
//            .load(getString(R.string.url) + "rkb/$img")
            .apply(requestOptions)
            .into(zoomImg!!)


        closeZoom?.setOnClickListener {
            zoomImageDialog?.dismiss()
        }

        // Show the zoom-in dialog
        zoomImageDialog?.show()
    }

    private fun loadImage(img: String?, imageView: ImageView, progressBar: ProgressBar) {
        // Show the progress bar for the current image while loading
        progressBar.visibility = View.VISIBLE

        if (img == "null" || img == null || img == "") {
            val uri = "@drawable/ic_camera_black" // Replace with your default image
            val imageResource = resources.getIdentifier(uri, null, requireContext().packageName)
            val res = resources.getDrawable(imageResource)
            imageView.setImageDrawable(res)
            // Hide the progress bar when the default image is set
            progressBar.visibility = View.GONE
        } else {
            val url = getString(R.string.url) + "assets.admin_master/announcement/image/$img"
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // Because file name is always the same
                .skipMemoryCache(true)

            Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle image loading failure here (e.g., show an error message)
                        progressBar.visibility = View.GONE // Hide the progress bar on failure
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Hide the progress bar for the current image when it is loaded successfully
                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)
        }
    }

}