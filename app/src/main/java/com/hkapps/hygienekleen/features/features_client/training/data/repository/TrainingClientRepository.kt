package com.hkapps.hygienekleen.features.features_client.training.data.repository

import com.hkapps.hygienekleen.features.features_client.training.model.listshifttraining.ListShiftTraining
import io.reactivex.Single

interface TrainingClientRepository {
    fun getListShiftTraining(
        projectCode: String
    ): Single<ListShiftTraining>
}