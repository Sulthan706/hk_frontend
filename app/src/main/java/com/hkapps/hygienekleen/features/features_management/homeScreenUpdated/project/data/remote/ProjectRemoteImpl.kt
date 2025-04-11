package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.service.ProjectService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.BranchesProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.ProjectsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.ProjectsManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class ProjectRemoteImpl @Inject constructor(private val service: ProjectService): ProjectRemote {

    override fun getBranchesProject(
        page: Int,
        perPage: Int
    ): Single<BranchesProjectManagementResponse> {
        return service.getBranchesProject(page, perPage)
    }

    override fun getProjectsManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse> {
        return service.getProjectsManagement(userId, keywords, filter, page, perPage)
    }

    override fun getProjectsTeknisi(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse> {
        return service.getProjectsTeknisi(userId, keywords, filter, page, perPage)
    }

    override fun getProjectBod(
        branchCode: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsBodResponse> {
        return service.getProjectBod(branchCode, filter, page, perPage)
    }

}