package com.hkapps.hygienekleen.features.features_vendor.service.approval.ui.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListAttendanceApprovalBinding
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.Content
import com.hkapps.hygienekleen.features.features_vendor.service.approval.viewmodel.ApprovalViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListAttendanceApprovalAdapter(
    private val context: Context,
    var listAttendance: ArrayList<Content>,
    var viewModel: ApprovalViewModel,
    val clickFrom: String
    ) : RecyclerView.Adapter<ListAttendanceApprovalAdapter.ViewHolder>() {

    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    inner class ViewHolder(val binding: ListAttendanceApprovalBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListAttendanceApprovalBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listAttendance[position]

        // set user image
        val imgClient = response.employeeImages
        val urlClient = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$imgClient"
        if (imgClient == "null" || imgClient == null || imgClient == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivListAttendanceApproval.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)
                .error(R.drawable.ic_error_image)

            Glide.with(context)
                .load(urlClient)
                .apply(requestOptions)
                .into(holder.binding.ivListAttendanceApproval)
        }

        // set data
        holder.binding.tvNamaListAttendanceApproval.text = response.employeeName
        holder.binding.tvNucJobListAttendanceApproval.text = "${response.employeeCode} | ${response.jobCode}"
        holder.binding.tvShiftListAttendanceApproval.text = response.shift
        holder.binding.tvTimeListAttendanceApproval.text = "${response.shiftStartAt} - ${response.shiftEndAt}"
        holder.binding.tvDescListAttendanceApproval.text = response.description
        holder.binding.tvTimeCreatedListAttendanceApproval.text = response.createdAt
        when (response.typeReport) {
            "ATTENDANCE_IN" -> {
                holder.binding.tvStatusInListAttendanceApproval.visibility = View.VISIBLE
                holder.binding.tvStatusOutListAttendanceApproval.visibility = View.GONE
                holder.binding.tvRequestListAttendanceApproval.text = "Request : Absen masuk di luar area"
            }
            "ATTENDANCE_OUT" -> {
                holder.binding.tvStatusInListAttendanceApproval.visibility = View.GONE
                holder.binding.tvStatusOutListAttendanceApproval.visibility = View.VISIBLE
                holder.binding.tvRequestListAttendanceApproval.text = "Request : Absen keluar di luar area"
            }
        }

        // set on click button
        holder.binding.btnDeclineListAttendanceApproval.setOnClickListener {
            showDialogWarning(response.idUserFlying, "decline", response.typeReport)
        }
        holder.binding.btnAcceptListAttendanceApproval.setOnClickListener {
            showDialogWarning(response.idUserFlying, "accept", response.typeReport)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showDialogWarning(idUserFlying: Int, warning: String, reportType: String) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_warning_decline_accept)

        val ivDecline = dialog.findViewById(R.id.ivDeclineWarning) as ImageView
        val ivAccept = dialog.findViewById(R.id.ivAcceptWarning) as ImageView
        val tvDecline = dialog.findViewById(R.id.tvDeclineWarning) as TextView
        val tvAccept = dialog.findViewById(R.id.tvAcceptWarning) as TextView
        val tvInfo = dialog.findViewById(R.id.tvInfoWarning) as TextView
        val btnBack = dialog.findViewById(R.id.btnBackWarning) as AppCompatButton
        val btnYes = dialog.findViewById(R.id.btnYesWarning) as AppCompatButton

        when(warning) {
            "decline" -> {
                ivDecline.visibility = View.VISIBLE
                ivAccept.visibility = View.GONE
                tvDecline.text = "Yakin ingin menolak permintaan?"
                tvDecline.visibility = View.VISIBLE
                tvAccept.visibility = View.GONE
                tvInfo.text = "Jika Ya, maka status permintaan tersebut akan ditolak"
                btnBack.setOnClickListener {
                    dialog.dismiss()
                }
                btnYes.setOnClickListener {
                    when(clickFrom) {
                        "management" -> viewModel.submitDeclineAttendanceManagement(userId, idUserFlying)
                        "leaders" -> viewModel.submitDeclineAttendance(userId, idUserFlying)
                    }
                    dialog.dismiss()
                }
            }
            "accept" -> {
                ivAccept.visibility = View.VISIBLE
                ivDecline.visibility = View.GONE
                tvAccept.text = "Yakin ingin menyetujui permintaan?"
                tvAccept.visibility = View.VISIBLE
                tvDecline.visibility = View.GONE
                tvInfo.text = "Jika Ya, maka status permintaan tersebut akan disetujui"
                btnBack.setOnClickListener {
                    dialog.dismiss()
                }
                btnYes.setOnClickListener {
                    when (reportType) {
                        "ATTENDANCE_IN" -> {
                            when(clickFrom) {
                                "management" -> viewModel.submitApproveAttendanceInManagement(userId, idUserFlying)
                                "leaders" -> viewModel.submitApproveAttendanceIn(userId, idUserFlying)
                            }
                            dialog.dismiss()
                        }
                        "ATTENDANCE_OUT" -> {
                            when(clickFrom) {
                                "management" -> viewModel.submitApproveAttendanceOutManagement(userId, idUserFlying)
                                "leaders" -> viewModel.submitApproveAttendanceOut(userId, idUserFlying)
                            }
                            dialog.dismiss()
                        }
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    override fun getItemCount(): Int {
        return listAttendance.size
    }
}