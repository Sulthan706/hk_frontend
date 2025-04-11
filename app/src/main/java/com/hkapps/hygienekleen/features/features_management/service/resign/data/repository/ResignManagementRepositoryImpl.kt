package com.hkapps.hygienekleen.features.features_management.service.resign.data.repository

import com.hkapps.hygienekleen.features.features_management.service.resign.data.remote.ResignManagementDataSource
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listreasonresign.ListReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.listresignmanagement.ListResignManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.resign.model.submitresign.SubmitResignResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ResignManagementRepositoryImpl @Inject constructor(private val dataSource: ResignManagementDataSource): ResignManagementRepository {
    override fun getListResignManagement(
        adminMasterId: Int,
        page: Int,
        size: Int
    ): Single<ListResignManagementResponseModel> {
        return dataSource.getListResignManagement(adminMasterId, page, size)
    }

    override fun getListReasonResignManagement(
        type: String,
        page: Int,
        size: Int
    ): Single<ListReasonResignResponseModel> {
        return dataSource.getListReasonResignManagement(type, page, size)
    }

    override fun submitResignManagement(
        idTurnOver: Int,
        adminMasterId: Int,
        reasonId: Int,
        approval: String
    ): Single<SubmitResignResponseModel> {
        return dataSource.submitResignManagement(idTurnOver, adminMasterId, reasonId, approval)
    }


}