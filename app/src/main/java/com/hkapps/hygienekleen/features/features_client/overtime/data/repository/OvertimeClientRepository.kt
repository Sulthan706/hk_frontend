package com.hkapps.hygienekleen.features.features_client.overtime.data.repository

import com.hkapps.hygienekleen.features.features_client.overtime.model.createOvertime.CreateOvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeChangeClient.DetailOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.getOvertimeRequestClient.DetailOvertimeRequestClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listLocation.LocationOvertimeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeChangeClient.ListOvertimeChangeClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listOvertimeReqClient.OvertimeReqClientResponse
import com.hkapps.hygienekleen.features.features_client.overtime.model.listSubLoc.SubLocOvertimeClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface OvertimeClientRepository {

    fun createOvertimeClient(
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
    ): Single<CreateOvertimeReqClientResponse>

    fun getListOvertimeChangeClient(
        projectId: String,
        employeeId: Int,
        page: Int
    ): Single<ListOvertimeChangeClientResponse>

    fun getDetailOvertimeChangeClient(
        overtimeId: Int
    ): Single<DetailOvertimeChangeClientResponse>

    fun getLocationOvertimeClient(
        projectId: String
    ): Single<LocationOvertimeClientResponse>

    fun getSubLocOvertimeClient(
        projectId: String,
        locationId: Int
    ): Single<SubLocOvertimeClientResponse>

    fun getListOvertimeReqClient(
        projectId: String,
        page: Int
    ): Single<OvertimeReqClientResponse>

    fun getDetailOvertimeRequestClient(
        overtimeTagihId: Int
    ): Single<DetailOvertimeRequestClientResponse>


}