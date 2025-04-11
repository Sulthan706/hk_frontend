package com.hkapps.hygienekleen.features.features_client.report.model.listArea

data class Content(
    val barcodePlottingArea: String,
    val codePlottingArea: String,
    val idLocation: Int,
    val idPosition: Int,
    val idProject: String,
    val idSubLocationArea: Int,
    val isAreaChecklist: String,
    val locationName: String,
    val shiftDescription: String,
    val shiftId: Int,
    val subLocationId: Int,
    val subLocationName: String
)