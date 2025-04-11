package com.hkapps.hygienekleen.features.facerecog.data.remote

import com.hkapps.hygienekleen.features.facerecog.data.service.FaceRecogService
import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.ListRandomGestureResponse
import com.hkapps.hygienekleen.features.facerecog.model.loginforfailure.ErrorFailureFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.profilevendor.GetProfileUserResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceRecogRemoteDataSourceImpl @Inject constructor(private val service: FaceRecogService) :
    FaceRecogRemoteDataSource {


    override fun getStatsRegisFaceRecog(employeeId: Int): Single<StatsRegisFaceResponseModel> {
        return service.getStatsRegisFaceRecog(employeeId)
    }

    override fun apiRegisterBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.apiRegisterBoth(employeeId,file)
    }

    override fun apiVerifyBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.apiVerifyBoth(employeeId, file)
    }

    override fun postRegisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.postRegisFaceRecog(employeeId, file)
    }

    override fun verifyFaceRecog(
        file: MultipartBody.Part,
        employeeId: Int
    ): Single<StatsRegisFaceResponseModel> {
        return service.verifyFaceRecog(file, employeeId)
    }

    override fun registerFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.registerFaceNewEmployee(employeeId, file)
    }

    override fun verifyUserFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.verifyUserFaceNewEmployee(employeeId, file)
    }


    override fun getStatsRegisManagementRecog(adminMaster: Int): Single<StatsRegisFaceResponseModel> {
        return service.getStatsRegisManagementRecog(adminMaster)
    }

    override fun apiRegisterManagementBoth(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
       return service.apiRegisterManagementBoth(adminMasterId, file)
    }

    override fun apiVerifyManagementBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.apiVerifyManagementBoth(employeeId, file)
    }

    override fun registerFaceNewManagement(
        nuc: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.registerFaceNewManagement(nuc,file)
    }

    override fun verifyUserFaceNewManagement(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.verifyUserFaceNewManagement(adminMasterId, file)
    }

    override fun postRegisManagementFaceRecog(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return service.postRegisManagementFaceRecog(adminMasterId,file)
    }

    override fun verifyManagementFaceRecog(
        file: MultipartBody.Part,
        adminMasterId: Int
    ): Single<StatsRegisFaceResponseModel> {
        return service.verifyManagementFaceRecog(file,adminMasterId)
    }

    override fun getProfileUser(employeeId: Int): Single<GetProfileUserResponseModel> {
        return service.getProfileUser(employeeId)
    }

    override fun postLoginForErrorFace(
        employeeId: Int,
        employeePassword: String
    ): Single<ErrorFailureFaceResponseModel> {
        return service.postLoginForErrorFace(employeeId, employeePassword)
    }

    override fun postManagementLoginForErrorFace(
        adminMasterId: Int,
        adminMasterPassword: String
    ): Single<ErrorFailureFaceResponseModel> {
        return service.postManagementLoginForErrorFace(adminMasterId, adminMasterPassword)
    }

    override fun getListGesture(): Single<ListRandomGestureResponse> {
        return service.getListGesture()
    }


}