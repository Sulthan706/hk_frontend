package com.hkapps.hygienekleen.features.features_client.setting.data.repository

import com.hkapps.hygienekleen.features.features_client.setting.data.remote.SettingClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePassClientRequestModel
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePasswordClientResponse
import com.hkapps.hygienekleen.features.features_client.setting.model.editProfile.EditProfileClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class SettingClientRepositoryImpl @Inject constructor(private val remoteDataSource: SettingClientRemoteDataSource) :
    SettingClientRepository {

    override fun changePassClient(changePassClientRequestModel: ChangePassClientRequestModel): Single<ChangePasswordClientResponse> {
        return remoteDataSource.changePassClient(changePassClientRequestModel)
    }

    override fun updateProfileClient(
        clientId: Int,
        clientEmail: String,
        clientName: String,
        file: MultipartBody.Part
    ): Single<EditProfileClientResponse> {
        return remoteDataSource.updateProfileClient(clientId, clientEmail, clientName, file)
    }

    override fun getProfileClient(clientId: Int): Single<EditProfileClientResponse> {
        return remoteDataSource.getProfileClient(clientId)
    }

}