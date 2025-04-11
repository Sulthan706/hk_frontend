package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.dacAbsentOpr.DacOperationalAbsentMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsent.DetailAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.detailAbsentByStatus.DetailAbsentByStatusResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listBranch.ListBranchAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listOperational.ListCountAbsentOprMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByBranch.ListProjectByBranchAbsentOprResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.model.listProjectByUser.ListProjectByUserAbsentOprResponse
import io.reactivex.Single

interface AbsentOprMgmntRemoteDataSource {

    fun getListBranch(): Single<ListBranchAbsentOprMgmntResponse>

    fun getListProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse>

    fun getListProjectByUser(
        employeeId: Int
    ): Single<ListProjectByUserAbsentOprResponse>

    fun getListAbsentCountOpr(
        projectCode: String,
        month: Int,
        year: Int,
        jobRole: String
    ): Single<ListCountAbsentOprMgmntResponse>

    fun getDetailAbsentOpr(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAbsentOprMgmntResponse>

    fun searchProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectByBranchAbsentOprResponse>

    fun getDetailAbsentByStatusOpr(
        employeeId: Int,
        projectCode: String,
        month: Int,
        year: Int,
        scheduleType: String,
        statusAttendance: String
    ): Single<DetailAbsentByStatusResponseModel>

    fun getDacOperational(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ): Single<DacOperationalAbsentMgmntResponse>

}
