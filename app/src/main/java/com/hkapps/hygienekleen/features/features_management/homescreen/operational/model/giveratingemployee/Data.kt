package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.giveratingemployee

data class Data(
    val createdAt: String,
    val employeeId: Int,
    val jobCode: String,
    val projectCode: String,
    val rating: Int,
    val ratingByUserId: Int,
    val ratingByUserRole: String,
    val ratingId: Int
)