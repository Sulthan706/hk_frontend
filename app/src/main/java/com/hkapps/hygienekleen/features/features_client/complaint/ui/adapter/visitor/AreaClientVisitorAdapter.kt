    package com.hkapps.hygienekleen.features.features_client.complaint.ui.adapter.visitor

    import android.annotation.SuppressLint
    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import com.hkapps.hygienekleen.databinding.ItemAreaCtalkVisitorBinding
    import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.Area

    class AreaClientVisitorAdapter(var listAreaCtalkVisitor: ArrayList<Area>): RecyclerView.Adapter<AreaClientVisitorAdapter.ViewHolder>()
    {
        private var isExpanded: Boolean = false
        private val defaultItemCount = 3
        inner class ViewHolder(val binding: ItemAreaCtalkVisitorBinding):
                RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemAreaCtalkVisitorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = listAreaCtalkVisitor[position]
            holder.binding.tvAreaCtalkVisitor.text = item.subLocationName
            holder.binding.tvCountAreaCtalkVisitor.text = item.count.toString()

            // Hide the views for items beyond the limit when not expanded
            if (!isExpanded && position >= defaultItemCount) {
                holder.binding.root.visibility = android.view.View.GONE
            } else {
                holder.binding.root.visibility = android.view.View.VISIBLE
            }
        }

        override fun getItemCount(): Int {
            return if (!isExpanded && listAreaCtalkVisitor.size > defaultItemCount) {
                defaultItemCount
            } else {
                listAreaCtalkVisitor.size
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun toggleExpansion() {
            isExpanded = !isExpanded
            notifyDataSetChanged() // Refresh the list to reflect changes
        }


    }