package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailSubmitPenilaian

data class Data(
    val auditType: String,
    val createdBy: Int,
    val date: String,
    val description: String,
    val idSubmitForm: Int,
    val image: String,
    val projectCode: String,
    val questionId: Int,
    val questionType: String,
    val scoreStatus: String
)