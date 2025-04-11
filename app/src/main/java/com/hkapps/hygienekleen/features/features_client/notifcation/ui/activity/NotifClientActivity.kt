package com.hkapps.hygienekleen.features.features_client.notifcation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityNotifClientBinding
import com.hkapps.hygienekleen.features.features_client.complaint.ui.activity.DetailHistoryComplaintActivity
import com.hkapps.hygienekleen.features.features_client.notifcation.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ContentListNotifClient
import com.hkapps.hygienekleen.features.features_client.notifcation.ui.adapter.ListNotifClientAdapter
import com.hkapps.hygienekleen.features.features_client.notifcation.ui.adapter.NotifClientAdapter
import com.hkapps.hygienekleen.features.features_client.notifcation.viewmodel.NotifClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.ConnectionTimeoutFragment
import com.hkapps.hygienekleen.utils.NoInternetConnectionCallback
import com.hkapps.hygienekleen.utils.hide
import com.hkapps.hygienekleen.utils.show
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton


class NotifClientActivity : AppCompatActivity(), ListNotifClientAdapter.ListNotifClient,
    NoInternetConnectionCallback {
    private lateinit var binding: ActivityNotifClientBinding
    private lateinit var clientAdapter: NotifClientAdapter
    private lateinit var listClientAdapter: ListNotifClientAdapter
    private lateinit var getNotificationsClientResponseModel: NotifClientResponseModel
    private val clientId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var page = 0
    private val intentNotif =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTIF_INTENT, "")
    private lateinit var rvSkeleton: Skeleton
    private var isLastPage = false
    private var reloadedNeeded = true
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")

    private val viewModel: NotifClientViewModel by lazy {
        ViewModelProviders.of(this).get(NotifClientViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        // set appbar layout
        binding.layoutAppbarNotifClient.tvAppbarTitle.text = "Notifikasi"
        binding.layoutAppbarNotifClient.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
            finish()
        }

        // set recycler view layout
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotificationClient.layoutManager = layoutManager

        var scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
//                    doLoadData()
                }
            }
        }

        binding.swipeRefreshLayoutNotifClient.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutNotifClient.setOnRefreshListener {
            page = 0
            loadData()
//            doLoadData()
        }

        binding.rvNotificationClient.addOnScrollListener(scrollListener)


        rvSkeleton = binding.rvNotificationClient.applySkeleton(R.layout.item_notification_client)
        rvSkeleton.showSkeleton()
        loadData()
//        doLoadData()
        connectionTimeout()
//        setObserver()
        setObserverList()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    //    private fun doLoadData() {
//        viewModel.getNotifClient(clientId, page)
//    }
    private fun loadData() {
        viewModel.listNotifClient(clientId, projectId, page)
    }


    private fun setObserverList() {
        viewModel.getListNotifClient().observe(this, Observer {
            if (rvSkeleton.isSkeleton()) rvSkeleton.showOriginal()
            binding.swipeRefreshLayoutNotifClient.isRefreshing = false
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.layoutEmptyStateNotifClient.hide()
                    binding.rvNotificationClient.show()
                    isLastPage = it.data.last

                    if (page == 0) {
                        binding.layoutEmptyStateNotifClient.hide()
                        binding.rvNotificationClient.show()
                        listClientAdapter =
                            ListNotifClientAdapter(
                                this, it.data.content as ArrayList<ContentListNotifClient>
                            ).also { it.setListener(this) }
                        binding.rvNotificationClient.adapter = listClientAdapter
                    } else {
                        listClientAdapter.listNotifClients.addAll(it.data.content)
                        listClientAdapter.notifyItemRangeChanged(
                            listClientAdapter.listNotifClients.size - it.data.size,
                            listClientAdapter.listNotifClients.size
                        )
                    }

                } else {
                    binding.rvNotificationClient.hide()
                    binding.layoutEmptyStateNotifClient.show()
                    // set appbar layout
                    binding.layoutAppbarNotifClient.tvAppbarTitle.text = "Notifikasi"
                    binding.layoutAppbarNotifClient.ivAppbarBack.setOnClickListener {
                        super.onBackPressed()
                        finish()
                    }
                }
            } else {
                binding.rvNotificationClient.hide()
                binding.layoutEmptyStateNotifClient.show()
                // set appbar layout
                binding.layoutAppbarNotifClient.tvAppbarTitle.text = "Notifikasi"
                binding.layoutAppbarNotifClient.ivAppbarBack.setOnClickListener {
                    super.onBackPressed()
                    finish()
                }
            }
        })

    }

    private fun connectionTimeout() {
        val connectionTimeoutFragment =
            ConnectionTimeoutFragment.newInstance().also { it.setListener(this) }
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.flConnectionTimeout_notif_client,
                connectionTimeoutFragment,
                "connectionTimeout"
            )
            .commit()
    }

    private fun reset() {
        page = 0
    }

    override fun onRetry() {
        binding.flConnectionTimeoutNotifClient.hide()
        rvSkeleton.showShimmer
        reset()
//        doLoadData()
        loadData()
    }



    override fun onClickNotifClient(notificationHistoryId: Int, isRead: String, relationId: Int) {
        if (isRead == "N") {
            viewModel.putReadNotifClient(notificationHistoryId, clientId)
            viewModel.putReadNotifClient().observe(this, Observer {
                val i = Intent(this, DetailHistoryComplaintActivity::class.java)
                startActivityForResult(i, CREATE_CODE)
                CarefastOperationPref.saveInt(
                    CarefastOperationPrefConst.ID_COMPLAINT_CLIENT,
                    relationId
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                )
            })

        } else {
            val i = Intent(this, DetailHistoryComplaintActivity::class.java)
            startActivity(i)
            CarefastOperationPref.saveInt(
                CarefastOperationPrefConst.ID_COMPLAINT_CLIENT,
                relationId
            )

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                this.reloadedNeeded = true
            }
        }
    }

    companion object {
        private const val CREATE_CODE = 31
    }

    override fun onResume() {
        super.onResume()
        if (this.reloadedNeeded) {
            loadData()
        }
        this.reloadedNeeded = false
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

}

