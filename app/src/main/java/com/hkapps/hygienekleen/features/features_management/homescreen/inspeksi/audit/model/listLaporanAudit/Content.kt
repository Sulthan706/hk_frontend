package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listLaporanAudit

data class Content(
    val auditCreatorName: String,
    val idAuditor: Int,
    val idReport: Int,
    val l1Checked: Boolean,
    val l2Checked: Boolean,
    val l3Checked: Boolean,
    val periodDate: String,
    val date: String
)