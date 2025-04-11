package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit

data class Data(
    val questionId: Int,
    val questionName: String,
    val score: Boolean,
    val scoreStatus: String,
    val idSubmitQuestion: Int
)