package com.hkapps.academy.features.features_participants.homescreen.home.data.repository

import com.hkapps.academy.features.features_participants.homescreen.home.model.listClass.ClassesHomeResponse
import com.hkapps.academy.features.features_participants.homescreen.home.model.listTraining.TrainingsHomeResponse
import io.reactivex.Single

interface HomeParticipantRepository {

    fun getListClassHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeResponse>

    fun getListTrainingHome(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<TrainingsHomeResponse>

}