package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch

data class AllBranchVisitResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)