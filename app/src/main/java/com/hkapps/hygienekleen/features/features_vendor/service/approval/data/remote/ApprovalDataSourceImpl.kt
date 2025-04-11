package com.hkapps.hygienekleen.features.features_vendor.service.approval.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.service.ApprovalService
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.approvalAttendance.ApprovalAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.ListAttendanceApprovalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.HistoryUserFlyingResponse
import io.reactivex.Single
import javax.inject.Inject

class ApprovalDataSourceImpl @Inject constructor(private val service: ApprovalService) : ApprovalDataSource {

    override fun getListAttendanceApproval(
        projectCode: String,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse> {
        return service.getListAttendanceApproval(projectCode, page, perPage)
    }

    override fun submitDeclineAttendance(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitDeclineAttendance(employeeId, idUserFlying)
    }

    override fun submitApproveAttendanceIn(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitApproveAttendanceIn(employeeId, idUserFlying)
    }

    override fun submitApproveAttendanceOut(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitApproveAttendanceOut(employeeId, idUserFlying)
    }

    override fun getHistoryUserFlying(
        userId: Int,
        projectCode: String,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse> {
        return service.getHistoryUserFlying(userId, projectCode, page, perPage, date)
    }

    override fun submitDeclineAttendanceManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitDeclineAttendanceManagement(userId, idUserFlying)
    }

    override fun submitApproveAttendanceInManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitApproveAttendanceInManagement(userId, idUserFlying)
    }

    override fun submitApproveAttendanceOutManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse> {
        return service.submitApproveAttendanceOutManagement(userId, idUserFlying)
    }

    override fun getListAttendanceApprovalManagement(
        userId: Int,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse> {
        return service.getListAttendanceApprovalManagement(userId, page, perPage)
    }

    override fun getHistoryUserFlyingManagement(
        userId: Int,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse> {
        return service.getHistoryUserFlyingManagement(userId, page, perPage, date)
    }

}