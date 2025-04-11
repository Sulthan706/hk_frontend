package com.hkapps.hygienekleen.features.features_client.overtime.data.remote

import com.hkapps.hygienekleen.features.features_client.overtime.data.service.OvertimeClientService
import com.hkapps.hygienekleen.features.features_client.overtime.model.createOvertime.CreateOvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeChangeClient.DetailOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeRequestClient.DetailOvertimeRequestClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listLocation.LocationOvertimeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient.ListOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.OvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listSubLoc.SubLocOvertimeClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class OvertimeClientRemoteDataSourceImpl @Inject constructor(private val service: OvertimeClientService) :
    OvertimeClientRemoteDataSource {

    override fun createOvertimeClient(
        employeeId: Int,
        projectId: String,
        title: String,
        description: String,
        locationId: Int,
        subLocationId: Int,
        date: String,
        startAt: String,
        endAt: String,
        file: MultipartBody.Part,
        totalWorker: Int
    ): Single<CreateOvertimeReqClientResponse> {
        return service.createOvertimeClient(
            employeeId,
            projectId,
            title,
            description,
            locationId,
            subLocationId,
            date,
            startAt,
            endAt,
            file,
            totalWorker
        )
    }

    override fun getListOvertimeChangeClient(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<ListOvertimeChangeClientResponse> {
        return service.getListOvertimeChangeClient(projectId, employeeId, page)
    }

    override fun getDetailOvertimeChangeClient(overtimeId: Int): Single<DetailOvertimeChangeClientResponse> {
        return service.getDetailOvertimeChangeClient(overtimeId)
    }

    override fun getLocationOvertimeClient(projectId: String): Single<LocationOvertimeClientResponse> {
        return service.getLocationOvertimeClient(projectId)
    }

    override fun getSubLocOvertimeClient(
        projectId: String,
        locationId: Int
    ): Single<SubLocOvertimeClientResponse> {
        return service.getSubLocOvertimeClient(projectId, locationId)
    }

    override fun getListOvertimeReqClient(
        projectId: String,
        page: Int
    ): Single<OvertimeReqClientResponse> {
        return service.getListOvertimeReqClient(projectId, page)
    }

    override fun getDetailOvertimeRequestClient(overtimeTagihId: Int): Single<DetailOvertimeRequestClientResponse> {
        return service.getDetailOvertimeRequestClient(overtimeTagihId)
    }


}