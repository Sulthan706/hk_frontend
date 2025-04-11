package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.remote.CftakManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.ComplaintsByEmployeeResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.createComplaint.CreateCftalkManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.ProjectsAllCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listLocation.LocationsProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.ProjectsEmployeeIdResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listSubLoc.SubLocationProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listTitleComplaint.TitlesCftalkManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class CftalkManagementRepositoryImpl @Inject constructor(private val dataSource: CftakManagementDataSource):
    CftalkManagementRepository {

    override fun getProjectsByEmployeeId(employeeId: Int): Single<ProjectsEmployeeIdResponse> {
        return dataSource.getProjectsByEmployeeId(employeeId)
    }

    override fun getAllProject(page: Int, size: Int): Single<ProjectsAllCftalkResponse> {
        return dataSource.getAllProject(page, size)
    }

    override fun getComplaintByEmployee(
        employeeId: Int,
        page: Int
    ): Single<ComplaintsByEmployeeResponse> {
        return dataSource.getComplaintByEmployee(employeeId, page)
    }

    override fun getComplaintByProject(
        projectId: String,
        page: Int
    ): Single<ComplaintsByEmployeeResponse> {
        return dataSource.getComplaintByProject(projectId, page)
    }

    override fun getTitleComplaint(): Single<TitlesCftalkManagementResponse> {
        return dataSource.getTitleComplaint()
    }

    override fun getLocationComplaint(projectId: String): Single<LocationsProjectCftalkResponse> {
        return dataSource.getLocationComplaint(projectId)
    }

    override fun getSubLocationComplaint(
        projectId: String,
        locationId: Int
    ): Single<SubLocationProjectCftalkResponse> {
        return dataSource.getSubLocationComplaint(projectId, locationId)
    }

    override fun createCftalkManagement(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        file: MultipartBody.Part,
        file2: MultipartBody.Part,
        file3: MultipartBody.Part,
        file4: MultipartBody.Part,
        complaintType: String
    ): Single<CreateCftalkManagementResponse> {
        return dataSource.createCftalkManagement(userId, projectId, title, description, locationId, subLocationId, file, file2, file3, file4, complaintType)
    }

    override fun getOtherProjects(userId: Int, page: Int): Single<ProjectsAllCftalkResponse> {
        return dataSource.getOtherProjects(userId, page)
    }

    override fun getSearchOtherProjects(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse> {
        return dataSource.getSearchOtherProjects(userId, page, keywords)
    }

    override fun getSearchAllProject(
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse> {
        return dataSource.getSearchAllProject(page, keywords)
    }

    override fun getProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse> {
        return dataSource.getProjectByBranch(branchCode, page, perPage)
    }

    override fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse> {
        return dataSource.getSearchProjectBranch(page, branchCode, keywords, perPage)
    }

}