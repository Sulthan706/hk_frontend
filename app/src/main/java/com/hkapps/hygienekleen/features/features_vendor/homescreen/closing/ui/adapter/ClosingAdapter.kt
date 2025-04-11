package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemDiversionBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.Diversion

class ClosingAdapter(
    val data : MutableList<Diversion>,
    val listener : OnClickDiversion
) : RecyclerView.Adapter<ClosingAdapter.ClosingViewHolder>()  {

    inner class ClosingViewHolder(val binding : ListItemDiversionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingViewHolder {
       return ClosingViewHolder(ListItemDiversionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ClosingViewHolder, position: Int) {
        holder.binding.apply {
            val areaItem = ": ${data[position].locationName}"
            val subLocation = ": ${data[position].subLocationName}"
            tvTypeWorkRkb.text = data[position].detailJob
            tvShift.text = data[position].shift
            tvAreaItemRkb.text = areaItem
            tvSubAreaItemRkb.text = subLocation
            tvWeekly.text = data[position].typeJob

            if(data[position].typeJob == "DAILY"){
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_skyblue)
            }else if(data[position].typeJob == "WEEKLY"){
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_orange)
            }else{
                tvWeekly.setBackgroundResource(R.drawable.bg_rounded_purple)
            }


            if(data[position].beforeImage != null){
                icImageBeforeFilled.visibility = View.VISIBLE
                icImageBeforeNotFilled.visibility = View.GONE
            }

            if(data[position].progressImage != null){
                icImageProgressFilled.visibility = View.VISIBLE
                icImageProgressNotFilled.visibility = View.GONE
            }

            if(data[position].afterImage != null){
                icImageAfterFilled.visibility = View.VISIBLE
                icImageAfterNotFilled.visibility = View.GONE
            }

            if(data[position].diverted){
                cardDetailDiversion.visibility = View.VISIBLE
                val divertedTo =  ": ${data[position].divertedTo ?: "-"}"
                val shiftDiversion = ": ${data[position].bashift ?: "-"}"
                tvDiversionFor.text = divertedTo
                tvShiftDiversion.text = shiftDiversion
                imgNext.setImageDrawable(ContextCompat.getDrawable(imgNext.context, R.drawable.ic_ba))

                cardDiversion.setOnClickListener {
                    listener.showToast()
                }

//                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                if(data[position].divertedTo != null){
//                   val givenDate = formatter.parse(data[position].divertedTo.toString())
//                   val today = Calendar.getInstance().apply {
//                       set(Calendar.HOUR_OF_DAY, 0)
//                       set(Calendar.MINUTE, 0)
//                       set(Calendar.SECOND, 0)
//                       set(Calendar.MILLISECOND, 0)
//                   }.time
//                   if(givenDate == today){
//                       cardDiversion.setOnClickListener {
//                           listener.showToast()
//                       }
//                   }else{
//
//                   }
//               }
            }else{
                card.setOnClickListener {
                  listener.onClickDetail(data[position])
                }
                cardDiversion.setOnClickListener {
                    listener.onDetailDiversion(data[position])
                }

                cardDetailDiversion.visibility = View.GONE
            }
        }
    }

    interface OnClickDiversion{

        fun onClickDetail(data : Diversion)
        fun onDetailDiversion(data : Diversion)

        fun showToast()


    }
}