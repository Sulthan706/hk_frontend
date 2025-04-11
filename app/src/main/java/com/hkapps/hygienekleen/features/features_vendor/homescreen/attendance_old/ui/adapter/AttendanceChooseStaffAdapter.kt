package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAttendanceChooseStaffBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.ChooseStaffDataResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.ui.activity.highLevel.AttendanceActivity
import kotlin.collections.ArrayList


open class AttendanceChooseStaffAdapter :
    RecyclerView.Adapter<AttendanceChooseStaffAdapter.DataViewHolder>(), Filterable {

    var staffData: ArrayList<ChooseStaffDataResponseModel> = ArrayList()
    var staffDataFiltered: ArrayList<ChooseStaffDataResponseModel> = ArrayList()
    var onItemClick: ((ChooseStaffDataResponseModel) -> Unit)? = null
    private var context: Context? = null

    inner class DataViewHolder(val binding: ItemAttendanceChooseStaffBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutItemAttendanceChooseStaff.setOnClickListener {
                onItemClick?.invoke(staffDataFiltered[adapterPosition])
            }
        }

        fun bind(result: ChooseStaffDataResponseModel) {
            result.name
//            Glide.with(itemView.imageView.context).load(result.downloadUrl).into(itemView.imageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        context = parent.context
        return DataViewHolder(
            ItemAttendanceChooseStaffBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        with(holder) {
            with(staffDataFiltered[position]) {
                binding.tvItemChooseAttendance.text = name
                binding.layoutItemAttendanceChooseStaff.setOnClickListener {
                    Toast.makeText(context,""+staffDataFiltered[position].name,Toast.LENGTH_SHORT).show()
                    val i = Intent(context, AttendanceActivity::class.java)
                    i.putExtra("page", 1)
                    i.putExtra("name_staff", staffDataFiltered[position].name)
                    context?.startActivity(i)
                }
            }
        }
    }

    override fun getItemCount(): Int = staffDataFiltered.size

    fun initData(list: List<ChooseStaffDataResponseModel>) {
        staffData = list as ArrayList<ChooseStaffDataResponseModel>
        staffDataFiltered = staffData
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) staffDataFiltered = staffData else {
                    val filteredList = ArrayList<ChooseStaffDataResponseModel>()
                    staffData
                        .filter {
                            (it.name.contains(constraint!!))

                        }
                        .forEach { filteredList.add(it) }
                    staffDataFiltered = filteredList

                }
                return FilterResults().apply { values = staffDataFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                staffDataFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<ChooseStaffDataResponseModel>
                notifyDataSetChanged()
            }
        }
    }
}