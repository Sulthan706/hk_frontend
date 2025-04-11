package com.hkapps.hygienekleen.features.features_management.report.model.mainreportctalk

data class Data(
    val listComplaints: ListComplaints,
    val totalClosed: Int,
    val totalComplaintToday: Int,
    val totalComplaints: Int,
    val totalDone: Int,
    val totalFasilitasRusak: Int,
    val totalGangguanHama: Int,
    val totalKeamanan: Int,
    val totalKebersihan: Int,
    val totalKeselamatanKesehatan: Int,
    val totalOnprogress: Int,
    val totalSikapDanPerilaku: Int,
    val totalTenagaKerja: Int,
    val totalWaiting: Int
)