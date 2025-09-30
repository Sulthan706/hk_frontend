package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListItemFormMrBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MaterialRequestSend
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanData

class SelectedMRAdapter(
    private val data: MutableList<MaterialRequestSend>,
    private val allItemsMap: Map<Int, ItemMRData>,
    private val allUnitsMap: Map<Int, SatuanData>,
    private val unitList: List<SatuanData>,
    private val onItemRemoved: (MaterialRequestSend) -> Unit,
    private val onQuantityChanged: (MaterialRequestSend, Int) -> Unit,
    private val onUnitChanged: (MaterialRequestSend, Int) -> Unit,
    private val onAddNewItem: () -> Unit,
    private val onEditItem: (MaterialRequestSend) -> Unit // Callback untuk edit item
) : RecyclerView.Adapter<SelectedMRAdapter.SelectedMRViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_ADD_BUTTON = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) TYPE_ADD_BUTTON else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedMRViewHolder {
        val binding = ListItemFormMrBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SelectedMRViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedMRViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ADD_BUTTON -> holder.bindAddButton()
            TYPE_ITEM -> holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size + 1 // +1 for add button

    inner class SelectedMRViewHolder(
        private val binding: ListItemFormMrBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentTextWatcher: TextWatcher? = null

        fun bind(item: MaterialRequestSend) {
            binding.apply {
                setupSelectedItem(item)
            }
        }

        fun bindAddButton() {
            binding.apply {
                setupAddButton()
            }
        }

        private fun setupSelectedItem(item: MaterialRequestSend) {
            binding.apply {
                // Clear previous listeners first
                clearListeners()

                // Get item dan unit details
                val itemData = allItemsMap[item.idItem]
                val unitData = allUnitsMap[item.idSatuan]

                // Set values
                tvPosition.text = itemData?.namaItem ?: "Item ${item.idItem}"
                tvGender.text = unitData?.namaSatuan ?: "Unit ${item.idSatuan}"
                etCount.setText(item.qtyRequest.toString())

                // Show both buttons for selected items
                add.visibility = View.GONE // Hide add button for selected items
                remove.visibility = View.VISIBLE

                // Set background to indicate this is a selected item
                root.setBackgroundResource(android.R.color.white)
                root.alpha = 1.0f

                // Enable all interactions
                tvPosition.isEnabled = true
                tvGender.isEnabled = true
                etCount.isEnabled = true

                // Setup listeners
                setupItemListeners(item)
            }
        }

        private fun setupAddButton() {
            binding.apply {
                // Clear previous listeners first
                clearListeners()

                // Setup untuk add more items
                tvPosition.text = "Tambah Item Lain"
                tvGender.text = "Klik untuk memilih item"
                etCount.setText("")
                etCount.hint = "Jumlah"

                // Show add button, hide remove button
                add.visibility = View.VISIBLE
                remove.visibility = View.GONE

                // Set different background for add button
                root.setBackgroundResource(android.R.color.transparent)
                root.alpha = 0.7f

                // Setup add button listener
                add.setOnClickListener {
                    onAddNewItem()
                }

                // Make the entire row clickable for add button
                root.setOnClickListener {
                    onAddNewItem()
                }

                // Disable direct text interactions for add button row
                tvPosition.isEnabled = false
                tvGender.isEnabled = false
                etCount.isEnabled = false
            }
        }

        private fun setupItemListeners(item: MaterialRequestSend) {
            binding.apply {
                // Item selection click - untuk edit item
                tvPosition.setOnClickListener {
                    onEditItem(item)
                }

                // Unit selection
                tvGender.setOnClickListener { view ->
                    showUnitSpinner(view, item)
                }

                // Quantity changes
                setupQuantityListener(item)

                // Remove button
                remove.setOnClickListener {
                    showRemoveConfirmation(item)
                }

                // Make the entire row clickable for edit (except buttons)
                root.setOnClickListener {
                    onEditItem(item)
                }
            }
        }

        private fun setupQuantityListener(item: MaterialRequestSend) {
            // Remove existing listener
            currentTextWatcher?.let { watcher ->
                binding.etCount.removeTextChangedListener(watcher)
            }

            // Create new listener
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val quantity = s.toString().toIntOrNull() ?: 0
                    if (quantity != item.qtyRequest) {
                        onQuantityChanged(item, quantity)
                    }
                }
            }

            currentTextWatcher = textWatcher
            binding.etCount.addTextChangedListener(textWatcher)
        }

        private fun showUnitSpinner(view: View, item: MaterialRequestSend) {
            val popup = PopupMenu(view.context, view)

            unitList.forEachIndexed { index, unit ->
                popup.menu.add(0, unit.idSatuan, index, unit.namaSatuan)

                // Mark current selection
                if (unit.idSatuan == item.idSatuan) {
                    popup.menu.getItem(index).isChecked = true
                }
            }

            popup.setOnMenuItemClickListener { menuItem ->
                val selectedUnit = unitList.find { it.idSatuan == menuItem.itemId }
                selectedUnit?.let { unit ->
                    binding.tvGender.text = unit.namaSatuan
                    onUnitChanged(item, unit.idSatuan)
                }
                true
            }

            popup.show()
        }

        private fun showRemoveConfirmation(item: MaterialRequestSend) {
            val itemName = allItemsMap[item.idItem]?.namaItem ?: "Item ${item.idItem}"

            AlertDialog.Builder(binding.root.context)
                .setTitle("Remove Item")
                .setMessage("Remove $itemName from the list?")
                .setPositiveButton("Remove") { _, _ ->
                    onItemRemoved(item)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun clearListeners() {
            binding.apply {
                // Remove text watcher
                currentTextWatcher?.let { watcher ->
                    etCount.removeTextChangedListener(watcher)
                }
                currentTextWatcher = null

                // Clear click listeners
                tvPosition.setOnClickListener(null)
                tvGender.setOnClickListener(null)
                add.setOnClickListener(null)
                remove.setOnClickListener(null)
                root.setOnClickListener(null)
            }
        }
    }

    // Function to update adapter when data changes
    fun updateData() {
        notifyDataSetChanged()
    }

    // Function to add new item
    fun addItem(item: MaterialRequestSend) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    // Function to remove item
    fun removeItem(item: MaterialRequestSend) {
        val position = data.indexOf(item)
        if (position != -1) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Function to update specific item
    fun updateItem(item: MaterialRequestSend) {
        val position = data.indexOfFirst { it.idItem == item.idItem }
        if (position != -1) {
            data[position] = item
            notifyItemChanged(position)
        }
    }
}
