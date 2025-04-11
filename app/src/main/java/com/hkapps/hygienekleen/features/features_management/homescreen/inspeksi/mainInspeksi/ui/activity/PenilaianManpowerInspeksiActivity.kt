package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityPenilaianManpowerInspeksiBinding

class PenilaianManpowerInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPenilaianManpowerInspeksiBinding
    private var totalScore: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPenilaianManpowerInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set app bar
        binding.appbarPenilaianManpowerInspeksi.tvAppbarTitle.text = "Penilaian Manpower"
        binding.appbarPenilaianManpowerInspeksi.ivAppbarBack.setOnClickListener {
            onBackPressed()
        }

        // set default button submit
        binding.btnSubmitDisablePenilaianManpowerInspeksi.visibility = View.VISIBLE
        binding.btnSubmitEnablePenilaianManpowerInspeksi.visibility = View.GONE

        val objectValue = resources.getStringArray(R.array.penilaianInspeksi)
        val adapter = ArrayAdapter (this, R.layout.spinner_item, objectValue)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerGreeting1PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGreeting1PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val greeting1 = objectValue[position]
                binding.tvGreeting1PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when(position) {
                    0 -> {
                        binding.tvGreeting1PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGreeting1PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGreeting1PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGreeting1PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGreeting1PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGreeting2PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGreeting2PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val greeting2 = objectValue[position]
                binding.tvGreeting2PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when(position) {
                    0 -> {
                        binding.tvGreeting2PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGreeting2PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGreeting2PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGreeting2PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGreeting2PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGreeting3PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGreeting3PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val greeting3 = objectValue[position]
                binding.tvGreeting3PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGreeting3PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGreeting3PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGreeting3PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGreeting3PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGreeting3PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming1PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming1PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom1 = objectValue[position]
                binding.tvGrooming1PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming1PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGrooming1PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGrooming1PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGrooming1PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGrooming1PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming2PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming2PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom2 = objectValue[position]
                binding.tvGrooming2PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming2PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGrooming2PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGrooming2PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGrooming2PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGrooming2PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming3PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming3PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom3 = objectValue[position]
                binding.tvGrooming3PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming3PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGrooming3PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGrooming3PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGrooming3PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGrooming3PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming4PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming4PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom4 = objectValue[position]
                binding.tvGrooming4PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming4PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGrooming4PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGrooming4PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGrooming4PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGrooming4PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming5PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming5PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom5 = objectValue[position]
                binding.tvGrooming5PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming5PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                    }
                    1 -> {
                        binding.tvGrooming5PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                    }
                    2 -> {
                        binding.tvGrooming5PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                    }
                    3 -> {
                        binding.tvGrooming5PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                    }
                    4 -> {
                        binding.tvGrooming5PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.spinnerGrooming6PenilaianManpowerInspeksi.adapter = adapter
        binding.spinnerGrooming6PenilaianManpowerInspeksi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                long: Long
            ) {
                val groom6 = objectValue[position]
                binding.tvGrooming6PenilaianManpowerInspeksi.setTextColor(resources.getColor(R.color.black2))
                when (position) {
                    0 -> {
                        binding.tvGrooming6PenilaianManpowerInspeksi.text = "3"
                        totalScore += 3
                        binding.tvTotalScorePenilaianManpowerInspeksi.text = "$totalScore"
                    }
                    1 -> {
                        binding.tvGrooming6PenilaianManpowerInspeksi.text = "2"
                        totalScore += 2
                        binding.tvTotalScorePenilaianManpowerInspeksi.text = "$totalScore"
                    }
                    2 -> {
                        binding.tvGrooming6PenilaianManpowerInspeksi.text = "1"
                        totalScore += 1
                        binding.tvTotalScorePenilaianManpowerInspeksi.text = "$totalScore"
                    }
                    3 -> {
                        binding.tvGrooming6PenilaianManpowerInspeksi.text = "0"
                        totalScore += 0
                        binding.tvTotalScorePenilaianManpowerInspeksi.text = "$totalScore"
                    }
                    4 -> {
                        binding.tvGrooming6PenilaianManpowerInspeksi.text = "-1"
                        totalScore -= 1
                        binding.tvTotalScorePenilaianManpowerInspeksi.text = "$totalScore"
                    }
                }

                // set enable button submit
                binding.btnSubmitDisablePenilaianManpowerInspeksi.visibility = View.GONE
                binding.btnSubmitEnablePenilaianManpowerInspeksi.visibility = View.VISIBLE
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}