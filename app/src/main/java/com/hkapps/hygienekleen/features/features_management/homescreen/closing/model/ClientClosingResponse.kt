package com.hkapps.hygienekleen.features.features_management.homescreen.closing.model

import android.os.Parcel
import android.os.Parcelable

data class ClientClosingResponse(
    val code: Int,
    val `data`: List<ClientClosing>,
    val status: String
)

data class ClientClosing(
    val clientId: Int,
    val clientName: String,
    val levelJabatan: String?,
    val projectCode: String,
    val email: String,
    val photoProfile: String?,
    val status: String,
    val project: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(clientId)
        parcel.writeString(clientName)
        parcel.writeString(levelJabatan)
        parcel.writeString(projectCode)
        parcel.writeString(email)
        parcel.writeString(photoProfile)
        parcel.writeString(status)
        parcel.writeString(project)
    }

    companion object CREATOR : Parcelable.Creator<ClientClosing> {
        override fun createFromParcel(parcel: Parcel): ClientClosing {
            return ClientClosing(parcel)
        }

        override fun newArray(size: Int): Array<ClientClosing?> {
            return arrayOfNulls(size)
        }
    }
}
