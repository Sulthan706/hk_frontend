package com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor

data class Data(
    val fotoDepan: String,
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
    val validasiEmployee: String,
    val validasiManagement: String
)