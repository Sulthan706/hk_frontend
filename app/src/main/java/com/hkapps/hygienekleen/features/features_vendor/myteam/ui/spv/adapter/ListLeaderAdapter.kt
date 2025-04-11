package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.DataEmployee
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel

class ListLeaderAdapter(
    private val context: Context,
    var listLeader: ArrayList<DataEmployee>,
    private val viewModel: ShiftTimkuViewModel,
    private val shiftId: Int,
    private val projectId: String,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ListLeaderAdapter.ViewHolder>() {

    private lateinit var leaderCallback: ListLeaderCallback
//    private val projectId =
//        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var workerId: Int = 0
    private var belumAbsen: Int = 0
    private var semuaAbsen: Int = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_nameLeaderMyTeam)
        var tvJob: TextView = itemView.findViewById(R.id.tv_jobLeaderMyTeam)
        var ivImageLeader: ImageView = itemView.findViewById(R.id.iv_imageLeaderMyteam)
        var rlStatus: RelativeLayout = itemView.findViewById(R.id.rl_statusLeaderMyTeam)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_statusLeaderMyTeam)
        var tvAbsent: TextView = itemView.findViewById(R.id.tv_countEmployeeLeaderMyTeam)
        var tvBelumAbsen: TextView = itemView.findViewById(R.id.tv_countBelumAbsenLeaderMyTeam)
        var llCountAbsent: LinearLayout = itemView.findViewById(R.id.ll_countBelumAbsen)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val getLeader = listLeader[position]

            leaderCallback.onClickLeader(
                getLeader.employeeId,
                getLeader.employeeName,
                getLeader.idShift,
                getLeader.projectCode
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_leader_myteam, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(listLeader[position]) {
                val response = listLeader[position]

                holder.tvName.text = response.employeeName
                holder.tvJob.text = response.jobName
//        when(response.statusAttendance) {
//            "BELUM_ABSEN" -> {
//                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_primary)
//                holder.tvStatus.text = "Belum Absen"
//            }
//            "SEDANG_BEKERJA" -> {
//                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_success)
//                holder.tvStatus.text = "Sudah Absen"
//            }
//            "HADIR" -> {
//                holder.rlStatus.setBackgroundResource(R.drawable.bg_status_history_complaint_secondary)
//                holder.tvStatus.text = "Selesai Bekerja"
//            }
//        }

                // set user image
                val img = response.employee_photo_profile
                val url =
                    context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

                if (img == "null" || img == null || img == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource =
                        context.resources.getIdentifier(uri, null, context.packageName)
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

                llCountAbsent.visibility = View.GONE
//                // set count absent
//                viewModel.getCountAbsentOprResponseModel().observe(lifecycleOwner) {
//                    if (it.code == 200) {
//                        if (response.employeeId == it.data.employeeId) {
//                            holder.tvBelumAbsen.text = "${it.data.countEmployeeBelumAbsen}"
//                            holder.tvAbsent.text = "${it.data.countEmployee}"
//                        }
//                    }
//                }
//                viewModel.getCountAbsentOperator(projectId, response.employeeId, shiftId)

            }
        }
    }

    override fun getItemCount(): Int {
        return listLeader.size
    }

    fun setListener(listLeaderCallback: ListLeaderCallback) {
        this.leaderCallback = listLeaderCallback
    }

    interface ListLeaderCallback {
        fun onClickLeader(leaderId: Int, leaderName: String, shiftId: Int, projectId: String)
    }

}



