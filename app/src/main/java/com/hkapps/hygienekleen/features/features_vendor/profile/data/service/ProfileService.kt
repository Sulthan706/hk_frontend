package com.hkapps.hygienekleen.features.features_vendor.profile.data.service

import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {

    @GET("/api/v1/employee/profile")
    fun getProfileDataApi(
        @Query("employeeId") employeeId: Int
    ): Single<ProfileEmployeeResponseModel>

    @PUT("/api/v1/employee/profile/update")
    fun updateProfileDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("employeeEmail") employeeEmail: String,
        @Query("employeePhone") employeePhone: String,
        @Query("employeePhone2") employeePhone2: String,
        @Query("employeeAddress") employeeAddress: String,
        @Query("employeeKtpAddress") employeeKtpAddress: String,
        @Query("employeeBirthDate") employeeBirthDate: String,
        @Query("employeePlaceOfBirth") employeePlaceOfBirth: String,
        @Query("employeeGender") employeeGender: String,
        @Query("employeeMarriageStatus") employeeMarriageStatus: String,
        @Query("employeeMotherName") employeeMotherName: String,
        @Query("employeeReligion") employeeReligion: String,
        @Query("employeeCountChildren") employeeCountChildren: String,
        @Query("employeeNik") employeeNik: String,
    ): Single<UpdateProfileResponseModel>

    //put updatepass
    @PUT("api/v1/employee/profile/update-password")
    fun changePassApi(
        @Query("employeeId") employeeId: Int,
        @Body changePassRequestModel: ChangePassRequestModel
    ): Single<ChangePassResponseModel>

    @Multipart
    @PUT("/api/v1/employee/profile/update/image")
    fun updatePhotoProfile(
        @Query("employeeId") userId: Int,
        @Part img: MultipartBody.Part
    ): Single<UpdateProfileResponseModel>
}