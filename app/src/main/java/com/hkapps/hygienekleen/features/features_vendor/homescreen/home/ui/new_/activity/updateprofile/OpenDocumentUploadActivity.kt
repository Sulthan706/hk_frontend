package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.updateprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityOpenDocumentUploadBinding
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.setupEdgeToEdge

class OpenDocumentUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenDocumentUploadBinding
    private val typeDocument =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.TYPE_DOCUMENT,"")
    private val imageKTP =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_KTP,"")
    private val imageSertifikat =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SERTIFIKAT, "")
    private val imageSIO =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SIO, "")
    private val imageIjazah =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_IJAZAH, "")
    private val imageSkck =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.IMAGE_SKCK, "")


    var img:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOpenDocumentUploadBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupEdgeToEdge(binding.root,null)
        when(typeDocument){
            "KTP" -> {
                img = imageKTP
            }
            "SIO" -> {
                img = imageSIO
            }
            "IJAZAH" -> {
                img = imageIjazah
            }
            "SERTIFIKAT KOMPETENSI" -> {
                img = imageSertifikat
            }
            "SKCK" -> {
                img = imageSkck
            }
        }

        val urlClient =
            this.getString(R.string.url) + "/assets.admin_master/images/document/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                this.resources.getIdentifier(uri, null, this.packageName)
            val res = this.resources.getDrawable(imaResource)
//            holder.iv.setImageDrawable(res)
            binding.ivResultSelfie.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(this)
                .load(urlClient)
                .apply(requestOptions)
                .into(binding.ivResultSelfie)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        startActivity(Intent(this, ChangeDocumentActivity::class.java))
        finish()
    }
}