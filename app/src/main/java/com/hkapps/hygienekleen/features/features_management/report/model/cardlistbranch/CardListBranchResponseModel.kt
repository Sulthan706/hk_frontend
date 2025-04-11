package com.hkapps.hygienekleen.features.features_management.report.model.cardlistbranch

data class CardListBranchResponseModel(
    val code: Int,
    val `data`: List<DataCardBranch>,
    val status: String
)