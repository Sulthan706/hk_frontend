package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter

import android.content.Context
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
import com.hkapps.hygienekleen.features.features_client.myteam.model.listwithshiftemployee.Content


class MyTeamListEmployeeAdapter (var ListCfteams: ArrayList<Content>):
RecyclerView.Adapter<MyTeamListEmployeeAdapter.MyViewHolder>(){
    lateinit var context: Context

            inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
                var tvEmployeeName: TextView = view.findViewById(R.id.tvMyTeamEmployeeName)
                var tvEmployeeJobDetail: TextView = view.findViewById(R.id.tvMyTeamEmployeeCodeAndJobCode)
                var tvEmployeeMonth: TextView = view.findViewById(R.id.tvMyTeamClientsHistoryMonth)
                var tvEmployeeClock: TextView = view.findViewById(R.id.tvMyTeamClientsHistoryClock)
                var ivEmployee: ImageView = view.findViewById(R.id.ivMyTeamEmployee)
                var rvBtnDetail: CardView = view.findViewById(R.id.rvMyTeamCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_team_clients_employee, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemView = ListCfteams[position]
//        holder.rvBtnDetail.setOnClickListener {
//            val i = Intent(context, MyTeamClientEmployeeDetail::class.java)
//            context.startActivity(i)
//            CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_EMPLOYEE_CFTEAM, itemView.employeeId)
//        }
        holder.tvEmployeeName.text = itemView.employeeName
        holder.tvEmployeeJobDetail.text = "${itemView.employeeCode} | ${itemView.jobCode}"
        holder.tvEmployeeMonth.text = "${itemView.totalHari} Hari"
        holder.tvEmployeeClock.text = "${itemView.totalKerja} Jam"

        val urls = context!!.getString(R.string.url) + "assets.admin_master/images/photo_profile/" + itemView.imageEmployee

        if (itemView.imageEmployee == "null" || itemView.imageEmployee == null || itemView.imageEmployee == "") {
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
        return ListCfteams.size
    }
}