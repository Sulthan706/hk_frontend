package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivitySendLaporanMeetingBinding
import com.hkapps.hygienekleen.databinding.DialogCustomUploadingDokumenBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.Data
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.adapter.ClientsSendMeetingAdapter
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.meeting.fragment.BotSheetChooseClientFragment
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SendLaporanMeetingActivity : AppCompatActivity(), ClientsSendMeetingAdapter.ClientsSendMailCallback, BotSheetChooseClientFragment.OnDataSentListener {

    private lateinit var binding: ActivitySendLaporanMeetingBinding
    private val clickFrom = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLICK_FROM, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private var projectCode = ""
    private var listClient = ArrayList<Data>()
    private var loadingDialog: Dialog? = null
    private var flag = 1
    private var title = CarefastOperationPref.loadString(CarefastOperationPrefConst.TOPIC_MEETING, "")
    private var description = CarefastOperationPref.loadString(CarefastOperationPrefConst.NOTE_MEETING, "")
    private var date = CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_MEETING, "")
    private var startTime = CarefastOperationPref.loadString(CarefastOperationPrefConst.START_TIME_MEETING, "")
    private var endTime = CarefastOperationPref.loadString(CarefastOperationPrefConst.END_TIME_MEETING, "")
    private val mClick = CarefastOperationPref.loadString(CarefastOperationPrefConst.M_CLICK_FROM, "")
    private var fileDescriptions = CarefastOperationPref.loadString(CarefastOperationPrefConst.DESC_MEETING,"")
    private var imgUriUpload = CarefastOperationPref.loadString(CarefastOperationPrefConst.IMG_URI_MEETING, "")
    private var sizeSelectedClient = 0
    private var emailParticipant = ArrayList<String>()
    private var nameParticipant = ArrayList<String>()

    private val viewModel: InspeksiViewModel by lazy {
        ViewModelProviders.of(this).get(InspeksiViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendLaporanMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get list client from ChooseClientMeetingActivity
        when (clickFrom) {
            "mainInspeksi" -> {
                projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
            }
            "listMeeting" -> {
                projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_MEETING, "")
            }
            "chooseClient" -> {
                listClient = intent.getParcelableArrayListExtra("SelectedClients")
                    ?: ArrayList()

                sizeSelectedClient = intent.getIntExtra("sizeSelectedClient", 0)
                Log.d("SendLaporanMeeting", "onCreate: list client selected = $listClient")
                Log.d("SendLaporanMeeting", "onCreate: size selected client = $sizeSelectedClient")
            }
        }

        val uri = intent.getParcelableExtra<Uri>("imageUriInspeksi")
        Log.d("agrikece","$uri")

        // set appbar
        binding.appbarSendLaporanMeeting.tvAppbarTitle.text = "Kirim Laporan Meeting"
        binding.appbarSendLaporanMeeting.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSendLaporanMeeting.layoutManager = layoutManager

        // set recyclerview adapter
        binding.rvSendLaporanMeeting.visibility = View.VISIBLE
        val rvAdapter = ClientsSendMeetingAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendLaporanMeeting.adapter = rvAdapter

        // set on click add participant
        binding.rlSendLaporanMeeting.setOnClickListener {
            bottomDialogPartisipanMeeting()
        }

        // validate button submit
        if (listClient.isEmpty()) {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.VISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.GONE
        }

        binding.btnSubmitEnableSendLaporanMeeting.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableSendLaporanMeeting.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }
        Log.d("saduk", "oncraetet$listClient")

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
        viewModel.submitFormMeetingResponse.observe(this) {
            if (it.status == "SUCCESS") {
                hideLoading()
                showDialogSuccessSubmit()
            } else {
                hideLoading()
                flag = 1
                binding.btnSubmitEnableSendLaporanMeeting.isEnabled = true
                Toast.makeText(this, "Gagal submit form meeting", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bottomDialogPartisipanMeeting() {
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
            BotSheetChooseClientFragment().show(supportFragmentManager,"botsheetclient")
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

                listClient.add(Data(length + 1, etName?.text.toString(), etMail?.text.toString()))
                onResume()

                dialog.dismiss()
            } else {
                tvErrorEmail?.visibility = View.VISIBLE
            }

        }

        dialog.show()
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
            listClient.remove(Data(idClient, clientName, clientEmail))
            onResume()

            dialog.dismiss()
        }

        dialog.show()
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
            CarefastOperationPref.saveString(CarefastOperationPrefConst.CLICK_FROM, mClick)
            startActivity(Intent(this, ListMeetingInspeksiActivity::class.java))
            finish()
        }

        dialog.show()
    }

    private fun showUploadingDialog(
        context: Context,
        cancelable: Boolean = true
    ): Dialog {
        val binding = DialogCustomUploadingDokumenBinding.inflate(LayoutInflater.from(context))
        val progressDialog = Dialog(context)
        progressDialog.setContentView(binding.root)

        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setCancelable(cancelable)
            it.setCanceledOnTouchOutside(false)

//            //set timer cuma 1.5 detik buat loadingnya
//            val progressRunnable = Runnable { it.cancel() }
//            val pdCanceller = Handler()
//            pdCanceller.postDelayed(progressRunnable, 1500)

            return it
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()

        val rvAdapter = ClientsSendMeetingAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendLaporanMeeting.adapter = rvAdapter

        // validate button submit
        if (listClient.isEmpty()) {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.VISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.GONE
        }
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

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingDialog(this, loadingText)

        val uri = intent.getParcelableExtra<Uri>("imageUriInspeksi")

        if (uri != null) {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val file = createTempFiles(bitmap)
            val reqFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
            val image: MultipartBody.Part =
                MultipartBody.Part.createFormData("file", file?.name, reqFile!!)
            Log.d("agri","$image")

            // get clients email
            val length = listClient.size
            for (i in 0 until length) {
                emailParticipant.add(listClient[i].clientEmail)
                nameParticipant.add(listClient[i].clientName)
            }

            // get project code
            val projectId = when (mClick) {
                "listMeeting" -> CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_MEETING, "")
                "mainInspeksi" -> CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_CODE_LAST_VISIT, "")
                else -> ""
            }

            viewModel.submitFormMeeting(
                userId,
                projectId,
                title,
                description,
                date,
                startTime,
                endTime,
                image,
                fileDescriptions,
                emailParticipant,
                nameParticipant
            )
        } else {
            Log.e("agri", "Uri is null, unable to process the image.")
            // Handle the case where uri is null, perhaps show an error message to the user.
        }



    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    override fun onClickClients(idClient: Int, clientName: String, clientEmail: String) {
        showDialogDelete(idClient, clientName, clientEmail)
    }

    override fun onDataSent(dataList: ArrayList<Data>) {
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
        binding.rvSendLaporanMeeting.visibility = View.VISIBLE

        val rvAdapter = ClientsSendMeetingAdapter(
            this,
            listClient
        ).also { it.setListener(this) }
        binding.rvSendLaporanMeeting.adapter = rvAdapter

        // validate button submit
        if (dataList.isEmpty()) {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.INVISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.VISIBLE
        } else {
            binding.btnSubmitEnableSendLaporanMeeting.visibility = View.VISIBLE
            binding.btnSubmitDisableSendLaporanMeeting.visibility = View.GONE
        }
        binding.btnSubmitEnableSendLaporanMeeting.setOnClickListener {
            if (flag == 1) {
                binding.btnSubmitEnableSendLaporanMeeting.isEnabled = false
                showLoading(getString(R.string.loading_string2))
            }
            flag = 0
        }

        Log.d("saduk","$dataList")
    }

}