package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemMrBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ListHistoryUsedData

class HistoryUsedMRAdapter(
    var data: MutableList<ListHistoryUsedData>,
) : RecyclerView.Adapter<HistoryUsedMRAdapter.HistoryUsedMRViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryUsedMRViewHolder {
        val binding = ListItemMrBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryUsedMRViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size + 1

    override fun onBindViewHolder(holder: HistoryUsedMRViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition

        if (rowPos == 0) {
            // HEADER
            holder.binding.apply {
                setHeaderBg(rowNumber)
                setHeaderBg(tvKodeItem)
                setHeaderBg(tvNameItem)
                setHeaderBg(tvCount)
                setHeaderBg(tvUnit)
                setHeaderBg(tvUsed)

                rowNumber.text = "No"
                tvKodeItem.text = "Nama Item"
                tvNameItem.text = "Nama Pengguna"
                tvCount.text = "Quantity Item"
                tvUnit.text = "Satuan"
                tvUsed.text = "Tanggal Digunakan"
                tvReminder.visibility = View.GONE
                oaContainer.visibility = View.GONE
                tvKodeItem.setTextColor(ContextCompat.getColor(tvKodeItem.context, R.color.black))
                oaHeader.setTextColor(ContextCompat.getColor(oaHeader.context, R.color.black))
            }
        } else {
            // ROW DATA
            val item = data[rowPos - 1]
            holder.binding.apply {
                setContentBg(rowNumber)
                setContentBg(tvKodeItem)
                setContentBg(tvNameItem)
                setContentBg(tvCount)
                setContentBg(tvUnit)
                setContentBg(tvUsed)

                // isi data
                rowNumber.text = rowPos.toString()
                tvKodeItem.text = item.namaItem
                tvKodeItem.paintFlags = tvKodeItem.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                tvKodeItem.ellipsize = android.text.TextUtils.TruncateAt.END
                tvKodeItem.maxLines = 2

                tvCount.text = item.quantityItem.toString()
                tvUnit.text = item.namaSatuan
                tvNameItem.text = item.namaPengguna
                tvUsed.text = item.tanggalDigunakan

                tvReminder.visibility = View.GONE
                oaContainer.visibility = View.GONE
                oaDate.visibility = View.GONE
            }
        }
    }


    inner class HistoryUsedMRViewHolder(val binding: ListItemMrBinding) :
        RecyclerView.ViewHolder(binding.root)

    // fungsi bantu untuk styling
    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }
}
