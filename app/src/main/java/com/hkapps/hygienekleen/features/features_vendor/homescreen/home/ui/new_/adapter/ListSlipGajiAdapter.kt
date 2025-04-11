package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemChooserBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listSlipGaji.Data

class ListSlipGajiAdapter(
    var listSlipGaji: ArrayList<Data>
) : RecyclerView.Adapter<ListSlipGajiAdapter.ViewHolder>() {

    var selectedItem = -1
    private lateinit var listSlipGajiCallBack: ListSlipGajiCallBack

    inner class ViewHolder(val binding: ListItemChooserBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val slipGaji = listSlipGaji[adapterPosition]
            listSlipGajiCallBack.onClickSlipGaji(
                slipGaji.projectCode,
                slipGaji.employeeId,
                slipGaji.month,
                slipGaji.year
            )

            selectedItem = adapterPosition
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemChooserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listSlipGaji[position]

        holder.binding.tvItemChooser.text = item.paycheckCode

        if (selectedItem == position) {
            holder.binding.ivSelectedChooser.visibility = View.VISIBLE
            holder.binding.ivDefaultChooser.visibility = View.GONE
        } else {
            holder.binding.ivSelectedChooser.visibility = View.GONE
            holder.binding.ivDefaultChooser.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return listSlipGaji.size
    }

    fun setListener(listSlipGajiCallBack: ListSlipGajiCallBack) {
        this.listSlipGajiCallBack = listSlipGajiCallBack
    }

    interface ListSlipGajiCallBack {
        fun onClickSlipGaji(
            projectCode: String,
            employeeId: Int,
            month: Int,
            year: Int
        )
    }

}


