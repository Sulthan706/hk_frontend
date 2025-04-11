package com.hkapps.academy.features.features_trainer.myclass.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityFinishedCreateClassBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.activity.HomeTrainerActivity
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst

class FinishedCreateClassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishedCreateClassBinding

    private val category = AcademyOperationPref.loadString(AcademyOperationPrefConst.CATEGORY_CREATE_CLASS, "")
    private var clickFrom = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishedCreateClassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get clickFrom param from intent
        clickFrom = intent.getStringExtra("clickFrom").toString()

        // validate layout from clickFrom
        when(clickFrom) {
            "InviteParticipant" -> {
                binding.btnInviteFinishedCreateClass.visibility = View.GONE
                binding.tvInfoFinishedCreateClass.text = "Anda telah menambahkan peserta ke jadwal training."
                binding.tvFinishedCreateClass.text = "Lihat Detail Training"
            }
            "SummaryCreateClass" -> {
                when(category) {
                    "PRIVATE" -> binding.btnInviteFinishedCreateClass.visibility = View.VISIBLE
                    "PUBLIK" -> binding.btnInviteFinishedCreateClass.visibility = View.GONE
                }

                binding.tvInfoFinishedCreateClass.text = getString(R.string.info_finished_create_class)
                binding.tvFinishedCreateClass.text = "Kembali ke Daftar Kelas"
            }
        }

        // button invite participant
        binding.btnInviteFinishedCreateClass.setOnClickListener {
            val intent = Intent(this, InviteParticipantTrainingActivity::class.java)
            intent.putExtra("clickFrom", "finishedCreateClass")
            startActivity(intent)
        }

        // button return
        binding.btnReturnFinishedCreateClass.setOnClickListener {
            startActivity(Intent(this, HomeTrainerActivity::class.java))
            finishAffinity()
        }
    }
}