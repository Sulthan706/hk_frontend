package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusPenilaianDac.StatusDacResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import kotlin.collections.ArrayList

class DailyActivityTlAdapter(
    private val context: Context,
    var listDailyAct: ArrayList<DailyActivity>,
    var viewModel: ChecklistViewModel
) : RecyclerView.Adapter<DailyActivityTlAdapter.ViewHolder>() {

    private val View.lifecycleOwner get() = this.findViewTreeLifecycleOwner()

    private lateinit var dailyActCallBack: DailyActCallBack
    private lateinit var lifecycleOwner: LifecycleOwner
//    private val employeeId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.EMPLOYEE_ON_CHECKLIST, 0)
//    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
//    private val plottingId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.PLOTTING_ON_CHECKLIST, 0)
    private var activityId: Int = 0
    private val employeeId: Int = 6744
    private val projectCode: String = "CFHO"
    private val plottingId: Int = 1

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var tvTimeDaily: TextView = itemView.findViewById(R.id.tv_timeDaily)
        var tvActDaily: TextView = itemView.findViewById(R.id.tv_activityDaily)
        var penilaianDac: LinearLayout = itemView.findViewById(R.id.ll_penilaianDac)
        var barDac: RelativeLayout = itemView.findViewById(R.id.rv_barDac)
        var tvTime: TextView = itemView.findViewById(R.id.tv_checkTime)
        var ivCheck: ImageView = itemView.findViewById(R.id.iv_checkDac)
        var ivImage: ImageView = itemView.findViewById(R.id.iv_imageDac)
        var tvBeriNilai: TextView = itemView.findViewById(R.id.tv_beriNilai)

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            val getItem = listDailyAct[position]

            dailyActCallBack.onClickedDac(getItem.idSubLocationActivity)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_daily_activity, parent, false)
        parent.lifecycleOwner
        lifecycleOwner = parent.context as LifecycleOwner
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listDailyAct[position]
        activityId = response.idSubLocationActivity

        holder.tvActDaily.text = response.activity
        holder.tvTimeDaily.text = response.startAt + " - " + response.endAt

        viewModel.getStatusDac(employeeId, projectCode, plottingId, activityId)
        viewModel.statusDacResponseModel().observe(lifecycleOwner, Observer<StatusDacResponseModel> {
            if (it.code == 200) {
                val createdAt = it.data.createdAt
                val time = createdAt.subSequence(11, 16)

//                val dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
//                val timeCheck = LocalDateTime.parse(createdAt, dtf)
//
//                val hour = timeCheck.hour
//                val minute = timeCheck.minute
//                holder.tvTime.text = "$hour:$minute"

                holder.itemView.isEnabled = false
                holder.tvTime.text = time
                holder.penilaianDac.visibility = View.VISIBLE
                holder.ivImage.visibility = View.GONE
                holder.tvBeriNilai.visibility = View.GONE
                holder.barDac.setBackgroundResource(R.drawable.bg_dac_secondary)
            } else {
                holder.penilaianDac.visibility = View.GONE
                holder.tvBeriNilai.visibility = View.VISIBLE
                holder.barDac.setBackgroundResource(R.drawable.bg_dac_primary)
            }
        })

    }

    override fun getItemCount(): Int {
        return listDailyAct.size
    }

    fun setListener(dailyActCallback: DailyActCallBack) {
        this.dailyActCallBack = dailyActCallback
    }

    interface DailyActCallBack {
        fun onClickedDac(activityId: Int)
    }

}


