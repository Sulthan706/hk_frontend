package com.hkapps.hygienekleen.features.facerecog.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    val name : String,
    val images : String
): Parcelable
