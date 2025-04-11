package com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.remote

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

interface OvertimeRemoteDataSource {

    fun getOvertimeChange(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<ListOvertimeChangeResponseModel>

    fun getDetailOvertimeChangeOpr(
        overtimeId: Int
    ): Single<DetailOvertimeChangeOprResponse>

    fun getShiftOvertimeChange(
        projectId: String
    ): Single<ShiftOvertimeChangeResponse>

    fun getOperatorOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

    fun getReplaceOperatorOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

    fun getReplaceResignOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse>

    fun createOvertimeChange(
        employeeId: Int,
        operatorId: Int,
        operatorReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeResponse>

    fun getReplacePengawas(
        projectId: String,
        date: String,
        shiftId: Int,
    ): Single<OperatorOvertimeChangeResponse>

    fun getLocationsOvertime(
        projectId: String
    ): Single<LocationsOvertimeResponse>

    fun getSubLocationsOvertime(
        projectId: String,
        locationId: Int,
        shiftId: Int
    ): Single<SubLocationsOvertimeResponse>

    fun createOvertimeResign(
        createdById: Int,
        employeeId: Int,
        locationId: Int,
        subLocationId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeResignResponse>

    fun createOvertimePengawas(
        createdById: Int,
        employeeId: Int,
        employeeReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeResponse>

    fun getLocationByShift(
        projectId: String,
        shiftId: Int,
        date: String
    ): Single<LocationsOvertimeResponse>

    fun getEmployeeOvertimeResign(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<OperatorOvertimeChangeResponse>

    fun getListOvertimeChange(
        employeeId: Int,
        projectCode: String,
        page: Int
    ): Single<OvertimeChangeNewResponse>

    fun getFilterOvertimeChange(
        employeeId: Int,
        projectCode: String,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<OvertimeChangeNewResponse>

}