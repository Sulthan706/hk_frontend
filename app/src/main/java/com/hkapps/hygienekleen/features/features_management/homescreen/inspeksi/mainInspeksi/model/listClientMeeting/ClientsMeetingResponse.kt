package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting

data class ClientsMeetingResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)