package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.remote.AbsentOprMgmntRemoteDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DacOperationalAbsentMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.DetailAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.DetailAbsentByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.ListBranchAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.ListCountAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.ListProjectByBranchAbsentOprResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.ListProjectByUserAbsentOprResponse
import io.reactivex.Single
import javax.inject.Inject

class AbsentOprMgmntRepositoryImpl @Inject constructor(private val remoteDataSource: AbsentOprMgmntRemoteDataSource): AbsentOprMgmntRepository {

    override fun getListBranch(): Single<ListBranchAbsentOprMgmntResponse> {
        return remoteDataSource.getListBranch()
    }

    override fun getListProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse> {
        return remoteDataSource.getListProjectByBranch(branchCode, page, perPage)
    }

    override fun getListProjectByUser(employeeId: Int): Single<ListProjectByUserAbsentOprResponse> {
        return remoteDataSource.getListProjectByUser(employeeId)
    }

    override fun getListAbsentCountOpr(
        projectCode: String,
        month: Int,
        year: Int,
        jobRole: String
    ): Single<ListCountAbsentOprMgmntResponse> {
        return remoteDataSource.getListAbsentCountOpr(projectCode, month, year, jobRole)
    }

    override fun getDetailAbsentOpr(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAbsentOprMgmntResponse> {
        return remoteDataSource.getDetailAbsentOpr(projectCode, employeeId, month, year)
    }

    override fun searchProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse> {
        return remoteDataSource.searchProject(page, branchCode, keywords, perPage)
    }

    override fun getDetailAbsentByStatusOpr(
        employeeId: Int,
        projectCode: String,
        month: Int,
        year: Int,
        scheduleType: String,
        statusAttendance: String
    ): Single<DetailAbsentByStatusResponseModel> {
        return remoteDataSource.getDetailAbsentByStatusOpr(employeeId, projectCode, month, year, scheduleType, statusAttendance)
    }

    override fun getDacOperational(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ): Single<DacOperationalAbsentMgmntResponse> {
        return remoteDataSource.getDacOperational(employeeId, projectId, idDetailEmployeeProject)
    }

}