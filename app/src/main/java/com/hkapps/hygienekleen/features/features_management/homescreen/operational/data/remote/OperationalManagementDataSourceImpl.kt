package com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.service.OperationalManagementService
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

class OperationalManagementDataSourceImpl @Inject constructor(private val service: OperationalManagementService): OperationalManagementDataSource {

    override fun getListAllOperational(page: Int): Single<ListAllOperationalResponseModel> {
        return service.getListAllOperational(page)
    }

    override fun getListOperationalByRoleCEO(
        jobRole: String,
        page: Int
    ): Single<ListAllOperationalResponseModel> {
        return service.getListOperationalByRoleCEO(jobRole, page)
    }

    override fun getListOperationalProject(
        employeeId: Int,
        page: Int
    ): Single<ListOperationalProjectResponseModel> {
        return service.getListOperationalProject(employeeId, page)
    }

    override fun getListOperationalByRoleProject(
        employeeId: Int,
        jobRole: String,
        page: Int
    ): Single<ListOperationalProjectResponseModel> {
        return service.getListOperationalByRoleProject(employeeId, jobRole, page)
    }

    override fun getListAllOperationalCEO(page: Int): Single<ListCEOManagementResponseModel> {
        return service.getListAllOperationalCEO(page)
    }

    override fun getListAllOperationalBOD(page: Int): Single<ListCEOManagementResponseModel> {
        return service.getListAllOperationalBOD(page)
    }

    override fun getListManagementByRoleCeoBod(jabatan: String): Single<ListGmOmFmResponseModel> {
        return service.getListManagementByRoleCeoBod(jabatan)
    }

    override fun getListManagementByRoleFmGmOm(
        employeeId: Int,
        jabatan: String
    ): Single<ListGmOmFmResponseModel> {
        return service.getListManagementByRoleFmGmOm(employeeId, jabatan)
    }

    override fun getListGmOmFm(employeeId: Int): Single<ListGmOmFmResponseModel> {
        return service.getListGmOmFm(employeeId)
    }

    override fun getDetailOperational(employeeId: Int): Single<DetailOperationalResponseModel> {
        return service.getDetailOperational(employeeId)
    }

    override fun getDetailManagement(adminMasterId: Int): Single<DetailManagementResponseModel> {
        return service.getDetailManagement(adminMasterId)
    }

    override fun getDetailOperationalAttendance(
        projectCode: String,
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<DetailAttendanceOperationalResponseModel> {
        return service.getDetailOperationalAttendance(projectCode, employeeId, month, year)
    }

    override fun getHistoryAttendance(
        employeeId: Int,
        projectCode: String
    ): Single<HistoryAttendanceOperationalResponseModel> {
        return service.getHistoryAttendance(employeeId, projectCode)
    }

    override fun searchOperationalCeoBod(
        page: Int,
        keywords: String
    ): Single<ListAllOperationalResponseModel> {
        return service.searchOperationalCeoBod(page, keywords)
    }

    override fun searchOperationalGmOmFm(
        employeeId: Int,
        page: Int,
        keywords: String
    ): Single<ListOperationalProjectResponseModel> {
        return service.searchOperationalGmOmFm(employeeId, page, keywords)
    }

    override fun searchManagementUserCeo(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel> {
        return service.searchManagementUserCeo(page, keywords)
    }

    override fun searchManagementUserBod(
        page: Int,
        keywords: String
    ): Single<ListCEOManagementResponseModel> {
        return service.searchManagementUserBod(page, keywords)
    }

    override fun searchManagementUserGmOmFm(
        employeeId: Int,
        keywords: String
    ): Single<ListGmOmFmResponseModel> {
        return service.searchManagementUserGmOmFm(employeeId, keywords)
    }

    override fun getListOperationalByProjectCode(
        jobRole: String,
        projectCode: String,
        page: Int
    ): Single<ListOperationalByProjectCodeResponseModel> {
        return service.getListOperationalByProjectCode(jobRole, projectCode, page)
    }

    override fun getProfileRating(employeeId: Int): Single<RatingEmployeeResponse>{
        return service.getProfileRating(employeeId)
    }

    override fun giveEmployeeRating(
        ratingByUserId: RequestBody,
        employeeId: RequestBody,
        rating: RequestBody,
        projectCode: RequestBody,
        jobCode: RequestBody
    ): Single<GiveEmployeeRatingResponse> {
        return service.giveRatingEmployee(ratingByUserId,employeeId, rating, projectCode, jobCode)
    }

    override fun getBranchOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel> {
        return service.getBranchOperational(page, size)
    }

    override fun getBranchManagementOperational(
        page: Int,
        size: Int
    ): Single<BranchOperationalResponseModel> {
        return service.getBranchManagementOperational(page, size)
    }

    override fun getListManagementOperational(
        idCabang: Int,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtManagementResponseModel> {
        return service.getListManagementOperational(idCabang, role, page, size)
    }

    override fun getListEmployeeOperational(
        branchCode: String,
        role: String,
        page: Int,
        size: Int
    ): Single<ListOprtEmployeOperationalResponseModel> {
        return service.getListEmployeeOperational(branchCode, role, page, size)
    }

}