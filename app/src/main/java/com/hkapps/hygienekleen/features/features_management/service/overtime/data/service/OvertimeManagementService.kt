package com.hkapps.hygienekleen.features.features_management.service.overtime.data.service

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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OvertimeManagementService {

    @GET("/api/v2/overtime/ganti/management/filter/list-employee")
    fun getListOvertimeChange(
        @Query("employeeId") employeeId: Int,
        @Query("jabatan") jabatan: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int
    ): Single<OvertimeChangeManagementResponse>

    @GET("/api/v2/overtime/ganti/management/select/employee")
    fun getOperationalOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("overtimeType") overtimeType: String,
        @Query("shiftId") shiftId: Int,
        @Query("jabatan") jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    @GET("/api/v2/overtime/ganti/management/select/employee-replace")
    fun getReplaceOperationalOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jobCode") jobCode: String,
        @Query("jabatan") jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    @POST("/api/v2/overtime/ganti/management/create")
    fun createOvertimeChangeManagement(
        @Query("createdById") createdById: Int,
        @Query("employeeId") replaceOprId: Int,
        @Query("employeeReplaceId") operationalId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("overtimeType") overtimeType: String
    ): Single<CreateOvertimeChangeManagementResponse>

    @GET("/api/v1/management/project/v2/{id}")
    fun getProjectsOvertimeManagement(
        @Path("id") adminMasterId: Int
    ): Single<ProjectsOvertimeResponse>

    @GET("/api/v2/overtime/ganti/management/select/employee-replace-resign")
    fun getOperationalOvertimeResign(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jabatan") jabatan: String
    ): Single<OperationalOvertimeManagementResponse>

    @POST("/api/v2/overtime/ganti/management/resign/create")
    fun createOvertimeResignManagement(
        @Query("createdById") createdById: Int,
        @Query("employeeId") replaceOprId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("overtimeType") overtimeType: String
    ): Single<CreateOvertimeChangeManagementResponse>

    //new
    @GET("/api/v1/project/search/own/new/{adminMasterId}")
    fun getSearchProjectManagement(
        @Path("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<SearchProjectManagementResponseModel>

    @GET("/api/v2/overtime/ganti/management/select/employee-replace")
    fun getEmployeeReplaceManagement(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jobCode") jobCode: String,
        @Query("jabatan") jabatan: String
    ): Single<EmployeeReplaceResponseModel>

    @GET("/api/v1/project/shift")
    fun getListShiftManagement(
        @Query("projectId") projectId: String
    ): Single<ListShiftManagementResponseModel>

    @GET("/api/v2/overtime/ganti/management/select/employee")
    fun getEmployeeManagement(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("overtimeType") overtimeType: String,
        @Query("shiftId") shiftId: Int,
        @Query("jabatan") jabatan: String
    ): Single<SelectEmployeeMgmntResponseModel>

    @GET("/api/v2/overtime/ganti/management/select/employee-replace")
    fun getChangeEmployeeManagement(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jobCode") jobCode: String,
        @Query("jabatan") jabatan: String
    ): Single<ChangeEmployeeMgmntResponseModel>

    @GET("/api/v2/overtime/location")
    fun getLocationManagement(
        @Query("projectId") projectId: String,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): Single<LocationManagementResponseModel>

    @GET("/api/v2/overtime/sub-location/{projectId}/{locationId}/{shiftId}")
    fun getSublocationManagement(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int,
        @Path("shiftId") shiftId: Int,
    ): Single<SubLocationManagementResponseModel>

    @GET("/api/v2/overtime/ganti/management/plotting")
    fun getPlottingOperational(
        @Query("idEmployeeProject") idEmployeeProject: Int
    ): Single<PlottingOperationalResponse>

}