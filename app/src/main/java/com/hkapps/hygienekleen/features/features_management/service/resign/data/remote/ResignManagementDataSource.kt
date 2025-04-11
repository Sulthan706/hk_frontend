package com.hkapps.hygienekleen.features.features_management.service.resign.data.remote

import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ListReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ListResignManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign.SubmitResignResponseModel
import io.reactivex.Single

interface ResignManagementDataSource {

    fun getListResignManagement(
        adminMasterId: Int,
        page: Int,
        size: Int
    ): Single<ListResignManagementResponseModel>

    fun getListReasonResignManagement(
        type: String,
        page: Int,
        size: Int
    ): Single<ListReasonResignResponseModel>

    fun submitResignManagement(
        idTurnOver: Int,
        adminMasterId: Int,
        reasonId: Int,
        approval: String
    ): Single<SubmitResignResponseModel>

}