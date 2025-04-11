package com.hkapps.hygienekleen.features.features_vendor.profile.data.repository

import com.hkapps.hygienekleen.features.features_vendor.profile.data.remote.ProfileRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val remoteDataSource: ProfileRemoteDataSource) :
    ProfileRepository {

    override fun getProfile(employeeId: Int): Single<ProfileEmployeeResponseModel> {
        return remoteDataSource.getProfileRDS(employeeId)
    }

    override fun updateProfile(
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
    ): Single<UpdateProfileResponseModel> {
        return remoteDataSource.updateProfileRDS(employeeId,
            employeeEmail,
            employeePhone,
            employeePhone2,
            employeeAddress,
            employeeKtpAddress,
            employeeBirthDate,
            employeePlaceOfBirth,
            employeeGender,
            employeeMarriageStatus,
            employeeMotherName,
            employeeReligion,
            employeeCountChildren,
            employeeNik)
    }


    override fun changePass(
        employeeId: Int,
        changepass: ChangePassRequestModel
    ): Single<ChangePassResponseModel> {
        return remoteDataSource.changePassRDS(employeeId, changepass)
    }

    override fun updatePhotoProfile(
        userId: Int,
        img: MultipartBody.Part
    ): Single<UpdateProfileResponseModel> {
        return remoteDataSource.updatePhotoProfile(userId, img)
    }

}