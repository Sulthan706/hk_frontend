package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.remote

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

interface InspeksiDataSource {

    fun submitFormMeeting(
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
    ): Single<SubmitFormMeetingResponse>

    fun getListMeeting(
        userId: Int,
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListMeetingInspeksiResponse>

    fun getDetailMeeting(
        idMeeting: Int
    ): Single<DetailMeetingInspeksiResponse>

    fun getListAreaInspeksi(): Single<ListAreaInspeksiResponse>

    fun getListObjectInspeksi(
        idArea: Int
    ): Single<ListObjectInspeksiResponse>

    fun submitLaporanArea(
        createdBy: Int,
        projectCode: String,
        idArea: Int,
        idObject: Int,
        file: MultipartBody.Part,
        score: Int,
        description: String
    ): Single<SubmitLaporanAreaResponse>

    fun getListLaporanArea(
        userId: Int,
        projectCode: String,
        auditType: String,
        idArea: Int,
        date: String,
        page: Int
    ): Single<ListLaporanAreaResponse>

    fun getListKontrolArea(
        userId: Int,
        projectCode: String,
        date: String,
        typeVisit: String
    ): Single<ListKontrolAreaResponse>

    fun getListClientMeeting(
        projectCode: String
    ): Single<ClientsMeetingResponse>

}