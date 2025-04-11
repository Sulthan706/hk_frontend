package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent.AttendanceAlreadyAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.AttendanceNotAbsenResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result.FabHistoryResultResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model.FabPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.FabTeamResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch.FabSearchPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchStatusResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.regisfacerecog.RegisFaceRecogResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.userFlyingOperator.AttendanceUserFlyingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.verifyfacerecog.VerifyFaceResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.model.DailyActResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface AttendanceFixService {

    @GET("/api/v1/employee/activities")
    fun getDailyDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
    ): Single<DailyActResponseModel>

    @GET("/metadata/{params}")
    fun getChooseStaffDataApi(
        @Path("params") params: String,
    ): Single<ChooseStaffResponseModel>

    //GET QRCODE
    @GET("/api/v1/barcode/employee")
    fun getQRCodeDataApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<QRCodeResponseModel>

    //POST SELFIE
    @Multipart
    @POST("/api/v1/employee/attendance/in")
    fun postImageSelfieApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("barcodeKey") barcodeKey: String,
        @Part file: MultipartBody.Part,
    ): Single<SelfiePostResponseModel>

    //PUT SELFIE
    @Multipart
    @PUT("/api/v1/employee/attendance/out")
    fun putImageSelfieApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("barcodeKey") barcodeKey: String,
        @Part file: MultipartBody.Part,
    ): Single<SelfiePostResponseModel>


    //POST SELFIE GEO
    @Multipart
    @POST("/api/v1/employee/attendance/in/geo")
    fun postImageSelfieGeoApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("scheduleId") scheduleId: Int,
        @Part file: MultipartBody.Part,
    ): Single<GeoSelfiePostResponseModel>

    //PUT SELFIE GEO
    @Multipart
    @PUT("/api/v1/employee/attendance/out/geo")
    fun putImageSelfieGeoApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("scheduleId") scheduleId: Int,
        @Part file: MultipartBody.Part,
    ): Single<GeoSelfiePostResponseModel>

    //GET status absen
    @GET("/api/v1/employee/attendance/status")
    fun getAttendanceStatusApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<AttendanceStatusResponseModel>

    @GET("/api/v1/employee/attendance/geo/status")
    fun getAttendanceStatusOperatorApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<AttendanceStatusResponseModelNew>


    //GET history absen
    @GET("/api/v1/employee/attendance/history")
    fun getHistoryApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<HistoryResponseModel>

    //GET history absen w/ date
    @GET("/api/v1/employee/attendance/history/date")
    fun getHistoryByDateApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("datePrefix") datePrefix: String,
        @Query("dateSuffix") dateSuffix: String,
    ): Single<HistoryByDateResponseModel>

    //GET list staff not attendance
    @GET("/api/v1/project/shift")
    fun getShift(
        @Query("projectId") projectId: String
    ): Single<AttendanceShiftStaffNotAttendanceResponseModel>

    @GET("/api/v1/employee/attendance/belum-absen")
    fun getListStaffNotAttendance(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceResponseModel>


    @GET("/api/v1/employee/spv/attendance/belum-absen")
    fun getListStaffNotAttendanceSPV(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceSPVResponseModel>


    @GET("/api/v1/employee/chief-spv/attendance/belum-absen")
    fun getListStaffNotAttendanceCSPV(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffNotAttendanceCSPVResponseModel>

    @GET("/api/v1/employee/attendance/sudah-absen")
    fun getListStaffAlreadyAttendance(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceResponseModel>

    @GET("/api/v1/employee/spv/attendance/sudah-absen")
    fun getListStaffAlreadyAttendanceSPV(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceSPVResponseModel>

    @GET("/api/v1/employee/chief-spv/attendance/sudah-absen")
    fun getListStaffAlreadyAttendanceCSPV(
        @Query("projectCode") projectCode: String,
        @Query("employeeId") employeeId: Int,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceListStaffAlreadyAttendanceCSPVResponseModel>

    @GET("/api/v1/employee/attendance/in/check/{id}")
    fun getCheckAttendanceApi(
        @Path("id") id: Int,
        @Query("employeeId") employeeId: Int,
        @Query("projectId") scheduleId: String
    ): Single<AttendanceCheckResponseModel>


    @GET("/api/v1/employee/attendance/out/check")
    fun getCheckAttendanceOutApi(
        @Query("employeeId") employeeId: Int,
        @Query("scheduleId") scheduleId: Int
    ): Single<AttendanceCheckOutResponseModel>


    @GET("/api/v1/employee/schedule/v2")
    fun getEmployeeSchApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String
    ): Single<EmployeeSchResponseModel>


    @GET("/api/v1/employee/schedule/{id}")
    fun getEmployeeSchByIdApi(
        @Path("id") id: Int?,
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String
    ): Single<EmployeeSchByIdResponseModel>


    //MID
    @GET("/api/v1/employee/non-operator/schedule")
    fun getEmployeeSchMidApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String
    ): Single<EmployeeSchMidResponseModel>

    @Multipart
    @POST("/api/v1/employee/attendance/non-operator/in/geo")
    fun postImageSelfieGeoMidApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Part file: MultipartBody.Part,
    ): Single<GeoSelfiePostMidResponseModel>

    //PUT SELFIE GEO
    @Multipart
    @PUT("/api/v1/employee/attendance/non-operator/out/geo")
    fun putImageSelfieGeoMidApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Part file: MultipartBody.Part,
    ): Single<GeoSelfiePostMidResponseModel>

    //GET status absen
    @GET("/api/v1/employee/attendance/non-operator/geo/status")
    fun getAttendanceStatusMidOneSchApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<MidLevelOneSchStatusResponseModel>

    @GET("/api/v1/employee/non-operator/schedule")
    fun getAttendanceMidOneSchApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String,
    ): Single<MidLevelOneSchResponseModel>

    @GET("/api/v1/employee/attendance/non-operator/geo/in/check")
    fun getCheckAttendanceMidOneSchApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") scheduleId: String
    ): Single<AttendanceCheckResponseModel>

    @GET("/api/v1/employee/attendance/non-operator/geo/out/check")
    fun getCheckAttendanceOutMidOneSchApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ): Single<AttendanceCheckOutResponseModel>
//fab person
    @GET("/api/v1/project/team/team-leader")
    fun getMyTeam(
        @Query("projectId") projectId: String,
        @Query("employeeId") employeeId: Int,
        @Query("page") page: Int
    ): Single<FabPersonResponseModel>

    @GET("/api/v1/project/team/supervisor")
    fun getMyTeamSpv(
        @Query("projectId") projectId: String,
        @Query("page") page: Int
    ): Single<FabPersonResponseModel>

    @GET("/api/v1/project/team/chief-spv")
    fun getMyTeamCspv(
        @Query("projectId") projectId: String,
        @Query("page") page: Int
    ): Single<FabPersonResponseModel>
//fab team
    @GET("/api/v1/employee/attendance/history/team-leader")
    fun getHistoryTeamLeader(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int
    ): Single<FabTeamResponseModel>

    @GET("/api/v1/employee/attendance/history/spv")
    fun getHistoryTeamSPV(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int
    ): Single<FabTeamResponseModel>

    @GET("/api/v1/employee/attendance/history/spv")
    fun getHistoryTeamFM(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int
    ): Single<FabTeamResponseModel>

    @GET("/api/v1/employee/attendance/non-operator/history/month-year")
    fun getHistoryResult(
        @Query("employeeId") employeeId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
    ): Single<FabHistoryResultResponseModel>

    // fabsearchperson
    @GET("/api/v1/project/team/search-team")
    fun getSearchPerson(
        @Query("projectId") projectId: String,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<FabSearchPersonResponseModel>

    @GET("/api/v1/employee/attendance/non-operator/report/belum-absen")
    fun getListAttendanceNotAbsent(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceNotAbsenResponseModel>

    @GET("/api/v1/employee/attendance/non-operator/report/sudah-absen")
    fun getListAttendanceAlreadyAbsent(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<AttendanceAlreadyAbsentResponseModel>

    // attendance user flying
    @POST("/api/v1/user_flying/in/submit")
    fun postUserFlyingIn(
        @Query("employeeId") userId: Int,
        @Query("idSchedule") idSchedule: Int,
        @Query("projectCode") projectCode: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("address") address: String,
        @Query("radius") radius: String,
        @Query("description") description: String,
        @Query("deviceInfo") deviceInfo: String
    ): Single<AttendanceUserFlyingResponse>

    @POST("/api/v1/user_flying/out/submit")
    fun postUserFlyingOut(
        @Query("employeeId") userId: Int,
        @Query("idSchedule") idSchedule: Int,
        @Query("projectCode") projectCode: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("address") address: String,
        @Query("radius") radius: String,
        @Query("description") description: String,
        @Query("deviceInfo") deviceInfo: String
    ): Single<AttendanceUserFlyingResponse>

    @GET("/api/v1/user_flying/status")
    fun getStatusUserFlying(
        @Query("employeeId") userId: Int,
        @Query("idSchedule") idSchedule: Int,
        @Query("projectCode") projectCode: String,
        @Query("type") type: String
    ): Single<AttendanceUserFlyingResponse>

    @GET("/api/v1/user_flying/access")
    fun getAccessUserFlying(
        @Query("projectCode") projectCode: String
    ): Single<AttendanceUserFlyingResponse>

    @Multipart
    @POST("/api/v1/alfabeta/register")
    fun regisFaceRecog(
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part
    ): Single<RegisFaceRecogResponseModel>

    @Multipart
    @POST("/api/v1/alfabeta/verify")
    fun verifyFaceRecog(
        @Part file: MultipartBody.Part,
        @Query("employeeId") employeeId: Int
    ): Single<VerifyFaceResponseModel>

}