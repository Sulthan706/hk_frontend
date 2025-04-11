package com.hkapps.academy.features.features_participants.classes.data.repository

import com.hkapps.academy.features.features_participants.classes.data.remote.ClassParticipantDataSource
import com.hkapps.academy.features.features_participants.classes.model.listClass.ClassesParticipantResponse
import io.reactivex.Single
import javax.inject.Inject

class ClassParticipantRepositoryImpl @Inject constructor(private val dataSource: ClassParticipantDataSource):
    ClassParticipantRepository {
    override fun getListClassParticipant(
        userNuc: String,
        projectCode: String,
        levelJabatan: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesParticipantResponse> {
        return dataSource.getListClassParticipant(userNuc, projectCode, levelJabatan, date, region, page, size)
    }

}