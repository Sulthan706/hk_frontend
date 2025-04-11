package com.hkapps.hygienekleen.features.features_client.myteam.ui.new_.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkapps.hygienekleen.databinding.ItemAbsentCfteamBinding
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetailhistoryperson.TipeJadwal

class MyTeamAbsentAdapter(private var tipeJadwal: List<TipeJadwal>) :
    RecyclerView.Adapter<MyTeamAbsentAdapter.ViewHolder>() {
    private var context: Context? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyTeamAbsentAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemAbsentCfteamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(val binding: ItemAbsentCfteamBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            tipeJadwal.forEach {
                if (it.tipeJadwal == "ACTUAL SCHEDULE"){
                    binding.tvAbsenMasuk.text = it.jamMulai
                    binding.tvAbsenKeluar.text = it.jamAkhir

                } else {

                    binding.tvLemburGantiResult.text = "Lembur Ganti : ${it.jamMulai}-${it.jamAkhir}"
                    binding.tvLemburGantiStatus.text = it.statusSchedule
                    val textView = binding.tvLemburGantiStatus
                    when (it.statusSchedule) {
                        "LUPA_ABSEN" -> {
                            textView.text = "Lupa Absen"
                            textView.setTextColor(Color.parseColor("#FF5656"))
                        }
                        "TIDAK_HADIR" -> {
                            textView.text = "Tidak Hadir"
                            textView.setTextColor(Color.parseColor("#FF5656"))
                        }
                        "HADIR" -> {
                            textView.text = "Hadir"
                            textView.setTextColor(Color.parseColor("#00BD8C"))
                        }
                        "SEDANG_BEKERJA" -> {
                            textView.text = "Sedang Bekerja"
                            textView.setTextColor(Color.parseColor("#167FFC"))
                        }
                        "LIBUR" -> {
                            textView.text = "Libur"
                            textView.setTextColor(Color.parseColor("#607080"))
                        }
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return 1
    }
}