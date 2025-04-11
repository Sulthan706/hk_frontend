package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation

data class Validation(
    val projectCode : String,
    val projectName : String,
    val branchCode : String,
    val branchName : String,
    val scanIn : String,
    val scanOut : String,
)
