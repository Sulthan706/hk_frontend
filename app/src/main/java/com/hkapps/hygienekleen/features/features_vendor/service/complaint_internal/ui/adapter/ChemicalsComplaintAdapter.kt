package com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.model.chemicalsComplaintInternal.Data

class ChemicalsComplaintAdapter(
    var listChemical: ArrayList<Data>
): RecyclerView.Adapter<ChemicalsComplaintAdapter.ViewHolder>() {

    private lateinit var listChemicalsCallBack: ListChemicalsCallBack
    var selectedItem = -1

    inner class ViewHolder(val binding: ListItemChooserBinding):
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val selected: String

            if (binding.ivDefaultChooser.visibility == View.VISIBLE) {
                selected = "true"
                binding.ivSelectedChooser.visibility = View.VISIBLE
                binding.ivDefaultChooser.visibility = View.GONE
            } else {
                selected = "false"
                binding.ivSelectedChooser.visibility = View.GONE
                binding.ivDefaultChooser.visibility = View.VISIBLE
            }

            listChemicalsCallBack.onClickChemical(
                listChemical[adapterPosition].chemicalId,
                listChemical[adapterPosition].chemicalName,
                selected
            )

            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemChooserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvItemChooser.text = listChemical[position].chemicalName
    }

    override fun getItemCount(): Int {
        return listChemical.size
    }

    fun setListener (listChemicalsCallBack: ListChemicalsCallBack) {
        this.listChemicalsCallBack = listChemicalsCallBack
    }

    interface ListChemicalsCallBack {
        fun onClickChemical(chemicalId: Int, chemicalName: String, selected: String)
    }
}