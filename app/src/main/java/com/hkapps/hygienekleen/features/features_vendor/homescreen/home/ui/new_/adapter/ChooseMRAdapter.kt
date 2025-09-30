package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemCreateMaterialRequestBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequestSend
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ChooseMRAdapter.ChooseMRViewHolder

class ChooseMRAdapter(
    private val data: MutableList<ItemMRData>,
    private val dataUnit: List<SatuanData>,
    private val onDataChanged: (List<MaterialRequestSend>) -> Unit
) : RecyclerView.Adapter<ChooseMRViewHolder>() {

    // List untuk menyimpan data yang akan dikirim ke API
    private val materialRequestList = mutableListOf<MaterialRequestSend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseMRViewHolder {
        return ChooseMRViewHolder(
            ListItemCreateMaterialRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChooseMRViewHolder, position: Int) {
        val item = data[position]

        holder.binding.apply {
            // Reset listeners untuk menghindari konflik
            checkbox.setOnCheckedChangeListener(null)
            tvGender.setOnClickListener(null)
            etCount.removeTextChangedListener(holder.textWatcher)

            // Set data item
            tvItemReasonResign.text = item.namaItem

            // Inisialisasi atau ambil data existing untuk item ini
            val existingData = materialRequestList.find { it.idItem == item.idItem }
                ?: MaterialRequestSend(idItem = item.idItem).also {
                    materialRequestList.add(it)
                }

            // Set nilai default jika belum ada
            if (existingData.idSatuan == 0 && dataUnit.isNotEmpty()) {
                existingData.idSatuan = dataUnit[0].idSatuan
                tvGender.text = dataUnit[0].namaSatuan
            }

            // Set nilai count jika ada
            if (existingData.qtyRequest > 0) {
                etCount.setText(existingData.qtyRequest.toString())
            }

            // Setup spinner untuk unit
            setupUnitSpinner(holder, existingData)

            // Setup checkbox listener
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    // Remove dari list jika unchecked
                    materialRequestList.removeAll { it.idItem == item.idItem }
                    etCount.setText("")
                    tvGender.text = if (dataUnit.isNotEmpty()) dataUnit[0].namaSatuan else "Satuan"
                } else {
                    // Add ke list jika checked dan belum ada
                    if (materialRequestList.none { it.idItem == item.idItem }) {
                        materialRequestList.add(
                            MaterialRequestSend(
                                idItem = item.idItem,
                                idSatuan = dataUnit.firstOrNull()?.idSatuan ?: 0,
                                qtyRequest = 0
                            )
                        )
                    }
                }
                onDataChanged(getValidMaterialRequests())
            }

            // Setup EditText listener untuk quantity
            holder.textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val quantity = s.toString().toIntOrNull() ?: 0

                    // Update data di list
                    materialRequestList.find { it.idItem == item.idItem }?.let {
                        it.qtyRequest = quantity
                    }

                    // Auto check/uncheck checkbox berdasarkan quantity
                    if (quantity > 0 && !checkbox.isChecked) {
                        checkbox.isChecked = true
                    } else if (quantity <= 0 && checkbox.isChecked) {
                        checkbox.isChecked = false
                    }

                    onDataChanged(getValidMaterialRequests())
                }
            }
            etCount.addTextChangedListener(holder.textWatcher)
        }
    }

    private fun setupUnitSpinner(holder: ChooseMRViewHolder, materialRequest: MaterialRequestSend) {
        holder.binding.tvGender.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)

            dataUnit.forEachIndexed { index, unit ->
                popup.menu.add(0, unit.idSatuan, index, unit.namaSatuan)
            }

            popup.setOnMenuItemClickListener { menuItem ->
                val selectedUnit = dataUnit.find { it.idSatuan == menuItem.itemId }
                selectedUnit?.let { unit ->
                    holder.binding.tvGender.text = unit.namaSatuan

                    // Update data di list
                    materialRequest.idSatuan = unit.idSatuan
                    onDataChanged(getValidMaterialRequests())
                }
                true
            }

            popup.show()
        }
    }

    // Function untuk mendapatkan data yang valid (checked dan quantity > 0)
    private fun getValidMaterialRequests(): List<MaterialRequestSend> {
        return materialRequestList.filter { it.qtyRequest > 0 && it.idSatuan > 0 }
    }

    // Function untuk mendapatkan semua data (untuk keperluan lain)
    fun getAllMaterialRequests(): List<MaterialRequestSend> {
        return materialRequestList.toList()
    }

    // Function untuk mendapatkan data yang siap dikirim ke API
    fun getDataForAPI(): List<MaterialRequestSend> {
        return getValidMaterialRequests()
    }

    override fun getItemCount(): Int = data.size

    inner class ChooseMRViewHolder(val binding: ListItemCreateMaterialRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var textWatcher: TextWatcher? = null
    }
}