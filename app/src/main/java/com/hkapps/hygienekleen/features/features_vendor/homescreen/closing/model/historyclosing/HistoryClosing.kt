package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing

data class HistoryClosing(
    val rowNumber : Int,
    val idItemPekerjaan : Int,
    val date : String,
    val shift : String,
    val closingAt : String
)
