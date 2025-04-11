package com.hkapps.hygienekleen.features.facerecog.data.remote

import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.ListRandomGestureResponse
import com.hkapps.hygienekleen.features.facerecog.model.loginforfailure.ErrorFailureFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.profilevendor.GetProfileUserResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Part

interface FaceRecogRemoteDataSource {
    fun getStatsRegisFaceRecog(
        employeeId: Int
    ): Single<StatsRegisFaceResponseModel>
    fun apiRegisterBoth(
       employeeId: Int,
       file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun apiVerifyBoth(
       employeeId: Int,
       file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    fun postRegisFaceRecog(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun verifyFaceRecog(
        file: MultipartBody.Part,
        employeeId: Int
    ): Single<StatsRegisFaceResponseModel>

    fun registerFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    fun verifyUserFaceNewEmployee(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun getStatsRegisManagementRecog(
        adminMaster: Int
    ): Single<StatsRegisFaceResponseModel>

    fun apiRegisterManagementBoth(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun apiVerifyManagementBoth(
        employeeId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun registerFaceNewManagement(
        nuc: Int,
        @Part file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    fun verifyUserFaceNewManagement(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun postRegisManagementFaceRecog(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    fun verifyManagementFaceRecog(
        file: MultipartBody.Part,
        adminMasterId: Int
    ): Single<StatsRegisFaceResponseModel>


    fun getProfileUser(
        employeeId: Int
    ) : Single<GetProfileUserResponseModel>

    fun postLoginForErrorFace(
        employeeId: Int,
        employeePassword: String
    ): Single<ErrorFailureFaceResponseModel>


    fun postManagementLoginForErrorFace(
        adminMasterId: Int,
        adminMasterPassword: String
    ): Single<ErrorFailureFaceResponseModel>

    fun getListGesture():Single<ListRandomGestureResponse>
}