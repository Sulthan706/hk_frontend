package com.hkapps.hygienekleen.features.features_management.myteam.data.repository

import com.hkapps.hygienekleen.features.features_management.myteam.data.remote.MyTeamManagementDataSource
import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.ListBranchMyTeamResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.ListChiefSpvMyTeamManagementResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.ListManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.ListProjectManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class MyTeamManagementRepositoryImpl @Inject constructor(private val dataSource: MyTeamManagementDataSource): MyTeamManagementRepository {

    override fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectManagementResponse> {
        return dataSource.getListProjectManagement(branchCode, page, perPage)
    }

    override fun getListChiefManagement(projectId: String): Single<ListChiefSpvMyTeamManagementResponse> {
        return dataSource.getListChiefManagement(projectId)
    }

    override fun getListBranch(): Single<ListBranchMyTeamResponse> {
        return dataSource.getListBranch()
    }

    override fun getListManagement(projectCode: String): Single<ListManagementResponseModel> {
        return dataSource.getListManagement(projectCode)
    }

    override fun searchListProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponse> {
        return dataSource.searchListProject(page, branchCode, keywords, perPage)
    }

}