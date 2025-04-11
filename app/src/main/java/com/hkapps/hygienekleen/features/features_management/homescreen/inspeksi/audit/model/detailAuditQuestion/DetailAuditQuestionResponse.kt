package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditQuestion

data class DetailAuditQuestionResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)