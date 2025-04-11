package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.chiefspv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorData
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel

class ListSupervisorAdapter(
    private val context: Context,
    var listSupervisor: ArrayList<SupervisorData>,
    private val viewModel: ShiftTimkuViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val projectId: String
) : RecyclerView.Adapter<ListSupervisorAdapter.ViewHolder>() {

    private lateinit var spvCallback: ListSpvCallback
//    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")


    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_nameLeaderMyTeam)
        var tvJob: TextView = itemView.findViewById(R.id.tv_jobLeaderMyTeam)
        var ivImageLeader: ImageView = itemView.findViewById(R.id.iv_imageLeaderMyteam)
        var rlStatus: RelativeLayout = itemView.findViewById(R.id.rl_statusLeaderMyTeam)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_statusLeaderMyTeam)
        var tvAbsent: TextView = itemView.findViewById(R.id.tv_countEmployeeLeaderMyTeam)
        var tvBelumAbsen: TextView = itemView.findViewById(R.id.tv_countBelumAbsenLeaderMyTeam)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val getSpv = listSupervisor[position]

            spvCallback.onClickSpv(getSpv.employeeId, getSpv.employeeName, getSpv.idShift, getSpv.projectCode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_leader_myteam, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listSupervisor[position]

        holder.tvName.text = response.employeeName
        holder.tvJob.text = response.jobName
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

        // set user image
        val img = response.employee_photo_profile
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource = context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.ivImageLeader.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.ivImageLeader)
        }

        // set count absent
        viewModel.getCountAbsentLeaderResponseModel().observe(lifecycleOwner) {
            if (it.code == 200) {
              if (response.employeeId == it.data.employeeId) {
                  holder.tvBelumAbsen.text = "${it.data.countEmployeeBelumAbsen}"
                  holder.tvAbsent.text = "${it.data.countEmployee}"
                }
            }
        }
        viewModel.getCountAbsentLeader(projectId, response.employeeId, response.idShift)
    }

    override fun getItemCount(): Int {
        return listSupervisor.size
    }

    interface ListSpvCallback{
        fun onClickSpv(spvId: Int, spvName: String, shiftId: Int, projectCode: String)
    }

    fun setListener(listSpvCallback: ListSpvCallback) {
        this.spvCallback = listSpvCallback
    }
}