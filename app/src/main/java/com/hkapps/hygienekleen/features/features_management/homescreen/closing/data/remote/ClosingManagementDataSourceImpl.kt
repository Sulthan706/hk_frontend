package com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.service.ClosingManagementService
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

class ClosingManagementDataSourceImpl(
    private val service : ClosingManagementService
) : ClosingManagementDataSource {
    override fun getListDailyTargetManagement(
        id: Int,
        date: String
    ): Single<ListDailyTargetManagementResponse> {
        return service.getListDailyTargetManagement(id, date)
    }

    override fun getDetailDailyTargetManagement(
        id: String,
        date: String
    ): Single<DailyDetailTargetManagementResponse> {
        return service.getDetailDailyTargetManagement(id, date)
    }

    override fun getListDiversionManagement(
        locationId : Int,
        projectCode: String,
        date: String,
        page: Int,
        size: Int
    ): Single<DiversionResponse> {
        return service.getListDiversionManagement(locationId,projectCode, date, page, size)
    }

    override fun submitClosingManagement(
        projectCode: String,
        date: String,
        adminMasterId: Int
    ): Single<ClosingResponse> {
        return service.submitClosingManagement(projectCode,date,adminMasterId)
    }

    override fun generateFileClosingManagement(
        projectCode: String,
        date: String,
        userId: Int
    ): Single<ClosingResponse> {
        return service.generateFileClosingManagement(projectCode, date, userId)
    }

    override fun sendEmailClosingManagement(sendEmailClosingRequest: SendEmailClosingRequest): Single<ClosingResponse> {
        return service.sendEmailClosingManagement(sendEmailClosingRequest)
    }

    override fun getListClientClosing(projectCode: String): Single<ClientClosingResponse> {
        return service.getListClientClosing(projectCode)
    }

    override fun divertedManagement(
        idJobs: Int,
        adminMasterId: Int,
        description: String,
        date: String,
        diverted: Int,
        file: MultipartBody.Part
    ): Single<ClosingResponse> {
        return service.divertedManagement(idJobs,adminMasterId,description,date,diverted, file)
    }

    override fun getListHistoryClosingChief(
        projectCode: String,
        startAt: String,
        endAt: String,
        page: Int,
        perPage: Int
    ): Single<HistoryClosingResponse> {
        return service.getListClosingHistoryChief(projectCode,startAt, endAt, page, perPage)
    }

    override fun checkClosingChief(
        projectCode: String
    ): Single<CheckStatusChiefResponse> {
        return service.checkClosingChief(projectCode)
    }

    override fun checkClosingStatus(adminMasterId: Int): Single<CheckStatusChiefResponse> {
        return service.checkClosingPopupHome(adminMasterId)
    }
}