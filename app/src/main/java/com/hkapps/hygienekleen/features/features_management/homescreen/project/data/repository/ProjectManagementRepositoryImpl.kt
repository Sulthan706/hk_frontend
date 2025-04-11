package com.hkapps.hygienekleen.features.features_management.homescreen.project.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.project.data.remote.ProjectManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.ListClientProjectMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.ListAllBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.ListProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject.AttendanceProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject.ComplaintProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject.DetailProjectManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class ProjectManagementRepositoryImpl @Inject constructor(private val dataSource: ProjectManagementDataSource) : ProjectManagementRepository {

    override fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectManagementResponseModel> {
        return dataSource.getListProjectManagement(branchCode, page, perPage)
    }

    override fun getListProject(id: Int): Single<ListProjectResponseModel> {
        return dataSource.getListProject(id)
    }

    override fun getListBranch(): Single<ListAllBranchResponseModel> {
        return dataSource.getListBranch()
    }

    override fun getSearchProjectAll(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponseModel> {
        return dataSource.getSearchProjectAll(page, branchCode, keywords, perPage)
    }

    override fun getDetailProject(projectCode: String): Single<DetailProjectManagementResponse> {
        return dataSource.getDetailProject(projectCode)
    }

    override fun getComplaintProject(
        userId: Int,
        projectId: String,
        complaintType: ArrayList<String>
    ): Single<ComplaintProjectManagementResponse> {
        return dataSource.getComplaintProject(userId, projectId, complaintType)
    }

    override fun getAttendanceProject(
        projectCode: String,
        month: Int,
        year: Int
    ): Single<AttendanceProjectManagementResponse> {
        return dataSource.getAttendanceProject(projectCode, month, year)
    }

    override fun getListClientProject(projectCode: String): Single<ListClientProjectMgmntResponse> {
        return dataSource.getListClientProject(projectCode)
    }

}
