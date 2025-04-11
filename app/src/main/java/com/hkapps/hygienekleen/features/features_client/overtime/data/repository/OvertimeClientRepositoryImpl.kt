package com.hkapps.hygienekleen.features.features_client.overtime.data.repository

import com.hkapps.hygienekleen.features.features_client.overtime.data.remote.OvertimeClientRemoteDataSource
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

class OvertimeClientRepositoryImpl @Inject constructor(private val remoteDataSource: OvertimeClientRemoteDataSource) :
    OvertimeClientRepository {

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
        return remoteDataSource.createOvertimeClient(
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
        return remoteDataSource.getListOvertimeChangeClient(projectId, employeeId, page)
    }

    override fun getDetailOvertimeChangeClient(overtimeId: Int): Single<DetailOvertimeChangeClientResponse> {
        return remoteDataSource.getDetailOvertimeChangeClient(overtimeId)
    }

    override fun getLocationOvertimeClient(projectId: String): Single<LocationOvertimeClientResponse> {
        return remoteDataSource.getLocationOvertimeClient(projectId)
    }

    override fun getSubLocOvertimeClient(
        projectId: String,
        locationId: Int
    ): Single<SubLocOvertimeClientResponse> {
        return remoteDataSource.getSubLocOvertimeClient(projectId, locationId)
    }

    override fun getListOvertimeReqClient(
        projectId: String,
        page: Int
    ): Single<OvertimeReqClientResponse> {
        return remoteDataSource.getListOvertimeReqClient(projectId, page)
    }

    override fun getDetailOvertimeRequestClient(overtimeTagihId: Int): Single<DetailOvertimeRequestClientResponse> {
        return remoteDataSource.getDetailOvertimeRequestClient(overtimeTagihId)
    }


}