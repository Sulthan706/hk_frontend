package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.fragment

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentListAreaChecklistBinding
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea.Content
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.DetailAreaChecklistActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.activity.ListAreaOperationalChecklist
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.ui.new_.adapter.ListAreaChecklistAdapter
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.EndlessScrollingRecyclerView


class ListAreaChecklistFragment : Fragment(), ListAreaChecklistAdapter.ListAreaChecklistCallBack {

    private lateinit var binding: FragmentListAreaChecklistBinding
    private lateinit var adapter: ListAreaChecklistAdapter
    private val shiftId = CarefastOperationPref.loadInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, 0)
    private val projectCode = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_PROJECT_CODE, "")
    private var page = 0
    private var isLastPage = false
    private var reloadNeeded = true

    private val viewModel: ChecklistViewModel by lazy {
        ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListAreaChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set shimmer effect
        binding.shimmerListAreaChecklist.startShimmerAnimation()
        binding.shimmerListAreaChecklist.visibility = View.VISIBLE
        binding.rvListAreaChecklist.visibility = View.GONE
        binding.flNoInternetListAreaChecklist.visibility = View.GONE
        binding.flNoDataListAreaChecklist.visibility = View.GONE

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

    private fun viewIsOnline() {
        // set shimmer effect
        binding.shimmerListAreaChecklist.startShimmerAnimation()
        binding.shimmerListAreaChecklist.visibility = View.VISIBLE
        binding.rvListAreaChecklist.visibility = View.GONE
        binding.flNoInternetListAreaChecklist.visibility = View.GONE

        // set recyclerview layout
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvListAreaChecklist.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }
        }

        binding.swipeListAreaChecklist.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed(
                Runnable {
                    binding.swipeListAreaChecklist.isRefreshing = false
                    val i = Intent(requireContext(), ListAreaOperationalChecklist::class.java)
                    startActivity(i)
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.nothing, R.anim.nothing)
                }, 500
            )
        })
        binding.rvListAreaChecklist.addOnScrollListener(scrollListener)

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
                        binding.shimmerListAreaChecklist.stopShimmerAnimation()
                        binding.shimmerListAreaChecklist.visibility = View.GONE
                        binding.rvListAreaChecklist.visibility = View.VISIBLE
                    }, 1500)
                }
            }
        })
        viewModel.listAreaResponseModel.observe(requireActivity()) {
            if (it.code == 200) {
                isLastPage = it.data.last
                if (page == 0) {
                    // set rv adapter
                    adapter = ListAreaChecklistAdapter(
                        requireContext(),
                        it.data.content as ArrayList<Content>,
                        viewModel,
                        this
                    ). also { it.setListener(this) }
                    binding.rvListAreaChecklist.adapter = adapter
                } else {
                    adapter.listArea.addAll(it.data.content)
                    adapter.notifyItemRangeChanged(
                        adapter.listArea.size - it.data.content.size,
                        adapter.listArea.size
                    )
                }
            }
        }
    }

    private fun loadData() {
        viewModel.getListArea(projectCode, shiftId, page)
    }

    private fun noInternetState() {
        binding.shimmerListAreaChecklist.visibility = View.GONE
        binding.rvListAreaChecklist.visibility = View.GONE
        binding.flNoDataListAreaChecklist.visibility = View.GONE
        binding.flNoInternetListAreaChecklist.visibility = View.VISIBLE
        binding.layoutConnectionTimeout.btnRetry.setOnClickListener {
            val i = Intent(context, ListAreaOperationalChecklist::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    override fun onClickArea(shiftId: Int, plottingId: Int) {
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_PLOTTING_ID, plottingId)
        CarefastOperationPref.saveInt(CarefastOperationPrefConst.CHECKLIST_SHIFT_ID, shiftId)
        val i = Intent(requireContext(), DetailAreaChecklistActivity::class.java)
        startActivity(i)
//        startActivityForResult(i, EDIT_CODE)
    }

    override fun onResume() {
        super.onResume()
        if (reloadNeeded) {
            loadData()
        }
        reloadNeeded = false
    }

    override fun onPause() {
        super.onPause()
        reloadNeeded = true
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == EDIT_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                reloadNeeded = true
//            }
//        }
//    }

    companion object {
        private const val EDIT_CODE = 31
    }

}