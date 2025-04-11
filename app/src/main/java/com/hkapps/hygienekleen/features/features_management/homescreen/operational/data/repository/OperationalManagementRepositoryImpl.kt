package com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.remote.OperationalManagementDataSource
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
import javax.inject.Inject

class OperationalManagementRepositoryImpl @Inject constructor(private val dataSource: OperationalManagementDataSource) : OperationalManagementRepository {

    override fun getListAllOperational(page: Int): Single<ListAllOperationalResponseModel> {
        return dataSource.getListAllOperational(page)
    }

    override fun getListOperationalByRoleCEO(
        jobRole: String,
        page: Int
    ): Single<ListAllOperationalResponseModel> {
        return dataSource.getListOperationalByRoleCEO(jobRole, page)
    }

    override fun getListOperationalProject(
        employeeId: Int,
        page: Int
    ): Single<ListOperationalProjectResponseModel> {
        return dataSource.getListOperationalProject(employeeId, page)
    }

    override fun getListOperationalByRoleProject(
        employeeId: Int,
        jobRole: String,
        page: Int
    ): Single<ListOperationalProjectResponseModel> {
        return dataSource.getListOperationalByRoleProject(employeeId, jobRole, page)
    }

    override fun getListAllOperationalCEO(page: Int): Single<ListCEOManagementResponseModel> {
        return dataSource.getListAllOperationalCEO(page)
    }

    override fun getListAllOperationalBOD(page: Int): Single<ListCEOManagementResponseModel> {
        return dataSource.getListAllOperationalBOD(page)
    }

    override fun getListManagementByRoleCeoBod(jabatan: String): Single<ListGmOmFmResponseModel> {
        return dataSource.getListManagementByRoleCeoBod(jabatan)
    }

    override fun getListManagementByRoleFmGmOm(
        employeeId: Int,
        jabatan: String
    ): Single<ListGmOmFmResponseModel> {
        return dataSource.getListManagementByRoleFmGmOm(employeeId, jabatan)
    }

    override fun getListGmOmFm(employeeId: Int): Single<ListGmOmFmResponseModel> {
        return dataSource.getListGmOmFm(employeeId)
    }

    override fun getDetailOperational(employeeId: Int): Single<DetailOperationalResponseModel> {
        return dataSource.getDetailOperational(employeeId)
    }

    override fun getDetailManagement(adminMasterId: Int): Single<DetailManagementResponseModel> {
        return dataSource.getDetailManagement(adminMasterId)
    }

    override fun getDetailOperationalAttendance(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAttendanceOperationalResponseModel> {
        return dataSource.getDetailOperationalAttendance(projectCode, employeeId, month, year)
    }

    override fun getHistoryAttendance(
        employeeId: Int,
        projectCode: String
    ): Single<HistoryAttendanceOperationalResponseModel> {
        return dataSource.getHistoryAttendance(employeeId, projectCode)
    }

    override fun searchOperationalCeoBod(
        page: Int,
        keywords: String
    ): Single<ListAllOperationalResponseModel> {
        return dataSource.searchOperationalCeoBod(page, keywords)
    }

    override fun searchOperationalGmOmFm(
        employeeId: Int,
        page: Int,
        keywords: String
    ): Single<ListOperationalProjectResponseModel> {
        return dataSource.searchOperationalGmOmFm(employeeId, page, keywords)
    }

    override fun searchManagementUserCeo(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel> {
        return dataSource.searchManagementUserCeo(page, keywords)
    }

    override fun searchManagementUserBod(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel> {
        return dataSource.searchManagementUserBod(page, keywords)
    }

    override fun searchManagementUserGmOmFm(
        employeeId: Int,
        keywords: String
    ): Single<ListGmOmFmResponseModel> {
        return dataSource.searchManagementUserGmOmFm(employeeId, keywords)
    }

    override fun getListOperationalByProjectCode(
        jobRole: String,
        projectCode: String,
        page: Int
    ): Single<ListOperationalByProjectCodeResponseModel> {
        return dataSource.getListOperationalByProjectCode(jobRole, projectCode, page)
    }

    override fun getProfileRating(employeeId: Int): Single<RatingEmployeeResponse> {
        return dataSource.getProfileRating(employeeId)
    }

    override fun giveEmployeeRating(
        ratingByUserId: RequestBody,
        employeeId: RequestBody,
        rating: RequestBody,
        projectCode: RequestBody,
        jobCode: RequestBody
    ): Single<GiveEmployeeRatingResponse> {
        return dataSource.giveEmployeeRating(
            ratingByUserId,
            employeeId,
            rating,
            projectCode,
            jobCode
        )
    }

    override fun getBranchOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel> {
        return dataSource.getBranchOperational(page, size)
    }

    override fun getBranchManagementOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel> {
        return dataSource.getBranchManagementOperational(page, size)
    }

    override fun getListManagementOperational(
        idCabang: Int,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtManagementResponseModel> {
        return dataSource.getListManagementOperational(idCabang, role, page, size)
    }

    override fun getListEmployeeOperational(
        branchCode: String,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtEmployeOperationalResponseModel> {
        return dataSource.getListEmployeeOperational(branchCode, role, page, size)
    }


}