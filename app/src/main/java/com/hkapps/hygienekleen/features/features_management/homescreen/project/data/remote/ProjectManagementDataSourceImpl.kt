package com.hkapps.hygienekleen.features.features_management.homescreen.project.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.ListClientProjectMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.ListAllBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.ListProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject.AttendanceProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject.ComplaintProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject.DetailProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.project.data.service.ProjectManagementService
import io.reactivex.Single
import javax.inject.Inject

class ProjectManagementDataSourceImpl @Inject constructor(private val service: ProjectManagementService):
    ProjectManagementDataSource {

    override fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage:Int
    ): Single<ListProjectManagementResponseModel> {
        return service.getListProjectManagement(branchCode, page, perPage)
    }

    override fun getListProject(id: Int): Single<ListProjectResponseModel> {
        return service.getProjectCode(id)
    }

    override fun getListBranch(): Single<ListAllBranchResponseModel> {
        return service.getListBranch()
    }

    override fun getSearchProjectAll(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponseModel> {
        return service.getSearchProjectAll(page, branchCode, keywords, perPage)
    }

    override fun getDetailProject(projectCode: String): Single<DetailProjectManagementResponse> {
        return service.getDetailProject(projectCode)
    }

    override fun getComplaintProject(
        userId: Int,
        projectId: String,
        complaintType: ArrayList<String>
    ): Single<ComplaintProjectManagementResponse> {
        return service.getComplaintProject(userId, projectId, complaintType)
    }

    override fun getAttendanceProject(
        projectCode: String,
        month: Int,
        year: Int
    ): Single<AttendanceProjectManagementResponse> {
        return service.getAttendanceProject(projectCode, month, year)
    }

    override fun getListClientProject(projectCode: String): Single<ListClientProjectMgmntResponse> {
        return service.getListClientProject(projectCode)
    }

}
