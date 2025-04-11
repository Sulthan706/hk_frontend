package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.model.listQualityAudit

data class Data(
    val administrasiIsScored: Boolean,
    val hasilKerjaIsScored: Boolean,
    val manajemenProjectIsScored: Boolean,
    val manpowerIsScored: Boolean,
    val materialStockIsScored: Boolean,
    val organisasiIsScored: Boolean
)