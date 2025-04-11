package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemMrBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMr

class MrAdapter(
    val data : MutableList<ItemMr>
):RecyclerView.Adapter<MrAdapter.MrViewHolder>() {

    inner class MrViewHolder(val binding : ListItemMrBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MrViewHolder {
        return MrViewHolder(ListItemMrBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MrViewHolder, position: Int) {
        holder.binding.apply {
            tvNo.text = data[position].rowNumber.toString()
            tvQuantity.text = data[position].quantity.toString()
            tvItemCode.text = data[position].itemCode
            tvItemName.text = data[position].itemName
            tvUnitCode.text = data[position].unitCode
        }
    }
}