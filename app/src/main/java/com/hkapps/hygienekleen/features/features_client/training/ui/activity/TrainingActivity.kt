package com.hkapps.hygienekleen.features.features_client.training.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.databinding.ActivityTrainingBinding
import com.hkapps.hygienekleen.features.features_client.training.viewmodel.TrainingClientViewModel

class TrainingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingBinding
    private val viewModel: TrainingClientViewModel by lazy {
        ViewModelProviders.of(this).get(TrainingClientViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setObserver()
        //oncreate
    }

    //fun
    private fun setObserver(){
        viewModel.getListShift(projectCode = "01141403")
        viewModel.getListShiftTrainingViewModel().observe(this){
            if (it.code == 200){
                Toast.makeText(this, "ke hit cuk", Toast.LENGTH_SHORT).show()
            }
        }
    }
}