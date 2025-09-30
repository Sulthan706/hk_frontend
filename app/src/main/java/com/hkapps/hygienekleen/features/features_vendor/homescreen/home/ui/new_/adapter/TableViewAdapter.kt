package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MRDashboardData


class TableViewAdapter(
    val data : MutableList<MRDashboardData>,
    val move : (Int,Int) -> Unit
    ) : RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.bindingAdapterPosition

        if (rowPos == 0) {
            // Header Cells
            holder.apply {
                setHeaderBg(rowNumber)
                setHeaderBg(mrCode)
                setHeaderBg(mrMonth)
                setHeaderBg(mrYear)
                setHeaderBg(mrDate)
                setHeaderBg(oaContainer)
                setHeaderBg(opsContainer)

                rowNumber.text = "No"
                mrCode.text = "Kode MR"
                mrMonth.text = "Bulan"
                mrYear.text = "Tahun"
                mrDate.text = "Tanggal"


                rowNumber.setTextColor(ContextCompat.getColor(rowNumber.context, R.color.black))
                mrCode.setTextColor(ContextCompat.getColor(mrCode.context, R.color.black))
                mrMonth.setTextColor(ContextCompat.getColor(mrMonth.context, R.color.black))
                mrYear.setTextColor(ContextCompat.getColor(mrYear.context, R.color.black))
                mrDate.setTextColor(ContextCompat.getColor(mrDate.context, R.color.black))

                oaHeader.visibility = View.VISIBLE
                oaContent.visibility = View.GONE
                oaHeader.setTextColor(ContextCompat.getColor(oaHeader.context, R.color.black))


                opsHeader.visibility = View.VISIBLE
                opsContent.visibility = View.GONE
                opsHeader.setTextColor(ContextCompat.getColor(opsHeader.context, R.color.black))
            }
        } else {
            // Data Rows
            val modal = data[rowPos - 1]

            holder.apply {
                setContentBg(rowNumber)
                setContentBg(mrCode)
                setContentBg(mrMonth)
                setContentBg(mrYear)
                setContentBg(mrDate)
                setContentBg(oaContainer)
                setContentBg(opsContainer)

                mrCode.paintFlags = mrCode.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                mrCode.setOnClickListener {
                    move(modal.month,modal.year)
                }

                // Set data
                rowNumber.text = "${modal.rowNumber}"
                mrCode.text = modal.kodeMaterialRequest
                mrCode.ellipsize = android.text.TextUtils.TruncateAt.END
                mrCode.maxLines = 1

                mrMonth.text = modal.month.toString()
                mrYear.text = modal.year.toString()
                mrDate.text = modal.date

                oaHeader.visibility = View.GONE
                oaContent.visibility = View.VISIBLE

                // Set OA button color and state based on completion
                if (modal.approveOa != null) {
                    btnCheckOa.backgroundTintList = ContextCompat.getColorStateList(btnCheckOa.context, R.color.light_orange)
                    btnCheckOa.setIconResource(R.drawable.ic_check)
                    oaDate.text = modal.approveOa
                    oaDate.visibility = View.VISIBLE
                } else {
                    btnCheckOa.backgroundTintList = ContextCompat.getColorStateList(btnCheckOa.context, R.color.grayLine)
                    btnCheckOa.setIconResource(R.drawable.ic_check)
                    oaDate.text = ""
                    oaDate.visibility = View.GONE
                }

                opsHeader.visibility = View.GONE
                opsContent.visibility = View.VISIBLE

                if (modal.approveOps != null) {
                    btnCheckOps.backgroundTintList =
                        ContextCompat.getColorStateList(btnCheckOps.context, R.color.green)
                    btnCheckOps.setIconResource(R.drawable.ic_check)
                    opsDate.text = modal.approveOps
                    opsDate.visibility = View.VISIBLE
                } else {
                    btnCheckOps.backgroundTintList =
                        ContextCompat.getColorStateList(btnCheckOps.context, R.color.grayLine)
                    btnCheckOps.setIconResource(R.drawable.ic_check)
                    opsDate.text = ""
                    opsDate.visibility = View.GONE
                }

//                btnCheckOa.setOnClickListener {
//                    onOaClickListener?.invoke(modal, rowPos - 1)
//                }
//
//                btnCheckOps.setOnClickListener {
//                    onOpsClickListener?.invoke(modal, rowPos - 1)
//                }

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowNumber: TextView = itemView.findViewById(R.id.row_number)
        val mrCode: TextView = itemView.findViewById(R.id.mr_kode)
        val mrMonth: TextView = itemView.findViewById(R.id.mr_month)
        val mrYear: TextView = itemView.findViewById(R.id.mr_year)
        val mrDate: TextView = itemView.findViewById(R.id.mr_date)

        // OA elements
        val oaContainer: LinearLayout = itemView.findViewById(R.id.oa_container)
        val oaHeader: TextView = itemView.findViewById(R.id.oa_header)
        val oaContent: LinearLayout = itemView.findViewById(R.id.oa_content)
        val btnCheckOa: MaterialButton = itemView.findViewById(R.id.btn_check_oa)
        val oaDate: TextView = itemView.findViewById(R.id.oa_date)

        // Ops element
        val opsContainer: LinearLayout = itemView.findViewById(R.id.ops_container)
        val opsHeader: TextView = itemView.findViewById(R.id.ops_header)
        val opsContent: LinearLayout = itemView.findViewById(R.id.ops_content)
        val btnCheckOps: MaterialButton = itemView.findViewById(R.id.btn_check_ops)
        val opsDate: TextView = itemView.findViewById(R.id.ops_date)
    }

    interface OnClickTableView{
        fun onClickApproveOA()
        fun onClickApproveOPS()
    }
}