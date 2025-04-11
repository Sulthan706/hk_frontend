package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.ui.adapter.lowlevel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemScheduleCalendarLowBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.DataArrayContent
import java.text.SimpleDateFormat
import java.util.*


class ScheduleAdapter(
    private var context: Context? = null,
    var schContent: ArrayList<DataArrayContent>
) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private lateinit var scheduleCallback: ScheduleCallback


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemScheduleCalendarLowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = schContent[position]

        if (response.scheduleType == "LEMBUR GANTI") {
            holder.binding.tvSchShift.text = "LEMBUR GANTI"
            context?.resources?.let { holder.binding.tvSchDay.setTextColor(it.getColor(R.color.secondary_color)) }
            context?.resources?.let { holder.binding.tvSchMonth.setTextColor(it.getColor(R.color.secondary_color)) }
            holder.binding.tvSchTime.visibility = View.VISIBLE
            holder.binding.tvSchArea.visibility = View.VISIBLE
            holder.binding.tvSchSubArea.visibility = View.VISIBLE


            holder.binding.tvSchArea.text = response.locationName
            holder.binding.tvSchSubArea.text = response.subLocationName
            holder.binding.tvSchTime.text = response.startAt + " - " + response.endAt
            context?.resources?.let {
                holder.binding.llSchCalendar.setBackgroundColor(
                    it.getColor(
                        R.color.lembur
                    )
                )
            }
        } else {
            holder.binding.tvSchShift.text = response.shiftDescription

            if (response.shiftDescription == "Libur") {
                holder.binding.tvSchTime.visibility = View.INVISIBLE
                holder.binding.tvSchArea.visibility = View.INVISIBLE
                holder.binding.tvSchSubArea.visibility = View.INVISIBLE

                holder.binding.tvSchTime.text = ""
                holder.binding.tvSchArea.text = ""
                holder.binding.tvSchSubArea.text = ""
                context?.resources?.let { holder.binding.tvSchDay.setTextColor(it.getColor(R.color.redTxt)) }
                context?.resources?.let { holder.binding.tvSchMonth.setTextColor(it.getColor(R.color.redTxt)) }
                context?.resources?.let {
                    holder.binding.llSchCalendar.setBackgroundColor(
                        it.getColor(
                            R.color.secondary_color
                        )
                    )
                }
            } else {
                context?.resources?.let { holder.binding.tvSchDay.setTextColor(it.getColor(R.color.secondary_color)) }
                context?.resources?.let { holder.binding.tvSchMonth.setTextColor(it.getColor(R.color.secondary_color)) }
                holder.binding.tvSchTime.visibility = View.VISIBLE
                holder.binding.tvSchArea.visibility = View.VISIBLE
                holder.binding.tvSchSubArea.visibility = View.VISIBLE


                holder.binding.tvSchArea.text = response.locationName
                holder.binding.tvSchSubArea.text = response.subLocationName
                holder.binding.tvSchTime.text = response.startAt + " - " + response.endAt
                context?.resources?.let {
                    holder.binding.llSchCalendar.setBackgroundColor(
                        it.getColor(
                            R.color.actual
                        )
                    )
                }
            }
        }


        val sdfIn = SimpleDateFormat("dd-MM-yyyy")
        val sdfOut = SimpleDateFormat("dd")
        val input = response.date
        val date = sdfIn.parse(input)


        val sdfIn2 = SimpleDateFormat("dd-MM-yyyy")
        val sdfOut2 = SimpleDateFormat("MMM")
        val input2 = response.date
        val date2 = sdfIn2.parse(input2)

        holder.binding.tvSchDay.text = sdfOut.format(date)
        holder.binding.tvSchMonth.text = sdfOut2.format(date2)
    }

    override fun getItemCount(): Int {
        return schContent.size
    }

    inner class ViewHolder(val binding: ItemScheduleCalendarLowBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var position = adapterPosition
            var schedule = schContent[position]
            scheduleCallback.onClickSch(
                schedule.idDetailEmployeeProject,
                schedule.idProject,
                position,
                schedule.scheduleType
            )
        }
    }

    fun setListener(scheduleCallback: ScheduleCallback) {
        this.scheduleCallback = scheduleCallback
    }

    interface ScheduleCallback {
        fun onClickSch(scheduleId: Int, targetId: String, position: Int, type: String)
    }
}