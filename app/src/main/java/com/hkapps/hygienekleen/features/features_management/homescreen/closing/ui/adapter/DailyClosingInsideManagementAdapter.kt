package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemInsideDailyProgressBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyTargetManagement
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyClosingInsideManagementAdapter(
    private val data : List<DailyTargetManagement>,
    private val listener : DailyClosingManagementAdapter.OnClickDailyClosingManagement
) : RecyclerView.Adapter<DailyClosingInsideManagementAdapter.DailyClosingInsideManagementViewHolder>(){


    inner class DailyClosingInsideManagementViewHolder(val binding : ListItemInsideDailyProgressBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyClosingInsideManagementViewHolder {
        return DailyClosingInsideManagementViewHolder(ListItemInsideDailyProgressBinding.inflate(
            LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DailyClosingInsideManagementViewHolder, position: Int) {
       holder.binding.apply {
           tvDate.text = data[position].date
           tvStatusClosing.text = data[position].closingStatus.uppercase()
           if(data[position].totalTarget != 0){
               if(data[position].closingStatus.equals("closed",ignoreCase = true) && data[position].fileGenerated && data[position].emailSent){
                   btnHistory.visibility = View.VISIBLE
                   btnClosing.visibility = View.GONE
                   btnHistory.setOnClickListener {
                       listener.onClickHistory(data[position])
                   }
                   tvStatusClosing.setTextColor(ContextCompat.getColor(tvStatusClosing.context, R.color.green))
               }else{
                   btnClosing.visibility = View.VISIBLE
                   btnHistory.visibility = View.GONE
                   btnClosing.setOnClickListener {
                       listener.onCLickDetail(data[position],isYesterday(data[position].date))
                   }
                   tvStatusClosing.setTextColor(ContextCompat.getColor(tvStatusClosing.context, R.color.red))
               }
           }else{
               tvStatusClosing.text = "Closed"
               btnHistory.visibility = View.VISIBLE
               btnClosing.visibility = View.GONE
               btnHistory.setOnClickListener {
                   listener.onClickHistory(data[position])
               }
               tvStatusClosing.setTextColor(ContextCompat.getColor(tvStatusClosing.context, R.color.green))
           }
       }
    }

    fun isYesterday(dateString: String, format: String = "dd MMMM yyyy"): Boolean {
        val sdf = SimpleDateFormat(format, Locale("id", "ID"))

        val date = sdf.parse(dateString)

        val today = Calendar.getInstance()

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        return sdf.format(date) == sdf.format(yesterday.time)
    }



}