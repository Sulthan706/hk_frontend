package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.adapter.operator

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ItemMonthlyWorkreportCardBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb.ContentListDateRkb
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.ui.activity.operator.PeriodicOperatorCalendarActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalendarPeriodicOperatorAdapter(val listCalendarPeridoc: ArrayList<ContentListDateRkb>):
RecyclerView.Adapter<ListCalendarPeriodicOperatorAdapter.ViewHolder>(){

    private lateinit var listClickDateRkb: PeriodicOperatorCalendarActivity
    private var dateSelected =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.DATE_FROM_MONTH_RKB, "")
    var selectedItem = -1

    inner class ViewHolder(val binding: ItemMonthlyWorkreportCardBinding):
            RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        override fun onClick(p0: View?) {
            val itemListCalendarRkb = listCalendarPeridoc[adapterPosition]
            listClickDateRkb.onClickDateRkb(
                itemListCalendarRkb.idJob
            )
        }
        init {
            itemView.setOnClickListener(this)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMonthlyWorkreportCardBinding.inflate(LayoutInflater.from(
                parent.context
            ), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCalendarPeridoc[position]
        when(item.typeJob){
            "DAILY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_skyblue)
            }
            "WEEKLY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_orange)
            }
            "MONTHLY" -> {
                holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_rounded_purple)
            }
        }

        holder.binding.tvTypeWorkRkb.text = item.detailJob
        holder.binding.tvTypeRkb.text = item.typeJob
        holder.binding.tvAreaItemRkb.text = item.locationName
        holder.binding.tvSubAreaItemRkb.text = item.subLocationName
        // validasi icon
        if (item.approved) {
            holder.binding.ivStampRkb.setImageResource(R.drawable.ic_stamp)
            holder.binding.ivStampRkb.visibility = View.VISIBLE
        } else if (!item.approved && item.done) {
            holder.binding.ivStampRkb.setImageResource(R.drawable.ic_stamp_disable)
            holder.binding.ivStampRkb.visibility = View.VISIBLE
        } else {
            holder.binding.ivStampRkb.visibility = View.GONE
        }


        if (item.done){
            holder.binding.ivChecklistRkb.apply {
                setImageResource(R.drawable.ic_checklist_radial)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivChecklistRkb.visibility = View.GONE
        }

        if (item.diverted){
            holder.binding.ivBaRkb.apply {
                setImageResource(R.drawable.ic_ba)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivBaRkb.visibility = View.GONE
        }

        if (item.statusPeriodic == "NOT_REALITATION"){
            holder.binding.ivRealizationRkb.apply {
                setImageResource(R.drawable.ic_realization_rkb)
                visibility = View.VISIBLE
            }
        } else {
            holder.binding.ivRealizationRkb.visibility = View.GONE
        }
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
//        val msg = sdf.format(Date())

        if (item.divertedTo != dateSelected && item.statusPeriodic == "DIVERSION"){
            holder.binding.clMainRkbCard.isEnabled = false
            holder.binding.cvDivertedValidation.visibility = View.VISIBLE
            holder.binding.tvTypeRkb.setBackgroundResource(R.drawable.bg_grey1)
            holder.binding.tvTypeWorkRkb.setTextColor(Color.GRAY)
            holder.binding.tvAreaItemRkb.setTextColor(Color.GRAY)
            holder.binding.tvSubAreaItemRkb.setTextColor(Color.GRAY)
            val date = convertDate(item.divertedTo)
            holder.binding.tvDateDivertedTo.text = date.ifEmpty { "" }
        } else {
            holder.binding.clMainRkbCard.isEnabled = true
            holder.binding.cvDivertedValidation.visibility = View.GONE
            holder.binding.tvTypeWorkRkb.setTextColor(Color.BLACK)
            holder.binding.tvAreaItemRkb.setTextColor(Color.BLACK)
            holder.binding.tvSubAreaItemRkb.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return listCalendarPeridoc.size
    }

    fun convertDate(inputDateStr: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id","ID"))

        try {
            val date = inputDateFormat.parse(inputDateStr)
            return outputDateFormat.format(date)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    fun setListener(periodicOperatorCalendarActivity: PeriodicOperatorCalendarActivity){
        this.listClickDateRkb = periodicOperatorCalendarActivity
    }

    interface ClickDateRkb {
        fun onClickDateRkb(idJob: Int)
    }

}