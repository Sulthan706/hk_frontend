package com.hkapps.hygienekleen.features.features_client.myteam.data.service

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
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyTeamClientService {

    @GET("/api/v1/management/structural/{projectCode}")
    fun getListManagement(
        @Path("projectCode") projectCode: String
    ): Single<ManagementStructuralClientResponse>
    //home cfteam
    @GET("/api/v1/client/cfteam/count")
    fun getListCountCfTeam(
        @Query("projectCode") projectCode: String
    ) : Single<CfTeamCountResponseModel>
    //list cfteam pengawas & operator
    @GET("/api/v1/client/cfteam/list")
    fun getListEmployeeCfteam(
        @Query("projectCode") projectCode: String,
        @Query("role") role: String,
        @Query("page") page: Int
    ) : Single<ListEmployeeCfteamResponseModel>
    //list cfteam cfmanagement
    @GET("/api/v1/client/cfteam/listmanagement")
    fun getListManagementCfteam(
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int
    ) : Single<ListManagementCfteamResponseModel>
    //detail cfteam person
    @GET("/api/v1/client/cfteam/detailemployee")
    fun getDetailEmployeeCfteam(
        @Query("projectCode") projectCode: String,
        @Query("idEmployee") idEmployee:Int
        ) : Single<DetailCfteamResponseModel>
    //detail history person cfteam
    @GET("/api/v1/client/cfteam/detailScheduleEmployee")
    fun getDetailHistoryPersonCfteam(
        @Query("projectCode") projectCode: String,
        @Query("idEmployee") idEmployee:Int,
        @Query("localDate") localDate:String,
        @Query("month") monthDate:String,
        @Query("year") yearDate:String,
        @Query("jobCode") jobCode: String
    ) : Single<DetailHistoryPersonCfteamResponseModel>
    //history attendance cfteam
    @GET("/api/v1/client/attendance-history-page")
    fun getHistoryAttendanceCfteam(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String
    ): Single<HistoryAttendanceCfteamResponseModel>
    //schedule cfteam
    @GET("/api/v1/client/cfteam/schedule-team")
    fun getScheduleCfteam(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int
    ): Single<ScheduleTeamResponseModel>
    //list shift
    @GET("/api/v1/client/cfteam/list-shift")
    fun getListShiftCfteam(
        @Query("projectCode") projectCode:String
    ): Single<ListShiftReportResponseModel>
    //list cfteam baru dengan shift
    @GET("/api/v1/client/cfteam/list")
    fun getListCfteamByShift(
        @Query("projectCode") projectCode:String,
        @Query("role") role:String,
        @Query("shiftId") shiftId: Int,
        @Query("page") page: Int
    ): Single<ListCftemByShiftResponseModel>
}