package com.hkapps.hygienekleen.features.features_vendor.service.approval.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.approvalAttendance.ApprovalAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listAttendance.ListAttendanceApprovalResponse
import com.hkapps.hygienekleen.features.features_vendor.service.approval.model.listHistoryAttendance.HistoryUserFlyingResponse
import io.reactivex.Single

interface ApprovalRepository {

    fun getListAttendanceApproval (
        projectCode: String,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse>

    fun submitDeclineAttendance (
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun submitApproveAttendanceIn(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun submitApproveAttendanceOut(
        employeeId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun getHistoryUserFlying(
        userId: Int,
        projectCode: String,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse>

    fun submitDeclineAttendanceManagement (
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun submitApproveAttendanceInManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun submitApproveAttendanceOutManagement(
        userId: Int,
        idUserFlying: Int
    ): Single<ApprovalAttendanceResponse>

    fun getListAttendanceApprovalManagement (
        userId: Int,
        page: Int,
        perPage: Int
    ): Single<ListAttendanceApprovalResponse>

    fun getHistoryUserFlyingManagement(
        userId: Int,
        page: Int,
        perPage: Int,
        date: String
    ): Single<HistoryUserFlyingResponse>

}