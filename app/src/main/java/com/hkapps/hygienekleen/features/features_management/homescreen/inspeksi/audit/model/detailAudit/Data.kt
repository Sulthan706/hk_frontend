package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAudit

data class Data(
    val auditCreatorName: String,
    val branchName: String,
    val date: String,
    val idAuditor: Int,
    val idReport: Int,
    val l1Checked: Boolean,
    val l1Id: Int,
    val l2Checked: Boolean,
    val l2Id: Int,
    val l3Checked: Boolean,
    val l3Id: Int,
    val periodDate: String,
    val projectCode: String,
    val projectName: String
)