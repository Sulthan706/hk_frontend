package com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant

import android.os.Parcel
import android.os.Parcelable

data class ContentParcelable(
    val email: String,
    val jabatan: String,
    val levelJabatan: String,
    val name: String,
    val projectCode: String,
    val role: String,
    val userNuc: String
): Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(jabatan)
        parcel.writeString(levelJabatan)
        parcel.writeString(name)
        parcel.writeString(projectCode)
        parcel.writeString(role)
        parcel.writeString(userNuc)
    }

    companion object CREATOR : Parcelable.Creator<ContentParcelable> {
        override fun createFromParcel(parcel: Parcel): ContentParcelable {
            return ContentParcelable(parcel)
        }

        override fun newArray(size: Int): Array<ContentParcelable?> {
            return arrayOfNulls(size)
        }

    }
}