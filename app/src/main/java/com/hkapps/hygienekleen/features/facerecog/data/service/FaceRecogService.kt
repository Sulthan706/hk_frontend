package com.hkapps.hygienekleen.features.facerecog.data.service


import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.ListRandomGestureResponse
import com.hkapps.hygienekleen.features.facerecog.model.loginforfailure.ErrorFailureFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.profilevendor.GetProfileUserResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface FaceRecogService {

    @GET("/api/v1/recognition/employee/status/{employeeId}")
    fun getStatsRegisFaceRecog(
        @Path("employeeId") employeeId: Int
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/employee/register/both/{employeeId}")
    fun apiRegisterBoth(
        @Path("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("api/v1/recognition/employee/verify/both/{employeeId}")
    fun apiVerifyBoth(
        @Path("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/employee/register/alfabeta/{employeeId}")
    fun postRegisFaceRecog(
        @Path("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/employee/verify/alfabeta/{employeeId}")
    fun verifyFaceRecog(
        @Part file: MultipartBody.Part,
        @Path("employeeId") employeeId: Int
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/employee/register/compreface/{employeeId}")
    fun registerFaceNewEmployee(
        @Path("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/employee/verify/compreface/{employeeId}")
    fun verifyUserFaceNewEmployee(
        @Path("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    //management
    @GET("/api/v1/recognition/management/status/{adminMasterId}")
    fun getStatsRegisManagementRecog(
        @Path("adminMasterId") adminMaster: Int
    ): Single<StatsRegisFaceResponseModel>

    //  BOTH MANAGEMENT
    @Multipart
    @POST("api/v1/recognition/management/register/both/{adminMasterId}")
    fun apiRegisterManagementBoth(
        @Path("adminMasterId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/management/verify/both/{adminMasterId}")
    fun apiVerifyManagementBoth(
        @Path("adminMasterId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    // ALFABETA MANAGEMENT
    @Multipart
    @POST("/api/v1/recognition/management/register/alfabeta/{adminMasterId}")
    fun postRegisManagementFaceRecog(
        @Path("adminMasterId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/management/verify/alfabeta/{adminMasterId}")
    fun verifyManagementFaceRecog(
        @Part file: MultipartBody.Part,
        @Path("adminMasterId") employeeId: Int
    ):Single<StatsRegisFaceResponseModel>

    // COMPREFACE MANAGEMENT
    @Multipart
    @POST("/api/v1/recognition/management/register/compreface/{adminMasterId}")
    fun registerFaceNewManagement(
        @Path("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part
    ):Single<StatsRegisFaceResponseModel>

    @Multipart
    @POST("/api/v1/recognition/management/verify/compreface/{adminMasterId}")
    fun verifyUserFaceNewManagement(
        @Path("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part
    ): Single<StatsRegisFaceResponseModel>


    @GET("api/v1/employee/profile")
    fun getProfileUser(
        @Query("employeeId") employeeId: Int
    ) : Single<GetProfileUserResponseModel>

    @POST("api/v1/employee/input-password-face-recog")
    fun postLoginForErrorFace(
        @Query("employeeId") employeeId: Int,
        @Query("employeePassword") employeePassword: String
    ): Single<ErrorFailureFaceResponseModel>


    @POST("api/v1/management/input-password-face-recog")
    fun postManagementLoginForErrorFace(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("adminMasterPassword") adminMasterPassword: String
    ): Single<ErrorFailureFaceResponseModel>

    @GET("api/v1/compreface/gesture")
    fun getListGesture(): Single<ListRandomGestureResponse>

}