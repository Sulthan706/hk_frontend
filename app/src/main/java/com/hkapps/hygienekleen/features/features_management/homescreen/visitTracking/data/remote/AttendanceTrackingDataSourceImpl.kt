package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.service.AttendanceTrackingService
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.BranchNameManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.HistoryAttendanceNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.TotalProjectBranchResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch.AllBranchVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.ProjectCountDateNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.AttendancesProjectManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class AttendanceTrackingDataSourceImpl @Inject constructor(private val service: AttendanceTrackingService):
    AttendanceTrackingDataSource {

    override fun getAllAttendanceHistory(userId: Int): Single<AttendancesProjectManagementResponse> {
        return service.getAllAttendanceHistory(userId)
    }

    override fun getAttendanceHistoryDate(
        userId: Int,
        startDate: String
    ): Single<AttendancesProjectManagementResponse> {
        return service.getAttendanceHistoryDate(userId, startDate)
    }

    override fun getCountProjectDate(
        userId: Int,
        startDate: String,
        endDate: String
    ): Single<ProjectCountDateNewResponse> {
        return service.getCountProjectDate(userId, startDate, endDate)
    }

    override fun getBranchManagement(userId: Int): Single<BranchNameManagementResponse> {
        return service.getBranchManagement(userId)
    }

    override fun getAllBranch(): Single<AllBranchVisitResponse> {
        return service.getAllBranch()
    }

    override fun getTotalProjectBranch(branchCode: String): Single<TotalProjectBranchResponse> {
        return service.getTotalProjectBranch(branchCode)
    }

    override fun getHistoryAttendanceNew(
        userId: Int,
        month: Int,
        year: Int
    ): Single<HistoryAttendanceNewResponse> {
        return service.getHistoryAttendanceNew(userId, month, year)
    }

}