package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceDailyActHomeBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.ui.activity.DacNewActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DailyActDataArrayHomeResponseModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst


class HomeDacAdapter(
    private var dailyActDataArrayHomeResponseModel: ArrayList<DailyActDataArrayHomeResponseModel>,
) :
    RecyclerView.Adapter<HomeDacAdapter.ViewHolder>() {
    private var context: Context? = null
    private val UNSELECTED = -1
    private var selectedItem = UNSELECTED
    private val level =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_LEVEL_POSITION, "")

    private val statusAttendanceFirst =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_FIRST, "")

    private val statusAttendanceSecond =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.STATUS_ABSEN_SECOND, "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAttendanceDailyActHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            with(dailyActDataArrayHomeResponseModel[position]) {
                val response = dailyActDataArrayHomeResponseModel[position]

                if (level == "Operator" || level == "OPERATOR"){
                    binding.llArea.visibility = View.VISIBLE
                }else{
                    binding.llArea.visibility = View.GONE
                }
                CarefastOperationPref.saveString(CarefastOperationPrefConst.TIMEOUT_TIME, response.endAt)
                binding.cardDacHome.setOnClickListener {
//                    Toast.makeText(context,response.idDetailEmployeeProject.toString(),Toast.LENGTH_SHORT).show()
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.ID_DETAIL_PROJECT,
                        response.idDetailEmployeeProject
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.ID_PLOTTING,
                        response.idPlotting
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.ID_SHIFT,
                        response.idShift
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.ID_LOCATION,
                        response.locationId
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.DATE_SCH,
                        response.date
                    )
                    if (level == "Operator" || level == "OPERATOR"){
                        if (statusAttendanceFirst == "BERTUGAS" || statusAttendanceSecond == "BERTUGAS"){
                            val i = Intent(context, DacNewActivity::class.java)
                            context?.startActivity(i)
                        }else{

                            Toast.makeText(context,"Anda belum melakukan absen masuk.",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        //do nothing
                    }
                }

                when (response.shiftDescription) {
//                    "Shift Pagi" -> {
//                        binding.tvDailyHomeArea.text = " " + response.locationName
//            //                binding.tvDailyHomeArea.text = "Area1 Gudanggggggggggggggggg"
//
//                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
//            //                binding.tvDailyHomeSubArea.text = "SubArea1 Lantai 1111111111"
//                        binding.tvDailyHomeShift.text = response.shiftDescription
//                        binding.tvDailyHomeDate.text = response.date
//                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
//                    }
//                    "Shift Siang" -> {
//                        binding.tvDailyHomeArea.text = " " + response.locationName
//            //                binding.tvDailyHomeArea.text = "Area1 Gudanggggggggggggggggg"
//
//                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
//            //                binding.tvDailyHomeSubArea.text = "SubArea1 Lantai 1111111111"
//                        binding.tvDailyHomeShift.text = response.shiftDescription
//                        binding.tvDailyHomeDate.text = response.date
//                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
//                    }
//                    "Shift Malam" -> {
//                        binding.tvDailyHomeArea.text = " " + response.locationName
//            //                binding.tvDailyHomeArea.text = "Area1 Gudanggggggggggggggggg"
//
//                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
//            //                binding.tvDailyHomeSubArea.text = "SubArea1 Lantai 1111111111"
//                        binding.tvDailyHomeShift.text = response.shiftDescription
//                        binding.tvDailyHomeDate.text = response.date
//                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt
//                    }
                    "Week Days (Hari Sabtu dan Hari Minggu Office)" -> {
                        binding.tvDailyHomeArea.text = " " + response.locationName
                        //                binding.tvDailyHomeArea.text = "Area1 Gudanggggggggggggggggg"

                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName
                        //                binding.tvDailyHomeSubArea.text = "SubArea1 Lantai 1111111111"
                        binding.tvDailyHomeShift.text = "Week Days"
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt

                    }
                    "Libur" -> {
                        binding.tvDailyHomeArea.text = " "
                        binding.tvDailyHomeSubArea.text = " "
//                        binding.tvDailyHomeShift.text = response.shiftDescription
                        binding.tvDailyHomeDate.text = response.date
                        binding.tvDailyHomeTime.text = " "
                        when(response.scheduleType){
                            "ACTUAL SCHEDULE" ->{
                                binding.tvDailyHomeShift.text = "Actual | " + response.shiftDescription
                            }
                            "LEMBUR GANTI" -> {
                                binding.tvDailyHomeShift.text = "Lembur | " + response.shiftDescription
                            }
                        }
                    }
                    else -> {
//                        binding.tvDailyHomeArea.text = " "
//                        binding.tvDailyHomeSubArea.text = " "
//                        binding.tvDailyHomeShift.text = response.shiftDescription
//                        binding.tvDailyHomeDate.text = response.date
//                        binding.tvDailyHomeTime.text = " "

                        binding.tvDailyHomeArea.text = " " + response.locationName
                        binding.tvDailyHomeSubArea.text = " " + response.subLocationName

                        binding.tvDailyHomeDate.text = response.date

                        binding.tvDailyHomeTime.text = response.startAt + " - " + response.endAt

                        when(response.scheduleType){
                            "ACTUAL SCHEDULE" ->{
                                binding.tvDailyHomeShift.text = "Actual | " + response.shiftDescription
                            }
                            "LEMBUR GANTI" -> {
                                binding.tvDailyHomeShift.text = "Lembur | " + response.shiftDescription
                            }
                        }

                    }
                }
            }

        }
//        val rotateUp = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
//        rotateUp.duration = 300
//        rotateUp.fillAfter = true
//
//        val rotateDown = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
//        rotateDown.duration = 300
//        rotateDown.fillAfter = true
//
//        holder.binding.cardDacHome.setOnClickListener {
//            if (holder.binding.clContentHide.visibility == View.VISIBLE) {
//                TransitionManager.beginDelayedTransition(holder.binding.cardDacHome)
//                // card is expanded, collapse it
//                holder.binding.clContentHide.visibility = View.GONE
//                holder.binding.ivChevronDownDac.startAnimation(rotateDown)
//            } else {
//                TransitionManager.beginDelayedTransition(holder.binding.cardDacHome)
//                // card is collapsed, expand it
//                holder.binding.clContentHide.visibility = View.VISIBLE
//                holder.binding.ivChevronDownDac.startAnimation(rotateUp)
//            }
//        }


    }

    override fun getItemCount(): Int {
        return dailyActDataArrayHomeResponseModel.size
    }

    inner class ViewHolder(val binding: ItemAttendanceDailyActHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

}