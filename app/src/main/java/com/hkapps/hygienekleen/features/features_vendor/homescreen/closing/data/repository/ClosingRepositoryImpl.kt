package com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.ClientClosingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.model.SendEmailClosingRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.remote.ClosingRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.area.AreaAssignmentResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.closing.ListClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.diversion.DiversionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.historyclosing.HistoryClosingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.model.shift.DetailShiftResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import io.reactivex.Single

class ClosingRepositoryImpl(private val closingRemoteDataSource: ClosingRemoteDataSource): ClosingRepository {
    override fun getListDailyDiversion(
        idDetailEmployeeProject : Int,
        locationId : Int,
        page: Int,
        perPage: Int
    ): Single<DiversionResponse> {
        return closingRemoteDataSource.getListDailyDiversion(idDetailEmployeeProject,locationId, page, perPage)
    }

    override fun getAreaAssignment(
        projectCode: String,
        category : String,
        page: Int,
        perPage: Int
    ): Single<AreaAssignmentResponse> {
        return closingRemoteDataSource.getAreaAssignment(projectCode,category, page, perPage)
    }

    override fun getHistoryClosing(
        employeeId: Int,
        startAt : String,
        endAt : String,
        page: Int,
        perPage: Int
    ): Single<HistoryClosingResponse> {
        return closingRemoteDataSource.getHistoryClosing(employeeId,startAt,endAt, page, perPage)
    }

    override fun getShiftDiversion(idShift: Int, projectCode: String, page: Int, size: Int):Single<DetailShiftResponse> {
        return closingRemoteDataSource.getShiftDiversion(idShift, projectCode, page, size)
    }

    override fun submitDailyClosing(idDetailEmployeeProject: Int,employeeId: Int): Single<ClosingResponse> {
        return closingRemoteDataSource.submitDailyClosing(idDetailEmployeeProject, employeeId)
    }



    override fun listClosing(
        projectCode: String,
        startAt: String,
        endAt: String,
        roles : String,
        rolesTwo : String,
        status : String,
        page: Int,
        size: Int
    ): Single<ListClosingResponse> {
        return closingRemoteDataSource.listClosing(projectCode, startAt, endAt,roles,rolesTwo,status, page, size)
    }

    override fun generateFileClosingManagement(
        projectCode: String,
        date: String,
        userId: Int
    ): Single<ClosingResponse> {
        return closingRemoteDataSource.generateFileClosingManagement(projectCode, date, userId)
    }

    override fun sendEmailClosingManagement(sendEmailClosingRequest: SendEmailClosingRequest): Single<ClosingResponse> {
        return closingRemoteDataSource.sendEmailClosingManagement(sendEmailClosingRequest)
    }

    override fun getListClientClosing(projectCode: String): Single<ClientClosingResponse> {
        return closingRemoteDataSource.getListClientClosing(projectCode)
    }

    override fun getDetailDailyTargetChief(id: String, date: String,employeeId : Int): Single<DailyTargetResponse> {
        return closingRemoteDataSource.getDetailDailyTargetChief(id, date,employeeId)
    }

    override fun getListDiversionChief(
        locationId: Int,
        projectCode: String,
        date: String,
        page: Int,
        size: Int
    ): Single<DiversionResponse> {
        return closingRemoteDataSource.getListDiversionChief(locationId, projectCode, date, page, size)
    }

    override fun submitClosingChief(
        projectCode: String,
        date: String,
        userId: Int
    ): Single<ClosingResponse> {
        return closingRemoteDataSource.submitClosingChief(projectCode, date, userId)
    }

    override fun getListHistoryClosingChief(
        projectCode: String,
        startAt: String,
        endAt: String,
        page: Int,
        perPage: Int
    ): Single<HistoryClosingResponse> {
        return closingRemoteDataSource.getListHistoryClosingChief(projectCode, startAt, endAt, page, perPage)
    }

    override fun generateFileClosingSPV(
        projectCode: String,
        date: String,
        userId: Int
    ): Single<ClosingResponse> {
        return closingRemoteDataSource.generateFileClosingSPV(projectCode, date, userId)
    }

    override fun sendEmailClosingSPV(sendEmailClosingRequest: SendEmailClosingRequest): Single<ClosingResponse> {
        return closingRemoteDataSource.sendEmailClosingSPV(sendEmailClosingRequest)
    }
}