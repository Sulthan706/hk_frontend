package com.hkapps.hygienekleen.features.features_client.training.data.remote

import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
import io.reactivex.Single

interface TrainingClientRemoteDataSource {
    fun getListShiftTraining(
        projectCode: String
    ): Single<ListShiftTraining>
}