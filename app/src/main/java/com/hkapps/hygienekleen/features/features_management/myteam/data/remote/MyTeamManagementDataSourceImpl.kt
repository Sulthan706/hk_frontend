package com.hkapps.hygienekleen.features.features_management.myteam.data.remote

import com.hkapps.hygienekleen.features.features_management.myteam.data.service.MyTeamManagementService
import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.ListBranchMyTeamResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.ListChiefSpvMyTeamManagementResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.ListManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.ListProjectManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class MyTeamManagementDataSourceImpl @Inject constructor(private val service: MyTeamManagementService): MyTeamManagementDataSource{

    override fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectManagementResponse> {
        return service.getListProjectManagement(branchCode, page, perPage)
    }

    override fun getListChiefManagement(projectId: String): Single<ListChiefSpvMyTeamManagementResponse> {
        return service.getListChiefManagement(projectId)
    }

    override fun getListBranch(): Single<ListBranchMyTeamResponse> {
        return service.getListBranch()
    }

    override fun getListManagement(projectCode: String): Single<ListManagementResponseModel> {
        return service.getListManagement(projectCode)
    }

    override fun searchListProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponse> {
        return service.searchListProject(page, branchCode, keywords, perPage)
    }


}