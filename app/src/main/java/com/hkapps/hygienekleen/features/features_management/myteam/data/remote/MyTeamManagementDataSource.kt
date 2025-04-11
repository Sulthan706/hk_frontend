package com.hkapps.hygienekleen.features.features_management.myteam.data.remote

import com.hkapps.hygienekleen.features.features_management.myteam.model.listBranch.ListBranchMyTeamResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listChiefSpv.ListChiefSpvMyTeamManagementResponse
import com.hkapps.hygienekleen.features.features_management.myteam.model.listManagement.ListManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.myteam.model.listProject.ListProjectManagementResponse
import io.reactivex.Single

interface MyTeamManagementDataSource {

    fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectManagementResponse>

    fun getListChiefManagement(
        projectId: String
    ): Single<ListChiefSpvMyTeamManagementResponse>

    fun getListBranch(): Single<ListBranchMyTeamResponse>

    fun getListManagement(
        projectCode: String
    ): Single<ListManagementResponseModel>

    fun searchListProject(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponse>

}