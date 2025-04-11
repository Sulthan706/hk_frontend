package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityDetailNewsManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter.SliderAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.appbar.AppBarLayout

class DetailNewsManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsManagementBinding
    private val viewModel: HomeManagementViewModel by lazy {
        ViewModelProviders.of(this).get(HomeManagementViewModel::class.java)
    }
    private val newsId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.ID_NEWS, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsManagementBinding.inflate(layoutInflater)
        // Make the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        setContentView(binding.root)

        // Add top margin to the toolbar to account for the status bar
        val toolbarLayoutParams =
            binding.toolbarNewsManagement.layoutParams as ViewGroup.MarginLayoutParams
        val statusBarHeight = getStatusBarHeight()
        toolbarLayoutParams.topMargin = statusBarHeight
        binding.toolbarNewsManagement.layoutParams = toolbarLayoutParams


        binding.shimmerNewsManagement.startShimmerAnimation()
        loadData()
        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun trimText(text: String, maxLength: Int = 75): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text
        }
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun loadData() {
        viewModel.getDetailNewsManagement(newsId)
    }

    private fun setObserver() {
        viewModel.getDetailNewsManagementViewModel().observe(this) {
            if (it.code == 200){
                binding.shimmerNewsManagement.apply {
                    stopShimmerAnimation()
                    visibility = View.GONE
                }
                val globalTitleNewsManagement = trimText(it.data.newsTitle)
                val imageUrlManagement = it.data.newsImage
                val videoUrlManagement = it.data.newsVideo
                setupView(imageUrlManagement, videoUrlManagement, globalTitleNewsManagement)
                binding.tvTitleNewsManagement.text = trimText(it.data.newsTitle)

                val spannedText: Spanned = Html.fromHtml(it.data.newsDescription)
                val textSpan: Spannable = Spannable.Factory.getInstance().newSpannable(spannedText)
                Linkify.addLinks(textSpan, Linkify.ALL)
                binding.tvContentNewsManagement.text = textSpan
                binding.tvContentNewsManagement.movementMethod = LinkMovementMethod.getInstance()
                binding.tvContentNewsManagement.setOnClickListener { _ ->
                    val urlSpans: Array<URLSpan> = textSpan.getSpans(0, textSpan.length, URLSpan::class.java)
                    if (urlSpans.isNotEmpty()) {
                        val url = urlSpans[0].url
                        // Open the URL in a web browser
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url.toString())
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView(imageUrl:String, videoUrls:String, titleData: String){

        binding.appBarLayoutNewsManagement.visibility = View.VISIBLE
        binding.nsManagementnews.visibility = View.VISIBLE

        val appBarLayout: AppBarLayout = binding.appBarLayoutNewsManagement
        val toolbar: Toolbar = binding.toolbarNewsManagement
        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }
        //validation appbar hide/show
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val titleVisibleThreshold = -500 // Adjust this threshold as needed

            if (verticalOffset < titleVisibleThreshold) {
                // Show the title and enable the navigation icon
                binding.collapsingToolbarNewsManagement.title = titleData
                toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
                binding.tvTitleNewsManagement.visibility = View.GONE
            } else {
                // Hide the title and disable the navigation icon
                toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
                binding.collapsingToolbarNewsManagement.title = null
                binding.tvTitleNewsManagement.visibility = View.VISIBLE
            }
        })

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val images = listOf(imageUrl, imageUrl)
        val adapter = SliderAdapter(this, images)
        viewPager.adapter = adapter

        //count slider
//        val totalCountPage = adapter.itemCount
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//
//                // Update the indicator text
//                val indicatorText = "${position + 1}/$totalCountPage"
//                binding.tvNewsIndicator.text = indicatorText
//            }
//        })


    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }


}