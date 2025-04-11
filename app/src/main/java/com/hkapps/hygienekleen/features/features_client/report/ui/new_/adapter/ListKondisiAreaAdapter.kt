package com.hkapps.hygienekleen.features.features_client.report.ui.new_.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemListKondisiAreaBinding
import com.hkapps.hygienekleen.features.features_client.report.model.listkondisiarea.ContentKondisiArea
import com.hkapps.hygienekleen.features.features_client.report.ui.new_.activity.KondisiAreaActivity


class ListKondisiAreaAdapter(
    listKondisiArea1: KondisiAreaActivity,
    var listKondisiArea: ArrayList<ContentKondisiArea>
) : RecyclerView.Adapter<ListKondisiAreaAdapter.ViewHolder>() {
    private lateinit var listKondisiAreaCallback: ListKondisiAreaCallBack

    inner class ViewHolder(val binding: ItemListKondisiAreaBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener{
            init {
                itemView.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            val getArea = listKondisiArea[adapterPosition]
            listKondisiAreaCallback.onClickNotification(
                getArea.locationId,
                getArea.locationName
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListKondisiAreaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listKondisiArea[position]
        holder.binding.tvListAreaDashboardProject.text = response.locationName


    }

    override fun getItemCount(): Int {
        return listKondisiArea.size
    }


    fun setListener(listKondisiAreaCallback: ListKondisiAreaCallBack){
        this.listKondisiAreaCallback = listKondisiAreaCallback
    }


    interface ListKondisiAreaCallBack {
        fun onClickNotification(locationId: Int, locationName: String)
    }



}




