package com.hkapps.hygienekleen.features.features_client.myteam.data.repository

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

interface MyTeamClientRepository {

    fun getListManagement(
        projectCode: String
    ): Single<ManagementStructuralClientResponse>

    fun getListCountCfTeam(
        projectCode: String
    ): Single<CfTeamCountResponseModel>

    fun getListEmployeeCfteam(
        projectCode: String,
        role: String,
        page: Int
    ): Single<ListEmployeeCfteamResponseModel>

    fun getListManagementCfteam(
        projectCode: String,
        page: Int
    ) : Single<ListManagementCfteamResponseModel>

    fun getDetailEmployeeCfteam(
        projectCode: String,
        idEmployee: Int
    ) : Single<DetailCfteamResponseModel>

    fun getDetailHistoryPersonCfteam(
        projectCode: String,
        idEmployee: Int,
        localDate: String,
        monthDate: String,
        yearDate: String,
        jobCode: String
    ) : Single<DetailHistoryPersonCfteamResponseModel>

    fun getHistoryAttendanceCfteam(
        projectCode: String,
        date: String
    ): Single<HistoryAttendanceCfteamResponseModel>

    fun getScheduleCfteam(
        projectCode: String,
        date: String,
        shiftId: Int
    ): Single<ScheduleTeamResponseModel>

    fun getListShiftCfteam(
        projectCode: String
    ):Single<ListShiftReportResponseModel>

    fun getListCfteamByShift(
        projectCode: String,
        role: String,
        shiftId: Int,
        page: Int
    ):Single<ListCftemByShiftResponseModel>
}