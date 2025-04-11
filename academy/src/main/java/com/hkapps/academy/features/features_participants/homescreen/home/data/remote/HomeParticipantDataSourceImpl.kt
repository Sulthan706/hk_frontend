package com.hkapps.academy.features.features_participants.homescreen.home.data.remote

import com.hkapps.academy.features.features_participants.homescreen.home.data.service.HomeParticipantService
import com.hkapps.academy.features.features_participants.homescreen.home.model.listClass.ClassesHomeResponse
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.TrainingsHomeResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeParticipantDataSourceImpl @Inject constructor(private val service: HomeParticipantService):
    HomeParticipantDataSource {

    override fun getListClassHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeResponse> {
        return service.getListClassHome(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

    override fun getListTrainingHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<TrainingsHomeResponse> {
        return service.getListTrainingHome(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

}