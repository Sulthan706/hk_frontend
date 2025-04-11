package com.hkapps.hygienekleen.features.features_management.service.resign.data.remote

import com.hkapps.hygienekleen.features.features_management.service.resign.data.service.ResignManagementService
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ListReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ListResignManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign.SubmitResignResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ResignManagementDataSourceImpl @Inject constructor(private val service: ResignManagementService): ResignManagementDataSource {
    override fun getListResignManagement(
        adminMasterId: Int,
        page: Int,
        size: Int
    ): Single<ListResignManagementResponseModel> {
        return service.getListResignManagement(adminMasterId, page, size)
    }

    override fun getListReasonResignManagement(
        type: String,
        page: Int,
        size: Int
    ): Single<ListReasonResignResponseModel> {
        return service.getListReasonResignManagement(type, page, size)
    }

    override fun submitResignManagement(
        idTurnOver: Int,
        adminMasterId: Int,
        reasonId: Int,
        approval: String
    ): Single<SubmitResignResponseModel> {
        return service.submitResignManagement(idTurnOver, adminMasterId, reasonId, approval)
    }

}