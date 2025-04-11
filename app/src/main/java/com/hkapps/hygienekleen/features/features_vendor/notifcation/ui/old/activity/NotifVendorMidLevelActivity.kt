package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityNotifBinding
import com.hkapps.hygienekleen.features.features_vendor.notifcation.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.DataArrayContent
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel.NotifMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.old.adapter.NotifVendorMidLevelAdapter
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.ui.old.activity.ComplaintActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.hkapps.hygienekleen.utils.hide
import com.hkapps.hygienekleen.utils.show
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton


class NotifVendorMidLevelActivity : AppCompatActivity(),
    NotifVendorMidLevelAdapter.NotificationCallback,
    NoInternetConnectionCallback {
    private lateinit var binding: ActivityNotifBinding
    private lateinit var vendorAdapter: NotifVendorMidLevelAdapter
    private lateinit var getNotificationsResponseModel: NotifMidResponseModel
    private var page = 0
    private lateinit var rvSkeleton: Skeleton
    private var notificationPosition = 1
    private var targetId = ""
    private var notificationType = ""
    private var isLastPage = false
    private val clickFrom =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private val jobCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_POSITION, "")
    private val userLevel =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val notificationActivityVendorViewModel: NotifVendorViewModel by lazy {
        ViewModelProviders.of(this).get(NotifVendorViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (clickFrom == "Home") {
            binding.layoutAppbar.tvAppbarTitle.text = "Notifikasi"
            binding.tvEmptyNotif.text = "Tidak ada notifikasi"
            binding.ivCreateComplaint.visibility = View.GONE

            if (userLevel == "Operator") {
                binding.rvNotification.hide()
                binding.layoutEmptyState.show()
            } else {
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvNotification.layoutManager = layoutManager

                val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
                    override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                        if (!isLastPage) {
                            page++
                            doLoadData()
                        }
                    }
                }

                binding.swipeRefreshLayout.setColorSchemeResources(R.color.red)
                binding.swipeRefreshLayout.setOnRefreshListener {
                    page = 0
                    doLoadData()
                }

                binding.rvNotification.addOnScrollListener(scrollListener)

                rvSkeleton = binding.rvNotification.applySkeleton(R.layout.item_notif_skeleton)
                rvSkeleton.showSkeleton()

                doLoadData()
                connectionTimeout()
                setObserver()

                binding.ivCreateComplaint.setOnClickListener {
                    val i = Intent(this, ComplaintActivity::class.java)
                    startActivity(i)
                }
            }
        } else if (clickFrom == "Service") {
            binding.layoutAppbar.tvAppbarTitle.text = "CTalk"
            binding.tvEmptyNotif.text = "Tidak ada CTalk"
//            binding.ivCreateComplaint.visibility = View.VISIBLE
            binding.ivCreateComplaint.visibility = View.GONE

            if (userLevel == "Operator") {
                binding.rvNotification.hide()
                binding.layoutEmptyState.show()
            } else {
                val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvNotification.layoutManager = layoutManager

                val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
                    override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                        if (!isLastPage) {
                            page++
                            doLoadData()
                        }
                    }
                }

                binding.swipeRefreshLayout.setColorSchemeResources(R.color.red)
                binding.swipeRefreshLayout.setOnRefreshListener {
                    page = 0
                    doLoadData()
                }

                binding.rvNotification.addOnScrollListener(scrollListener)

                rvSkeleton = binding.rvNotification.applySkeleton(R.layout.item_notif_skeleton)
                rvSkeleton.showSkeleton()

                doLoadData()
                connectionTimeout()
                setObserver()

                binding.ivCreateComplaint.setOnClickListener {
                    val i = Intent(this, ComplaintActivity::class.java)
                    startActivity(i)
                }
            }
        }

        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun doLoadData() {
        notificationActivityVendorViewModel.getNotifMid(projectId, userLevel, page)
    }

    private fun setObserver() {
        notificationActivityVendorViewModel.getNotificationMidResponseModel()
            .observe(this, Observer {
                if (rvSkeleton.isSkeleton()) rvSkeleton.showOriginal()
                binding.swipeRefreshLayout.isRefreshing = false
                if (it.code == 200) {
                    if (it.dataNotifResponseModel.content.isNotEmpty()) {
                        binding.layoutEmptyState.hide()
                        binding.rvNotification.show()
                        isLastPage = it.dataNotifResponseModel.last
                        if (page == 0) {
                            getNotificationsResponseModel = it
                            vendorAdapter =
                                NotifVendorMidLevelAdapter(
                                    this, this, notificationActivityVendorViewModel,
                                    it.dataNotifResponseModel.content as ArrayList<DataArrayContent>
                                ).also { it.setListener(this) }
                            binding.rvNotification.adapter = vendorAdapter
                        } else {
                            vendorAdapter.notificationsContent.addAll(it.dataNotifResponseModel.content)
                            vendorAdapter.notifyItemRangeChanged(
                                vendorAdapter.notificationsContent.size - it.dataNotifResponseModel.content.size,
                                vendorAdapter.notificationsContent.size
                            )
                        }
                    } else {
                        binding.rvNotification.hide()
                        binding.layoutEmptyState.show()
                    }
                } else {
                    binding.rvNotification.hide()
                    binding.layoutEmptyState.show()
                }
            })

        notificationActivityVendorViewModel.isConnectionTimeout.observe(this) {
            if (it) {
                binding.flConnectionTimeout.show()
            }
        }
    }

    private fun connectionTimeout() {
        val connectionTimeoutFragment =
            ConnectionTimeoutFragment.newInstance().also { it.setListener(this) }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeout,
                connectionTimeoutFragment,
                "connectionTimeout"
            )
            .commit()
    }

    override fun onClickNotification(
        notificationId: Int,
        position: Int,
    ) {
        notificationPosition = position
        this.targetId = targetId

        val i = Intent(this, ComplaintNotificationActivity::class.java)
        CarefastOperationPref.saveInt(
            CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION,
            notificationId
        )
        startActivity(i)
    }

    private fun reset() {
        page = 0
    }

    override fun onRetry() {
        binding.flConnectionTimeout.hide()
        rvSkeleton.showShimmer
        reset()
        doLoadData()
    }

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }
}

