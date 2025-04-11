package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod

data class Content(
    val branchName: String,
    val projectName: String,
    val projectCode: String,
    val projectAddress: String,
    val projectLatitude: String,
    val projectLongitude: String,
    val projectRadius: Int,
    val idRkbBod: Int,
    val scanIn: String,
    val scanOut: String
)