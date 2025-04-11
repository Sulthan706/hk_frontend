package com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.remote

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

interface ClosingManagementDataSource {


    fun getListDailyTargetManagement(
        id : Int,
        date : String
    ): Single<ListDailyTargetManagementResponse>

    fun getDetailDailyTargetManagement(
        id : String,
        date : String
    ): Single<DailyDetailTargetManagementResponse>


    fun getListDiversionManagement(
        locationId : Int,
        projectCode : String,
        date : String,
        page : Int,
        size : Int
    ):Single<DiversionResponse>

    fun submitClosingManagement(
        projectCode : String,
        date : String,
        adminMasterId : Int
    ):Single<ClosingResponse>

    fun generateFileClosingManagement(
        projectCode : String,
        date : String,
        userId : Int,
    ):Single<ClosingResponse>

    fun sendEmailClosingManagement(
        sendEmailClosingRequest: SendEmailClosingRequest
    ):Single<ClosingResponse>

    fun getListClientClosing(
        projectCode : String
    ):Single<ClientClosingResponse>

    fun divertedManagement(
        idJobs : Int,
        adminMasterId : Int,
        description : String,
        date : String,
        diverted : Int,
        file: MultipartBody.Part
    ):Single<ClosingResponse>

    fun getListHistoryClosingChief(
        projectCode: String,
        startAt : String,
        endAt : String,
        page : Int,
        perPage : Int
    ):Single<HistoryClosingResponse>

    fun checkClosingChief(
        projectCode : String,
    ):Single<CheckStatusChiefResponse>

    fun checkClosingStatus(
        adminMasterId : Int
    ): Single<CheckStatusChiefResponse>




}
