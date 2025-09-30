package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserMrBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData

class ChooseSatuanAdapter(
    private val data : MutableList<SatuanData>,
    private var selectedFilter : String,
    private val listener : OnItemRequestMRClickListener
): RecyclerView.Adapter<ChooseSatuanAdapter.ChooserSatuanAdapterViewHolder>() {

    inner class ChooserSatuanAdapterViewHolder(val binding : ListItemChooserMrBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooserSatuanAdapterViewHolder {
        return ChooserSatuanAdapterViewHolder(ListItemChooserMrBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChooserSatuanAdapterViewHolder, position: Int) {
        holder.binding.apply {
            tvItemReasonResign.text = data[position].namaSatuan
            btnRadioReason.isChecked = data[position].namaSatuan == selectedFilter
            btnRadioReason.setOnClickListener {
                if (selectedFilter != data[position].namaSatuan) {
                    selectedFilter =  data[position].namaSatuan
                    listener.onItemRequestMRClick(data[position].idSatuan,data[position].namaSatuan)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun updateData(newData : List<SatuanData>){
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }


    interface OnItemRequestMRClickListener{

        fun onItemRequestMRClick(idItem : Int, namaItem : String)
    }
}