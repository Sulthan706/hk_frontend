package com.hkapps.hygienekleen.features.features_client.setting.data.remote

import com.hkapps.hygienekleen.features.features_client.setting.data.service.SettingClientService
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePassClientRequestModel
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePasswordClientResponse
import com.hkapps.hygienekleen.features.features_client.setting.model.editProfile.EditProfileClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class SettingClientRemoteDataSourceImpl @Inject constructor(private val service: SettingClientService) :
    SettingClientRemoteDataSource {

    override fun changePassClient(changePassClientRequestModel: ChangePassClientRequestModel): Single<ChangePasswordClientResponse> {
        return service.changePassClient(changePassClientRequestModel)
    }

    override fun updateProfileClient(
        clientId: Int,
        clientEmail: String,
        clientName: String,
        file: MultipartBody.Part
    ): Single<EditProfileClientResponse> {
        return service.updateProfileClient(clientId, clientEmail, clientName, file)
    }

    override fun getProfileClient(clientId: Int): Single<EditProfileClientResponse> {
        return service.getProfileClient(clientId)
    }

}