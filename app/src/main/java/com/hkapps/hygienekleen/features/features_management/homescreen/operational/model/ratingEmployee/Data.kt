package com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.ratingEmployee

data class Data(
    val employeeCode: String,
    val employeeId: Int,
    val employeeName: String,
    val employeePhotoProfile: String,
    val jobCode: String?,
    val jobName: String,
    val mountOfRating: Int,
    val projectCode: String,
    val ratingByUserId: Int,
    val ratingByUserRole: String,
    val ratingId: Int,
    val resultRating: String,
    val totalRating: Int
)