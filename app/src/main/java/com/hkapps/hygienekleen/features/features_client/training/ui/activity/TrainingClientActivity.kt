package com.hkapps.hygienekleen.features.features_client.training.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityTrainingClientBinding

class TrainingClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingClientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTrainingClientBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondary_color)


        binding.llBtnCalPencapaianTraining.setOnClickListener {
//            val i = Intent(this, AgendaTrainingActivity::class.java)
//            startActivity(i)
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
        }

//        binding.rlBtnListPencapaianTraining.setOnClickListener{
//            val i = Intent(this, ListPencapaianTrainingActivity::class.java)
//            startActivity(i)
//        }

        binding.ivBtnBackTraining.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}