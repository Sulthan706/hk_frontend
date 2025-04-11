package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listBranch.BranchesProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectBod.ProjectsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.model.listProjectManagement.ProjectsManagementResponse
import io.reactivex.Single

interface ProjectRemote {

    fun getBranchesProject(
        page: Int,
        perPage: Int
    ): Single<BranchesProjectManagementResponse>

    fun getProjectsManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse>

    fun getProjectsTeknisi(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsManagementResponse>

    fun getProjectBod(
        branchCode: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsBodResponse>

}