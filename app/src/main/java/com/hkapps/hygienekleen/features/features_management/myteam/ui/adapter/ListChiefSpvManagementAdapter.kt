package com.hkapps.hygienekleen.features.features_management.myteam.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ListLeaderMyteamBinding
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.Data
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel

class ListChiefSpvManagementAdapter(
    private var context: Context,
    var listChief: ArrayList<Data>,
    private val viewModel: MyTeamManagementViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ListChiefSpvManagementAdapter.ViewHolder>() {

    private lateinit var listChiefSpvManagementCallback: ListChiefSpvManagementCallback

    inner class ViewHolder(val binding: ListLeaderMyteamBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val response = listChief[adapterPosition]
            listChiefSpvManagementCallback.onClickChief(response.idEmployee, response.employeeName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ListLeaderMyteamBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listChief[position]

        holder.binding.tvNameLeaderMyTeam.text = response.employeeName
        holder.binding.tvJobLeaderMyTeam.text = response.jobName

        // set user image
        val img = response.employeePhotoProfile
        val url =
            context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"

        if (img == "null" || img == null || img == "") {
            val uri =
                "@drawable/profile_default" // where myresource (without the extension) is the file
            val imaResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            val res = context.resources.getDrawable(imaResource)
            holder.binding.ivImageLeaderMyteam.setImageDrawable(res)
        } else {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true)

            Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.binding.ivImageLeaderMyteam)
        }

        // set count absent
        holder.binding.llCountBelumAbsen.visibility = View.GONE
//        viewModel.getCountAbsentOprResponseModel().observe(lifecycleOwner) {
//            if (it.code == 200) {
//                if (response.employeeId == it.data.employeeId) {
//                    holder.tvBelumAbsen.text = "${it.data.countEmployeeBelumAbsen}"
//                    holder.tvAbsent.text = "/ ${it.data.countEmployee}"
//                }
//            }
//        }
//        viewModel.getCountAbsentOperator(projectId, response.employeeId, shiftId)

    }

    override fun getItemCount(): Int {
        return listChief.size
    }

    fun setListener(listChiefSpvManagementCallback: ListChiefSpvManagementCallback) {
        this.listChiefSpvManagementCallback = listChiefSpvManagementCallback
    }

    interface ListChiefSpvManagementCallback {
        fun onClickChief(chiefId: Int, chiefName: String)
    }
}