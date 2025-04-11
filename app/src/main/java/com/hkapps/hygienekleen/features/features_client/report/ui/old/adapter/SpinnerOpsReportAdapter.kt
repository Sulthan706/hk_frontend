package com.hkapps.hygienekleen.features.features_client.report.ui.old.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.features.features_client.report.model.ListOperatorSpinnerReportModel

class SpinnerOpsReportAdapter(
    context: Context, listOps: List<ListOperatorSpinnerReportModel>
) : ArrayAdapter<ListOperatorSpinnerReportModel>(context, 0, listOps) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    private fun customView(position: Int, convertView: View?, parent: ViewGroup): View {

        val list = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner_operator, parent, false)

        list?.let {
            val tvOperational = view.findViewById<TextView>(R.id.tv_operationalItemSpinnerOperator)
            val tvName = view.findViewById<TextView>(R.id.tv_nameItemSpinnerOperator)
            val tvNuc = view.findViewById<TextView>(R.id.tv_nucItemSpinnerOperator)
            val ivProfile = view.findViewById<ImageView>(R.id.iv_itemSpinnerOperator)

            if (position == 0) {
                tvName.visibility = View.GONE
                tvNuc.visibility = View.GONE
                ivProfile.visibility = View.GONE
                tvOperational.visibility = View.VISIBLE
            } else {
                tvName.visibility = View.VISIBLE
                tvNuc.visibility = View.VISIBLE
                ivProfile.visibility = View.VISIBLE
                tvOperational.visibility = View.GONE

                tvName.text = list.name!!
                tvNuc.text = list.nuc!!

                // set photo profile
                val img = list.profileImage!!
                val url = context.getString(R.string.url) + "assets.admin_master/images/photo_profile/$img"
                if (img == "") {
                    val uri =
                        "@drawable/profile_default" // where myresource (without the extension) is the file
                    val imaResource =
                        context.resources.getIdentifier(uri, null, context.packageName)
                    val res = context.resources.getDrawable(imaResource)
                    ivProfile.setImageDrawable(res)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_error_image)

                    Glide.with(context)
                        .load(url)
                        .apply(requestOptions)
                        .into(ivProfile)
                }
            }
        }

        return view
    }

}