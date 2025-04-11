package com.hkapps.hygienekleen.features.features_vendor.profile.data.remote

import com.hkapps.hygienekleen.features.features_vendor.profile.data.service.ProfileService
import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(private val service: ProfileService) :
    ProfileRemoteDataSource {

    override fun getProfileRDS(employeeId: Int): Single<ProfileEmployeeResponseModel> {
        return service.getProfileDataApi(employeeId)
    }

    override fun updateProfileRDS(
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
        return service.updateProfileDataApi(employeeId, employeeEmail, employeePhone, employeePhone2, employeeAddress, employeeKtpAddress, employeeBirthDate, employeePlaceOfBirth, employeeGender, employeeMarriageStatus, employeeMotherName, employeeReligion, employeeCountChildren, employeeNik)
    }

    override fun changePassRDS(
        employeeId: Int,
        requestModel: ChangePassRequestModel
    ): Single<ChangePassResponseModel> {
        return service.changePassApi(employeeId, requestModel)
    }

    override fun updatePhotoProfile(
        userId: Int,
        img: MultipartBody.Part
    ): Single<UpdateProfileResponseModel> {
        return service.updatePhotoProfile(userId, img)
    }

}