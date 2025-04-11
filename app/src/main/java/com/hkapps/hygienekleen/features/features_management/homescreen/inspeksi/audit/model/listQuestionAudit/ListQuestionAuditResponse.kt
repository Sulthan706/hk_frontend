package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQuestionAudit

data class ListQuestionAuditResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)