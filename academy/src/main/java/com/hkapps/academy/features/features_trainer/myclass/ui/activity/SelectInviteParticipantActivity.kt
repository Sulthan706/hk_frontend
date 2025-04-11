package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.academy.databinding.ActivitySelectInviteParticipantBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.Content
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ContentParcelable
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ParticipantClassInviteAdapter
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.SelectedParticipantInviteAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import com.hkapps.academy.utils.EndlessScrollingRecyclerView

class SelectInviteParticipantActivity : AppCompatActivity(),
    ParticipantClassInviteAdapter.ListParticipantCallBack,
    SelectedParticipantInviteAdapter.ListSelectedCallBack
{

    private lateinit var binding: ActivitySelectInviteParticipantBinding
    private lateinit var rvListAdapter: ParticipantClassInviteAdapter
    private lateinit var rvSelectedAdapter: SelectedParticipantInviteAdapter

    private var projectCode = ""
    private var position = ""
    private var isLastPage = false
    private var page = 0
    private var clickFrom = ""
    private val listParticipant = ArrayList<ContentParcelable>()

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectInviteParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get extra string from intent
        clickFrom = intent.getStringExtra("clickFrom").toString()

        // set appbar
        when (clickFrom) {
            "project" -> {
                projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_CODE_INVITE_PARTICIPANT, "")
                position = AcademyOperationPref.loadString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, "")

                binding.appbarSelectInviteParticipant.tvAppbarTitle.text = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_NAME_INVITE_PARTICIPANT, "")
            }
            "position" -> {
                projectCode = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_CODE_CREATE_CLASS, "")
                position = AcademyOperationPref.loadString(AcademyOperationPrefConst.POSITION_INVITE_PARTICIPANT, "")

                binding.appbarSelectInviteParticipant.tvAppbarTitle.text = AcademyOperationPref.loadString(AcademyOperationPrefConst.POSITION_INVITE_PARTICIPANT, "")
            }
            else -> clickFrom
        }
        binding.appbarSelectInviteParticipant.tvAppbarSubTitle.text = "Tambah Peserta Training"
        binding.appbarSelectInviteParticipant.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // set recycler view horizontal
        val layoutManagerHorizontal = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedInviteParticipant.layoutManager = layoutManagerHorizontal

        // set recycler view vertical
        val layoutManagerVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListInviteParticipant.layoutManager = layoutManagerVertical

        // set scroll listener
        val scrollListener = object : EndlessScrollingRecyclerView(layoutManagerVertical) {
            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
                if (!isLastPage) {
                    page++
                    loadData()
                }
            }

        }
        binding.rvListInviteParticipant.addOnScrollListener(scrollListener)

        // set on click next
        binding.ivSelectInviteParticipant.setOnClickListener {
            if (listParticipant.size == 0) {
                binding.tvInfoSelectInviteParticipant.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed( {
                    binding.tvInfoSelectInviteParticipant.visibility = View.INVISIBLE
                }, 1500)
            } else {
                binding.tvInfoSelectInviteParticipant.visibility = View.INVISIBLE

                val intent = Intent(this, InviteParticipantTrainingActivity::class.java)
                intent.putParcelableArrayListExtra("selectedParticipants", listParticipant)
                intent.putExtra("clickFrom", "selectedParticipant")
                startActivity(intent)
            }
        }

        // set layout selected participant
        if (listParticipant.size == 0) {
            binding.llSelectedInviteParticipant.visibility = View.GONE
        } else {
            binding.llSelectedInviteParticipant.visibility = View.VISIBLE
        }

        loadData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.listParticipantInviteModel.observe(this) {
            if (it.code == 200) {
                if (it.data.empty) {
                    binding.rvListInviteParticipant.adapter = null
                    Toast.makeText(this, "tidak ada data participant", Toast.LENGTH_SHORT).show()
                } else {
                    isLastPage = it.data.last
                    if (page == 0) {
                        rvListAdapter = ParticipantClassInviteAdapter(
                            it.data.content as ArrayList<Content>
                        ).also { it.setListener(this) }
                        binding.rvListInviteParticipant.adapter = rvListAdapter
                    } else {
                        rvListAdapter.listParticipant.addAll(it.data.content)
                        rvListAdapter.notifyItemRangeChanged(
                            rvListAdapter.listParticipant.size - it.data.content.size,
                            rvListAdapter.listParticipant.size
                        )
                    }
                }
            } else {
                binding.rvListInviteParticipant.adapter = null
                Toast.makeText(this, "Gagal mengambil list participant", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.getListParticipantClass("", projectCode, position, page)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            page = 0
            isLastPage = false
            finish()
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onClickParticipant(
        participantNuc: String,
        participantName: String,
        email: String,
        role: String,
        position: String,
        jabatan: String,
        projectCode: String
    ) {

        val removeParticipant = listParticipant.find { it.userNuc == participantNuc }
        if (removeParticipant == null) {
            listParticipant.add(ContentParcelable(email, jabatan, position, participantName, projectCode, role, participantNuc))
        } else {
            listParticipant.remove(removeParticipant)
        }

        if (listParticipant.size == 0) {
            binding.llSelectedInviteParticipant.visibility = View.GONE
        } else {
            binding.llSelectedInviteParticipant.visibility = View.VISIBLE
            binding.tvCountSelectedInviteParticipant.text = "Invite ${listParticipant.size} Peserta"
        }

        rvSelectedAdapter = SelectedParticipantInviteAdapter(listParticipant).also { it.setListener(this) }
        binding.rvSelectedInviteParticipant.adapter = rvSelectedAdapter
    }

    @SuppressLint("SetTextI18n")
    override fun onClickSelected(participantNuc: String) {
        val removeParticipant = listParticipant.find { it.userNuc == participantNuc }
        if (removeParticipant != null) {
            listParticipant.remove(removeParticipant)
        }

        rvSelectedAdapter = SelectedParticipantInviteAdapter(listParticipant).also { it.setListener(this) }
        binding.rvSelectedInviteParticipant.adapter = rvSelectedAdapter

        if (listParticipant.size == 0) {
            binding.llSelectedInviteParticipant.visibility = View.GONE
        } else {
            binding.llSelectedInviteParticipant.visibility = View.VISIBLE
            binding.tvCountSelectedInviteParticipant.text = "Invite ${listParticipant.size} Peserta"
        }
    }

}