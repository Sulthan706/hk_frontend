package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject

import com.google.gson.annotations.SerializedName

data class ListProjectInfo(
    @SerializedName("projects")
    val projects: List<ListProjectData>
)
