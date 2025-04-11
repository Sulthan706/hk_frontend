package com.hkapps.hygienekleen.features.features_client.training.data.remote

import com.hkapps.hygienekleen.features.features_client.training.data.service.TrainingClientService
import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
import io.reactivex.Single
import javax.inject.Inject

class TrainingClientRemoteDataSourceImpl @Inject constructor(private val service: TrainingClientService):
TrainingClientRemoteDataSource{

    override fun getListShiftTraining(projectCode: String): Single<ListShiftTraining> {
        return service.getListShiftTraining(projectCode)
    }

}