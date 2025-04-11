package com.hkapps.hygienekleen.features.features_management.service.overtime.data.repository

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

interface OvertimeManagementRepository {

    fun getListOvertimeChange (
        employeeId: Int,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<OvertimeChangeManagementResponse>

    fun getOperationalOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    fun getReplaceOperationalOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    fun createOvertimeChangeManagement(
        createdById: Int,
        replaceOprId: Int,
        operationalId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeManagementResponse>

    fun getProjectsOvertimeManagement(
        adminMasterId: Int
    ): Single<ProjectsOvertimeResponse>

    fun getOperationalOvertimeResign(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    fun createOvertimeResignManagement(
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
    ): Single<CreateOvertimeChangeManagementResponse>

    fun getSearchProjectManagement(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<SearchProjectManagementResponseModel>

    fun getEmployeeReplaceManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<EmployeeReplaceResponseModel>

    fun getListShiftManagement(
        projectId: String
    ): Single<ListShiftManagementResponseModel>

    fun getEmployeeManagement(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<SelectEmployeeMgmntResponseModel>

    fun getChangeEmployeeManagement(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<ChangeEmployeeMgmntResponseModel>

    fun getLocationManagement(
        projectId: String,
        shiftId: Int,
        date: String
    ): Single<LocationManagementResponseModel>

    fun getSublocationManagement(
        projectId: String,
        locationId: Int,
        shiftId: Int,
    ): Single<SubLocationManagementResponseModel>

    fun getPlottingOperational(
        idEmployeeProject: Int
    ): Single<PlottingOperationalResponse>

}