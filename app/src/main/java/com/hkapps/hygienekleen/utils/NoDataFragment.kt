package com.hkapps.hygienekleen.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hkapps.hygienekleen.databinding.FragmentNoDataBinding

class NoDataFragment : Fragment() {
    private var _binding: FragmentNoDataBinding? = null
    private val binding get() = _binding!!
    private var listener: NoInternetConnectionCallback? = null

    fun setListener(listener: NoInternetConnectionCallback) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance() = NoDataFragment()
    }
}