package com.hkapps.hygienekleen.features.features_management.homescreen.operational.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hkapps.hygienekleen.databinding.SpinnerCustomLayoutBinding


class StarAdapter(val context: Context, var dataSource: List<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(com.hkapps.hygienekleen.R.layout.spinner_custom_layout, parent, false)
        val binding = SpinnerCustomLayoutBinding.bind(view)
        when(position){

            1->{
                binding.star1.visibility = View.GONE
                binding.star2.visibility = View.VISIBLE
                binding.star3.visibility = View.VISIBLE
                binding.star4.visibility = View.VISIBLE
                binding.star5.visibility = View.VISIBLE
            }
            2->{
                binding.star2.visibility = View.GONE
                binding.star1.visibility = View.GONE
                binding.star3.visibility = View.VISIBLE
                binding.star4.visibility = View.VISIBLE
                binding.star5.visibility = View.VISIBLE
            }
            3->{
                binding.star3.visibility = View.GONE
                binding.star4.visibility = View.GONE
                binding.star5.visibility = View.GONE
                binding.star1.visibility = View.VISIBLE
                binding.star2.visibility = View.VISIBLE
            }
            4->{
                binding.star2.visibility = View.GONE
                binding.star3.visibility = View.GONE
                binding.star4.visibility = View.GONE
                binding.star5.visibility = View.GONE
                binding.star1.visibility = View.VISIBLE
            }

        }
        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {

    }

}