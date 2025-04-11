package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.detailRoutine.DetailRoutineReportResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listClient.ClientsRoutineResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.listRoutine.ListRoutineVisitedResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.model.submitRoutine.SubmitFormRoutineResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface RoutineRepository {

    fun getListClient(
        projectCode: String
    ): Single<ClientsRoutineResponse>

    fun submitFormRoutine(
        userId: Int,
        projectCode: String,
        title: String,
        description: String,
        date: String,
        file: MultipartBody.Part,
        fileDescription:String,
        emailParticipant: ArrayList<String>
    ): Single<SubmitFormRoutineResponse>

    fun getListRoutine(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListRoutineVisitedResponse>

    fun getDetailRoutine(
        idRoutine: Int
    ): Single<DetailRoutineReportResponse>

}