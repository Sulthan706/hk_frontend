package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.ui.activity


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hkapps.hygienekleen.databinding.ActivityOverviewImageBinding


class OverviewImageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOverviewImageBinding

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBar = "Overview Image"
        binding.appbarOverviewWeeklyProgress.tvAppbarTitle.text = appBar
        binding.appbarOverviewWeeklyProgress.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        val uriStringBefore = intent.getStringExtra("imageUriB")
        val uriStringAfter = intent.getStringExtra("imageUriA")

        val uriBefore: Uri? = uriStringBefore?.let { Uri.parse(it) }
        val uriAfter: Uri? = uriStringAfter?.let { Uri.parse(it) }
        binding.ivResultImage.setImageDrawable(null)

        val imageApiBefore = intent.getStringExtra("imageApiB")
        val imageApiAfter = intent.getStringExtra("imageApiA")


        if (uriStringBefore != null) {
            uriBefore?.let {
                binding.ivResultImage.setImageURI(it)
            }
        }else if(uriStringAfter != null){
            uriAfter?.let {
                binding.ivResultImage.setImageURI(it)
            }
        }else if(imageApiBefore != null){
            Glide.with(binding.ivResultImage.context).load(imageApiBefore).into(binding.ivResultImage)
        }else if(imageApiAfter != null ){
            Glide.with(binding.ivResultImage.context).load(imageApiAfter).into(binding.ivResultImage)
        }else{
            Toast.makeText(this, "Gambar tidak bisa ditampilkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}