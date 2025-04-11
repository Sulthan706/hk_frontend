package com.hkapps.academy.features.features_participants.training.data.repository

import com.hkapps.academy.features.features_participants.training.data.remote.TrainingParticipantDataSource
import com.hkapps.academy.features.features_participants.training.model.listTraining.TrainingsCalendarResponse
import com.hkapps.academy.features.features_participants.training.model.scheduleTraining.ScheduleTrainingResponse
import io.reactivex.Single
import javax.inject.Inject

class TrainingParticipantRepositoryImpl @Inject constructor(private val dataSource: TrainingParticipantDataSource):
    TrainingParticipantRepository {
    override fun getScheduleTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        month: Int,
        year: Int
    ): Single<ScheduleTrainingResponse> {
        return dataSource.getScheduleTraining(userNuc, projectCode, levelJabatan, month, year)
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
        return dataSource.getListTraining(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

}