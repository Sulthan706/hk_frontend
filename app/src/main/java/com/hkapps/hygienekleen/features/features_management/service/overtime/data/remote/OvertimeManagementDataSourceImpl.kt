package com.hkapps.hygienekleen.features.features_management.service.overtime.data.remote

import com.hkapps.hygienekleen.features.features_management.service.overtime.data.service.OvertimeManagementService
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

class OvertimeManagementDataSourceImpl @Inject constructor(private val service: OvertimeManagementService):
    OvertimeManagementDataSource {

    override fun getListOvertimeChange(
        employeeId: Int,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<OvertimeChangeManagementResponse> {
        return service.getListOvertimeChange(employeeId, jabatan, startDate, endDate, page)
    }

    override fun getOperationalOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return service.getOperationalOvertimeChange(projectId, date, overtimeType, shiftId, jabatan)
    }

    override fun getReplaceOperationalOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return service.getReplaceOperationalOvertimeChange(projectId, date, shiftId, jobCode, jabatan)
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
        return service.createOvertimeChangeManagement(createdById, replaceOprId, operationalId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getProjectsOvertimeManagement(
        adminMasterId: Int
    ): Single<ProjectsOvertimeResponse> {
        return service.getProjectsOvertimeManagement(adminMasterId)
    }

    override fun getOperationalOvertimeResign(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse> {
        return service.getOperationalOvertimeResign(projectId, date, shiftId, jabatan)
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
        return service.createOvertimeResignManagement(createdById, replaceOprId, locationId, subLocationId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getSearchProjectManagement(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<SearchProjectManagementResponseModel> {
        return service.getSearchProjectManagement(adminMasterId, page, keywords)
    }

    override fun getEmployeeReplaceManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<EmployeeReplaceResponseModel> {
        return service.getEmployeeReplaceManagement(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun getListShiftManagement(projectId: String): Single<ListShiftManagementResponseModel> {
        return service.getListShiftManagement(projectId)
    }

    override fun getEmployeeManagement(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<SelectEmployeeMgmntResponseModel> {
        return service.getEmployeeManagement(projectId, date, overtimeType, shiftId, jabatan)
    }

    override fun getChangeEmployeeManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<ChangeEmployeeMgmntResponseModel> {
        return service.getChangeEmployeeManagement(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun getLocationManagement(
        projectId: String,
        shiftId: Int,
        date: String
    ): Single<LocationManagementResponseModel> {
        return service.getLocationManagement(projectId, shiftId, date)
    }

    override fun getSublocationManagement(
        projectId: String,
        locationId: Int,
        shiftId: Int
    ): Single<SubLocationManagementResponseModel> {
        return service.getSublocationManagement(projectId, locationId, shiftId)
    }

    override fun getPlottingOperational(idEmployeeProject: Int): Single<PlottingOperationalResponse> {
        return service.getPlottingOperational(idEmployeeProject)
    }


}