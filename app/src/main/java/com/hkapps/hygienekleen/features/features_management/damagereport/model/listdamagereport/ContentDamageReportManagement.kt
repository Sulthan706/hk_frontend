package com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport

data class ContentDamageReportManagement(
    val fotoDepan: String,
    val projectName: String,
    val fotoKerusakan: String,
    val gambarDetailBak: String,
    val idDetailBakMesin: Int,
    val jenisMesin: String,
    val keteranganAssets: String,
    val keteranganBak: String,
    val kodeBak: String,
    val kodeMesin: String,
    val merkMesin: String,
    val tglDibuat: String,
    val tglWaktuDibuat: String,
    val tipeBapp: String,
    val validasi: String,
    val validasiManagement: String,
    val validasiEmployee: String
)