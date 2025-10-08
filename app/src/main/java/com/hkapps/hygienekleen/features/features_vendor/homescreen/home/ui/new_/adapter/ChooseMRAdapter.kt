package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListItemCreateMaterialRequestBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequestSend
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter.ChooseMRAdapter.ChooseMRViewHolder

class ChooseMRAdapter(
    private val data: MutableList<ItemMRData>,
    private val dataUnit: List<SatuanData>,
    private val isEditMode: Boolean = false,
    private val editItem: MaterialRequestSend? = null,
    private val existingSelections: List<MaterialRequestSend> = emptyList(),
    private val onDataChanged: (List<MaterialRequestSend>) -> Unit
) : RecyclerView.Adapter<ChooseMRViewHolder>() {

    // List untuk menyimpan data yang akan dikirim ke API
    private val materialRequestList = mutableListOf<MaterialRequestSend>()

    // Map untuk tracking state setiap item
    private val itemStateMap = mutableMapOf<Int, ItemState>()

    data class ItemState(
        var isSelected: Boolean = false,
        var quantity: Int = 0,
        var selectedUnitId: Int = 0,
        var selectedUnitName: String = "",
        var isDisabled: Boolean = false // untuk disable item yang sudah dipilih di luar edit mode
    )

    init {
        // Prefill dari existingSelections
        existingSelections.forEach { existing ->
            val unit = dataUnit.find { it.idSatuan == existing.idSatuan }
            itemStateMap[existing.idItem] = ItemState(
                isSelected = true,
                quantity = existing.qtyRequest,
                selectedUnitId = existing.idSatuan,
                selectedUnitName = unit?.namaSatuan ?: "Pilih Satuan",
                isDisabled = false // selalu bisa diedit
            )

            // Tambahkan juga ke materialRequestList
            materialRequestList.add(existing.copy())
        }

        notifyDataChanged()
    }

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

        // Initialize atau get existing state
        val itemState = itemStateMap.getOrPut(item.idItem) {
            ItemState(
                selectedUnitId = dataUnit.firstOrNull()?.idSatuan ?: 0,
                selectedUnitName = dataUnit.firstOrNull()?.namaSatuan ?: "Pilih Satuan",
                isDisabled = false
            )
        }

        holder.binding.apply {
            clearListeners(holder)

            tvItemReasonResign.text = item.namaItem

            // Selalu enabled
            checkbox.isEnabled = true
            etCount.isEnabled = true
            tvGender.isEnabled = true
            root.alpha = 1.0f

            // Checkbox state
            checkbox.isChecked = itemState.isSelected

            // Quantity
            etCount.setText(
                if (itemState.quantity > 0) itemState.quantity.toString() else ""
            )

            // Unit
            tvGender.text = itemState.selectedUnitName

            // Listener
            setupCheckboxListener(holder, item, itemState)
            setupQuantityListener(holder, item, itemState)
            setupUnitSpinner(holder, item, itemState)
        }
    }

    private fun clearListeners(holder: ChooseMRViewHolder) {
        holder.binding.apply {
            checkbox.setOnCheckedChangeListener(null)
            tvGender.setOnClickListener(null)
            etCount.removeTextChangedListener(holder.textWatcher)
        }
    }

    private fun setupCheckboxListener(
        holder: ChooseMRViewHolder,
        item: ItemMRData,
        itemState: ItemState
    ) {
        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            itemState.isSelected = isChecked

            if (isEditMode && editItem != null && item.idItem == editItem.idItem) {
                // Dalam edit mode, jika user uncheck item yang sedang diedit
                // maka akan menghapus item tersebut dari list
                if (!isChecked) {
                    // Reset quantity
                    itemState.quantity = 0
                    holder.binding.etCount.setText("")
                }
            } else {
                // Dalam add mode, normal behavior
                if (!isChecked) {
                    // Reset quantity when unchecked
                    itemState.quantity = 0
                    holder.binding.etCount.setText("")
                } else {
                    // Set default quantity when checked
                    if (itemState.quantity <= 0) {
                        itemState.quantity = 1
                        holder.binding.etCount.setText("1")
                    }
                }
            }

            updateMaterialRequestList()
            notifyDataChanged()
        }
    }

    private fun setupQuantityListener(
        holder: ChooseMRViewHolder,
        item: ItemMRData,
        itemState: ItemState
    ) {
        holder.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val quantity = s.toString().toIntOrNull() ?: 0
                itemState.quantity = quantity

                // Auto check/uncheck checkbox based on quantity
                if (quantity > 0 && !itemState.isSelected) {
                    itemState.isSelected = true
                    holder.binding.checkbox.isChecked = true
                } else if (quantity <= 0 && itemState.isSelected) {
                    itemState.isSelected = false
                    holder.binding.checkbox.isChecked = false
                }

                updateMaterialRequestList()
                notifyDataChanged()
            }
        }
        holder.binding.etCount.addTextChangedListener(holder.textWatcher)
    }

    private fun setupUnitSpinner(
        holder: ChooseMRViewHolder,
        item: ItemMRData,
        itemState: ItemState
    ) {
        holder.binding.tvGender.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)

            dataUnit.forEachIndexed { index, unit ->
                popup.menu.add(0, unit.idSatuan, index, unit.namaSatuan)

                // Mark current selection
                if (unit.idSatuan == itemState.selectedUnitId) {
                    popup.menu.getItem(index).isChecked = true
                }
            }

            popup.setOnMenuItemClickListener { menuItem ->
                val selectedUnit = dataUnit.find { it.idSatuan == menuItem.itemId }
                selectedUnit?.let { unit ->
                    itemState.selectedUnitId = unit.idSatuan
                    itemState.selectedUnitName = unit.namaSatuan
                    holder.binding.tvGender.text = unit.namaSatuan

                    updateMaterialRequestList()
                    notifyDataChanged()
                }
                true
            }

            popup.show()
        }
    }

    private fun updateMaterialRequestList() {
        materialRequestList.clear()

        itemStateMap.forEach { (itemId, state) ->
            if (state.isSelected && state.quantity > 0 && state.selectedUnitId > 0) {
                materialRequestList.add(
                    MaterialRequestSend(
                        idItem = itemId,
                        qtyRequest = state.quantity,
                        idSatuan = state.selectedUnitId
                    )
                )
            }
        }
    }

    private fun notifyDataChanged() {
        onDataChanged(materialRequestList.toList())
    }

    // Function untuk mendapatkan data yang valid (checked dan quantity > 0)
    fun getValidMaterialRequests(): List<MaterialRequestSend> {
        return materialRequestList.filter { it.qtyRequest > 0 && it.idSatuan > 0 }
    }

    // Function untuk mendapatkan semua data
    fun getAllMaterialRequests(): List<MaterialRequestSend> {
        return materialRequestList.toList()
    }

    // Function untuk mendapatkan data yang siap dikirim ke API
    fun getDataForAPI(): List<MaterialRequestSend> {
        return getValidMaterialRequests()
    }

    // Function untuk reset semua selection
    fun clearAllSelections() {
        itemStateMap.clear()
        materialRequestList.clear()
        notifyDataSetChanged()
        notifyDataChanged()
    }

    // Function untuk set existing selections (if needed for edit mode)
    fun setExistingSelections(existingSelections: List<MaterialRequestSend>) {
        existingSelections.forEach { selection ->
            val unit = dataUnit.find { it.idSatuan == selection.idSatuan }
            itemStateMap[selection.idItem] = ItemState(
                isSelected = true,
                quantity = selection.qtyRequest,
                selectedUnitId = selection.idSatuan,
                selectedUnitName = unit?.namaSatuan ?: "Unknown Unit"
            )
        }
        updateMaterialRequestList()
        notifyDataSetChanged()
        notifyDataChanged()
    }

    override fun getItemCount(): Int = data.size

    inner class ChooseMRViewHolder(val binding: ListItemCreateMaterialRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var textWatcher: TextWatcher? = null
    }
}