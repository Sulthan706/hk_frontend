package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.remote.MonthlyWorkReportDataSource
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

class MonthlyWorkReportRepositoryImpl @Inject constructor (private val dataSource: MonthlyWorkReportDataSource) :
    MonthlyWorkReportRepository {

    override fun getHomeListWork(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<MonthRkbResponseModel> {
        return dataSource.getHomeListWork(projectCode, startDate, endDate)
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
        return dataSource.getListStatusRkb(employeeId, projectCode, startDate, endDate, filterBy, page, perPage,locationId)
    }

    override fun getTargetByDateRkb(
        employeeId: Int,
        projectCode: String,
        date: String,
        page: Int,
        perPage: Int
    ): Single<TargetByDateRkbResponseModel> {
        return dataSource.getTargetByDateRkb(employeeId, projectCode, date, page, perPage)
    }

    override fun getDetailChecklistRkb(idJob: Int): Single<DetailChecklistRkbResponseModel> {
        return dataSource.getDetailChecklistRkb(idJob)
    }

    override fun putCreateBaRkb(
        idJobs: Int,
        employeeId: Int,
        description: String,
        date: String,
        divertedShift : Int,
        file: MultipartBody.Part
    ): Single<CreateBaResponseModel> {
        return dataSource.putCreateBaRkb(idJobs, employeeId, description, date,divertedShift, file)
    }

    override fun putUploadPhotoRkb(
        idJobs: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        uploadType: String,
        comment: String
    ): Single<SuccessUploadPhotoResponseModel> {
        return dataSource.putUploadPhotoRkb(idJobs, employeeId, file, uploadType, comment)
    }

    override fun getEventCalendarRkb(
        projectCode: String,
        employeeId: Int,
        month: String,
        year: String
    ): Single<EventCalendarRkbResponseModel> {
        return dataSource.getEventCalendarRkb(projectCode, employeeId, month, year)
    }

    override fun getHomeRkb(projectCode: String): Single<HomeRkbResponseModel> {
        return dataSource.getHomeRkb(projectCode)
    }

    override fun putApproveJobRkb(
        idJobs: Int,
        employeeId: Int
    ): Single<ApproveJobRkbResponseModel> {
        return dataSource.putApproveJobRkb(idJobs, employeeId)
    }

    override fun getStatusApproveJob(employeeId: Int): Single<StatusApprovalJobResponseModel> {
        return dataSource.getStatusApproveJob(employeeId)
    }

    override fun getEscalationRkb(
        employeeId: Int,
        projectCode: String
    ): Single<EscallationLowLevResponseModel> {
        return dataSource.getEscalationRkb(employeeId, projectCode)
    }

    override fun getLimitasiCreateBa(
        employeeId: Int,
        idJobs: Int
    ): Single<LimitasiCreateBaResponseModel> {
        return dataSource.getLimitasiCreateBa(employeeId, idJobs)
    }

    override fun getStatsRkbOperator(
        employeeId: Int,
        projectCode: String
    ): Single<GetStatsRkbOperatorResponseModel> {
        return dataSource.getStatsRkbOperator(employeeId, projectCode)
    }

    override fun getRkbDailyTarget(
        adminMasterId: Int,
        projectCode: String
    ): Single<DailyTargetResponse> {
        return dataSource.getRkbDailyTarget(adminMasterId, projectCode)
    }

    override fun getRkbDailyTargetV2(
        employeeId: Int,
        projectCode: String
    ): Single<ListDailyTargetResponse> {
        return dataSource.getRkbDailyTargetV2(employeeId, projectCode)
    }

    override fun getDetailRkbDailyTarget(
        idDetailEmployeeProject: Int,
        employeeId : Int): Single<DailyTargetResponse> {
        return dataSource.getDetailRkbDailyTarget(idDetailEmployeeProject,employeeId)
    }


}