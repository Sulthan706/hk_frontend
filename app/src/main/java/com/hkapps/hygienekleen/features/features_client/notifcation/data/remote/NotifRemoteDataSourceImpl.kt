package com.hkapps.hygienekleen.features.features_client.notifcation.data.remote

import com.hkapps.hygienekleen.features.features_client.notifcation.data.service.NotifClientService
import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ListNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.readnotifclient.ReadNotifClientResponseModel
import io.reactivex.Single
import javax.inject.Inject

class NotifRemoteDataSourceImpl @Inject constructor(private val clientService: NotifClientService) :
    NotifRemoteDataSource {

    override fun getNotifClient(clientId: Int, page: Int): Single<NotifClientResponseModel> {
        return clientService.getNotifClient(clientId, page)
    }

    override fun getListNotifClient(
        clientId: Int,
        projectCode: String,
        page: Int
    ): Single<ListNotifClientResponseModel> {
        return clientService.getListNotifClient(clientId, projectCode, page)
    }

    override fun putReadNotifClient(
        notificationId: Int,
        clientId: Int
    ): Single<ReadNotifClientResponseModel> {
        return clientService.putReadNotifClient(notificationId, clientId)
    }
}