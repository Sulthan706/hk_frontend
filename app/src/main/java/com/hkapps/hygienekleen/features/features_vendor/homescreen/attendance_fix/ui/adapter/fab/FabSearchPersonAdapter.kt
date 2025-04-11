package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.adapter.fab

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch.DataFabSearch
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.ui.activity.midlevel.new_.HistoryAttendanceDetailPerson

class FabSearchPersonAdapter (var searchPerson: ArrayList<DataFabSearch>):
RecyclerView.Adapter<FabSearchPersonAdapter.MyViewHolder>(){
    lateinit var context: Context

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvEmployee: TextView = view.findViewById(R.id.tvMyTeamEmployeeName)
        var tvEmployeeCode: TextView = view.findViewById(R.id.tvMyTeamEmployeeCode)
        var rvEmployeeBtn: CardView = view.findViewById(R.id.rvMyTeamCard)
        var ivEmployee: ImageView = view.findViewById(R.id.ivMyTeamEmployee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fab_history_attendance_person, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = searchPerson[position]

        holder.tvEmployee.text = item.employeeName
        holder.tvEmployeeCode.text = "${item.employeeNuc} | ${item.jobName}"
        holder.rvEmployeeBtn.setOnClickListener {
            val i = Intent(context, HistoryAttendanceDetailPerson::class.java)
            i.putExtra("employeeId", item.employeeId)
            i.putExtra("employeeName", item.employeeName)
            i.putExtra("employeeNuc", item.employeeNuc)
            i.putExtra("employeeJobName", item.jobName)
            i.putExtra("employeePhotoProfile",item.employeePhotoProfile)
            context.startActivity(i)
        }
        val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/" + item.employeePhotoProfile

        if (item.employeePhotoProfile == "null" || item.employeePhotoProfile == null || item.employeePhotoProfile == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imageResource = context!!.resources.getIdentifier(uri, null, context!!.packageName)
            val res = context!!.resources.getDrawable(imageResource)
            holder.ivEmployee.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context!!)
                .load(urls)
                .apply(requestOptions)
                .into(holder.ivEmployee)
        }
    }

    override fun getItemCount(): Int {
        return searchPerson.size
    }
}