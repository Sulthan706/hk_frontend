package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySendRoutineReportBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity.InspeksiMainActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.adapter.ClientsSendRoutineAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.ui.fragment.BotDialogClientsRoutineFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel.RoutineViewModel
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SendRoutineReportActivity : AppCompatActivity(), ClientsSendRoutineAdapter.ClientsSendMailCallback, BotDialogClientsRoutineFragment.OnDataRoutineSetListener {

    private lateinit var binding: ActivitySendRoutineReportBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
    private var title = CarefastOperationPref.loadString(CarefastOperationPrefConst.TOPIC_ROUTINE, "")
    private var description = CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTE_ROUTINE, "")
    private var date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_ROUTINE, "")
    private var imgDescriptions = CarefastOperationPref.loadString(CarefastOperationPrefConst.IMG_DESC_ROUTINE,"")

    private var listClient = ArrayList<Data>()
    private var loadingDialog: Dialog? = null
    private var flag = 1
    private var sizeSelectedClient = 0
    private var emailParticipant = ArrayList<String>()
    private var nameParticipant = ArrayList<String>()

    private val viewModel: RoutineViewModel by lazy {
        ViewModelProviders.of(this).get(RoutineViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendRoutineReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get list client from chooseClientRoutineActivity
        if (clickFrom == "chooseClient") {
            listClient = intent.getParcelableArrayListExtra("SelectedClients")
                ?: ArrayList()

            sizeSelectedClient = intent.getIntExtra("sizeSelectedClient", 0)
        }

        // set appbar
        binding.appbarSendRoutineReport.tvAppbarTitle.text = "Kirim Laporan Kunjungan"
        binding.appbarSendRoutineReport.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSendRoutineReport.layoutManager = layoutManager

        // set recyclerview adapter
        binding.rvSendRoutineReport.visibility = View.VISIBLE
        val rvAdapter = ClientsSendRoutineAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendRoutineReport.adapter = rvAdapter

        // set on click add report recipient
        binding.rlSendRoutineReport.setOnClickListener {
            bottomDialogReportRecipient()
        }

        // validate button submit
        if (listClient.isEmpty()) {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.VISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.GONE
        }

        binding.btnSubmitEnableSendRoutineReport.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableSendRoutineReport.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.submitFormRoutineResponse.observe(this) {
            if (it.status == "SUCCESS") {
                hideLoading()
                showDialogSuccessSubmit()
            } else {
                hideLoading()
                flag = 1
                binding.btnSubmitEnableSendRoutineReport.isEnabled = true
                Toast.makeText(this, "Gagal submit form rutin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialogSuccessSubmit() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog_custom_success_upload_document)
        val btnOke = dialog.findViewById<AppCompatButton>(R.id.btnOkeDialogSuccessUploadDocument)

        btnOke?.setOnClickListener {
            dialog.dismiss()
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, mClick)
            startActivity(Intent(this, InspeksiMainActivity::class.java))
            finish()
        }

        dialog.show()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        // get clients email
        val length = listClient.size
        for (i in 0 until length) {
            emailParticipant.add(listClient[i].clientEmail)
            nameParticipant.add(listClient[i].clientName)
        }

        val uri = intent.getParcelableExtra<Uri>("imageUriRoutine")

        if (uri != null) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            Log.d("agri","$image")

            viewModel.submitFormRoutine(
                userId,
                projectCode,
                title,
                description,
                date,
                image,
                imgDescriptions,
                emailParticipant
            )
        } else {
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "file",
                    "null",
                    RequestBody.create(MultipartBody.FORM, "")
                )

            viewModel.submitFormRoutine(
                userId,
                projectCode,
                title,
                description,
                date,
                image,
                imgDescriptions,
                emailParticipant
            )
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun createTempFiles(bitmap: Bitmap): File? {
        val file = File(
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "_" + "photoDcument.jpeg"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun bottomDialogReportRecipient() {
        val dialog = BottomSheetDialog(this)
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
//            startActivity(Intent(this, ChooseClientMeetingActivity::class.java))
            BotDialogClientsRoutineFragment().show(supportFragmentManager,"botsheetclient")
        }

        buttonManual?.setOnClickListener {
            bottomDialogInputPartisipan()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun bottomDialogInputPartisipan() {
        val dialog = BottomSheetDialog(this)
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

                listClient.add(
                    Data(
                        length + 1,
                        etName?.text.toString(),
                        etMail?.text.toString()
                    )
                )
                onResume()

                dialog.dismiss()
            } else {
                tvErrorEmail?.visibility = View.VISIBLE
            }

        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()

        val rvAdapter = ClientsSendRoutineAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendRoutineReport.adapter = rvAdapter

        // validate button submit
        if (listClient.isEmpty()) {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.VISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.GONE
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onClickClientsRoutine(idClient: Int, clientName: String, clientEmail: String) {
        showDialogDelete(idClient, clientName, clientEmail)
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogDelete(idClient: Int, clientName: String, clientEmail: String) {
        val dialog = Dialog(this)
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
            listClient.remove(
                Data(
                    idClient,
                    clientName,
                    clientEmail
                )
            )
            onResume()

            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDataRoutineSent(dataList: ArrayList<Data>) {
        if (listClient.size != 0) {
            val length = dataList.size
            for (i in 0 until length) {
                listClient.add(
                    Data(
                        dataList[i].clientId+listClient.size,
                        dataList[i].clientName,
                        dataList[i].clientEmail
                    )
                )
            }
        } else {
            listClient = dataList
        }

        binding.rvSendRoutineReport.visibility = View.VISIBLE

        val rvAdapter = ClientsSendRoutineAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendRoutineReport.adapter = rvAdapter

        // validate button submit
        if (dataList.isEmpty()) {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendRoutineReport.visibility = View.VISIBLE
            binding.btnSubmitDisableSendRoutineReport.visibility = View.GONE
        }
        binding.btnSubmitEnableSendRoutineReport.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableSendRoutineReport.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }
    }

}