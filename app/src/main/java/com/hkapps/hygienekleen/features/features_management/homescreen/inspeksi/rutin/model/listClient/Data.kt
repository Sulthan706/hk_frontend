package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient

import android.os.Parcel
import android.os.Parcelable

data class Data(
    val clientId: Int,
    val clientName: String,
    val clientEmail: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeInt(clientId)
        parcel.writeString(clientName)
        parcel.writeString(clientEmail)
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}