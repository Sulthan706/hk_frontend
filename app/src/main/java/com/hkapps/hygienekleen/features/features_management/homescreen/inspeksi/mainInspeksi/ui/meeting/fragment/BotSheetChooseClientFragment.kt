package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentBotSheetChooseClientBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter.ChooseClientsAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BotSheetChooseClientFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentBotSheetChooseClientBinding
    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val listChooseClient = ArrayList<Data>()
    private var onDataSentListener: OnDataSentListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBotSheetChooseClientBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLaporanKondisiArea.layoutManager = layoutManager

        binding.btnEnableChooseClientMeeting.setOnClickListener {
            onDataSentListener?.onDataSent(listChooseClient)
            dismiss() // Close the bottom sheet after sending data
        }


        loadData()
        setObserver()
    }

    private fun loadData() {
        viewModel.getListClientMeeting(projectCode)
    }

    private fun setObserver() {
        viewModel.listClientMeetingResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tv1ChooseClientMeeting.visibility = View.VISIBLE
                    binding.rvLaporanKondisiArea.visibility = View.VISIBLE
                    binding.tvEmptyChooseClientMeeting.visibility = View.GONE

                    val rvAdapter = ChooseClientsAdapter(
                        requireContext(),
                        it.data as ArrayList<Data>
                    ).also { it1 -> it1.setListener(this)}
                    binding.rvLaporanKondisiArea.adapter = rvAdapter

                } else {
                    binding.tv1ChooseClientMeeting.visibility = View.GONE
                    binding.rvLaporanKondisiArea.visibility = View.GONE
                    binding.btnDisableChooseClientMeeting.visibility = View.GONE
                    binding.btnEnableChooseClientMeeting.visibility = View.GONE
                    binding.tvEmptyChooseClientMeeting.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Gagal mengambil list data klien", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onChooseClient(clientId: Int, clientName: String, clientEmail: String, imageView: ImageView) {
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

    interface OnDataSentListener {
        fun onDataSent(dataList: ArrayList<Data>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataSentListener) {
            onDataSentListener = context
        } else {
            throw RuntimeException("$context must implement OnDataSentListener")
        }
    }

}