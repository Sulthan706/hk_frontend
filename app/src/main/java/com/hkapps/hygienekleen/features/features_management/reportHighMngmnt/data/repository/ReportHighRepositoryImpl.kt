package com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.repository

import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.remote.ReportHighDataSource
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.attendanceReportManagement.AttendanceReportHighResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.countAttendanceReport.CountAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listBranch.BranchesAttendanceReportResponse
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.model.listProject.ProjectsAttendanceReportResponse
import io.reactivex.Single
import javax.inject.Inject

class ReportHighRepositoryImpl @Inject constructor(private val dataSource: ReportHighDataSource):
    ReportHighRepository {

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
        return dataSource.getListAttendanceReportHigh(
            branchCode, projectCode, startAt, endAt, order, whatToOrder, page, size
        )
    }

    override fun getListBranchAttendanceReport(): Single<BranchesAttendanceReportResponse> {
        return dataSource.getListBranchAttendanceReport()
    }

    override fun getCountAttendanceReportHigh(
        branchCode: String,
        projectCode: String,
        startAt: String,
        endAt: String
    ): Single<CountAttendanceReportResponse> {
        return dataSource.getCountAttendanceReportHigh(branchCode, projectCode, startAt, endAt)
    }

    override fun getListProjectAttendanceReport(branchCode: String): Single<ProjectsAttendanceReportResponse> {
        return dataSource.getListProjectAttendanceReport(branchCode)
    }

}