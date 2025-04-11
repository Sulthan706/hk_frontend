package com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.SubmitRegisMekariResponse
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.generatetokenmekari.TokenMekariResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari.TrialMekariResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MekariService {

    @POST("/api/v1/mekari/register/{employeeId}")
    fun submitRegisMekari(
        @Path("employeeId") employeeId: Int
    ): Single<SubmitRegisMekariResponse>

    @GET("/api/v1/employee/check-mekari")
    fun getCheckMekari(
        @Query("employeeId") employeeId: Int
    ): Single<SubmitRegisMekariResponse>

    @POST("api/v1/mekari/access_token")
    fun generateTokenMekari(
        @Query("employeeId") employeeId: Int
    ):Single<TokenMekariResponseModel>

    @GET("api/v1/mekari/trial")
    fun getTrialMekari(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int
    ):Single<TrialMekariResponseModel>

}