package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemMrBinding
import com.hkapps.hygienekleen.databinding.TableListItemBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMr

class MrAdapter(
    var data: MutableList<ItemMr>,
    private var isMr : Boolean = false,
    private val isApprove: Boolean = false,
    private val move: (id : Int) -> Unit
) : RecyclerView.Adapter<MrAdapter.MrViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MrViewHolder {
        val binding = ListItemMrBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MrViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size + 1

    override fun onBindViewHolder(holder: MrViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition

        if (rowPos == 0) {
            // HEADER
            holder.binding.apply {
                setHeaderBg(rowNumber)
                setHeaderBg(tvKodeItem)
                setHeaderBg(tvNameItem)
                setHeaderBg(tvCount)
                setHeaderBg(tvUnit)

                rowNumber.text = "No"
                tvKodeItem.text = "Kode Item"
                tvNameItem.text = "Name Item"
                tvCount.text = "Quantity"
                tvUnit.text = "Satuan"
                tvUsed.text = "Penggunaan"
                tvReminder.text = "Sisa"
                oaHeader.text = "Approval"

                // aturan visibility
                if (isApprove) {
                    // tampil OA
                    setHeaderBg(oaContainer)

                    oaContainer.visibility = View.VISIBLE
                    oaHeader.visibility = View.VISIBLE
                    oaContent.visibility = View.GONE

                    // sembunyikan used/reminder
                    tvUsed.visibility = View.GONE
                    tvReminder.visibility = View.GONE
                } else {
                    if (isMr) {
                        // kalau mode MR → hide used/reminder
                        tvUsed.visibility = View.GONE
                        tvReminder.visibility = View.GONE
                        oaContainer.visibility = View.GONE
                    } else {
                        setHeaderBg(tvUsed)
                        setHeaderBg(tvReminder)
                        // kalau bukan MR → tampilkan used/reminder
                        tvUsed.visibility = View.VISIBLE
                        tvReminder.visibility = View.VISIBLE
                        oaContainer.visibility = View.GONE
                    }
                }

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

                // isi data
                rowNumber.text = item.rowNumber.toString()
                tvKodeItem.text = item.itemCode
                tvKodeItem.paintFlags = tvKodeItem.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                tvKodeItem.ellipsize = android.text.TextUtils.TruncateAt.END
                tvKodeItem.maxLines = 1

                tvCount.text = item.quantity.toString()
                tvUnit.text = item.unitCode
                tvNameItem.text = item.itemName

                // aturan visibility
                if (isApprove) {
                    // tampil OA
                    oaContainer.visibility = View.VISIBLE
                    oaHeader.visibility = View.GONE
                    oaContent.visibility = View.VISIBLE

                    // sembunyikan used/reminder
                    tvUsed.visibility = View.GONE
                    tvReminder.visibility = View.GONE
                } else {
                    if (isMr) {
                        // kalau mode MR → hide used/reminder
                        tvUsed.visibility = View.GONE
                        tvReminder.visibility = View.GONE
                        oaContainer.visibility = View.GONE
                    } else {
                        // kalau bukan MR → tampilkan used/reminder
                        tvUsed.visibility = View.VISIBLE
                        tvReminder.visibility = View.VISIBLE
                        tvUsed.text = item.used.toString()
                        tvReminder.text = item.reminder.toString()
                        oaContainer.visibility = View.GONE
                    }
                }

                btnCheckOa.setOnClickListener { move(data[rowPos - 1].idMaterialRequest) }


                oaDate.visibility = View.GONE
            }
        }
    }


    inner class MrViewHolder(val binding: ListItemMrBinding) :
        RecyclerView.ViewHolder(binding.root)

    // fungsi bantu untuk styling
    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }
}
