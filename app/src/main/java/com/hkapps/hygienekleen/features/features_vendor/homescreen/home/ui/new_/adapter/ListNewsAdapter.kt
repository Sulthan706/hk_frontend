package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.adapter

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListHomeNewsBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews.Content

class ListNewsAdapter(
    var homeNews: ArrayList<Content>
) : RecyclerView.Adapter<ListNewsAdapter.ViewHolder>() {
    var selectedItem = -1
    private lateinit var listNews: ListNews

    inner class ViewHolder(val binding: ItemListHomeNewsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val putNews = homeNews[adapterPosition]
            listNews.onClickNews(
                putNews.newsId,
                putNews.isRead
            )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListHomeNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = homeNews[position]

        if (item.isRead == "N") {
            holder.binding.clMainNews.setBackgroundColor(Color.parseColor("#9AF1F5"))
        }
        holder.binding.tvMainContentNews.text = item.newsTitle
        holder.binding.tvSecondMainContentNews.text = Html.fromHtml(item.newsDescription)
        holder.binding.tvDateStatusNotif.text = item.newsUpdatedAtDate
    }

    override fun getItemCount(): Int {
        return homeNews.size
    }

    fun setListener(listNews: ListNews) {
        this.listNews = listNews
    }

    interface ListNews {
        fun onClickNews(
            newsId: Int,
            isRead: String
        )
    }

}


