package com.hkapps.academy.features.features_trainer.homescreen.home.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hkapps.academy.R
import com.hkapps.academy.databinding.ActivityHomeTrainerBinding
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment.AchievementTrainerFragment
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment.HomeTrainerFragment
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment.InfoTrainerFragment
import com.hkapps.academy.features.features_trainer.homescreen.home.ui.fragment.MyClassFragment

class HomeTrainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeTrainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavTrainer.background = null
        navigateDefaultFragment()

        binding.bottomNavTrainer.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuHomeTrainer -> {
                    if (binding.bottomNavTrainer.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(HomeTrainerFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuClassTrainer -> {
                    if (binding.bottomNavTrainer.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(MyClassFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuAchievementTrainer -> {
                    if (binding.bottomNavTrainer.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(AchievementTrainerFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menuInfoTrainer -> {
                    if (binding.bottomNavTrainer.selectedItemId == it.itemId) {
                        //doNothing
                    } else {
                        replaceFragment(InfoTrainerFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHomeTrainer, fragment)
            .commit()
    }

    private fun navigateDefaultFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flHomeTrainer, HomeTrainerFragment())
            .commit()
    }
}