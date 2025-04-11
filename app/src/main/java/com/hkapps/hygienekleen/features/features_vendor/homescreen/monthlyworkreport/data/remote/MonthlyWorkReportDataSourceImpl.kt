package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.service.MonthlyWorkReportService
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
import javax.inject.Inject

class MonthlyWorkReportDataSourceImpl @Inject constructor (private val service: MonthlyWorkReportService) : MonthlyWorkReportDataSource {

    override fun getHomeListWork(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<MonthRkbResponseModel> {
        return service.getHomeListWork( projectCode, startDate, endDate)
    }

    override fun getListStatusRkb(
        employeeId: Int,
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int
    ): Single<TargetStatusRkbResponseModel> {
        return service.getListStatusRkb(employeeId, projectCode, startDate, endDate, filterBy, page, perPage,locationId)
    }

    override fun getTargetByDateRkb(
        employeeId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<TargetByDateRkbResponseModel> {
        return service.getTargetByDateRkb(employeeId, projectCode, date, page, perPage)
    }

    override fun getDetailChecklistRkb(idJob: Int): Single<DetailChecklistRkbResponseModel> {
        return service.getDetailChecklistRkb(idJob)
    }

    override fun putCreateBaRkb(
        idJobs: Int,
        employeeId: Int,
        description: String,
        date: String,
        divertedShift : Int,
        file: MultipartBody.Part
    ): Single<CreateBaResponseModel> {
        return service.putCreateBaRkb(idJobs, employeeId, description, date,divertedShift, file)
    }

    override fun putUploadPhotoRkb(
        idJobs: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        uploadType: String,
        comment: String
    ): Single<SuccessUploadPhotoResponseModel> {
        return service.putUploadPhotoRkb(idJobs, employeeId, file, uploadType, comment)
    }

    override fun getEventCalendarRkb(
        projectCode: String,
        employeeId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbResponseModel> {
        return service.getEventCalendarRkb(projectCode, employeeId, month, year)
    }


    override fun getHomeRkb(projectCode: String): Single<HomeRkbResponseModel> {
        return service.getHomeRkb( projectCode)
    }

    override fun putApproveJobRkb(
        idJobs: Int,
        employeeId: Int
    ): Single<ApproveJobRkbResponseModel> {
        return service.putApproveJobRkb(idJobs, employeeId)
    }

    override fun getStatusApproveJob(employeeId: Int): Single<StatusApprovalJobResponseModel> {
        return service.getStatusApproveJob(employeeId)
    }

    override fun getEscalationRkb(
        employeeId: Int,
        projectCode: String
    ): Single<EscallationLowLevResponseModel> {
        return service.getEscalationRkb(employeeId, projectCode)
    }

    override fun getLimitasiCreateBa(
        employeeId: Int,
        idJobs: Int
    ): Single<LimitasiCreateBaResponseModel> {
        return service.getLimitasiCreateBa(employeeId, idJobs)
    }

    override fun getStatsRkbOperator(
        employeeId: Int,
        projectCode: String
    ): Single<GetStatsRkbOperatorResponseModel> {
        return service.getStatsRkbOperator(employeeId, projectCode)
    }

    override fun getRkbDailyTarget(
        adminMasterId: Int,
        projectCode: String
    ): Single<DailyTargetResponse> {
        return service.getRkbDailyTarget(adminMasterId, projectCode)
    }

    override fun getRkbDailyTargetV2(
        employeeId: Int,
        projectCode: String
    ): Single<ListDailyTargetResponse> {
        return service.getRkbDailyTargetV2(employeeId, projectCode)
    }

    override fun getDetailRkbDailyTarget(
        idDetailEmployeeProject: Int,
        employeeId : Int): Single<DailyTargetResponse> {
        return service.getDetailRkbDailyTarget(idDetailEmployeeProject,employeeId)
    }


}