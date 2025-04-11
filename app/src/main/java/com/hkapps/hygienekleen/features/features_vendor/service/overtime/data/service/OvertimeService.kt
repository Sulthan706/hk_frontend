package com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.service

import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.ListOvertimeChangeResponse.ListOvertimeChangeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeChange.CreateOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.createOvertimeResign.CreateOvertimeResignResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.detailOvertimeChangeOpr.DetailOvertimeChangeOprResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.listOvertimeNew.OvertimeChangeNewResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.locationsOvertime.LocationsOvertimeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.operatorOvertimeChange.OperatorOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.shiftOvertimeChange.ShiftOvertimeChangeResponse
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.model.subLocationsOvertime.SubLocationsOvertimeResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OvertimeService {

    @GET("/api/v1/overtime/ganti")
    fun getOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("employeeId") employeeId: Int,
        @Query("page") page: Int?
    ): Single<ListOvertimeChangeResponseModel>

    @GET("/api/v1/overtime/ganti/{overtimeId}")
    fun getDetailOvertimeChangeOpr(
        @Path("overtimeId") overtimeId: Int
    ): Single<DetailOvertimeChangeOprResponse>

//    @GET("/api/v1/overtime/ganti/select/shift")
//    fun getShiftOvertimeChange(
//        @Query("projectId") projectId: String,
//        @Query("employeeId") employeeId: Int,
//        @Query("date") date: String
//    ): Single<ShiftOvertimeChangeResponse>

    @GET("api/v1/project/shift")
    fun getShiftOvertimeChange(
        @Query("projectId") projectId: String
    ): Single<ShiftOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/select/employee")
    fun getOperatorOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("overtimeType") overtimeType: String,
        @Query("shiftId") shiftId: Int,
        @Query("jabatan") jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/select/employee-replace")
    fun getReplaceOperatorOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jobCode") jobCode: String,
        @Query("jabatan") jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/select/employee-replace-resign")
    fun getReplaceResignOvertimeChange(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("jabatan") jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

//    @POST("/api/v1/overtime/ganti/create")
//    fun createOvertimeChange(
//        @Query("createdById") employeeId: Int,
//        @Query("employeeId") operatorId: Int,
//        @Query("employeeReplaceId") operatorReplaceId: Int,
//        @Query("projectId") projectId: String,
//        @Query("title") title: String,
//        @Query("description") description: String,
//        @Query("date") date: String,
//        @Query("shiftId") shiftId: Int
//    ): Single<CreateOvertimeChangeResponse>

    @POST("/api/v2/overtime/ganti/create")
    fun createOvertimeChange(
        @Query("createdById") employeeId: Int,
        @Query("employeeId") operatorId: Int,
        @Query("employeeReplaceId") operatorReplaceId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("overtimeType") overtimeType: String
    ): Single<CreateOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/select/leader-replace")
    fun getReplacePengawas(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int
    ): Single<OperatorOvertimeChangeResponse>

    @GET("/api/v1/complaint/location/{projectId}")
    fun getLocationsOvertime(
        @Path("projectId") projectId: String
    ): Single<LocationsOvertimeResponse>

    @GET("/api/v2/overtime/location")
    fun getLocationByShift(
        @Query("projectId") projectId: String,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): Single<LocationsOvertimeResponse>

    @GET("/api/v2/overtime/sub-location/{projectId}/{locationId}/{shiftId}")
    fun getSubLocationsOvertime(
        @Path("projectId") projectId: String,
        @Path("locationId") locationId: Int,
        @Path("shiftId") shiftId: Int
    ): Single<SubLocationsOvertimeResponse>

    @POST("/api/v2/overtime/ganti/resign/create")
    fun createOvertimeResign(
        @Query("createdById") createdById: Int,
        @Query("employeeId") employeeId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("overtimeType") overtimeType: String
    ): Single<CreateOvertimeResignResponse>

    @POST("/api/v2/overtime/ganti/leader/create")
    fun createOvertimePengawas(
        @Query("createdById") createdById: Int,
        @Query("employeeId") employeeId: Int,
        @Query("employeeReplaceId") employeeReplaceId: Int,
        @Query("projectId") projectId: String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("date") date: String,
        @Query("shiftId") shiftId: Int,
        @Query("overtimeType") overtimeType: String
    ): Single<CreateOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/select/resign/employee")
    fun getEmployeeOvertimeResign(
        @Query("projectId") projectId: String,
        @Query("date") date: String,
        @Query("overtimeType") overtimeType: String,
        @Query("shiftId") shiftId: Int,
        @Query("locationId") locationId: Int,
        @Query("subLocationId") subLocationId: Int
    ): Single<OperatorOvertimeChangeResponse>

    @GET("/api/v2/overtime/ganti/list-employee")
    fun getListOvertimeChange(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("page") page: Int
    ): Single<OvertimeChangeNewResponse>

    @GET("/api/v2/overtime/ganti/filter/list-employee")
    fun getFilterOvertimeChange(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
        @Query("jabatan") jabatan: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int
    ): Single<OvertimeChangeNewResponse>

}