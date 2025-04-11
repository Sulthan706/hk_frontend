package com.hkapps.hygienekleen.features.features_client.training.data.service

import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainingClientService {
    @GET("api/v1/client/cfteam/list-shift")
    fun getListShiftTraining(
        @Query("projectCode") projectCode: String
    ): Single<ListShiftTraining>
    //academygetscheduleoffline

}