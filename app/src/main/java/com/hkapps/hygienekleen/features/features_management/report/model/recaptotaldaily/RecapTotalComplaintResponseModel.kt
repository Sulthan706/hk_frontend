package com.hkapps.hygienekleen.features.features_management.report.model.recaptotaldaily

data class RecapTotalComplaintResponseModel(
    val code: Int,
    val `data`: List<DataCardRecap>,
    val status: String
)