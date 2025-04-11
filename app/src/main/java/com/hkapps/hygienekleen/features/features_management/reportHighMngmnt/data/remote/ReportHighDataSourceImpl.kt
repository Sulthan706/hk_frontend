package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.remote

import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.service.ReportHighService
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.AttendanceReportHighResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.countAttendanceReport.CountAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch.BranchesAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listProject.ProjectsAttendanceReportResponse
import io.reactivex.Single
import javax.inject.Inject

class ReportHighDataSourceImpl @Inject constructor(private val service: ReportHighService):
    ReportHighDataSource {
    override fun getListAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String,
        order: String,
        whatToOrder: String,
        page: Int,
        size: Int
    ): Single<AttendanceReportHighResponse> {
        return service.getListAttendanceReportHigh(
            branchCode, projectCode, startAt, endAt, order, whatToOrder, page, size
        )
    }

    override fun getListBranchAttendanceReport(): Single<BranchesAttendanceReportResponse> {
        return service.getListBranchAttendanceReport()
    }

    override fun getCountAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String
    ): Single<CountAttendanceReportResponse> {
        return service.getCountAttendanceReportHigh(branchCode, projectCode, startAt, endAt)
    }

    override fun getListProjectAttendanceReport(branchCode: String): Single<ProjectsAttendanceReportResponse> {
        return service.getListProjectAttendanceReport(branchCode)
    }

}