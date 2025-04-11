package com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.remote.ClosingManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.CheckStatusChiefResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.DailyDetailTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ListDailyTargetManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import io.reactivex.Single
import okhttp3.MultipartBody

class ClosingManagementRepositoryImpl(
    private val remoteDataSource: ClosingManagementDataSource
) : ClosingManagementRepository {
    override fun getListDailyTargetManagement(
        id: Int,
        date: String
    ): Single<ListDailyTargetManagementResponse> {
        return remoteDataSource.getListDailyTargetManagement(id, date)
    }

    override fun getDetailDailyTargetManagement(
        id: String,
        date: String
    ): Single<DailyDetailTargetManagementResponse> {
        return remoteDataSource.getDetailDailyTargetManagement(id, date)
    }

    override fun getListDiversionManagement(
        locationId : Int,
        projectCode: String,
        date: String,
        page: Int,
        size: Int
    ): Single<DiversionResponse> {
        return remoteDataSource.getListDiversionManagement(locationId,projectCode, date, page, size)
    }

    override fun submitClosingManagement(
        projectCode: String,
        date: String,
        adminMasterId: Int
    ): Single<ClosingResponse> {
        return remoteDataSource.submitClosingManagement(projectCode, date, adminMasterId)
    }

    override fun generateFileClosingManagement(
        projectCode: String,
        date: String,
        userId: Int
    ): Single<ClosingResponse> {
        return remoteDataSource.generateFileClosingManagement(projectCode, date, userId)
    }

    override fun sendEmailClosingManagement(sendEmailClosingRequest: SendEmailClosingRequest): Single<ClosingResponse> {
       return remoteDataSource.sendEmailClosingManagement(sendEmailClosingRequest)
    }

    override fun getListClientClosing(projectCode: String): Single<ClientClosingResponse> {
        return remoteDataSource.getListClientClosing(projectCode)
    }

    override fun divertedManagement(
        idJobs: Int,
        adminMasterId: Int,
        description: String,
        date: String,
        diverted: Int,
        file: MultipartBody.Part
    ): Single<ClosingResponse> {
        return remoteDataSource.divertedManagement(idJobs, adminMasterId, description, date, diverted, file)
    }

    override fun getListHistoryClosingChief(
        projectCode: String,
        startAt: String,
        endAt: String,
        page: Int,
        perPage: Int
    ): Single<HistoryClosingResponse> {
        return remoteDataSource.getListHistoryClosingChief(projectCode, startAt, endAt, page, perPage)
    }

    override fun checkClosingChief(
        projectCode: String
    ): Single<CheckStatusChiefResponse> {
        return remoteDataSource.checkClosingChief(projectCode)
    }

    override fun checkClosingStatus(adminMasterId: Int): Single<CheckStatusChiefResponse> {
        return remoteDataSource.checkClosingStatus(adminMasterId)
    }


}