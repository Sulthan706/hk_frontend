package com.hkapps.hygienekleen.features.features_vendor.service.approval.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.remote.ApprovalDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.approvalAttendance.ApprovalAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.ListAttendanceApprovalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.HistoryUserFlyingResponse
import io.reactivex.Single
import javax.inject.Inject

class ApprovalRepositoryImpl @Inject constructor(private val dataSource: ApprovalDataSource) : ApprovalRepository {

    override fun getListAttendanceApproval(
        projectCode: String,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse> {
        return dataSource.getListAttendanceApproval(projectCode, page, perPage)
    }

    override fun submitDeclineAttendance(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitDeclineAttendance(employeeId, idUserFlying)
    }

    override fun submitApproveAttendanceIn(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitApproveAttendanceIn(employeeId, idUserFlying)
    }

    override fun submitApproveAttendanceOut(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitApproveAttendanceOut(employeeId, idUserFlying)
    }

    override fun getHistoryUserFlying(
        userId: Int,
        projectCode: String,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse> {
        return dataSource.getHistoryUserFlying(userId, projectCode, page, perPage, date)
    }

    override fun submitDeclineAttendanceManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitDeclineAttendanceManagement(userId, idUserFlying)
    }

    override fun submitApproveAttendanceInManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitApproveAttendanceInManagement(userId, idUserFlying)
    }

    override fun submitApproveAttendanceOutManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return dataSource.submitApproveAttendanceOutManagement(userId, idUserFlying)
    }

    override fun getListAttendanceApprovalManagement(
        userId: Int,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse> {
        return dataSource.getListAttendanceApprovalManagement(userId, page, perPage)
    }

    override fun getHistoryUserFlyingManagement(
        userId: Int,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse> {
        return dataSource.getHistoryUserFlyingManagement(userId, page, perPage, date)
    }

}