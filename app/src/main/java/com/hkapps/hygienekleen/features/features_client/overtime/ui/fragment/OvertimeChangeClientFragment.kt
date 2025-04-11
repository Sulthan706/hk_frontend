package com.hkapps.hygienekleen.features.features_client.overtime.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentOvertimeChangeClientBinding
import com.hkapps.hygienekleen.features.features_client.overtime.EndlessScrollingRecyclerView
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient.Content
import com.hkapps.hygienekleen.features.features_client.overtime.ui.activity.DetailOvertimeChangeClientActivity
import com.hkapps.hygienekleen.features.features_client.overtime.ui.activity.OvertimeClientActivity
import com.hkapps.hygienekleen.features.features_client.overtime.ui.adapter.OvertimeChangeClientAdapter
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst

class OvertimeChangeClientFragment : Fragment(), OvertimeChangeClientAdapter.ListOvertimeChangeClientCallback {

    private lateinit var binding: FragmentOvertimeChangeClientBinding
    private var page = 0
    private var isLastPage = false

    private lateinit var adapter: OvertimeChangeClientAdapter
    private val projectId = CarefastOperationPref.loadString(CarefastOperationPrefConst.CLIENT_PROJECT_CODE, "")
    private val userId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)
//    private val userId: Int = 9084

    private val viewModel: OvertimeClientViewModel by lazy {
        ViewModelProviders.of(this).get(OvertimeClientViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOvertimeChangeClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerOvertimeChangeClient.startShimmerAnimation()
        binding.shimmerOvertimeChangeClient.visibility = View.VISIBLE
        binding.rvOvertimeChangeClient.visibility = View.GONE
        binding.flNoInternetOvertimeChangeClient.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isOnline(requireActivity())
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    viewIsOnline()
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    viewIsOnline()
                    return true
                }
            }
        } else {
            noInternetState()
            return true
        }
        return false
    }

    private fun noInternetState() {
        binding.shimmerOvertimeChangeClient.visibility = View.GONE
        binding.rvOvertimeChangeClient.visibility = View.GONE
        binding.flNoDataOvertimeChangeClient.visibility = View.GONE
        binding.flNoInternetOvertimeChangeClient.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, OvertimeClientActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
        }
    }

    private fun viewIsOnline() {
        // set first layout
        binding.shimmerOvertimeChangeClient.startShimmerAnimation()
        binding.shimmerOvertimeChangeClient.visibility = View.VISIBLE
        binding.rvOvertimeChangeClient.visibility = View.GONE
        binding.flNoInternetOvertimeChangeClient.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOvertimeChangeClient.layoutManager = layoutManager

        val scrollListner = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }

        binding.swipeRefreshLayoutOvertimeChangeClient.setColorSchemeResources(R.color.red)
        binding.swipeRefreshLayoutOvertimeChangeClient.setOnRefreshListener {
            page = 0
            loadData()
        }

        binding.rvOvertimeChangeClient.addOnScrollListener(scrollListner)

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.isLoading?.observe(requireActivity(), Observer<Boolean?> { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_SHORT).show()
                } else {
                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        binding.shimmerOvertimeChangeClient.stopShimmerAnimation()
                        binding.shimmerOvertimeChangeClient.visibility = View.GONE
                        binding.rvOvertimeChangeClient.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listOvertimeChangeClientModel().observe(requireActivity()) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    isLastPage = it.data.last
                    if (page == 0) {
                        adapter = OvertimeChangeClientAdapter(requireContext(), it.data.content as ArrayList<Content>).also { it.setListener(this) }
                        binding.rvOvertimeChangeClient.adapter = adapter
                    } else {
                        adapter.listOvertime.addAll(it.data.content)
                        adapter.notifyItemRangeChanged(adapter.listOvertime.size - it.data.content.size, adapter.listOvertime.size)
                    }
                } else {
                    noDataState()
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListOvertimeChangeClient(projectId, userId, page)
    }

    private fun noDataState() {
        binding.shimmerOvertimeChangeClient.visibility = View.GONE
        binding.rvOvertimeChangeClient.visibility = View.GONE
        binding.flNoInternetOvertimeChangeClient.visibility = View.GONE
        binding.flNoDataOvertimeChangeClient.visibility = View.VISIBLE
    }

    private fun reset() {
        page = 0
    }

    override fun onClickItem(overtimeId: Int) {
        val i = Intent(requireContext(), DetailOvertimeChangeClientActivity::class.java)
        i.putExtra("overtimeIdChangeClient", overtimeId)
        startActivity(i)
    }
}