package com.hkapps.hygienekleen.features.features_management.homescreen.project.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listClient.ListClientProjectMgmntResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listbranch.ListAllBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listproject.ListProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.listprojectmanagement.ListProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.attendanceProject.AttendanceProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.complaintProject.ComplaintProjectManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.project.model.profileProject.detailProject.DetailProjectManagementResponse
import io.reactivex.Single

interface ProjectManagementDataSource {

    fun getListProjectManagement(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectManagementResponseModel>

    fun getListProject(
        id: Int
    ): Single<ListProjectResponseModel>

    fun getListBranch(): Single<ListAllBranchResponseModel>

    fun getSearchProjectAll(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectManagementResponseModel>

    fun getDetailProject(
        projectCode: String
    ): Single<DetailProjectManagementResponse>

    fun getComplaintProject(
        userId: Int,
        projectId: String,
        complaintType: ArrayList<String>
    ): Single<ComplaintProjectManagementResponse>

    fun getAttendanceProject(
        projectCode: String,
        month: Int,
        year: Int
    ): Single<AttendanceProjectManagementResponse>

    fun getListClientProject(
        projectCode: String
    ): Single<ListClientProjectMgmntResponse>

}