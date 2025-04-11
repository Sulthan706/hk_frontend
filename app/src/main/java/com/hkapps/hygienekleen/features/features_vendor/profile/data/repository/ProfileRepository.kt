package com.hkapps.hygienekleen.features.features_vendor.profile.data.repository

import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import io.reactivex.Single
import okhttp3.MultipartBody

interface ProfileRepository {
    fun getProfile(employeeId: Int): Single<ProfileEmployeeResponseModel>

    fun updateProfile(
        employeeId: Int,
        employeeEmail: String,
        employeePhone: String,
        employeePhone2: String,
        employeeAddress: String,
        employeeKtpAddress: String,
        employeeBirthDate: String,
        employeePlaceOfBirth: String,
        employeeGender: String,
        employeeMarriageStatus: String,
        employeeMotherName: String,
        employeeReligion: String,
        employeeCountChildren: String,
        employeeNik: String
    ): Single<UpdateProfileResponseModel>

    fun changePass(employeeId: Int, changepass: ChangePassRequestModel): Single<ChangePassResponseModel>

    fun updatePhotoProfile(
        userId: Int,
        img: MultipartBody.Part
    ): Single<UpdateProfileResponseModel>

}