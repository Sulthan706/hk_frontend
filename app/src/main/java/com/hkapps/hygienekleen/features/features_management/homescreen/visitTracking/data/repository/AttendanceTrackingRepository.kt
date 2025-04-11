package com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.BranchNameManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.HistoryAttendanceNew.HistoryAttendanceNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.TotalProjectBranchResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listBranch.AllBranchVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listCountProjectNew.ProjectCountDateNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.model.listHistoryAttendance.AttendancesProjectManagementResponse
import io.reactivex.Single

interface AttendanceTrackingRepository {

    fun getAllAttendanceHistory(
        userId: Int
    ): Single<AttendancesProjectManagementResponse>

    fun getAttendanceHistoryDate(
        userId: Int,
        startDate: String
    ): Single<AttendancesProjectManagementResponse>

    fun getCountProjectDate(
        userId: Int,
        startDate: String,
        endDate: String
    ): Single<ProjectCountDateNewResponse>

    fun getBranchManagement(
        userId: Int
    ): Single<BranchNameManagementResponse>

    fun getAllBranch(): Single<AllBranchVisitResponse>

    fun getTotalProjectBranch(
        branchCode: String
    ): Single<TotalProjectBranchResponse>

    fun getHistoryAttendanceNew(
        userId: Int,
        month: Int,
        year: Int
    ): Single<HistoryAttendanceNewResponse>

}