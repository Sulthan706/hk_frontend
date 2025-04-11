package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.remote.InspeksiDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.detailMeeting.DetailMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listArea.ListAreaInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listClientMeeting.ClientsMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listKontrolArea.ListKontrolAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listLaporanKondisiArea.ListLaporanAreaResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listMeeting.ListMeetingInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.listObject.ListObjectInspeksiResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitFormMeeting.SubmitFormMeetingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.model.submitLaporanArea.SubmitLaporanAreaResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class InspeksiRepositoryImpl @Inject constructor(private val dataSource: InspeksiDataSource):
    InspeksiRepository {

    override fun submitFormMeeting(
        userId: Int,
        projectCode: String,
        title: String,
        description: String,
        date: String,
        startTime: String,
        endTime: String,
        file: MultipartBody.Part,
        fileDescription:String,
        emailParticipant: ArrayList<String>,
        nameParticipant: ArrayList<String>
    ): Single<SubmitFormMeetingResponse> {
        return dataSource.submitFormMeeting(userId, projectCode, title, description, date, startTime, endTime, file, fileDescription, emailParticipant, nameParticipant)
    }

    override fun getListMeeting(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListMeetingInspeksiResponse> {
        return dataSource.getListMeeting(userId, projectCode, date, page)
    }

    override fun getDetailMeeting(idMeeting: Int): Single<DetailMeetingInspeksiResponse> {
        return dataSource.getDetailMeeting(idMeeting)
    }

    override fun getListAreaInspeksi(): Single<ListAreaInspeksiResponse> {
        return dataSource.getListAreaInspeksi()
    }

    override fun getListObjectInspeksi(idArea: Int): Single<ListObjectInspeksiResponse> {
        return dataSource.getListObjectInspeksi(idArea)
    }

    override fun submitLaporanArea(
        createdBy: Int,
        projectCode: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String
    ): Single<SubmitLaporanAreaResponse> {
        return dataSource.submitLaporanArea(createdBy, projectCode, idArea, idObject, file, score, description)
    }

    override fun getListLaporanArea(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        date: String,
        page: Int
    ): Single<ListLaporanAreaResponse> {
        return dataSource.getListLaporanArea(userId, projectCode, auditType, idArea, date, page)
    }

    override fun getListKontrolArea(
        userId: Int,
        projectCode: String,
        date: String,
        typeVisit: String
    ): Single<ListKontrolAreaResponse> {
        return dataSource.getListKontrolArea(userId, projectCode, date, typeVisit)
    }

    override fun getListClientMeeting(projectCode: String): Single<ClientsMeetingResponse> {
        return dataSource.getListClientMeeting(projectCode)
    }

}