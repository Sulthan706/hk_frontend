package com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.adapter.midlevel

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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.service.permission.model.midlevel.ListPermissionResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.DetailPermissionActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.ui.teamleader.activity.OvertimeTeamleadActivity
import com.hkapps.hygienekleen.features.features_vendor.service.permission.ui.activity.midlevel.ListPermission

class ListPermissionAdapter(
    private val context: Context,
    var viewModel: PermissionViewModel,
    var listPermission: ArrayList<ListPermissionResponseModel>
) : RecyclerView.Adapter<ListPermissionAdapter.ViewHolder>() {

    private lateinit var listPermissionCallBack: ListPermissionCallBack
    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
    private val projectCode =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")

    private val View.lifecycleOwner get() = this.findViewTreeLifecycleOwner()
    private lateinit var lifecycleOwner: LifecycleOwner

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_nama_list_permission)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_title_list_permission)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date_list_permission)
        var tvShift: TextView = itemView.findViewById(R.id.tv_time_list_permission)
        var tvSubLoc: TextView = itemView.findViewById(R.id.tv_subLocation_list_permission)
        var tvLoc: TextView = itemView.findViewById(R.id.tv_location_list_permission)
        var ivPermission: ImageView = itemView.findViewById(R.id.iv_list_permission_)
        var tvIzinkan: TextView = itemView.findViewById(R.id.tv_izinkan)
        var tvTolak: TextView = itemView.findViewById(R.id.tv_tolak)
        var tvResultButton: TextView = itemView.findViewById(R.id.tv_result_button)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.ll_tolak_izin)
        var clPermission: ConstraintLayout = itemView.findViewById(R.id.cl_permission_mid)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getPermission = listPermission[adapterPosition]
            listPermissionCallBack.onClickedPermission(getPermission.permissionId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_permission_mid_level, parent, false)
        parent.lifecycleOwner
        lifecycleOwner = parent.context as LifecycleOwner
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listPermission[position]
        val status = response.statusPermission

        holder.tvName.text = response.employeeName
        holder.tvTitle.text = response.title

        if (response.startDate == response.endDate) {
            holder.tvDate.text = response.startDate
        } else {
            holder.tvDate.text = response.startDate + " - " + response.endDate
        }

        holder.tvLoc.visibility = View.GONE
        holder.tvShift.visibility = View.GONE
        holder.tvSubLoc.text = response.shiftDescription

        holder.clPermission.setOnClickListener {
            CarefastOperationPref.saveInt(
                CarefastOperationPrefConst.PERMISSION_ID,
                response.permissionEmployeeId
            )
            val i = Intent(context, DetailPermissionActivity::class.java)
            context.startActivity(i)
        }

        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/"
        context.let {
            Glide.with(it)
                .load(url + response.employeePhotoProfile)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.ivPermission)
        }

        val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        when (status) {
            "WAITING" -> {
                holder.linearLayout.visibility = View.VISIBLE
                holder.tvResultButton.visibility = View.GONE

                holder.tvTolak.setOnClickListener {
//                    val fragmentManager: FragmentManager =
//                        (context as FragmentActivity).supportFragmentManager
//                    val fragmentTransaction: FragmentTransaction =
//                        fragmentManager.beginTransaction()
//                    val dialog = DenialPermissionDialog()
//                    dialog.show(fragmentTransaction, "ChooseOperatorDialog")
//                    viewModel.putDenialOperatorNew(
//                        response.permissionEmployeeId
//                    )
//                    val a = Intent(context, ListPermission::class.java)
//                    context.startActivity(a)

                    showDialogDenial(response.permissionEmployeeId)
                }

                holder.tvIzinkan.setOnClickListener {
//                    CarefastOperationPref.saveString(
//                        CarefastOperationPrefConst.DATE_OPERATOR_PERMISSION,
//                        currentDate
//                    )
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.PERMISSION_ID,
//                        response.permissionId
//                    )
//
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_SHIFT_OPERATOR,
//                        response.shiftId
//                    )
//
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_DETAIL_PROJECT_OPERATOR,
//                        response.idDetailEmployeeProject
//                    )

                    //nyalakan ini kalo mau pilih operator
//                    val fragmentManager: FragmentManager =
//                        (context as FragmentActivity).supportFragmentManager
//                    val fragmentTransaction: FragmentTransaction =
//                        fragmentManager.beginTransaction()
//                    val dialog = ChooseOperatorPermissionDialog()
//                    dialog.show(fragmentTransaction, "ChooseOperatorDialog")
//

//                    viewModel.putSubmitOperatorNew(
//                        response.permissionEmployeeId,
//                        userId,
//                        projectCode
//                    )
//                    val a = Intent(context, ListPermission::class.java)
//                    context.startActivity(a)
//                    Toast.makeText(context, ""  + response.permissionId, Toast.LENGTH_SHORT).show()

                    showDialogAllow(response.permissionEmployeeId)
                }
            }
            "REFUSE" -> {
                holder.linearLayout.visibility = View.GONE
                holder.tvResultButton.visibility = View.VISIBLE

                holder.tvResultButton.text = "Permohonan ditolak"
                holder.tvResultButton.background =
                    ContextCompat.getDrawable(context, R.drawable.rounded_ditolak)
            }
            "ACCEPT" -> {
                holder.linearLayout.visibility = View.GONE
                holder.tvResultButton.visibility = View.VISIBLE

                holder.tvResultButton.text = "Permohonan disetujui"
                holder.tvResultButton.background =
                    ContextCompat.getDrawable(context, R.drawable.rounded_izinkan)
            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return listPermission.size
    }

    fun setListener(ListPermissionCallBack: ListPermissionCallBack) {
        this.listPermissionCallBack = ListPermissionCallBack
    }

    interface ListPermissionCallBack {
        fun onClickedPermission(permissionId: Int)
    }


    //pop up modal
    private fun showDialogAllow(permissionId: Int) {
        val dialog = let { Dialog(context) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putSubmitOperatorNew(
                permissionId,
                userId,
                projectCode
            )
            val a = Intent(context, OvertimeTeamleadActivity::class.java)
            context.startActivity(a)
            (context as Activity).finish()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun showDialogDenial(permissionId: Int) {
        val dialog = let { Dialog(context) }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_custom_layout_permission_denial)
        dialog.show()

        val yesBtn = dialog.findViewById(R.id.btn_yes) as AppCompatButton
        yesBtn.setOnClickListener {
            viewModel.putDenialOperatorNew(
                permissionId
            )
            val a = Intent(context, ListPermission::class.java)
            context.startActivity(a)
            (context as Activity).finish()
        }
        val noBtn = dialog.findViewById(R.id.btn_no) as AppCompatButton
        noBtn.setOnClickListener {
            dialog.cancel()
        }
    }
}