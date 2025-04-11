package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityMyTeamClientsHistoryBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.listwithshiftemployee.Content
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlistmanagement.ContentCfteamMgmnt
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter.MyTeamListCfManagementAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter.MyTeamListEmployeeAdapter
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.activity.ProfileOperationalActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyTeamClientsListActivity : AppCompatActivity(), MyTeamListCfManagementAdapter.ItemPhoneManagement {
    private lateinit var binding: ActivityMyTeamClientsHistoryBinding

    //viewmodel
    private val viewModel: MyTeamClientViewModel by lazy {
        ViewModelProviders.of(this).get(MyTeamClientViewModel::class.java)
    }

    //pref
    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val role =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.ROLE_CLIENT, "")

    //adapter
    lateinit var adapters: MyTeamListEmployeeAdapter
    lateinit var adapterMgmnt: MyTeamListCfManagementAdapter

    //val
    private var loadingDialog: Dialog? = null
    var page: Int = 0
    private var isLastPage = false

    private var shiftId:Int = 0
    private var shiftValue = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamClientsHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)

        when (role) {
            "Operator" -> {
                binding.appbarHistoryComplaint.tvAppbarTitle.text = "Daftar Operator"
            }
            "Pengawas" -> {
                binding.appbarHistoryComplaint.tvAppbarTitle.text = "Daftar Pengawas"
            }
            "Management" -> {
                binding.appbarHistoryComplaint.tvAppbarTitle.text = "Daftar CF Manajemen"
                binding.llShiftCfteamSelected.visibility = View.GONE
            }
        }

        binding.appbarHistoryComplaint.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }
        //shift spinner
        viewModel.getListShiftCfteam(projectId)
        viewModel.getListShiftCfteamViewModel().observe(this){
            if (it.code == 200){
                val sizeShift = it.data.size
                for (i in 0 until  sizeShift){
                    shiftValue.add(it.data[i].shiftDescription)
                }
                binding.spinnerShiftMyteam.adapter = ArrayAdapter(this, R.layout.spinner_item, shiftValue)
                binding.spinnerShiftMyteam.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                        shiftId = it.data[position].shiftId

                        defaultLayout()
                        if (role == "Pengawas" || role == "Operator") {
                            loadData()
                        } else {
                            loadDataManagement()
                        }
                        showLoading("Loading text..")
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }

                }
            } else {
                val defaultSubLocData = resources.getStringArray(R.array.noChoose)
                binding.spinnerShiftMyteam.adapter = ArrayAdapter(this, R.layout.spinner_item, defaultSubLocData)
                Toast.makeText(this, "Gagal mengambil data sub area", Toast.LENGTH_SHORT).show()
            }
        }




        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListEmployeeCfteam.layoutManager = layoutManager

        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    if (role == "Management") {
                        loadDataManagement()
                    } else {
                        loadData()
                    }
                }
            }
        }
        val sdf = SimpleDateFormat("MMMM yyyy")
        val currentDate = sdf.format(Date())
        binding.tvDateMonthNowCfTeam.text = currentDate
        binding.rvListEmployeeCfteam.addOnScrollListener(scrollListener)
//        Log.d("TAG", "$loginas")
        if (role == "Operator" || role == "Pengawas"){
            loadData()
        } else {
            loadDataManagement()
        }
        setObserver()
        setObserverManagement()
        showLoading("Loading text...")
        //oncreate
    }

    private fun defaultLayout() {
        binding.rvListEmployeeCfteam.visibility = View.GONE
        binding.tvEmptyStateListCfteam.visibility = View.VISIBLE
    }

    private fun loadDataManagement() {
        viewModel.getListManagementCfteam(projectId, page)
    }

    //fun
    private fun loadData() {
        viewModel.getListCfteamByShift(projectId, role, shiftId, page)
    }

    private fun setObserver() {
        viewModel.getListCfteamByShiftViewModel().observe(this, Observer {
            if (it.code == 200) {
                binding.rvListEmployeeCfteam.visibility = View.VISIBLE
                binding.tvEmptyStateListCfteam.visibility = View.GONE

                if (it.data.content.isEmpty()) {
                    binding.tvEmptyStateListCfteam.visibility = View.VISIBLE
                    binding.rvListEmployeeCfteam.visibility = View.GONE
                } else {
                    isLastPage = false
                    if (page == 0) {
                        adapters = MyTeamListEmployeeAdapter(it.data.content as ArrayList<Content>)
                        binding.rvListEmployeeCfteam.adapter = adapters
                    } else {

                        adapters.ListCfteams.addAll(it.data.content)
                        adapters.notifyItemRangeChanged(
                            adapters.ListCfteams.size - it.data.size,
                            adapters.ListCfteams.size
                        )
                    }
                }



            } else {
                Toast.makeText(this, "Gagal Mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }

    private fun setObserverManagement() {
        viewModel.getListManagementCfteamViewModel().observe(this, Observer {
            if (it.code == 200) {
                binding.rvListEmployeeCfteam.visibility = View.VISIBLE
                binding.tvEmptyStateListCfteam.visibility = View.GONE
                if (it.data.content.isEmpty()) {
                    binding.tvEmptyStateListCfteam.visibility = View.VISIBLE
                    binding.rvListEmployeeCfteam.visibility = View.GONE
                } else {
                    isLastPage = false
                    if (page == 0) {
                        adapterMgmnt =
                            MyTeamListCfManagementAdapter(it.data.content as ArrayList<ContentCfteamMgmnt>).also { it.setListener(this) }
                        binding.rvListEmployeeCfteam.adapter = adapterMgmnt
                    } else {

                        adapterMgmnt.ListMgmntCfteam.addAll(it.data.content)
                        adapterMgmnt.notifyItemRangeChanged(
                            adapterMgmnt.ListMgmntCfteam.size - it.data.size,
                            adapterMgmnt.ListMgmntCfteam.size
                        )
                    }
                }

            } else {
                Toast.makeText(this, "Gagal Mengambil data", Toast.LENGTH_SHORT).show()
            }
            hideLoading()
        })
    }


    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onClick(managementPhone: String) {
        dialogPhone(managementPhone)
    }
    private fun dialogPhone(managementPhone: String) {
        val view = View.inflate(this, R.layout.dialog_item_phone_client, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
//        dialog.setCancelable(false)
//        val btnBack = dialog.findViewById<Button>(R.id.btnBack)
        val text = dialog.findViewById<TextView>(R.id.tvPhoneMgmnt)
        text.text = managementPhone



        text.setOnClickListener {
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                makeRequest()
            } else {
                val call = Intent(Intent.ACTION_CALL)
                call.data = Uri.parse("tel:$managementPhone")
                startActivity(call)
            }

        }


        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CALL_PHONE),
            ProfileOperationalActivity.CALL_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ProfileOperationalActivity.CALL_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the call phone permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



}