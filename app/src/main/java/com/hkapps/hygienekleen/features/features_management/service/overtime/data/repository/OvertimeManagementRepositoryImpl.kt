package com.hkapps.hygienekleen.features.features_management.service.overtime.data.repository

import com.hkapps.hygienekleen.features.features_management.service.overtime.data.remote.OvertimeManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.changeemployeemanagement.ChangeEmployeeMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.createOvertimeChange.CreateOvertimeChangeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.employeereplace.EmployeeReplaceResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.getlocationmanagement.LocationManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.getsublocationmanagement.SubLocationManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listOvertimeChange.OvertimeChangeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listProjectManagement.ProjectsOvertimeResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listshift.ListShiftManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.plottingOperationalResponse.PlottingOperationalResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.searchProjectManagement.SearchProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectEmployee.OperationalOvertimeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectemployeemanagement.SelectEmployeeMgmntResponseModel
import io.reactivex.Single
import javax.inject.Inject

class OvertimeManagementRepositoryImpl @Inject constructor(private val dataSource: OvertimeManagementDataSource):
    OvertimeManagementRepository {

    override fun getListOvertimeChange(
        employeeId: Int,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<OvertimeChangeManagementResponse> {
        return dataSource.getListOvertimeChange(employeeId, jabatan, startDate, endDate, page)
    }

    override fun getOperationalOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return dataSource.getOperationalOvertimeChange(projectId, date, overtimeType, shiftId, jabatan)
    }

    override fun getReplaceOperationalOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return dataSource.getReplaceOperationalOvertimeChange(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun createOvertimeChangeManagement(
        createdById: Int,
        replaceOprId: Int,
        operationalId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeManagementResponse> {
        return dataSource.createOvertimeChangeManagement(createdById, replaceOprId, operationalId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getProjectsOvertimeManagement(
        adminMasterId: Int
    ): Single<ProjectsOvertimeResponse> {
        return dataSource.getProjectsOvertimeManagement(adminMasterId)
    }

    override fun getOperationalOvertimeResign(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return dataSource.getOperationalOvertimeResign(projectId, date, shiftId, jabatan)
    }

    override fun createOvertimeResignManagement(
        createdById: Int,
        replaceOprId: Int,
        locationId: Int,
        subLocationId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeManagementResponse> {
        return dataSource.createOvertimeResignManagement(createdById, replaceOprId, locationId, subLocationId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getSearchProjectManagement(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<SearchProjectManagementResponseModel> {
        return dataSource.getSearchProjectManagement(adminMasterId, page, keywords)
    }

    override fun getEmployeeReplaceManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<EmployeeReplaceResponseModel> {
        return dataSource.getEmployeeReplaceManagement(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun getListShiftManagement(projectId: String): Single<ListShiftManagementResponseModel> {
        return dataSource.getListShiftManagement(projectId)
    }

    override fun getEmployeeManagement(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<SelectEmployeeMgmntResponseModel> {
        return dataSource.getEmployeeManagement(projectId, date, overtimeType, shiftId, jabatan)
    }

    override fun getChangeEmployeeManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<ChangeEmployeeMgmntResponseModel> {
        return dataSource.getChangeEmployeeManagement(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun getLocationManagement(
        projectId: String,
        shiftId: Int,
        date: String
    ): Single<LocationManagementResponseModel> {
        return dataSource.getLocationManagement(projectId, shiftId, date)
    }

    override fun getSublocationManagement(
        projectId: String,
        locationId: Int,
        shiftId: Int
    ): Single<SubLocationManagementResponseModel> {
        return dataSource.getSublocationManagement(projectId, locationId, shiftId)
    }

    override fun getPlottingOperational(idEmployeeProject: Int): Single<PlottingOperationalResponse> {
        return dataSource.getPlottingOperational(idEmployeeProject)
    }

}