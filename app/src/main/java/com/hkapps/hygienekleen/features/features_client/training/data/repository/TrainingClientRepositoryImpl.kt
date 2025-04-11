package com.hkapps.hygienekleen.features.features_client.training.data.repository

import com.hkapps.hygienekleen.features.features_client.training.data.remote.TrainingClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
import io.reactivex.Single
import javax.inject.Inject

class TrainingClientRepositoryImpl @Inject constructor(private val remoteDataSource: TrainingClientRemoteDataSource):
TrainingClientRepository{

    override fun getListShiftTraining(projectCode: String): Single<ListShiftTraining> {
        return remoteDataSource.getListShiftTraining(projectCode)
    }

}