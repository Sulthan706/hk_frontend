package com.hkapps.hygienekleen.features.features_client.dashboardProject.data.remote

import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.DetailDashboardProjectResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.historyAttendance.AttendanceProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.EmployeeProjectTeamResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listShift.ShiftProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listSubLoc.SubLocProjectClientResponse
import io.reactivex.Single

interface DashboardProjectRemoteDataSource {

    fun getDetailDashboardProject(
        projectCode: String
    ): Single<DetailDashboardProjectResponse>

    fun getSubLocProjectClient(
        projectCode: String,
        locationId: Int
    ): Single<SubLocProjectClientResponse>

    fun getShiftProjectClient(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ): Single<ShiftProjectClientResponse>

    fun getListTeamEmployeeClient(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<EmployeeProjectTeamResponse>

    fun getHistoryAttendanceProject(
        projectCode: String,
        date: String
    ): Single<AttendanceProjectClientResponse>

}