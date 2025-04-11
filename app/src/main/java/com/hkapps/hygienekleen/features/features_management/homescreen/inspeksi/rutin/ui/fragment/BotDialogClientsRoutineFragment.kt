package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.fragment

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
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel.RoutineViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.adapter.ChooseClientsRoutineAdapter

class BotDialogClientsRoutineFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBotSheetChooseClientBinding
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private val listClientRoutine = ArrayList<Data>()
    private var onDataRoutineSentListener: OnDataRoutineSetListener? = null

    private val viewModel: RoutineViewModel by lazy {
        ViewModelProviders.of(this).get(RoutineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBotSheetChooseClientBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLaporanKondisiArea.layoutManager = layoutManager

        binding.btnEnableChooseClientMeeting.setOnClickListener {
            onDataRoutineSentListener?.onDataRoutineSent(listClientRoutine)
            dismiss()
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.listClientResponse.observe(this) {
            if (it.code == 200) {
                if (it.data.isNotEmpty()) {
                    binding.tv1ChooseClientMeeting.visibility = View.VISIBLE
                    binding.rvLaporanKondisiArea.visibility = View.VISIBLE
                    binding.tvEmptyChooseClientMeeting.visibility = View.GONE

                    val rvAdapter = ChooseClientsRoutineAdapter(
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
            listClientRoutine.add(Data(clientId, clientName, clientEmail))
        } else {
            if (imageView.tag == "check") {
                imageView.tag = "uncheck"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_uncheckbox))
                listClientRoutine.remove(Data(clientId, clientName, clientEmail))
            } else {
                imageView.tag = "check"
                imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_checkbox))
                listClientRoutine.add(Data(clientId, clientName, clientEmail))
            }
        }

        // validate button choose
        if (listClientRoutine.isNotEmpty()) {
            binding.btnDisableChooseClientMeeting.visibility = View.GONE
            binding.btnEnableChooseClientMeeting.visibility = View.VISIBLE
        } else {
            binding.btnDisableChooseClientMeeting.visibility = View.VISIBLE
            binding.btnEnableChooseClientMeeting.visibility = View.INVISIBLE
        }
    }

    private fun loadData() {
        viewModel.getListClient(projectCode)
    }

    interface OnDataRoutineSetListener {
        fun onDataRoutineSent(dataList: ArrayList<Data>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDataRoutineSetListener) {
            onDataRoutineSentListener = context
        } else {
            throw RuntimeException("$context must implement OnDataSentRoutineListener")
        }
    }

}