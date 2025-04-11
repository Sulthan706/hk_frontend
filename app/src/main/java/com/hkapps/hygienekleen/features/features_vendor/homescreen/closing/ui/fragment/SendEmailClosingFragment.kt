package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentSendEmailClosingBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosing
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.EmailSendClosing
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter.ClientSendClosingAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListShiftChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.ChooseClientClosingActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.PdfViewSalaryActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar


class SendEmailClosingFragment : Fragment(), ClientSendClosingAdapter.ClientsSendMailCallback {


    private lateinit var binding : FragmentSendEmailClosingBinding

    private var listClient = ArrayList<ClientClosing>()

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private var to = listOf<EmailSendClosing>()

    private var cc = listOf<EmailSendClosing>()

    private var loadingDialog: Dialog? = null

    private var flag = 1

    private val jobLevel = CarefastOperationPref.loadString(CarefastOperationPrefConst.JOB_LEVEL,"")

    private var projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private val closingViewModel by lazy {
        ViewModelProvider(this)[ClosingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendEmailClosingBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDialogSuccessSubmit()
        loadData()
        submitClosing()
        binding.relativeEmailTo.setOnClickListener {
            bottomDialogParticipantMeeting(false)
        }
        binding.relativeEmailCc.setOnClickListener {
            bottomDialogParticipantMeeting(true)
        }
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedDataList = result.data?.getParcelableArrayListExtra<ClientClosing>("selectedDataList")

                selectedDataList?.let { dataList ->
                    listClient.addAll(dataList)
                    listClient = ArrayList(listClient.distinct())
                    val rvAdapter = ClientSendClosingAdapter(
                        requireContext(),
                        listClient
                    ).also { it.setListener(this) }
                    binding.rvClient.adapter = rvAdapter
                    binding.rvClient.layoutManager = LinearLayoutManager(requireContext())

                    // validate button submit
                    if (listClient.isEmpty()) {
                        binding.btnSendEmailEnabled.visibility = View.INVISIBLE
                        binding.btnSendEmailDisabled.visibility = View.VISIBLE
                    } else {
                        binding.btnSendEmailEnabled.visibility = View.VISIBLE
                        binding.btnSendEmailDisabled.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun submitClosing(){
        binding.btnSendEmailEnabled.setOnClickListener {
            to = listClient
                .filter { it.clientName.contains("to:", ignoreCase = true) }
                .map {
                    val cleanedName = it.clientName.replace("to:", "", ignoreCase = true).trim()
                    EmailSendClosing(name = cleanedName, email = it.email)
                }

            cc = listClient.filter { it.clientName.contains("cc:", ignoreCase = true) }
                .map { val cleanedName = it.clientName.replace("cc:", "", ignoreCase = true).trim()
                    EmailSendClosing(name = cleanedName, email = it.email) }

            if(to.isNotEmpty()){
                Log.d("TESTEDX","$to $cc")
                if(jobLevel.contains("chief",ignoreCase = true)){
                    closingViewModel.sendEmailClosing(
                        SendEmailClosingRequest(projectCode, getYesterdayDate(), userId, to, cc))
                    closingViewModel.sendEmailClosingModel.observe(viewLifecycleOwner){
                        if(it.code == 200){
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,true)
                            startActivity(Intent(requireContext(), ListShiftChecklistActivity::class.java))
                            activity?.finishAffinity()
                        }else{
                            Toast.makeText(requireContext(), "Gagal closing", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    closingViewModel.sendEmailClosingSPV(
                        SendEmailClosingRequest(projectCode, getYesterdayDate(), userId, to, cc))
                    closingViewModel.sendEmailClosingSPVModel.observe(viewLifecycleOwner){
                        if(it.code == 200){
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.SUCCESS_CLOSING_DIALOG,true)
                            startActivity(Intent(requireContext(), ListShiftChecklistActivity::class.java))
                            activity?.finishAffinity()
                        }else{
                            Toast.makeText(requireContext(), "Gagal closing", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(requireContext(), "Isi email to terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun loadData(){
        closingViewModel.getDailyTargetChief(projectCode,getYesterdayDate(),userId)
        closingViewModel.detailDailyTargetChief.observe(viewLifecycleOwner){ response ->
            if(response.code == 200){
                binding.tvDate.text = response.data.closedAt
                binding.tvPengawas.text = response.data.closedBy
                binding.tvSeeFile.setOnClickListener {
                    startActivity(Intent(requireActivity(),PdfViewSalaryActivity::class.java).also{
                        it.putExtra("file","https://ops.hk-indonesia.id/hksite/assets/rkbreport/${response.data.file}")
                    })
                }
            }else{
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bottomDialogParticipantMeeting(isCC : Boolean) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_choose_partisipan)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomSheetChoosePartisipan)
        val buttonChoose = dialog.findViewById<AppCompatButton>(R.id.btnClientChoosePartisipan)
        val buttonManual = dialog.findViewById<AppCompatButton>(R.id.btnManualChoosePartisipan)

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        buttonChoose?.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(context, ChooseClientClosingActivity::class.java)
            if(!isCC){
                intent.putExtra("emailFor","to")
                intent.putExtra("projectCode",projectCode)
            }else{
                intent.putExtra("emailFor","CC")
                intent.putExtra("projectCode",projectCode)
            }
            resultLauncher.launch(intent)
        }

        buttonManual?.setOnClickListener {
            bottomDialogInputParticipant(isCC)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun bottomDialogInputParticipant(isCC: Boolean) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.bottom_sheets_input_partisipan)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivCloseBottomInputPartisipan)
        val etName = dialog.findViewById<AppCompatEditText>(R.id.etNameBottomInputPartisipan)
        val etMail = dialog.findViewById<AppCompatEditText>(R.id.etMailBottomInputPartisipan)
        val tvErrorEmail = dialog.findViewById<TextView>(R.id.tvErrorEmailBottomInputPartisipan)
        val button = dialog.findViewById<AppCompatButton>(R.id.btnSubmitBottomInputPartisipan)

        ivClose?.setOnClickListener {
            dialog.dismiss()
        }

        etMail?.addTextChangedListener {
            tvErrorEmail?.visibility = View.GONE
        }

        val length = listClient.size
        button?.setOnClickListener {
            if (Patterns.EMAIL_ADDRESS.matcher(etMail?.text.toString()).matches()) {
                tvErrorEmail?.visibility = View.GONE

                val newName = if (!isCC) "to: ${etName?.text.toString()}" else "cc: ${etName?.text.toString()}"
                val newEmail = etMail?.text.toString()


                val isDuplicate = listClient.any { it.clientName == newName && it.email == newEmail }

                if (!isDuplicate) {
                    listClient.add(
                        ClientClosing(
                            clientId = length + 1,
                            clientName = newName,
                            levelJabatan = "",
                            projectCode = "",
                            email = newEmail,
                            photoProfile = "",
                            status = "",
                            project = ""
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Client dengan nama dan email yang sama sudah ada.", Toast.LENGTH_SHORT).show()
                }
                onResume()

                dialog.dismiss()
            } else {
                tvErrorEmail?.visibility = View.VISIBLE
            }

        }

        dialog.show()
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.layout_dialog_success_generate_file)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btn_next_send_email)
        btnOke?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogDelete(idClient: Int, clientName: String, clientEmail: String) {
        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_warning_meeting)
        val textView = dialog.findViewById<TextView>(R.id.tvDialogWarningMeeting)
        val btnBack = dialog.findViewById<AppCompatButton>(R.id.btnBackDialogWarningMeeting)
        val btnDelete = dialog.findViewById<AppCompatButton>(R.id.btnYesDialogWarningMeeting)

        textView?.text = "Hapus email \n $clientEmail?"
        btnBack?.setOnClickListener {
            dialog.dismiss()
        }
        btnDelete?.setOnClickListener {
            listClient.removeAll { it.clientId == idClient && it.clientName == clientName && it.email == clientEmail }
            onResume()

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()

        val rvAdapter = ClientSendClosingAdapter(
            requireContext(),
            listClient
        ).also { it.setListener(this) }
        binding.rvClient.adapter = rvAdapter
        binding.rvClient.layoutManager = LinearLayoutManager(requireContext())

        rvAdapter.notifyDataSetChanged()
        // validate button submit
        if (listClient.isEmpty()) {
            binding.btnSendEmailEnabled.visibility = View.GONE
            binding.btnSendEmailDisabled.visibility = View.VISIBLE
        } else {
            binding.btnSendEmailEnabled.visibility = View.VISIBLE
            binding.btnSendEmailDisabled.visibility = View.GONE
        }
    }


    override fun onClickClients(idClient: Int, clientName: String, clientEmail: String) {
        showDialogDelete(idClient, clientName, clientEmail)
    }
}