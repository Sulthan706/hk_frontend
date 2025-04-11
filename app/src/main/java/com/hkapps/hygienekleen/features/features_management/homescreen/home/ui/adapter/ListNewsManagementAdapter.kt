package com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListHomeNewsBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listnewsmanagement.ContentListNewsManagement
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeNewsManagementActivity


class ListNewsManagementAdapter (val listNewsManagement: ArrayList<ContentListNewsManagement>):
RecyclerView.Adapter<ListNewsManagementAdapter.ViewHolder>() {

    private lateinit var listClickNewsCallback : HomeNewsManagementActivity
    inner class ViewHolder(val binding: ItemListHomeNewsBinding):
    RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            val response = listNewsManagement[adapterPosition]
            listClickNewsCallback.onClickNews(
                response.newsId,
                response.isRead
            )
            notifyDataSetChanged()
        }
        init {
            itemView.setOnClickListener(this)
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListNewsManagementAdapter.ViewHolder {
        return ViewHolder(ItemListHomeNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        ))
    }

    override fun onBindViewHolder(holder: ListNewsManagementAdapter.ViewHolder, position: Int) {
        val item = listNewsManagement[position]
        if (item.isRead == "N") {
            holder.binding.clMainNews.setBackgroundColor(Color.parseColor("#9AF1F5"))
        }
        holder.binding.tvMainContentNews.text = item.newsTitle.ifEmpty { "-" }
        holder.binding.tvSecondMainContentNews.text = Html.fromHtml(item.newsDescription)
        holder.binding.tvDateStatusNotif.text = item.newsUpdatedAtDate.ifEmpty { "-" }

    }

    override fun getItemCount(): Int {
        return listNewsManagement.size
    }

    fun setListener(listNewsManagementCallback: HomeNewsManagementActivity){
        this.listClickNewsCallback = listNewsManagementCallback
    }

    interface ListNewsManagementCallback {
        fun onClickNews(newsId: Int, isRead: String)
    }


}