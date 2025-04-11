package com.hkapps.hygienekleen.features.splash.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SplashModel(
    @SerializedName("splash_url")
    @Expose val
    splashUrl: String
)
