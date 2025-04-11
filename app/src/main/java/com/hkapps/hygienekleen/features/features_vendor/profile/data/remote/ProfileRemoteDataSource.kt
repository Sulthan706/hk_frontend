package com.hkapps.hygienekleen.features.features_vendor.profile.data.remote
import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import io.reactivex.Single
import okhttp3.MultipartBody

interface ProfileRemoteDataSource {
    fun getProfileRDS(employeeId: Int): Single<ProfileEmployeeResponseModel>

    fun updateProfileRDS(
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

    fun changePassRDS(
        employeeId: Int,
        requestModel: ChangePassRequestModel
    ): Single<ChangePassResponseModel>

    fun updatePhotoProfile(
        userId: Int,
        img: MultipartBody.Part
    ): Single<UpdateProfileResponseModel>

}