package com.hkapps.academy.features.features_participants.training.model.listTraining

data class Content(
    val alternateLocation: String,
    val appLink: String,
    val appName: String,
    val category: String,
    val durationInMinute: Int,
    val jenisKelas: String,
    val locationDescription: String,
    val moduleCode: String,
    val moduleId: Int,
    val moduleName: String,
    val participant: String,
    val projectCode: String,
    val quota: Any,
    val trainerName: String,
    val trainerNuc: String,
    val trainingDate: String,
    val trainingEnd: String,
    val trainingId: Int,
    val trainingLocationCode: String,
    val trainingName: String,
    val trainingStart: String
)