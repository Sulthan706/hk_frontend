package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.service.RoutineService
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine.DetailRoutineReportResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.ClientsRoutineResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.ListRoutineVisitedResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.submitRoutine.SubmitFormRoutineResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class RoutineDataSourceImpl @Inject constructor(private val service: RoutineService):
    RoutineDataSource {

    override fun getListClient(
        projectCode: String
    ): Single<ClientsRoutineResponse> {
        return service.getListClient(projectCode)
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
        return service.submitFormRoutine(userId, projectCode, title, description, date, file, fileDescription, emailParticipant)
    }

    override fun getListRoutine(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListRoutineVisitedResponse> {
        return service.getListRoutine(userId, projectCode, date, page)
    }

    override fun getDetailRoutine(idRoutine: Int): Single<DetailRoutineReportResponse> {
        return service.getDetailRoutine(idRoutine)
    }

}