package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.old.teamlead.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.listStaffBertugas.DataOperator

class StaffCheckTlAdapter(private val context: Context, var listStaff: ArrayList<DataOperator>)
    : RecyclerView.Adapter<StaffCheckTlAdapter.ViewHolder>() {

    private lateinit var staffCallBack: StaffCallBack

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivFoto: ImageView = itemView.findViewById(R.id.iv_fotoStaff)
        var tvName: TextView = itemView.findViewById(R.id.tv_userNameStaff)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_areaStaff)
        var tvSubLoc: TextView = itemView.findViewById(R.id.tv_subAreaStaff)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val getStaff = listStaff[position]

            staffCallBack.onClickedStaff(getStaff.employeeId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_anggota_checklist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listStaff[position]

        holder.tvName.text = response.employeeName
        holder.tvLocation.text = response.locationName
        holder.tvSubLoc.text = response.subLocationName


        val url = context!!.getString(R.string.url) +"assets.admin_master/images/photo_profile/"
        context.let {
            it?.let { it1 ->
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                    .skipMemoryCache(true)

                Glide.with(it1)
                    .load(url + response.employee_photo_profile)
                    .apply(requestOptions)
                    .into(holder.ivFoto)
            }
        }
    }

    override fun getItemCount(): Int {
        return listStaff.size
    }

    fun setListener(staffCallBack: StaffCallBack) {
        this.staffCallBack = staffCallBack
    }

    interface StaffCallBack {
        fun onClickedStaff(employeeId: Int)
    }

}