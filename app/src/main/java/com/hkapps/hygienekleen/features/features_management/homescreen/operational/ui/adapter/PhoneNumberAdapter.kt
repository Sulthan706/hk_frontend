package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ListPhoneNumberBinding

class PhoneNumberAdapter(
    private var context: Context,
    var listPhone: ArrayList<String>
): RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder>() {

    private lateinit var listPhoneNumberCallBack: ListPhoneNumberCallBack

    inner class ViewHolder(val binding: ListPhoneNumberBinding): RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val response = listPhone[adapterPosition]
            listPhoneNumberCallBack.onClickPhone(response)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListPhoneNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val response = listPhone[position]
        Log.d("respo", "$response")
        if (response == "" || response == null) {
            holder.binding.clListPhoneNumber.visibility = View.GONE
            holder.binding.clListPhoneNumber.layoutParams = ViewGroup.LayoutParams(0, 0)
        } else {
            holder.binding.clListPhoneNumber.visibility = View.VISIBLE
            holder.binding.tvListPhoneNumber.text = response
        }
    }

    override fun getItemCount(): Int {
        return listPhone.size
    }

    fun setListener(listPhoneNumberCallBack: ListPhoneNumberCallBack) {
        this.listPhoneNumberCallBack = listPhoneNumberCallBack
    }

    interface ListPhoneNumberCallBack {
        fun onClickPhone(phoneNumber: String)
    }
}