package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.detailAuditKualitas

data class Data(
    val administrasiIsScored: Boolean,
    val auditType: String,
    val createdBy: Int,
    val hasilKerjaIsScored: Boolean,
    val idAuditKualitas: Int,
    val manajemenProjectIsScored: Boolean,
    val manpowerIsScored: Boolean,
    val materialStockIsScored: Boolean,
    val organisasiIsScored: Boolean,
    val projectCode: String,
    val resultScore: String
)