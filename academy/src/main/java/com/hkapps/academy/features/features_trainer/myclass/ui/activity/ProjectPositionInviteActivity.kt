package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityProjectPositionInviteBinding
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.Data
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.PositionsInviteClassAdapter
import com.hkapps.academy.features.features_trainer.myclass.ui.adapter.ProjectsInviteClassAdapter
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst

class ProjectPositionInviteActivity : AppCompatActivity(),
    PositionsInviteClassAdapter.ListPositionCallBack,
    ProjectsInviteClassAdapter.ListProjectCallBack
{

    private lateinit var binding: ActivityProjectPositionInviteBinding
    private lateinit var rvAdapterProject: ProjectsInviteClassAdapter
    private lateinit var rvAdapterPosition: PositionsInviteClassAdapter

    private val projectCodeCreateClass = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_CODE_CREATE_CLASS, "")
    private val projectNameCreateClass = AcademyOperationPref.loadString(AcademyOperationPrefConst.PROJECT_NAME_CREATE_CLASS, "")
    private val positionCreateClass = AcademyOperationPref.loadString(AcademyOperationPrefConst.PARTICIPANT_CREATE_CLASS, "")
    private var clickFrom = ""

    private val viewModel: ClassTrainerViewModel by lazy {
        ViewModelProviders.of(this).get(ClassTrainerViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectPositionInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get string from intent
        clickFrom = intent.getStringExtra("clickFrom").toString()

        // set app bar
        binding.appbarProjectPositionInvite.tvAppbarTitle.text = "Invite Peserta Training"
        binding.appbarProjectPositionInvite.ivAppbarBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding.appbarProjectPositionInvite.ivAppbarSearch.setOnClickListener {
            startActivity(Intent(this, SearchParticipantActivity::class.java))
        }

        when (clickFrom) {
            "project" -> {
                binding.tvPilihProjectPositionInvite.text = "Pilih Project"
                binding.svProjectPositionInvite.visibility = View.INVISIBLE
                if (projectCodeCreateClass == "Semua") {
                    // set visible icon search
                    binding.ivSearchProjectPositionInvite.visibility = View.VISIBLE
                    binding.ivSearchProjectPositionInvite.setOnClickListener {
                        if (binding.svProjectPositionInvite.visibility == View.INVISIBLE) {
                            binding.tvPilihProjectPositionInvite.visibility = View.GONE
                            binding.svProjectPositionInvite.visibility = View.VISIBLE
                            binding.ivSearchProjectPositionInvite.setImageResource(R.drawable.ic_closes_grey2client)


                        } else {
                            binding.svProjectPositionInvite.visibility = View.INVISIBLE
                            binding.tvPilihProjectPositionInvite.visibility = View.VISIBLE
                            binding.ivSearchProjectPositionInvite.setImageResource(R.drawable.ic_search_grey2client)
                        }
                    }

                    viewModel.getProjectsInviteParticipant("")
                } else {
                    // hide icon search
                    binding.ivSearchProjectPositionInvite.visibility = View.GONE

                    val listProject = ArrayList<Data>()
                    listProject.add(Data(0, "", projectCodeCreateClass, projectNameCreateClass))
                    rvAdapterProject = ProjectsInviteClassAdapter(listProject).also { it.setListener(this) }
                    binding.rvProjectPositionInvite.adapter = rvAdapterProject
                }
            }
            "position" -> {
                binding.tvPilihProjectPositionInvite.text = "Pilih Jabatan"
                binding.svProjectPositionInvite.visibility = View.INVISIBLE
                binding.ivSearchProjectPositionInvite.visibility = View.GONE
                if (positionCreateClass == "Semua") {
                    val listPosition = ArrayList<String>()
                    listPosition.add("Operator")
                    listPosition.add("Pengawas")
                    listPosition.add("Management")
                    listPosition.add("BOD")
                    listPosition.add("CEO")

                    rvAdapterPosition = PositionsInviteClassAdapter(listPosition).also { it.setListener(this) }
                    binding.rvProjectPositionInvite.adapter = rvAdapterPosition
                } else {
                    val listPosition = ArrayList<String>()
                    listPosition.add(positionCreateClass)

                    rvAdapterPosition = PositionsInviteClassAdapter(listPosition).also { it.setListener(this) }
                    binding.rvProjectPositionInvite.adapter = rvAdapterPosition
                }
            }
            else -> {
                binding.tvPilihProjectPositionInvite.text = "error"
            }
        }

        // set recycler view
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvProjectPositionInvite.layoutManager = layoutManager

        // set scroll listener
//        val scrollListener = object : EndlessScrollingRecyclerView(layoutManager) {
//            override fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView) {
//                if (!isLastPage) {
//                    page++
//                    loadData()
//                }
//            }
//
//        }
//        binding.rvProjectPositionInvite.addOnScrollListener(scrollListener)



        setObserver()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        viewModel.projectsInviteModel.observe(this) {
            if (it.code == 200) {
                rvAdapterProject = ProjectsInviteClassAdapter(
                    it.data as ArrayList<Data>
                ).also { it.setListener(this) }
                binding.rvProjectPositionInvite.adapter = rvAdapterProject

                // set search view
                binding.svProjectPositionInvite.setOnQueryTextListener( object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        rvAdapterProject.filter.filter(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        rvAdapterProject.filter.filter(newText)
                        return true
                    }

                })
            } else {
                binding.rvProjectPositionInvite.adapter = null
                Toast.makeText(this, "Gagal mengambil data list project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }

    }

    override fun onClickPosition(position: String) {
        AcademyOperationPref.saveString(AcademyOperationPrefConst.POSITION_INVITE_PARTICIPANT, position)

        val intent = Intent(this, SelectInviteParticipantActivity::class.java)
        intent.putExtra("clickFrom", "position")
        startActivity(intent)
    }

    override fun onClickProject(projectCode: String, projectName: String) {
        AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_CODE_INVITE_PARTICIPANT, projectCode)
        AcademyOperationPref.saveString(AcademyOperationPrefConst.PROJECT_NAME_INVITE_PARTICIPANT, projectName)

        val intent = Intent(this, SelectInviteParticipantActivity::class.java)
        intent.putExtra("clickFrom", "project")
        startActivity(intent)
    }
}