package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ActivitySearchParticipantBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.Content
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.SearchParticipantNameAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.utils.EndlessScrollingRecyclerView

class SearchParticipantActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchParticipantBinding
    private lateinit var rvAdapter: SearchParticipantNameAdapter
    private val projectCode = "Semua"
    private val position = "Semua"
    private var page = 0
    private var isLastPage = false
    private var name = ""

    private val viewModel : ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set on back button
        binding.ivBackSearchParticipant.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.svSearchParticipant.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query == "") {
                    binding.llDefaultSearchParticipant.visibility = View.VISIBLE
                    binding.tvEmptySearchParticipant.visibility = View.GONE
                    binding.rvSearchParticipant.adapter = null
                } else {
                    binding.llDefaultSearchParticipant.visibility = View.GONE
                    binding.tvEmptySearchParticipant.visibility = View.GONE
                    binding.rvSearchParticipant.adapter = null

                    name = query
                    loadData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null || newText == "") {
                    binding.llDefaultSearchParticipant.visibility = View.VISIBLE
                    binding.tvEmptySearchParticipant.visibility = View.GONE
                    binding.rvSearchParticipant.adapter = null
                }
                return true
            }

        })

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvSearchParticipant.layoutManager = layoutManager

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData(name)
                }
            }

        }
        binding.rvSearchParticipant.addOnScrollListener(scrollListener)

        setObserver()
    }

    private fun setObserver() {
        viewModel.searchParticipantNameModel.observe(this) {
            if (it.code == 200) {
                if (it.data.content.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.rvSearchParticipant.visibility = View.VISIBLE
                        binding.tvEmptySearchParticipant.visibility = View.GONE
                    }, 1500)

                    isLastPage = it.data.last

                    if (page == 0) {
                        rvAdapter = SearchParticipantNameAdapter(
                            it.data.content as ArrayList<Content>
                        )
                        binding.rvSearchParticipant.adapter = rvAdapter
                    } else {
                        rvAdapter.listParticipant.addAll(it.data.content)
                        rvAdapter.notifyItemRangeChanged(
                            rvAdapter.listParticipant.size - it.data.content.size,
                            rvAdapter.listParticipant.size
                        )
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed( {
                        binding.tvEmptySearchParticipant.visibility = View.VISIBLE
                        binding.rvSearchParticipant.visibility = View.GONE
                        binding.rvSearchParticipant.adapter = null
                    }, 1500)
                }
            } else {
                binding.llDefaultSearchParticipant.visibility = View.VISIBLE
                binding.rvSearchParticipant.adapter = null
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(query: String) {
        viewModel.getSearchParticipantName(query, projectCode, position, page)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }
}