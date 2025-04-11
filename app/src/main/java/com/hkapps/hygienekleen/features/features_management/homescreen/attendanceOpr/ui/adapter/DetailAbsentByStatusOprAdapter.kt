package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyActHomeBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.Data

class DetailAbsentByStatusOprAdapter(
    var detailByStatus: ArrayList<Data>
): RecyclerView.Adapter<DetailAbsentByStatusOprAdapter.ViewHolder>() {

    private lateinit var absentByStatusCallback: AbsentByStatusCallBack
    private var context: Context? = null

    inner class ViewHolder(val binding: ItemAttendanceDailyActHomeBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val getAbsent = detailByStatus[adapterPosition]
            absentByStatusCallback.onClickAbsentByStatus(getAbsent.idEmployee, getAbsent.idProject, getAbsent.idDetailEmployeeProject)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemAttendanceDailyActHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(detailByStatus[position]){
                val response = detailByStatus[position]
                
//                binding.cardDacHome.setOnClickListener {
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_DETAIL_PROJECT,
//                        response.idDetailEmployeeProject
//                    )
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_PLOTTING,
//                        response.idPlotting
//                    )
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_SHIFT,
//                        response.idShift
//                    )
//                    CarefastOperationPref.saveInt(
//                        CarefastOperationPrefConst.ID_LOCATION,
//                        response.locationId
//                    )
//                    CarefastOperationPref.saveString(
//                        CarefastOperationPrefConst.DATE_SCH,
//                        response.date
//                    )
////                    val i = Intent(context, ProfileOperationalActivity::class.java)
////                    context?.startActivity(i)
//                }

                when (response.shiftDescription) {
                    "Shift Pagi" -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
                    }
                    "Shift Siang" -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
                    }
                    "Shift Malam" -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
                    }
                    "Week Days (Hari Sabtu dan Hari Minggu Office)" -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        binding.tvDailyHomeShift.text = "Week Days"
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
                    }
                    "Libur" -> {
                        binding.tvDailyHomeArea.text = " "
                        binding.tvDailyHomeSubArea.text = " "
                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = " "
                    }
                    else -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
                    }
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return detailByStatus.size
    }

    fun setListener(absentByStatusCallback: AbsentByStatusCallBack) {
        this.absentByStatusCallback = absentByStatusCallback
    }

    interface AbsentByStatusCallBack {
        fun onClickAbsentByStatus(employeeId: Int, projectId: String, idDetailEmployeeProject: Int)
    }
}