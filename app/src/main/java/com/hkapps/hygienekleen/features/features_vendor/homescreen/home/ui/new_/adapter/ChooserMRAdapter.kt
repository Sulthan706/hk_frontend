package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserMrBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData

class ChooserMRAdapter(
    private val data : MutableList<ItemMRData>,
    private var selectedFilter : String,
    private val listener : OnItemRequestMRClickListener
): RecyclerView.Adapter<ChooserMRAdapter.ChooserMRAdapterViewHolder>() {

    inner class ChooserMRAdapterViewHolder(val binding : ListItemChooserMrBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooserMRAdapterViewHolder {
        return ChooserMRAdapterViewHolder(ListItemChooserMrBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ChooserMRAdapterViewHolder, position: Int) {
        holder.binding.apply {
            tvItemReasonResign.text = data[position].namaItem
            btnRadioReason.isChecked = data[position].namaItem == selectedFilter
            btnRadioReason.setOnClickListener {
                if (selectedFilter != data[position].namaItem) {
                    selectedFilter =  data[position].namaItem
                    listener.onItemRequestMRClick(data[position].idItem,data[position].namaItem,data[position].unitCode ?: "",data[position].quantity)
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun updateData(newData : List<ItemMRData>){
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }


    interface OnItemRequestMRClickListener{

        fun onItemRequestMRClick(idItem : Int, namaItem : String, unitCode : String,quantity : Int)
    }
}