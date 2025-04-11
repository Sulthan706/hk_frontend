package com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.remote.OvertimeRemoteDataSource
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
import javax.inject.Inject

class OvertimeRepositoryImpl @Inject constructor(private val remoteDataSource: OvertimeRemoteDataSource) :
    OvertimeRepository {

    override fun getOvertimeChange(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<ListOvertimeChangeResponseModel> {
        return remoteDataSource.getOvertimeChange(projectId, employeeId, page)
    }

    override fun getDetailOvertimeChangeOpr(overtimeId: Int): Single<DetailOvertimeChangeOprResponse> {
        return remoteDataSource.getDetailOvertimeChangeOpr(overtimeId)
    }

    override fun getShiftOvertimeChange(projectId: String): Single<ShiftOvertimeChangeResponse> {
        return remoteDataSource.getShiftOvertimeChange(projectId)
    }

    override fun getOperatorOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse> {
        return remoteDataSource.getOperatorOvertimeChange(projectId, date, overtimeType, shiftId, jabatan)
    }

    override fun getReplaceOperatorOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse> {
        return remoteDataSource.getReplaceOperatorOvertimeChange(projectId, date, shiftId, jobCode, jabatan)
    }

    override fun getReplaceResignOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ): Single<OperatorOvertimeChangeResponse> {
        return remoteDataSource.getReplaceResignOvertimeChange(projectId, date, shiftId, jabatan)
    }

    override fun createOvertimeChange(
        employeeId: Int,
        operatorId: Int,
        operatorReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeResponse> {
        return remoteDataSource.createOvertimeChange(employeeId, operatorId, operatorReplaceId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getReplacePengawas(
        projectId: String,
        date: String,
        shiftId: Int
    ): Single<OperatorOvertimeChangeResponse> {
        return remoteDataSource.getReplacePengawas(projectId, date, shiftId)
    }

    override fun getLocationsOvertime(projectId: String): Single<LocationsOvertimeResponse> {
        return remoteDataSource.getLocationsOvertime(projectId)
    }

    override fun getSubLocationsOvertime(
        projectId: String,
        locationId: Int,
        shiftId: Int
    ): Single<SubLocationsOvertimeResponse> {
        return remoteDataSource.getSubLocationsOvertime(projectId, locationId, shiftId)
    }

    override fun createOvertimeResign(
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
    ): Single<CreateOvertimeResignResponse> {
        return remoteDataSource.createOvertimeResign(createdById, employeeId, locationId, subLocationId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun createOvertimePengawas(
        createdById: Int,
        employeeId: Int,
        employeeReplaceId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ): Single<CreateOvertimeChangeResponse> {
        return remoteDataSource.createOvertimePengawas(createdById, employeeId, employeeReplaceId, projectId, title, description, date, shiftId, overtimeType)
    }

    override fun getLocationByShift(
        projectId: String,
        shiftId: Int,
        date: String
    ): Single<LocationsOvertimeResponse> {
        return remoteDataSource.getLocationByShift(projectId, shiftId, date)
    }

    override fun getEmployeeOvertimeResign(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ): Single<OperatorOvertimeChangeResponse> {
        return remoteDataSource.getEmployeeOvertimeResign(projectId, date, overtimeType, shiftId, locationId, subLocationId)
    }

    override fun getListOvertimeChange(
        employeeId: Int,
        projectCode: String,
        page: Int
    ): Single<OvertimeChangeNewResponse> {
        return remoteDataSource.getListOvertimeChange(employeeId, projectCode, page)
    }

    override fun getFilterOvertimeChange(
        employeeId: Int,
        projectCode: String,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Single<OvertimeChangeNewResponse> {
        return remoteDataSource.getFilterOvertimeChange(employeeId, projectCode, jabatan, startDate, endDate, page)
    }

}