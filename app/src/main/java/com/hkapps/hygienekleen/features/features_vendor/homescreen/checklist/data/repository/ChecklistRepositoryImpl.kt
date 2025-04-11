package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.remote.ChecklistRemoteDataSource
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
import javax.inject.Inject

class ChecklistRepositoryImpl @Inject constructor(private val remoteDataSource: ChecklistRemoteDataSource) :
    ChecklistRepository {

    override fun getDailyAct(userId: Int, projectCode: String): Single<DailyActivityResponseModel> {
        return remoteDataSource.getDailyAct(userId, projectCode)
    }

    override fun getStatusAttendanceCheck(
        userId: Int,
        projectCode: String
    ): Single<AttendanceStatusResponse> {
        return remoteDataSource.getStatusAttendanceCheck(userId, projectCode)
    }

    override fun getListStaff(
        userId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<StaffResponseModel> {
        return remoteDataSource.getListStaff(userId, projectCode, shiftId)
    }

    override fun getObjectDac(
        activityId: Int,
        projectCode: String
    ): Single<ObjectActivityResponseModel> {
        return remoteDataSource.getObjectDac(activityId, projectCode)
    }

    override fun postPenilaianObj(
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
    ): Single<PenilaianObjResponse> {
        return remoteDataSource.postPenilaianObj(
            employeeId,
            projectCode,
            activityId,
            userId,
            plottingId,
            firstObject,
            secondObject,
            thirdObject,
            fourthObject,
            fifthObject,
            firstValue,
            secondValue,
            thirdValue,
            fourthValue,
            fifthValue,
            note,
            uploadImage
        )
    }

    override fun getStatusDac(
        employeeId: Int,
        projectCode: String,
        plottingId: Int,
        activityId: Int
    ): Single<StatusDacResponseModel> {
        return remoteDataSource.getStatusDac(employeeId, projectCode, plottingId, activityId)
    }

    override fun getOtoritasChecklist(projectCode: String): Single<OtoritasChecklistResponse> {
        return remoteDataSource.getOtoritasChecklist(projectCode)
    }

    override fun getListShift(projectId: String): Single<ListShiftChecklistResponse> {
        return remoteDataSource.getListShift(projectId)
    }

    override fun getListArea(
        projectCode: String,
        shiftId: Int,
        page: Int
    ): Single<ListAreaChecklistResponse> {
        return remoteDataSource.getListArea(projectCode, shiftId, page)
    }

    override fun getCheckPlotting(plottingId: Int): Single<CheckPlottingChecklistResponse> {
        return remoteDataSource.getCheckPlotting(plottingId)
    }

    override fun getDetailAreaChecklist(
        projectCode: String,
        shiftId: Int,
        plottingId: Int
    ): Single<DetailAreaChecklistResponse> {
        return remoteDataSource.getDetailAreaChecklist(projectCode, shiftId, plottingId)
    }

    override fun getListReviewArea(
        employeeId: Int,
        projectId: String,
        plottingId: Int,
        activityId: Int
    ): Single<ListReviewAreaChecklistResponse> {
        return remoteDataSource.getListReviewArea(employeeId, projectId, plottingId, activityId)
    }

    override fun submitChecklist(
        projectId: String,
        submitBy: Int,
        pengawasId: Int,
        plottingId: Int,
        shiftId: Int,
        checklistReviewId: Int,
        notes: String,
        file: MultipartBody.Part,
        operationalsId: ArrayList<Int>
    ): Single<SubmitChecklistResponseModel> {
        return remoteDataSource.submitChecklist(projectId, submitBy, pengawasId, plottingId, shiftId, checklistReviewId, notes, file, operationalsId)
    }

    override fun getListOperator(
        projectId: String,
        shiftId: Int
    ): Single<ListOperatorChecklistResponse> {
        return remoteDataSource.getListOperator(projectId, shiftId)
    }

    override fun getDetailOperatorChecklist(
        employeeId: Int,
        projectId: String,
        idDetailEmployeeProject: Int
    ): Single<DetailOperatorChecklistResponse> {
        return remoteDataSource.getDetailOperatorChecklist(employeeId, projectId, idDetailEmployeeProject)
    }

    override fun getSearchListArea(
        projectCode: String,
        shiftId: Int,
        page: Int,
        keywords: String
    ): Single<ListAreaChecklistResponse> {
        return remoteDataSource.getSearchListArea(projectCode, shiftId, page, keywords)
    }

    override fun getSearchListOperator(
        projectId: String,
        shiftId: Int,
        keywords: String
    ): Single<ListOperatorChecklistResponse> {
        return remoteDataSource.getSearchListOperator(projectId, shiftId, keywords)
    }

}