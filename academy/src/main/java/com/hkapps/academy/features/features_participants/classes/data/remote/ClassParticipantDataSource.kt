package com.hkapps.academy.features.features_participants.classes.data.remote

import com.hkapps.academy.features.features_participants.classes.model.listClass.ClassesParticipantResponse
import io.reactivex.Single

interface ClassParticipantDataSource {

    fun getListClassParticipant(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesParticipantResponse>

}