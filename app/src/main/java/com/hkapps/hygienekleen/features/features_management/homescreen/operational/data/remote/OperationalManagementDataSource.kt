package com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.BranchOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailattendance.DetailAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailmanagement.DetailManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailoperational.DetailOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.giveratingemployee.GiveEmployeeRatingResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance.HistoryAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listalloperational.ListAllOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.ListCEOManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listemployee.ListOprtEmployeOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.ListGmOmFmResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listmanagement.ListOprtManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalbyprojectcode.ListOperationalByProjectCodeResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalproject.ListOperationalProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.ratingEmployee.RatingEmployeeResponse
import io.reactivex.Single
import okhttp3.RequestBody

interface OperationalManagementDataSource {
    fun getListAllOperational(
        page: Int,
    ): Single<ListAllOperationalResponseModel>

    fun getListOperationalByRoleCEO(
        jobRole: String,
        page: Int
    ): Single<ListAllOperationalResponseModel>

    fun getListOperationalProject(
        employeeId: Int,
        page: Int
    ): Single<ListOperationalProjectResponseModel>

    fun getListOperationalByRoleProject(
        employeeId: Int,
        jobRole: String,
        page: Int
    ): Single<ListOperationalProjectResponseModel>

    fun getListAllOperationalCEO(
        page: Int
    ): Single<ListCEOManagementResponseModel>

    fun getListAllOperationalBOD(
        page: Int
    ): Single<ListCEOManagementResponseModel>

    fun getListManagementByRoleCeoBod(
        jabatan: String
    ): Single<ListGmOmFmResponseModel>

    fun getListManagementByRoleFmGmOm(
        employeeId: Int,
        jabatan: String
    ): Single<ListGmOmFmResponseModel>

    fun getListGmOmFm(
        employeeId: Int
    ): Single<ListGmOmFmResponseModel>

    fun getDetailOperational(
        employeeId: Int
    ): Single<DetailOperationalResponseModel>

    fun getDetailManagement(
        adminMasterId: Int
    ): Single<DetailManagementResponseModel>

    fun getDetailOperationalAttendance(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAttendanceOperationalResponseModel>

    fun getHistoryAttendance(
        employeeId: Int,
        projectCode: String,
    ): Single<HistoryAttendanceOperationalResponseModel>

    fun searchOperationalCeoBod(
        page: Int,
        keywords: String
    ): Single<ListAllOperationalResponseModel>

    fun searchOperationalGmOmFm(
        employeeId: Int,
        page: Int,
        keywords: String
    ): Single<ListOperationalProjectResponseModel>

    fun searchManagementUserCeo(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel>

    fun searchManagementUserBod(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel>

    fun searchManagementUserGmOmFm(
        employeeId: Int,
        keywords: String
    ): Single<ListGmOmFmResponseModel>

    fun getListOperationalByProjectCode(
        jobRole: String,
        projectCode: String,
        page: Int
    ): Single <ListOperationalByProjectCodeResponseModel>

    fun getProfileRating(
        employeeId: Int
    ): Single<RatingEmployeeResponse>

    fun giveEmployeeRating(
        ratingByUserId: RequestBody,
        employeeId: RequestBody,
        rating: RequestBody,
        projectCode: RequestBody,
        jobCode: RequestBody,
    ): Single<GiveEmployeeRatingResponse>

    fun getBranchOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel>

    fun getBranchManagementOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel>

    fun getListManagementOperational(
        idCabang: Int,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtManagementResponseModel>

    fun getListEmployeeOperational(
        branchCode: String,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtEmployeOperationalResponseModel>

}