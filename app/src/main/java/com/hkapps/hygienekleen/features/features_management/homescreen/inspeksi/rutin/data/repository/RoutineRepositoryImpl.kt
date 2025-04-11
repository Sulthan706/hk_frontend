package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.remote.RoutineDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine.DetailRoutineReportResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.ClientsRoutineResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.ListRoutineVisitedResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.submitRoutine.SubmitFormRoutineResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(private val dataSource: RoutineDataSource):
    RoutineRepository {

    override fun getListClient(
        projectCode: String
    ): Single<ClientsRoutineResponse> {
        return dataSource.getListClient(projectCode)
    }

    override fun submitFormRoutine(
        userId: Int,
        projectCode: String,
        title: String,
        description: String,
        date: String,
        file: MultipartBody.Part,
        fileDescription: String,
        emailParticipant: ArrayList<String>
    ): Single<SubmitFormRoutineResponse> {
        return dataSource.submitFormRoutine(userId, projectCode, title, description, date, file, fileDescription, emailParticipant)
    }

    override fun getListRoutine(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListRoutineVisitedResponse> {
        return dataSource.getListRoutine(userId, projectCode, date, page)
    }

    override fun getDetailRoutine(idRoutine: Int): Single<DetailRoutineReportResponse> {
        return dataSource.getDetailRoutine(idRoutine)
    }

}