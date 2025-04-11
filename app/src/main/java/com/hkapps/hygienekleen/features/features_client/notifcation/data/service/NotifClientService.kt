package com.hkapps.hygienekleen.features.features_client.notifcation.data.service

import com.hkapps.hygienekleen.features.features_client.notifcation.model.NotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.listnotifclient.ListNotifClientResponseModel
import com.hkapps.hygienekleen.features.features_client.notifcation.model.readnotifclient.ReadNotifClientResponseModel
import io.reactivex.Single
import retrofit2.http.*

interface NotifClientService {

    @GET("/api/v1/client/notif/dac")
    fun getNotifClient(
        @Query("clientId") clientId: Int,
        @Query("page") page: Int
    ): Single<NotifClientResponseModel>
    //list notif client
    @GET("/api/v1/client/notif/history")
    fun getListNotifClient(
        @Query("clientId") clientId: Int,
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int
    ): Single<ListNotifClientResponseModel>
    //readnotif
    @PUT("/api/v1/client/notif/read")
    fun putReadNotifClient(
        @Query("notificationId") notificationId: Int,
        @Query("clientId") clientId: Int
    ) :Single<ReadNotifClientResponseModel>
}