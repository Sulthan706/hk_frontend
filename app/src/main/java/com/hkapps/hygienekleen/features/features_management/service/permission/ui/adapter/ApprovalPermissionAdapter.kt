package com.hkapps.hygienekleen.features.features_management.service.permission.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListPermissionManagementBinding
import com.hkapps.hygienekleen.features.features_management.service.overtime.ui.activity.OvertimeManagementActivity
import com.hkapps.hygienekleen.features.features_management.service.permission.model.approvalPermissionManagement.Content
import com.hkapps.hygienekleen.features.features_management.service.permission.ui.activity.PermissionsApprovalManagementActivity
import com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel.PermissionManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ApprovalPermissionAdapter(
    private val context: Context,
    var viewModel: PermissionManagementViewModel,
    var listPermission: ArrayList<Content>
    ): RecyclerView.Adapter<ApprovalPermissionAdapter.ViewHolder>() {

    private val employeeApprovalId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    inner class ViewHolder(val binding: ListPermissionManagementBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPermissionManagementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listPermission[position]

        // set data
        holder.binding.tvNamaListPermission.text = response.employeeName
        holder.binding.tvTitleListPermission.text = response.title
        if (response.startDate == response.endDate) {
            holder.binding.tvDateListPermission.text = response.startDate
        } else {
            holder.binding.tvDateListPermission.text = "${response.startDate} - ${response.endDate}"
        }
        holder.binding.tvShiftListPermission.text = response.shiftDescription
        holder.binding.tvProjectNameListPermission.text = response.projectId

        // set photo employee
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/"
        Glide.with(context)
            .load(url + response.employeePhotoProfile)
            .apply(RequestOptions.fitCenterTransform())
            .into(holder.binding.ivListPermission)

        // validate layout by status
        when(response.statusPermission) {
            "WAITING" -> {
                holder.binding.llTolakIzin.visibility = View.VISIBLE
                holder.binding.tvResultButton.visibility = View.GONE

                holder.binding.tvTolak.setOnClickListener {
                    showDialogDenial(response.permissionEmployeeId)
                }
                holder.binding.tvIzinkan.setOnClickListener {
                    showDialogAllow(response.permissionEmployeeId)
                }
            }
            "REFUSE" -> {
                holder.binding.llTolakIzin.visibility = View.GONE
                holder.binding.tvResultButton.visibility = View.VISIBLE

                holder.binding.tvResultButton.text = "Permohonan ditolak"
                holder.binding.tvResultButton.setBackgroundResource(R.drawable.rounded_ditolak)
            }
            "ACCEPT" -> {
                holder.binding.llTolakIzin.visibility = View.GONE
                holder.binding.tvResultButton.visibility = View.VISIBLE

                holder.binding.tvResultButton.text = "Permohonan disetujui"
                holder.binding.tvResultButton.setBackgroundResource(R.drawable.rounded_izinkan)
            }
            else -> {
                holder.binding.llTolakIzin.visibility = View.GONE
                holder.binding.tvResultButton.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return listPermission.size
    }

    private fun showDialogAllow(permissionId: Int) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putAcceptPermission(permissionId, employeeApprovalId)
            val a = Intent(context, OvertimeManagementActivity::class.java)
            context.startActivity(a)
            (context as Activity).finish()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun showDialogDenial(permissionId: Int) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission_denial)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putDenyPermission(permissionId)
            val a = Intent(context, PermissionsApprovalManagementActivity::class.java)
            context.startActivity(a)
            (context as Activity).finish()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }
}