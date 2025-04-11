package com.hkapps.hygienekleen.features.features_management.service.resign.data.service

import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ListReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ListResignManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign.SubmitResignResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ResignManagementService {

    @GET("api/v1/resign/management/get/all")
    fun getListResignManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListResignManagementResponseModel>

    @GET("api/v1/resign/management/get/resign-reasons")
    fun getListReasonResignManagement(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListReasonResignResponseModel>

    @PATCH("api/v1/resign/management/patch/assign")
    fun submitResignManagement(
        @Query("idTurnOver") idTurnOver: Int,
        @Query("adminMasterId") adminMasterId: Int,
        @Query("reasonId") reasonId: Int,
        @Query("approval") approval: String
    ): Single<SubmitResignResponseModel>


}