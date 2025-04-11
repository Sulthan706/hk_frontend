package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailArea

data class Data(
    val barcodePlottingArea: String,
    val codePlottingArea: String,
    val idLocation: Int,
    val idPosition: Int,
    val idProject: String,
    val idSubLocationArea: Int,
    val locationName: String,
    val operational: List<Operational>,
    val pengawas: Pengawas,
    val shiftDescription: String,
    val shiftId: Int,
    val subLocationId: Int,
    val subLocationName: String
)