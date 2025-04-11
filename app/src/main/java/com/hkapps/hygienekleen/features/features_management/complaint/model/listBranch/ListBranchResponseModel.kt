package com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch

data class ListBranchResponseModel(
    val code: Int,
    val status: String,
    val data: List<Data>,
)