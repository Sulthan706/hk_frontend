package com.hkapps.hygienekleen.features.features_client.notifcation.data.repository

import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ListNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.readnotifclient.ReadNotifClientResponseModel
import io.reactivex.Single
import retrofit2.http.Query

interface NotifRepository {

    fun getNotifClient(
        clientId: Int,
        page: Int
    ): Single<NotifClientResponseModel>

    fun getListNotifClient(
        clientId: Int,
        projectCode: String,
        page: Int
    ): Single<ListNotifClientResponseModel>

    fun putReadNotifClient(
        @Query("notificationId") notificationId: Int,
        @Query("clientId") clientId: Int
    ) :Single<ReadNotifClientResponseModel>

}