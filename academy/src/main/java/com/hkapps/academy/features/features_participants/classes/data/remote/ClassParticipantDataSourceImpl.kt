package com.hkapps.academy.features.features_participants.classes.data.remote

import com.hkapps.academy.features.features_participants.classes.data.service.ClassParticipantService
import com.hkapps.academy.features.features_participants.classes.model.listClass.ClassesParticipantResponse
import io.reactivex.Single
import javax.inject.Inject

class ClassParticipantDataSourceImpl @Inject constructor(private val service: ClassParticipantService):
    ClassParticipantDataSource {
    override fun getListClassParticipant(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesParticipantResponse> {
        return service.getListClassParticipant(userNuc, projectCode, levelJabatan, date, region, page, size)
    }
}