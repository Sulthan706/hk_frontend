package com.hkapps.hygienekleen.features.features_client.myteam.data.remote

import com.hkapps.hygienekleen.features.features_client.myteam.data.service.MyTeamClientService
import com.hkapps.hygienekleen.features.features_client.myteam.model.ManagementStructuralClientResponse
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamcount.CfTeamCountResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetail.DetailCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetailhistoryperson.DetailHistoryPersonCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlist.ListEmployeeCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlistmanagement.ListManagementCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam.ScheduleTeamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.historyattendance.HistoryAttendanceCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.listwithshiftemployee.ListCftemByShiftResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listshiftreport.ListShiftReportResponseModel
import io.reactivex.Single
import javax.inject.Inject

class MyTeamClientRemoteDataSourceImpl @Inject constructor(private val service: MyTeamClientService) :
    MyTeamClientRemoteDataSource {

    override fun getListManagement(projectCode: String): Single<ManagementStructuralClientResponse> {
        return service.getListManagement(projectCode)
    }

    override fun getListCountCfTeam(projectCode: String): Single<CfTeamCountResponseModel> {
        return  service.getListCountCfTeam(projectCode)
    }

    override fun getListEmployeeCfteam(
        projectCode: String,
        role: String,
        page: Int
    ): Single<ListEmployeeCfteamResponseModel> {
        return service.getListEmployeeCfteam(projectCode, role, page)
    }

    override fun getListManagementCfteam(
        projectCode: String,
        page: Int
    ): Single<ListManagementCfteamResponseModel> {
        return service.getListManagementCfteam(projectCode, page)
    }

    override fun getDetailEmployeeCfteam(
        projectCode: String,
        idEmployee: Int
    ): Single<DetailCfteamResponseModel> {
        return service.getDetailEmployeeCfteam(projectCode, idEmployee)
    }

    override fun getDetailHistoryPersonCfteam(
        projectCode: String,
        idEmployee: Int,
        localDate: String,
        monthDate: String,
        yearDate: String,
        jobCode: String
    ): Single<DetailHistoryPersonCfteamResponseModel> {
        return service.getDetailHistoryPersonCfteam(projectCode, idEmployee, localDate, monthDate, yearDate, jobCode)
    }

    override fun getHistoryAttendanceCfteam(
        projectCode: String,
        date: String
    ): Single<HistoryAttendanceCfteamResponseModel> {
        return service.getHistoryAttendanceCfteam(projectCode, date)
    }

    override fun getScheduleCfteam(
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<ScheduleTeamResponseModel> {
        return service.getScheduleCfteam(projectCode, date, shiftId)
    }

    override fun getListShiftCfteam(projectCode: String): Single<ListShiftReportResponseModel> {
        return service.getListShiftCfteam(projectCode)
    }

    override fun getListCfteamByShift(
        projectCode: String,
        role: String,
        shiftId: Int,
        page: Int
    ): Single<ListCftemByShiftResponseModel> {
        return service.getListCfteamByShift(projectCode, role, shiftId, page)
    }

}