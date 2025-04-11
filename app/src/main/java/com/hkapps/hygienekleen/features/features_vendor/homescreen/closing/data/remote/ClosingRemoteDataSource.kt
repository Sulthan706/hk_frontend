package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignmentResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ListClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift.DetailShiftResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import io.reactivex.Single


interface ClosingRemoteDataSource {

    fun getListDailyDiversion(
        idDetailEmployeeProject : Int,
        locationId : Int,
        page: Int, perPage: Int,
    ):Single<DiversionResponse>

    fun getAreaAssignment(
        projectCode : String,
        category : String,
        page : Int,
        perPage : Int
    ): Single<AreaAssignmentResponse>


    fun getHistoryClosing(
        employeeId: Int,
        startAt : String,
        endAt : String,
        page : Int,
        perPage : Int
    ): Single<HistoryClosingResponse>

    fun getShiftDiversion(
        idShift : Int,
        projectCode : String,
        page: Int,
        size: Int,
    ):Single<DetailShiftResponse>

    fun submitDailyClosing(
        idDetailEmployeeProject : Int,
        employeeId : Int,
    ):Single<ClosingResponse>



    fun listClosing(
        projectCode : String,
        startAt : String,
        endAt : String,
        roles : String,
        rolesTwo : String,
        status : String,
        page : Int,
        size : Int,
    ):Single<ListClosingResponse>

    fun getDetailDailyTargetChief(
        id : String,
        date : String,
        employeeId : Int
    ): Single<DailyTargetResponse>

    fun getListDiversionChief(
        locationId : Int,
        projectCode : String,
        date : String,
        page : Int,
        size : Int
    ):Single<DiversionResponse>


    fun submitClosingChief(
        projectCode : String ,
        date : String,
        userId : Int,
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

    fun getListHistoryClosingChief(
        projectCode: String,
        startAt : String,
        endAt : String,
        page : Int,
        perPage : Int
    ):Single<HistoryClosingResponse>

    fun generateFileClosingSPV(
        projectCode : String,
        date : String,
        userId : Int,
    ):Single<ClosingResponse>

    fun sendEmailClosingSPV(
        sendEmailClosingRequest: SendEmailClosingRequest
    ):Single<ClosingResponse>



}