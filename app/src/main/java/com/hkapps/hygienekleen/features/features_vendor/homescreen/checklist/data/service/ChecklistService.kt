package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailArea.DetailAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailOperator.DetailOperatorChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea.ListAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listOperator.ListOperatorChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listReview.ListReviewAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.listStaffBertugas.StaffResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listShift.ListShiftChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.onCheckPlotting.CheckPlottingChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.submitChecklist.SubmitChecklistResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.objectActivity.ObjectActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.penilaianObj.PenilaianObjResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusAbsen.AttendanceStatusResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusPenilaianDac.StatusDacResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.otoritasChecklist.OtoritasChecklistResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ChecklistService {

    @GET("api/v1/employee/attendance/status")
    fun getStatusAttendanceCheck(
        @Query("employeeId") userId: Int,
        @Query("projectCode") projectCode: String
    ): Single<AttendanceStatusResponse>

    @GET("api/v1/employee/activities/new")
    fun getDailyAct(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String
    ): Single<DailyActivityResponseModel>

    @GET("api/v1/project/team/operator")
    fun getListStaff(
        @Query("employeeId") userId: Int,
        @Query("projectId") projectCode: String,
        @Query("shiftId") shiftId: Int
    ): Single<StaffResponseModel>

    @GET("api/v1/checklist/activity/object")
    fun getObjectDac(
        @Query("activityId") activityId: Int,
        @Query("projectId") projectCode: String
    ): Single<ObjectActivityResponseModel>

    @Multipart
    @POST("/api/v1/checklist/activity/object/submit")
    fun postPenilaianObj(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("activityId") activityId: Int,
        @Query("submitBy") userId: Int,
        @Query("plottingId") plottingId: Int,
        @Query("objectFirst") firstObject: String?,
        @Query("objectSecond") secondObject: String?,
        @Query("objectThird") thirdObject: String?,
        @Query("objectFourth") fourthObject: String?,
        @Query("objectFifth") fifthObject: String?,
        @Query("objectFirstValue") firstValue: String?,
        @Query("objectSecondValue") secondValue: String?,
        @Query("objectThirdValue") thirdValue: String?,
        @Query("objectFourthValue") fourthValue: String?,
        @Query("objectFifthValue") fifthValue: String?,
        @Query("notes") notes: String,
        @Part file: MultipartBody.Part
    ): Single<PenilaianObjResponse>

    @GET("api/v1/checklist/activity/employee")
    fun getStatusDac(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectCode: String,
        @Query("plottingId") plottingId: Int,
        @Query("activityId") activityId: Int
    ): Single<StatusDacResponseModel>

    @GET("api/v1/project/otoritas/checklist/{projectCode}")
    fun getOtoritasChecklist(
        @Path("projectCode") projectCode: String
    ): Single<OtoritasChecklistResponse>


    // api new checklist
    @GET("/api/v1/checklist/shift")
    fun getListShift(
        @Query("projectId") projectId: String
    ): Single<ListShiftChecklistResponse>

    @GET("/api/v1/project/shift/area")
    fun getListArea(
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("page") page: Int
    ): Single<ListAreaChecklistResponse>

    @GET("/api/v1/checklist/plotting")
    fun getCheckPlotting(
        @Query("plottingId") plottingId: Int
    ): Single<CheckPlottingChecklistResponse>

    @GET("/api/v1/project/plotting/area")
    fun getDetailAreaChecklist(
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("plottingId") plottingId: Int
    ): Single<DetailAreaChecklistResponse>

    @GET("/api/v1/checklist/list/review")
    fun getListReviewArea(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String,
        @Query("plottingId") plottingId: Int,
        @Query("activityId") activityId: Int
    ): Single<ListReviewAreaChecklistResponse>

    @Multipart
    @POST("/api/v1/checklist/area")
    fun submitChecklist(
        @Query("projectId") projectId: String,
        @Query("submitBy") submitBy: Int,
        @Query("pengawasId") pengawasId: Int,
        @Query("plottingId") plottingId: Int,
        @Query("shiftId") shiftId: Int,
        @Query("checklistReviewId") checklistReviewId: Int,
        @Query("notes") notes: String,
        @Part file: MultipartBody.Part,
        @Query("operationalsId") operationalsId: ArrayList<Int>
    ): Single<SubmitChecklistResponseModel>

    @GET("/api/v1/checklist/operational")
    fun getListOperator(
        @Query("projectId") projectId: String,
        @Query("shiftId") shiftId: Int
    ): Single<ListOperatorChecklistResponse>

    @GET("/api/v1/employee/dac")
    fun getDetailOperatorChecklist(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String,
        @Query("idDetailEmployeeProject") idDetailEmployeeProject: Int
    ): Single<DetailOperatorChecklistResponse>

    @GET("/api/v1/project/shift/area/search")
    fun getSearchListArea(
        @Query("projectCode") projectCode: String,
        @Query("shiftId") shiftId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<ListAreaChecklistResponse>

    @GET("/api/v1/checklist/operational/search")
    fun getSearchListOperator(
        @Query("projectId") projectId: String,
        @Query("shiftId") shiftId: Int,
        @Query("keywords") keywords: String
    ): Single<ListOperatorChecklistResponse>
}