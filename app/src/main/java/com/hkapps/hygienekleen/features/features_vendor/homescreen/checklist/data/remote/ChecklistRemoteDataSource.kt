package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.remote

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

interface ChecklistRemoteDataSource {

    fun getDailyAct(userId: Int, projectCode: String): Single<DailyActivityResponseModel>

    fun getStatusAttendanceCheck(userId: Int, projectCode: String): Single<AttendanceStatusResponse>

    fun getListStaff(userId: Int, projectCode: String, shiftId: Int): Single<StaffResponseModel>

    fun getObjectDac(activityId: Int, projectCode: String): Single<ObjectActivityResponseModel>

    fun postPenilaianObj(
        employeeId: Int,
        projectCode: String,
        activityId: Int,
        userId: Int,
        plottingId: Int,
        firstObject: String?,
        secondObject: String?,
        thirdObject: String?,
        fourthObject: String?,
        fifthObject: String?,
        firstValue: String?,
        secondValue: String?,
        thirdValue: String?,
        fourthValue: String?,
        fifthValue: String?,
        note: String,
        uploadImage: MultipartBody.Part
    ): Single<PenilaianObjResponse>

    fun getStatusDac(employeeId: Int, projectCode: String, plottingId: Int, activityId: Int): Single<StatusDacResponseModel>

    fun getOtoritasChecklist(projectCode: String): Single<OtoritasChecklistResponse>


    // new checklist
    fun getListShift(
        projectId: String
    ): Single<ListShiftChecklistResponse>

    fun getListArea(
        projectCode: String,
        shiftId: Int,
        page: Int
    ): Single<ListAreaChecklistResponse>

    fun getCheckPlotting(
        plottingId: Int
    ): Single<CheckPlottingChecklistResponse>

    fun getDetailAreaChecklist(
        projectCode: String,
        shiftId: Int,
        plottingId: Int
    ): Single<DetailAreaChecklistResponse>

    fun getListReviewArea(
        employeeId: Int,
        projectId: String,
        plottingId: Int,
        activityId: Int
    ): Single<ListReviewAreaChecklistResponse>

    fun submitChecklist(
        projectId: String,
        submitBy: Int,
        pengawasId: Int,
        plottingId: Int,
        shiftId: Int,
        checklistReviewId: Int,
        notes: String,
        file: MultipartBody.Part,
        operationalsId: ArrayList<Int>
    ): Single<SubmitChecklistResponseModel>

    fun getListOperator(
        projectId: String,
        shiftId: Int
    ): Single<ListOperatorChecklistResponse>

    fun getDetailOperatorChecklist(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ): Single<DetailOperatorChecklistResponse>

    fun getSearchListArea(
        projectCode: String,
        shiftId: Int,
        page: Int,
        keywords: String
    ): Single<ListAreaChecklistResponse>

    fun getSearchListOperator(
        projectId: String,
        shiftId: Int,
        keywords: String
    ): Single<ListOperatorChecklistResponse>

}
