package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.remote.ProjectRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.BranchesProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.ProjectsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.ProjectsManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(private val dataSource: ProjectRemote): ProjectRepository {

    override fun getBranchesProject(
        page: Int,
        perPage: Int
    ): Single<BranchesProjectManagementResponse> {
        return dataSource.getBranchesProject(page, perPage)
    }

    override fun getProjectsManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse> {
        return dataSource.getProjectsManagement(userId, keywords, filter, page, perPage)
    }

    override fun getProjectsTeknisi(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse> {
        return dataSource.getProjectsTeknisi(userId, keywords, filter, page, perPage)
    }

    override fun getProjectBod(
        branchCode: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsBodResponse> {
        return dataSource.getProjectBod(branchCode, filter, page, perPage)
    }

}