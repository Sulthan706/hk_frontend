package com.hkapps.hygienekleen.features.features_management.homescreen.home.model.typevaccine

data class ListTypeVaccineMngmtResponseModel(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)