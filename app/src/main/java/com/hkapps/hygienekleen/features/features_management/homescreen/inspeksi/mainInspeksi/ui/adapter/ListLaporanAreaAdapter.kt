package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListKondisiAreaInspeksiBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.Content

class ListLaporanAreaAdapter(
    private val context: Context,
    var listLaporanArea: ArrayList<Content>,
    private val listFrom: String,
    private val viewModel: AuditViewModel,
    private val viewLifecycle: LifecycleOwner
): RecyclerView.Adapter<ListLaporanAreaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListKondisiAreaInspeksiBinding):
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListKondisiAreaInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listLaporanArea[position]

        // set visibility spinner
        if (listFrom == "laporanKondisiArea") {
            holder.binding.spinnerListKondisiAreaInspeksi.visibility = View.VISIBLE
            holder.binding.tvPenilaianListKondisiAreaInspeksi.visibility = View.GONE
        } else {
            holder.binding.spinnerListKondisiAreaInspeksi.visibility = View.GONE
            holder.binding.tvPenilaianListKondisiAreaInspeksi.visibility = View.VISIBLE
        }

        holder.binding.tvAreaListKondisiAreaInspeksi.text = response.areaName
        holder.binding.tvObjectListKondisiAreaInspeksi.text = response.objectName

        // set spinner
        val objectValue = context.resources.getStringArray(R.array.penilaianInspeksi)
        val spinnerAdapter = ArrayAdapter(context, R.layout.spinner_item, objectValue)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        holder.binding.spinnerListKondisiAreaInspeksi.adapter = spinnerAdapter
        holder.binding.spinnerListKondisiAreaInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, long: Long) {
                val score = when (objectValue[position]) {
                    "Melebihi Ekspektasi/Ekselen" -> 4
                    "Sangat Baik" -> 3
                    "Standar" -> 2
                    "Jelek/Kotor" -> 1
                    "Sangat Jelek/Kotor/Rusak" -> 0
                    else -> -1
                }
                holder.binding.tvScoreListKondisiAreaInspeksi.text = "$score"
                holder.binding.tvActionListKondisiAreaInspeksi.text = when (score) {
                    4, 3 -> ": Dipertahankan"
                    2 -> ": Harus ditingkatkan"
                    0, 1 -> ": Perlu perbaikan/tindakan"
                    else -> ": -"
                }
                holder.binding.tvTindakanListKondisiAreaInspeksi.text = when (score) {
                    4, 3, 2 -> ": -"
                    1 -> ": Tindakan segera"
                    0 -> ": Tindakan sangat segera"
                    else -> ": -"
                }

                viewModel.updateHasilKerjaAudit(response.idHasilKerja, score)
                viewModel.updateHasilKerjaResponse.observe(viewLifecycle) {
                    if (it.code == 200) {
                        Toast.makeText(context, "Berhasil update nilai", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Gagal update nilai", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        val scoreSelection = when(response.score) {
            4 -> 0
            3 -> 1
            2 -> 2
            1 -> 3
            0 -> 4
            else -> -1
        }
        holder.binding.spinnerListKondisiAreaInspeksi.setSelection(scoreSelection)

        holder.binding.tvPenilaianListKondisiAreaInspeksi.text = when (response.score) {
            4 -> "Melebihi Ekspektasi/Ekselen"
            3 -> "Sangat Baik"
            2 -> "Standar"
            1 -> "Jelek/Kotor"
            0 -> "Sangat Jelek/Kotor/Rusak"
            else -> "error"
        }
        holder.binding.tvScoreListKondisiAreaInspeksi.text = response.score.toString()
        holder.binding.tvActionListKondisiAreaInspeksi.text = when (response.score) {
            4, 3 -> ": Dipertahankan"
            2 -> ": Harus ditingkatkan"
            0, 1 -> ": Perlu perbaikan/tindakan"
            else -> ": -"
        }
        holder.binding.tvTindakanListKondisiAreaInspeksi.text = when (response.score) {
            4, 3, 2 -> ": -"
            1 -> ": Tindakan segera"
            0 -> ": Tindakan sangat segera"
            else -> ": -"
        }
        holder.binding.tvNoteListKondisiAreaInspeksi.text = if (response.description == "" ||
            response.description == "null" || response.description == null) {
            ": -"
        } else {
            ": ${response.description}"
        }

        // set photo employee
        val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/"
        Glide.with(context)
            .load(url + response.areaImage)
            .apply(RequestOptions.fitCenterTransform())
            .into(holder.binding.ivPreviewDialogLaporanAreaInspeksi)
    }

    override fun getItemCount(): Int {
        return listLaporanArea.size
    }

}