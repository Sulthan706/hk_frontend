package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityChooseClientMeetingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter.ChooseClientsAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ChooseClientMeetingActivity : AppCompatActivity(), ChooseClientsAdapter.ChooseClientsCallback {

    private lateinit var binding: ActivityChooseClientMeetingBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val projectCode = when(clickFrom) {
        "listMeeting" -> CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_MEETING, "")
        "mainInspeksi" -> CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
        else -> ""
    }
    private val listChooseClient = ArrayList<Data>()

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseClientMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarChooseClientMeeting.tvAppbarTitle.text = "Daftar Klien"
        binding.appbarChooseClientMeeting.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // default layout
        binding.tv1ChooseClientMeeting.visibility = View.GONE
        binding.rvLaporanKondisiArea.visibility = View.GONE
        binding.btnDisableChooseClientMeeting.visibility = View.GONE
        binding.btnEnableChooseClientMeeting.visibility = View.INVISIBLE
        binding.tvEmptyChooseClientMeeting.visibility = View.VISIBLE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvLaporanKondisiArea.layoutManager = layoutManager

        // set on click button choose
        binding.btnEnableChooseClientMeeting.setOnClickListener {
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, "chooseClient")
            val i = Intent(this, SendLaporanMeetingActivity::class.java)
            i.putExtra("SelectedClients", listChooseClient)
            i.putExtra("sizeSelectedClient", listChooseClient.size)
            startActivity(i)
            finish()
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listClientMeetingResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tv1ChooseClientMeeting.visibility = View.VISIBLE
                    binding.rvLaporanKondisiArea.visibility = View.VISIBLE
                    binding.tvEmptyChooseClientMeeting.visibility = View.GONE

                    val rvAdapter = ChooseClientsAdapter(
                        this,
                        it.data as ArrayList<Data>
                    )
                    binding.rvLaporanKondisiArea.adapter = rvAdapter
                    
                } else {
                    binding.tv1ChooseClientMeeting.visibility = View.GONE
                    binding.rvLaporanKondisiArea.visibility = View.GONE
                    binding.btnDisableChooseClientMeeting.visibility = View.GONE
                    binding.btnEnableChooseClientMeeting.visibility = View.GONE
                    binding.tvEmptyChooseClientMeeting.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(this, "Gagal mengambil list data klien", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListClientMeeting(projectCode)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onChooseClient(
        clientId: Int,
        clientName: String,
        clientEmail: String,
        imageView: ImageView
    ) {
        if (imageView.tag == null) {
            imageView.tag = "check"
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox))
            listChooseClient.add(Data(clientId, clientName, clientEmail))
        } else {
            if (imageView.tag == "check") {
                imageView.tag = "uncheck"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_uncheckbox))
                listChooseClient.remove(Data(clientId, clientName, clientEmail))
            } else {
                imageView.tag = "check"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox))
                listChooseClient.add(Data(clientId, clientName, clientEmail))
            }
        }

        // validate button choose
        if (listChooseClient.isNotEmpty()) {
            binding.btnDisableChooseClientMeeting.visibility = View.GONE
            binding.btnEnableChooseClientMeeting.visibility = View.VISIBLE
        } else {
            binding.btnDisableChooseClientMeeting.visibility = View.VISIBLE
            binding.btnEnableChooseClientMeeting.visibility = View.INVISIBLE
        }
    }

}