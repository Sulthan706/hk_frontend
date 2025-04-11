package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea

data class Content(
    val idSubLocationArea: Int,
    val idProject: String,
    val idLocation: Int,
    val locationName: String,
    val subLocationId: Int,
    val subLocationName: String,
    val idPosition: Int,
    val codePlottingArea: String,
    val barcodePlottingArea: String,
    val shiftId: Int,
    val shiftDescription: String,
    val isAreaChecklist: String
)