package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlistmanagement.ContentCfteamMgmnt


class MyTeamListCfManagementAdapter(var ListMgmntCfteam: ArrayList<ContentCfteamMgmnt>):
RecyclerView.Adapter<MyTeamListCfManagementAdapter.MyViewHolder>(){
    lateinit var context: Context
    private lateinit var itemPhoneManagement: ItemPhoneManagement
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        var tvEmployeeName: TextView = view.findViewById(R.id.tvMyTeamEmployeeNameMgmnt)
        var tvEmployeJob: TextView = view.findViewById(R.id.tvMyTeamEmployeeCodeAndJobCodeMgmnt)
        var ivManagementCall: ImageView = view.findViewById(R.id.ivBtnCallManagement)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SuspiciousIndentation")
        override fun onClick(p0: View?) {
          val phoneItem = ListMgmntCfteam[adapterPosition]
            itemPhoneManagement.onClick(
                phoneItem.managementPhone
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_team_clients_cfmanagement,parent,false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemView = ListMgmntCfteam[position]
        
        holder.tvEmployeeName.text = itemView.managementName
        holder.tvEmployeJob.text = itemView.managementJob
        holder.ivManagementCall.setOnClickListener {
            dialogPhone(itemView.managementPhone)
        }

    }

    override fun getItemCount(): Int {
        return ListMgmntCfteam.size
    }

    fun setListener(itemPhoneManagement: ItemPhoneManagement){
        this.itemPhoneManagement = itemPhoneManagement
    }

    interface ItemPhoneManagement {
        fun onClick(
            managementPhone: String
        )
    }
    private fun dialogPhone(managementPhone: String) {
        val view = View.inflate(context, R.layout.dialog_item_phone_client, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
//        dialog.setCancelable(false)
//        val btnBack = dialog.findViewById<Button>(R.id.btnBack)
        val text = dialog.findViewById<TextView>(R.id.tvPhoneMgmnt)
        text.text = managementPhone
        text.setOnClickListener {

        }


        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }




}