package com.hkapps.hygienekleen.features.features_client.notifcation.data.repository

import com.hkapps.hygienekleen.features.features_client.notifcation.data.remote.NotifRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ListNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.readnotifclient.ReadNotifClientResponseModel
import io.reactivex.Single
import javax.inject.Inject

class NotifRepositoryImpl @Inject constructor(private val remoteDataSource: NotifRemoteDataSource) :
    NotifRepository {

    override fun getNotifClient(clientId: Int, page: Int): Single<NotifClientResponseModel> {
        return remoteDataSource.getNotifClient(clientId, page)
    }

    override fun getListNotifClient(
        clientId: Int,
        projectCode: String,
        page: Int
    ): Single<ListNotifClientResponseModel> {
        return remoteDataSource.getListNotifClient(clientId, projectCode, page)
    }

    override fun putReadNotifClient(
        notificationId: Int,
        clientId: Int
    ): Single<ReadNotifClientResponseModel> {
        return remoteDataSource.putReadNotifClient(notificationId, clientId)
    }
}