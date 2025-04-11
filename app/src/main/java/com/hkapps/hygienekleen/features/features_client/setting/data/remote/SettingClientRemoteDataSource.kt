package com.hkapps.hygienekleen.features.features_client.setting.data.remote

import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePassClientRequestModel
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePasswordClientResponse
import com.hkapps.hygienekleen.features.features_client.setting.model.editProfile.EditProfileClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface SettingClientRemoteDataSource {

    fun changePassClient(
        changePassClientRequestModel: ChangePassClientRequestModel
    ): Single<ChangePasswordClientResponse>

    fun updateProfileClient(
        clientId: Int,
        clientEmail: String,
        clientName: String,
        file: MultipartBody.Part
    ): Single<EditProfileClientResponse>

    fun getProfileClient(
        clientId: Int
    ): Single<EditProfileClientResponse>

}