package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.remote.AttendanceTrackingDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.BranchNameManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.HistoryAttendanceNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.TotalProjectBranchResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch.AllBranchVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.ProjectCountDateNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.AttendancesProjectManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class AttendanceTrackingRepositoryImpl @Inject constructor(private val dataSource: AttendanceTrackingDataSource):
    AttendanceTrackingRepository {

    override fun getAllAttendanceHistory(userId: Int): Single<AttendancesProjectManagementResponse> {
        return dataSource.getAllAttendanceHistory(userId)
    }

    override fun getAttendanceHistoryDate(
        userId: Int,
        startDate: String
    ): Single<AttendancesProjectManagementResponse> {
        return dataSource.getAttendanceHistoryDate(userId, startDate)
    }

    override fun getCountProjectDate(
        userId: Int,
        startDate: String,
        endDate: String
    ): Single<ProjectCountDateNewResponse> {
        return dataSource.getCountProjectDate(userId, startDate, endDate)
    }

    override fun getBranchManagement(userId: Int): Single<BranchNameManagementResponse> {
        return dataSource.getBranchManagement(userId)
    }

    override fun getAllBranch(): Single<AllBranchVisitResponse> {
        return dataSource.getAllBranch()
    }

    override fun getTotalProjectBranch(branchCode: String): Single<TotalProjectBranchResponse> {
        return dataSource.getTotalProjectBranch(branchCode)
    }

    override fun getHistoryAttendanceNew(
        userId: Int,
        month: Int,
        year: Int
    ): Single<HistoryAttendanceNewResponse> {
        return dataSource.getHistoryAttendanceNew(userId, month, year)
    }

}