package com.hkapps.academy.features.features_participants.training.data.remote

import com.hkapps.academy.features.features_participants.training.model.listTraining.TrainingsCalendarResponse
import com.hkapps.academy.features.features_participants.training.model.scheduleTraining.ScheduleTrainingResponse
import io.reactivex.Single

interface TrainingParticipantDataSource {

    fun getScheduleTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        month: Int,
        year: Int
    ): Single<ScheduleTrainingResponse>

    fun getListTraining(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<TrainingsCalendarResponse>

}