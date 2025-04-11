package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.service.AbsentOprMgmntService
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DacOperationalAbsentMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.DetailAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.DetailAbsentByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.ListBranchAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.ListCountAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.ListProjectByBranchAbsentOprResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.ListProjectByUserAbsentOprResponse
import io.reactivex.Single
import javax.inject.Inject

class AbsentOprMgmntRemoteDataSourceImpl @Inject constructor(private val service: AbsentOprMgmntService): AbsentOprMgmntRemoteDataSource {

    override fun getListBranch(): Single<ListBranchAbsentOprMgmntResponse> {
        return service.getListBranch()
    }

    override fun getListProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse> {
        return service.getListProjectByBranch(branchCode, page, perPage)
    }

    override fun getListProjectByUser(employeeId: Int): Single<ListProjectByUserAbsentOprResponse> {
        return service.getListProjectByUser(employeeId)
    }

    override fun getListAbsentCountOpr(
        projectCode: String,
        month: Int,
        year: Int,
        jobRole: String
    ): Single<ListCountAbsentOprMgmntResponse> {
        return service.getListAbsentCountOpr(projectCode, month, year, jobRole)
    }

    override fun getDetailAbsentOpr(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAbsentOprMgmntResponse> {
        return service.getDetailAbsentOpr(projectCode, employeeId, month, year)
    }

    override fun searchProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse> {
        return service.searchProject(page, branchCode, keywords, perPage)
    }

    override fun getDetailAbsentByStatusOpr(
        employeeId: Int,
        projectCode: String,
        month: Int,
        year: Int,
        scheduleType: String,
        statusAttendance: String
    ): Single<DetailAbsentByStatusResponseModel> {
        return service.getDetailAbsentByStatusOpr(employeeId, projectCode, month, year, scheduleType, statusAttendance)
    }

    override fun getDacOperational(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ): Single<DacOperationalAbsentMgmntResponse> {
        return service.getDacOperational(employeeId, projectId, idDetailEmployeeProject)
    }

}