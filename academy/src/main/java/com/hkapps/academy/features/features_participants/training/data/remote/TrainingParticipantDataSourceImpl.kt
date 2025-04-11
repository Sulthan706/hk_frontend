package com.hkapps.academy.features.features_participants.training.data.remote

import com.hkapps.academy.features.features_participants.training.data.service.TrainingParticipantService
import com.hkapps.academy.features.features_participants.training.model.listTraining.TrainingsCalendarResponse
import com.hkapps.academy.features.features_participants.training.model.scheduleTraining.ScheduleTrainingResponse
import io.reactivex.Single
import javax.inject.Inject

class TrainingParticipantDataSourceImpl @Inject constructor(private val service: TrainingParticipantService):
    TrainingParticipantDataSource {
    override fun getScheduleTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        month: Int,
        year: Int
    ): Single<ScheduleTrainingResponse> {
        return service.getScheduleTraining(userNuc, projectCode, levelJabatan, month, year)
    }

    override fun getListTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<TrainingsCalendarResponse> {
        return service.getListTraining(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

}