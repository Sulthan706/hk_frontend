package com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hkapps.hygienekleen.databinding.ActivityListOperatorTeamleadBinding
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.spv.adapter.ViewPagerTeamSpvAdapter
import com.hkapps.hygienekleen.features.features_vendor.myteam.ui.teamlead.activity.SearchStaffMyteamTlActivity

class ListOperatorTeamleadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListOperatorTeamleadBinding
    private lateinit var viewPagerAdapter: ViewPagerTeamSpvAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOperatorTeamleadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set appbar layout
        val teamleadName:String = intent.getStringExtra("leaderName").toString()
        val leaderId:Int = intent.getIntExtra("leaderId", 0)

        binding.appbarOperatorTeamlead.tvAppbarTitle.text = teamleadName
        binding.appbarOperatorTeamlead.ivAppbarBack.setOnClickListener {
            super.onBackPressed()
            finish()
//            CarefastOperationPref.saveInt(CarefastOperationPrefConst.LEADER_ID, 0)
        }
        binding.appbarOperatorTeamlead.ivAppbarSearch.setOnClickListener {
            val i = Intent(this, SearchStaffMyteamTlActivity::class.java)
            startActivity(i)
            finish()
        }

        // set tab layout & viewpager
        viewPagerAdapter = ViewPagerTeamSpvAdapter(supportFragmentManager, this)
        this.binding.viewPagerOperatorTeamlead.adapter = viewPagerAdapter
        this.binding.tabLayoutOperatorTeamlead.setupWithViewPager(this.binding.viewPagerOperatorTeamlead)
        binding.viewPagerOperatorTeamlead.currentItem = 0
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
//            CarefastOperationPref.saveInt(CarefastOperationPrefConst.LEADER_ID, 0)
    }
}