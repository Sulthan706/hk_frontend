package com.hkapps.hygienekleen.features.features_client.setting.data.service

import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePassClientRequestModel
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePasswordClientResponse
import com.hkapps.hygienekleen.features.features_client.setting.model.editProfile.EditProfileClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface SettingClientService {

    @PUT("/api/v1/client/profile/udpate-password")
    fun changePassClient(
        @Body changePassClientRequestModel: ChangePassClientRequestModel
    ): Single<ChangePasswordClientResponse>

    @Multipart
    @PUT("/api/v1/client/profile/update")
    fun updateProfileClient(
        @Query("clientId") clientId: Int,
        @Query("clientEmail") clientEmail: String,
        @Query("clientName") clientName: String,
        @Part file: MultipartBody.Part
    ): Single<EditProfileClientResponse>

    @GET("/api/v1/client/profile/{clientId}")
    fun getProfileClient(
        @Path("clientId") clientId: Int
    ): Single<EditProfileClientResponse>
}