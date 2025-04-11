package com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.new_.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMidNotifBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.activity.lowlevel.ScheduleActivity
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.ContentNotificationMid
import com.hkapps.hygienekleen.features.features_vendor.notifcation.ui.new_.adapter.NotifMidAdapter
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.activity.DetailComplaintInternalActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.lowlevel.HistoryPermissionActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.DetailPermissionActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton


class   NotifMidActivity : AppCompatActivity(), NotifMidAdapter.ListNotifDataHistory {
    private lateinit var binding: ActivityMidNotifBinding
    private val viewModel: NotifVendorViewModel by lazy {
        ViewModelProviders.of(this).get(NotifVendorViewModel::class.java)
    }

    //pref
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val userLevelPosition =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val employeeId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    //adapter
    private lateinit var adapter: NotifMidAdapter

    //val
    private var category: String = "Semua"
    private var flagIzin = false
    private var flagLembur = false
    private var flagCTalk = false
    private var flagCFTalk = false
    var page: Int = 0
    private var isLastPage = false
    private var loadingDialog: Dialog? = null
    private lateinit var rvSkeleton: Skeleton
    private val notificationId: Int = 101

    private var reloadedNeeded = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMidNotifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.layoutAppbar.tvAppbarTitle.text = "Notifikasi"
        binding.layoutAppbar.tvAppbarTitle.setOnClickListener {
            createNotificationChannel()
        }
        binding.layoutAppbar.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        val btnIzin = binding.tvCatNotifIzin
        val btnLembur = binding.tvCatNotifLembur
        val btnCtalk = binding.tvCatNotifCtalk
        val btnCFtalk = binding.tvCatNotifCftalk


        btnIzin.setOnClickListener {
            if (flagIzin) {
                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)

                falseIzin()
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                allNotif()
            } else {
                flagIzin = true
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid_aktif)
                btnIzin.setTextColor(Color.parseColor("#F47721"))

                falseIzin()
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                izin()
            }
        }
        btnLembur.setOnClickListener {
            if (flagLembur) {
                flagLembur = false
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagCTalk = false
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)
                flagCFTalk = false
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                allNotif()
            } else {
                flagLembur = true
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid_aktif)
                btnLembur.setTextColor(Color.parseColor("#F47721"))

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagCTalk = false
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)
                flagCFTalk = false
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                lembur()
            }
        }
        btnCtalk.setOnClickListener {
            if (flagCTalk) {
                flagCTalk = false
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagLembur = false
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                flagCFTalk = false
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                allNotif()
            } else {
                flagCTalk = true
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid_aktif)
                btnCtalk.setTextColor(Color.parseColor("#F47721"))

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagLembur = false
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                flagCFTalk = false
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                cTalk()
            }
        }
        btnCFtalk.setOnClickListener {
            if (flagCFTalk) {
                flagCFTalk = false
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCFtalk.setTextColor(Color.BLACK)

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagLembur = false
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                flagCTalk = false
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)

                allNotif()
            } else {
                flagCFTalk = true
                btnCFtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid_aktif)
                btnCFtalk.setTextColor(Color.parseColor("#F47721"))

                flagIzin = false
                btnIzin.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnIzin.setTextColor(Color.BLACK)
                flagLembur = false
                btnLembur.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnLembur.setTextColor(Color.BLACK)
                flagCTalk = false
                btnCtalk.setBackgroundResource(R.drawable.bg_transparent_notif_mid)
                btnCtalk.setTextColor(Color.BLACK)

                cFTalk()
            }
        }
        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotificationHistory.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (userLevelPosition != "Operator") {
                        loadDataLeader()
                    } else {
                        loadDataOperator()
                    }
                }
            }
        }

        binding.rvNotificationHistory.addOnScrollListener(scrollListener)

        binding.swipeHistoryNotification.setColorSchemeResources(R.color.red)
//        binding.swipeHistoryNotification.setOnRefreshListener {
//            loadData()
//            reset()
//        }
        binding.swipeHistoryNotification.setOnRefreshListener {
            Handler().postDelayed({
                binding.swipeHistoryNotification.isRefreshing = false
                val i = Intent(this, NotifMidActivity::class.java)
                startActivity(i)
                finish()
                overridePendingTransition(R.anim.nothing, R.anim.nothing)
            },500
            )
        }

        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()
        setObserver()
        loadData()
        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))

        Log.d("cate", "$category")
        //oncreate^

    }

    //fun
    private fun reset() {
        page = 0
    }

    private fun loadData() {
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(employeeId, projectCode, category, page)
        } else {
            viewModel.getNotifMidLeader(employeeId, projectCode, category, page)
        }
    }

    private fun loadDataLeader() {
        viewModel.getNotifMidLeader(
            employeeId,
            projectCode,
            category,
            page
        )
    }

    private fun loadDataOperator() {
        viewModel.getNotifMidOperator(
            employeeId,
            projectCode,
            category,
            page
        )
    }

    override fun onRestart() {
        super.onRestart()
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    private fun falseIzin() {
        flagLembur = false
        flagCFTalk = false
        flagCTalk = false
    }

    private fun allNotif() {
        category = "Semua"
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(
                employeeId, projectCode, category, page
            )
        } else {
            viewModel.getNotifMidLeader(
                employeeId, projectCode, category, page
            )
        }
        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()

        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }

    private fun lembur() {
        category = "Lembur"
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(
                employeeId, projectCode, category, page
            )
        } else {
            viewModel.getNotifMidLeader(
                employeeId, projectCode, category, page
            )
        }
        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()

        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }

    private fun izin() {
        category = "Izin"
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(
                employeeId, projectCode, category, page
            )
        } else {
            viewModel.getNotifMidLeader(
                employeeId, projectCode, category, page
            )
        }
        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()
        setObserver()
        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }

    private fun cTalk() {
        category = "CTalk"
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(
                employeeId, projectCode, category, page
            )
        } else {
            viewModel.getNotifMidLeader(
                employeeId, projectCode, category, page
            )
        }
        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()

        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }

    private fun cFTalk() {
        category = "CFTalk"
        if (userLevelPosition == "Operator") {
            viewModel.getNotifMidOperator(
                employeeId, projectCode, category, page
            )
        } else {
            viewModel.getNotifMidLeader(
                employeeId, projectCode, category, page
            )
        }
        rvSkeleton =
            binding.rvNotificationHistory.applySkeleton(com.hkapps.hygienekleen.R.layout.item_shimmer)
        rvSkeleton.showSkeleton()

        showLoading(getString(com.hkapps.hygienekleen.R.string.loading_string))
    }

    private fun createNotificationChannel() {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "")
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, builder.build())
    }


    private fun setObserver() {
        viewModel.getNotificationLeaderViewModel().observe(this, Observer {

            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvNotificationHistory.visibility = View.VISIBLE
                    binding.tvEmptyStateNotification.visibility = View.GONE

                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = NotifMidAdapter(
                            this, it.data.content as ArrayList<ContentNotificationMid>
                        ).also { it.setListener(this) }
                        binding.rvNotificationHistory.adapter = adapter
                    } else {
                        adapter.notifMid.addAll(it.data.content as ArrayList<ContentNotificationMid>)
                        adapter.notifyItemRangeChanged(
                            adapter.notifMid.size - it.data.size,
                            adapter.notifMid.size
                        )
                    }
                } else {
                    rvSkeleton.showShimmer = false
                    binding.rvNotificationHistory.visibility = View.INVISIBLE
                    binding.tvEmptyStateNotification.visibility = View.VISIBLE
                }
            }
            hideLoading()
        })
        viewModel.getNotificationOperatorViewModel().observe(this, Observer {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    binding.rvNotificationHistory.visibility = View.VISIBLE
                    binding.tvEmptyStateNotification.visibility = View.GONE
                } else {
                    rvSkeleton.showShimmer = false
                    binding.rvNotificationHistory.visibility = View.INVISIBLE
                    binding.tvEmptyStateNotification.visibility = View.VISIBLE
                }

                isLastPage = it.data.last
                if (page == 0) {
                    adapter = NotifMidAdapter(
                         this, it.data.content as ArrayList<ContentNotificationMid>
                    ).also { it.setListener(this) }
                    binding.rvNotificationHistory.adapter = adapter
                } else {
                    adapter.notifMid.addAll(it.data.content as ArrayList<ContentNotificationMid>)

                    adapter.notifyItemRangeChanged(
                        adapter.notifMid.size - it.data.size,
                        adapter.notifMid.size
                    )
                }
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }

    override fun onClickNotification(
        notificationHistoryId: Int,
        isRead: String,
        notificationType: String,
        relationId: Int
    ) {
        if (userLevelPosition == "Operator") {
            when (isRead) {
                "N" -> {
                    when (notificationType) {
                        "PERMISSION" -> {
                            viewModel.putReadNotificationMid(notificationHistoryId, employeeId)
                            viewModel.putReadNotifMid().observe(this, Observer {
                                CarefastOperationPref.saveInt(
                                    CarefastOperationPrefConst.PERMISSION_ID,
                                    relationId
                                )
                                CarefastOperationPref.saveString(
                                    CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                                )
                                val i = Intent(this, HistoryPermissionActivity::class.java)
                                startActivityForResult(i, CREATE_CODE)
                            })
                        }
                        "OVERTIME" -> {
                            viewModel.putReadNotificationMid(notificationHistoryId, employeeId)
                            viewModel.putReadNotifMid().observe(this, Observer {
                                CarefastOperationPref.saveString(
                                    CarefastOperationPrefConst.NOTIF_INTENT,
                                    "notification"
                                )
                                val i = Intent(this, ScheduleActivity::class.java)
                                startActivityForResult(i, CREATE_CODE)

                            })
                        }
                        "COMPLAINT INTERNAL" -> {
                            viewModel.putReadNotificationMid(notificationHistoryId, employeeId)
                            viewModel.putReadNotifMid().observe(this, Observer {
                                val i = Intent(this, NotifMidActivity::class.java)
                                startActivity(i)
                            })
                        }
                        "COMPLAINT CLIENT" -> {
                            viewModel.putReadNotificationMid(notificationHistoryId, employeeId)
                            viewModel.putReadNotifMid().observe(this, Observer {
                                val i = Intent(this, NotifMidActivity::class.java)
                                startActivity(i)
                            })
                        }
                    }

                }
                "Y" -> {
                    when (notificationType) {
                        "PERMISSION" -> {
                            CarefastOperationPref.saveString(
                                CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                            )
                            val i = Intent(this, HistoryPermissionActivity::class.java)
                            startActivityForResult(i, CREATE_CODE)
                        }
                        "OVERTIME" -> {
                            CarefastOperationPref.saveString(
                                CarefastOperationPrefConst.NOTIF_INTENT,
                                "notification"
                            )
                            val i = Intent(this, ScheduleActivity::class.java)
                            startActivityForResult(i, CREATE_CODE)
                        }
                    }
                }
            }

            //pengawas
        } else {

            when (isRead) {
                "N" -> {
                    viewModel.putReadNotificationMid(notificationHistoryId, employeeId)
                    viewModel.putReadNotifMid().observe(this, Observer {
                        if (it.code == 200) {
                            when (notificationType) {
                                "PERMISSION" -> {
                                    CarefastOperationPref.saveInt(
                                        CarefastOperationPrefConst.PERMISSION_ID,
                                        relationId
                                    )
                                    CarefastOperationPref.saveString(
                                        CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                                    )
                                    val i = Intent(this, DetailPermissionActivity::class.java)
                                    startActivityForResult(i, CREATE_CODE)

                                }
                                "OVERTIME" -> {
                                    val i = Intent(this, ScheduleActivity::class.java)
                                    startActivity(i)
//                                    Toast.makeText(this, "hehe", Toast.LENGTH_SHORT).show()

                                }
                                "COMPLAINT CLIENT" -> {
                                    CarefastOperationPref.saveInt(
                                        CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION,
                                        relationId
                                    )
                                    CarefastOperationPref.saveString(
                                        CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                                    )
                                    val i =
                                        Intent(this, DetailComplaintInternalActivity::class.java)
                                    startActivityForResult(i, CREATE_CODE)
                                }
                                "COMPLAINT INTERNAL" -> {
                                    CarefastOperationPref.saveInt(
                                        CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION,
                                        relationId
                                    )
                                    CarefastOperationPref.saveString(
                                        CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                                    )
                                    val i = Intent(
                                        this,
                                        DetailComplaintInternalActivity::class.java
                                    )
                                    startActivityForResult(i, CREATE_CODE)
                                }
                            }

                        } else {
                            Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                "Y" -> {
                    when (notificationType) {
                        "PERMISSION" -> {
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.PERMISSION_ID,
                                relationId
                            )
                            CarefastOperationPref.saveString(
                                CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                            )
                            val i = Intent(this, DetailPermissionActivity::class.java)
                            startActivity(i)
                        }
                        "OVERTIME" -> {
                            val i = Intent(this, ScheduleActivity::class.java)
                            startActivity(i)
                        }
                        "COMPLAINT CLIENT" -> {
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION,
                                relationId
                            )
                            CarefastOperationPref.saveString(
                                CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                            )
                            val i = Intent(this, DetailComplaintInternalActivity::class.java)
                            startActivity(i)
                        }
                        "COMPLAINT INTERNAL" -> {
                            CarefastOperationPref.saveInt(
                                CarefastOperationPrefConst.COMPLAINT_ID_NOTIFICATION,
                                relationId
                            )
                            CarefastOperationPref.saveString(
                                CarefastOperationPrefConst.NOTIF_INTENT, "notification"
                            )
                            val i = Intent(this, DetailComplaintInternalActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
            }
        }
        hideLoading()
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish();
    }
}
