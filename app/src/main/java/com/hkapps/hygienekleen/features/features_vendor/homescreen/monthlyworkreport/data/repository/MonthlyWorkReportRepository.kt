package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.approvejob.ApproveJobRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.createba.CreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.ListDailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.detailchecklistrkb.DetailChecklistRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.escallationrkb.EscallationLowLevResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.eventcalendar.EventCalendarRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkbnew.HomeRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.limitcreateba.LimitasiCreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.monthrkb.MonthRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.rkboperator.GetStatsRkbOperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.statusapprovejobs.StatusApprovalJobResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb.TargetByDateRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.TargetStatusRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.uploadjob.succesupload.SuccessUploadPhotoResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface MonthlyWorkReportRepository {
    fun getHomeListWork(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<MonthRkbResponseModel>

    fun getListStatusRkb(
        employeeId: Int,
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<TargetStatusRkbResponseModel>

    fun getTargetByDateRkb(
        employeeId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<TargetByDateRkbResponseModel>

    fun getDetailChecklistRkb(
        idJob:Int
    ): Single<DetailChecklistRkbResponseModel>

    fun putCreateBaRkb(
        idJobs: Int,
        employeeId: Int,
        description: String,
        date: String,
        divertedShift : Int,
        file: MultipartBody.Part
    ): Single<CreateBaResponseModel>

    fun putUploadPhotoRkb(
        idJobs: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        uploadType: String,
        comment: String
    ): Single<SuccessUploadPhotoResponseModel>

    fun getEventCalendarRkb(
        projectCode: String,
        employeeId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbResponseModel>
    fun getHomeRkb(
        projectCode: String
    ): Single<HomeRkbResponseModel>

    fun putApproveJobRkb(
        idJobs: Int,
        employeeId: Int
    ): Single<ApproveJobRkbResponseModel>

    fun getStatusApproveJob(
        employeeId: Int
    ): Single<StatusApprovalJobResponseModel>

    fun getEscalationRkb(
        employeeId: Int,
        projectCode: String
    ): Single<EscallationLowLevResponseModel>

    fun getLimitasiCreateBa(
        employeeId: Int,
        idJobs: Int
    ): Single<LimitasiCreateBaResponseModel>

    fun getStatsRkbOperator(
        employeeId: Int,
        projectCode: String
    ): Single<GetStatsRkbOperatorResponseModel>

    fun getRkbDailyTarget(
        adminMasterId: Int,
        projectCode: String
    ):Single<DailyTargetResponse>

    fun getRkbDailyTargetV2(
        employeeId: Int,
        projectCode: String
    ):Single<ListDailyTargetResponse>


    fun getDetailRkbDailyTarget(
        idDetailEmployeeProject: Int,
        employeeId : Int
    ):Single<DailyTargetResponse>

}