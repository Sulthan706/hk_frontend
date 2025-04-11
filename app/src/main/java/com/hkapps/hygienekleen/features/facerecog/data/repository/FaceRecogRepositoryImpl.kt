package com.hkapps.hygienekleen.features.facerecog.data.repository

import com.hkapps.hygienekleen.features.facerecog.data.remote.FaceRecogRemoteDataSource
import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.ListRandomGestureResponse
import com.hkapps.hygienekleen.features.facerecog.model.loginforfailure.ErrorFailureFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.profilevendor.GetProfileUserResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class FaceRecogRepositoryImpl @Inject constructor(private val remoteDataSource: FaceRecogRemoteDataSource) :
    FaceRecogRepository {

    override fun getStatsRegisFaceRecog(employeeId: Int): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.getStatsRegisFaceRecog(employeeId)
    }

    override fun apiRegisterBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.apiRegisterBoth(employeeId, file)
    }

    override fun apiVerifyBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.apiVerifyBoth(employeeId, file)
    }

    override fun postRegisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.postRegisFaceRecog(employeeId, file)
    }

    override fun verifyFaceRecog(
        file: MultipartBody.Part,
        employeeId: Int
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.verifyFaceRecog(file, employeeId)
    }

    override fun registerFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.registerFaceNewEmployee(employeeId, file)
    }

    override fun verifyUserFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.verifyUserFaceNewEmployee(employeeId, file)
    }


    override fun getProfileUser(employeeId: Int): Single<GetProfileUserResponseModel> {
        return remoteDataSource.getProfileUser(employeeId)
    }

    override fun postLoginForErrorFace(
        employeeId: Int,
        employeePassword: String
    ): Single<ErrorFailureFaceResponseModel> {
        return remoteDataSource.postLoginForErrorFace(employeeId, employeePassword)
    }

    override fun getStatsRegisManagementRecog(adminMaster: Int): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.getStatsRegisManagementRecog(adminMaster)
    }

    override fun apiRegisterManagementBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.apiRegisterManagementBoth(employeeId, file)
    }

    override fun apiVerifyManagementBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.apiVerifyManagementBoth(employeeId, file)
    }

    override fun registerFaceNewManagement(
        nuc: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.registerFaceNewManagement(nuc, file)
    }

    override fun verifyUserFaceNewManagement(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.verifyUserFaceNewManagement(adminMasterId, file)
    }

    override fun postRegisManagementFaceRecog(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.postRegisManagementFaceRecog(adminMasterId, file)
    }

    override fun verifyManagementFaceRecog(
        file: MultipartBody.Part,
        adminMasterId: Int
    ): Single<StatsRegisFaceResponseModel> {
        return remoteDataSource.verifyManagementFaceRecog(file, adminMasterId)
    }


    override fun postManagementLoginForErrorFace(
        adminMasterId: Int,
        adminMasterPassword: String
    ): Single<ErrorFailureFaceResponseModel> {
        return remoteDataSource.postManagementLoginForErrorFace(adminMasterId, adminMasterPassword)
    }


    override fun getListGesture(): Single<ListRandomGestureResponse> {
        return remoteDataSource.getListGesture()
    }


}