package com.hkapps.academy.features.features_participants.homescreen.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityHomeTrainingBinding
import com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment.AchievementFragment
import com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment.HomeTrainingFragment
import com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment.InfoFragment
import com.hkapps.academy.features.features_participants.homescreen.home.ui.fragment.MyTrainingFragment

class HomeTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavTraining.background = null
//        binding.bottomNavTraining.menu.getItem(1).isEnabled = false
        navigateDefaultFragment()

        binding.bottomNavTraining.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuHomeTraining -> {
                    if (binding.bottomNavTraining.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(HomeTrainingFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuMyTraining -> {
                    if (binding.bottomNavTraining.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(MyTrainingFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuAchievement -> {
                    if (binding.bottomNavTraining.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(AchievementFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuInfo -> {
                    if (binding.bottomNavTraining.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(InfoFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHomeTraining, fragment)
            .commit()
    }

    private fun navigateDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHomeTraining, HomeTrainingFragment())
            .commit()
    }
}