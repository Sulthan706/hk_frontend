package com.hkapps.hygienekleen.features.features_client.myteam.data.repository

import com.hkapps.hygienekleen.features.features_client.myteam.data.remote.MyTeamClientRemoteDataSource
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

class MyTeamClientRepositoryImpl @Inject constructor(private val dataSource: MyTeamClientRemoteDataSource) :
    MyTeamClientRepository {

    override fun getListManagement(projectCode: String): Single<ManagementStructuralClientResponse> {
        return dataSource.getListManagement(projectCode)
    }

    override fun getListCountCfTeam(projectCode: String): Single<CfTeamCountResponseModel> {
        return dataSource.getListCountCfTeam(projectCode)
    }

    override fun getListEmployeeCfteam(
        projectCode: String,
        role: String,
        page: Int
    ): Single<ListEmployeeCfteamResponseModel> {
        return dataSource.getListEmployeeCfteam(projectCode, role, page)
    }

    override fun getListManagementCfteam(
        projectCode: String,
        page: Int
    ): Single<ListManagementCfteamResponseModel> {
        return dataSource.getListManagementCfteam(projectCode, page)
    }

    override fun getDetailEmployeeCfteam(
        projectCode: String,
        idEmployee: Int
    ): Single<DetailCfteamResponseModel> {
        return dataSource.getDetailEmployeeCfteam(projectCode, idEmployee)
    }

    override fun getDetailHistoryPersonCfteam(
        projectCode: String,
        idEmployee: Int,
        localDate: String,
        monthDate: String,
        yearDate: String,
        jobCode: String
    ): Single<DetailHistoryPersonCfteamResponseModel> {
        return dataSource.getDetailHistoryPersonCfteam(projectCode, idEmployee, localDate, monthDate, yearDate, jobCode)
    }

    override fun getHistoryAttendanceCfteam(
        projectCode: String,
        date: String
    ): Single<HistoryAttendanceCfteamResponseModel> {
        return dataSource.getHistoryAttendanceCfteam(projectCode, date)
    }

    override fun getScheduleCfteam(
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<ScheduleTeamResponseModel> {
        return dataSource.getScheduleCfteam(projectCode, date, shiftId)
    }

    override fun getListShiftCfteam(projectCode: String): Single<ListShiftReportResponseModel> {
        return dataSource.getListShiftCfteam(projectCode)
    }


    override fun getListCfteamByShift(
        projectCode: String,
        role: String,
        shiftId: Int,
        page: Int
    ): Single<ListCftemByShiftResponseModel> {
        return dataSource.getListCfteamByShift(projectCode, role, shiftId, page)
    }


}