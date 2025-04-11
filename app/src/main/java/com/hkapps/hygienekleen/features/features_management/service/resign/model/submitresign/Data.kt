package com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign

data class Data(
    val adminId: Int,
    val cabang: String,
    val createdAtTurnOver: String,
    val idApproved: Int,
    val idTurnOver: Int,
    val isJob: Any,
    val kodeAwalProject: String,
    val kodeProjectSekarang: String,
    val namaKaryawan: String,
    val nucTurnOver: String,
    val posisiAwal: String,
    val posisiSekarang: String,
    val proposalType: String,
    val resignReasonId: Int,
    val statusApprove: String,
    val tanggalBerlaku: String,
    val tanggalPermintaan: String,
    val tipeTurnOver: String,
    val updateAtApprove: String,
    val updatedAtTurnOver: Any
)