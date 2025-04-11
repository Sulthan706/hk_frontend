package com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel

data class TeamleadResponseModel(
    val code: Int,
    val status: String,
    val data: List<DataEmployee>
)
