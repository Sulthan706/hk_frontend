package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.DataOperator
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class ListOperatorAdapter(
    private val context: Context,
    var listOperator: ArrayList<DataOperator>,
    private val viewModel: ShiftTimkuViewModel,
    private val shiftId: Int
)
    : RecyclerView.Adapter<ListOperatorAdapter.ViewHolder>(), LifecycleOwner {

    private lateinit var listOperatorCallback: ListOperatorCallBack
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var notAbsent: Int = 0
    private var absent: Int = 0
    private lateinit var lifecycleOwner: LifecycleOwner

    private val View.lifecycleOwner get() = this.findViewTreeLifecycleOwner()

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var ivFoto: ImageView = itemView.findViewById(R.id.iv_fotoUserTimku)
        var tvNama: TextView = itemView.findViewById(R.id.tv_userNameOperator)
        var tvArea: TextView = itemView.findViewById(R.id.tv_areaOperator)
        var tvSubArea: TextView = itemView.findViewById(R.id.tv_subAreaOperator)
        var rlStatus: RelativeLayout = itemView.findViewById(R.id.rl_statusOperatorMyTeam)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_statusOperatorMyTeam)
        var tvAbsent: TextView = itemView.findViewById(R.id.tv_countAbsentOperatorMyTeam)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val getOperator = listOperator[position]
//            listOperatorCallback.onClickItem(getOperator.employeeName, getOperator.locationName, getOperator.subLocationName)
//            Toast.makeText(
//                context,
//                "" + getOperator,
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parent.lifecycleOwner
        lifecycleOwner = parent.context as LifecycleOwner
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_anggota_timku, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listOperator[position]

        holder.tvNama.text = response.employeeName
        holder.tvArea.text = response.locationName
        holder.tvSubArea.text = response.subLocationName
        if (response.statusAttendance == "" || response.statusAttendance == null || response.statusAttendance == "null") {
            holder.rlStatus.visibility = View.GONE
        } else {
            when(response.statusAttendance) {
                "BELUM_ABSEN" -> {
                    holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
                    holder.tvStatus.text = "Belum Absen"
                }
                "SEDANG_BEKERJA" -> {
                    holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_success)
                    holder.tvStatus.text = "Sudah Absen"
                }
                "HADIR" -> {
                    holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
                    holder.tvStatus.text = "Selesai Bekerja"
                }
            }
        }

        // set user images
        val img = response.employee_photo_profile
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource = context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.ivFoto.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.ivFoto)
        }

        // set count absent
        viewModel.getCountAbsentOperator(projectCode, response.employeeId, shiftId)
        viewModel.getCountAbsentOprResponseModel().observe(lifecycleOwner) {
            if (it.code == 200) {
                holder.tvAbsent.text = "${it.data.countEmployeeBelumAbsen} / ${it.data.countEmployee}"
            }
        }
    }

    override fun getItemCount(): Int {
        return listOperator.size
    }

    fun setListener (listOperatorCallBack: ListOperatorCallBack) {
        this.listOperatorCallback = listOperatorCallBack
    }

    interface ListOperatorCallBack {
        fun onClickItem(userName: String, userArea: String, userSubArea: String)
    }

    override val lifecycle: Lifecycle
        get() = TODO("Not yet implemented")

}