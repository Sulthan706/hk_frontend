package com.hkapps.hygienekleen.features.features_vendor.myteam.data.service

import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsent.CountAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel.CountAbsentMidLevelResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.TeamleadResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TimkuService {

    @GET("api/v1/project/shift")
    fun getListShiftTimku(@Query("projectCode") projectCode: String): Single<ShiftResponseModel>

    @GET("api/v1/project/team/operator")
    fun getListOperatorTimku(
        @Query("employeeId") userId: Int,
        @Query("projectId") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<OperatorResponseModel>

    @GET("api/v1/project/team/operator/date")
    fun getListOperatorByDate(
        @Query("employeeId") userId: Int,
        @Query("projectId") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): Single<OperatorResponseModel>

//    @GET("api/v1/project/team/leader")
//    fun getListLeaderMyteam(
//        @Query("employeeId") userId: Int,
//        @Query("projectId") projectCode: String
//    ): Single<TeamleadResponseModel>

    @GET("api/v1/project/spv/team")
    fun getListLeaderTimku(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<TeamleadResponseModel>

    @GET("api/v1/project/chief-spv/team")
    fun getListSpvTimku(
        @Query("projectId") projectCode: String
    ): Single<SupervisorResponseModel>

    @GET("api/v1/employee/attendance/belum-absen/")
    fun getCountAbsentOperator(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<CountAbsentResponseModel>

    @GET("api/v1/employee/spv/attendance/belum-absen")
    fun getCountAbsentLeader(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<CountAbsentMidLevelResponseModel>

    @GET("api/v1/employee/chief-spv/attendance/belum-absen/")
    fun getCountAbsentSpv(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int
    ): Single<CountAbsentMidLevelResponseModel>

    @GET("api/v1/project/team-leader/list")
    fun getListLeaderTimkuNew(
        @Query("projectId") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<TeamleadResponseModel>

}