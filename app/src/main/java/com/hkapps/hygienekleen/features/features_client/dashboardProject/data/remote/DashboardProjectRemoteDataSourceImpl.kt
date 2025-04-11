package com.hkapps.hygienekleen.features.features_client.dashboardProject.data.remote

import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.service.DashboardProjectService
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.DetailDashboardProjectResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.historyAttendance.AttendanceProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listEmployee.EmployeeProjectTeamResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listShift.ShiftProjectClientResponse
import com.hkapps.hygienekleen.features.features_client.dashboardProject.model.listSubLoc.SubLocProjectClientResponse
import io.reactivex.Single
import javax.inject.Inject

class DashboardProjectRemoteDataSourceImpl @Inject constructor(private val service: DashboardProjectService) :
    DashboardProjectRemoteDataSource {

    override fun getDetailDashboardProject(projectCode: String): Single<DetailDashboardProjectResponse> {
        return service.getDetailDashboardProject(projectCode)
    }

    override fun getSubLocProjectClient(
        projectCode: String,
        locationId: Int
    ): Single<SubLocProjectClientResponse> {
        return service.getSubLocProjectClient(projectCode, locationId)
    }

    override fun getShiftProjectClient(
        projectCode: String,
        locationId: Int,
        subLocationId: Int
    ): Single<ShiftProjectClientResponse> {
        return service.getShiftProjectClient(projectCode, locationId, subLocationId)
    }

    override fun getListTeamEmployeeClient(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<EmployeeProjectTeamResponse> {
        return service.getListTeamEmployeeClient(projectCode, date, shiftId, locationId, subLocationId)
    }

    override fun getHistoryAttendanceProject(
        projectCode: String,
        date: String
    ): Single<AttendanceProjectClientResponse> {
        return service.getHistoryAttendanceProject(projectCode, date)
    }

}