package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.service.CftalkManagementService
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

class CftalkManagementDataSourceImpl @Inject constructor(private val service: CftalkManagementService):
    CftakManagementDataSource {

    override fun getProjectsByEmployeeId(employeeId: Int): Single<ProjectsEmployeeIdResponse> {
        return service.getProjectsByEmployeeId(employeeId)
    }

    override fun getAllProject(page: Int, size: Int): Single<ProjectsAllCftalkResponse> {
        return service.getAllProject(page, size)
    }

    override fun getComplaintByEmployee(
        employeeId: Int,
        page: Int
    ): Single<ComplaintsByEmployeeResponse> {
        return service.getComplaintByEmployee(employeeId, page)
    }

    override fun getComplaintByProject(
        projectId: String,
        page: Int
    ): Single<ComplaintsByEmployeeResponse> {
        return service.getComplaintByProject(projectId, page)
    }

    override fun getTitleComplaint(): Single<TitlesCftalkManagementResponse> {
        return service.getTitleComplaint()
    }

    override fun getLocationComplaint(projectId: String): Single<LocationsProjectCftalkResponse> {
        return service.getLocationComplaint(projectId)
    }

    override fun getSubLocationComplaint(
        projectId: String,
        locationId: Int
    ): Single<SubLocationProjectCftalkResponse> {
        return service.getSubLocationComplaint(projectId, locationId)
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
        return service.createCftalkManagement(userId, projectId, title, description, locationId, subLocationId, file, file2, file3, file4, complaintType)
    }

    override fun getOtherProjects(userId: Int, page: Int): Single<ProjectsAllCftalkResponse> {
        return service.getOtherProjects(userId, page)
    }

    override fun getSearchOtherProjects(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse> {
        return service.getSearchOtherProjects(userId, page, keywords)
    }

    override fun getSearchAllProject(
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse> {
        return service.getSearchAllProject(page, keywords)
    }

    override fun getProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse> {
        return service.getProjectByBranch(branchCode, page, perPage)
    }

    override fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse> {
        return service.getSearchProjectBranch(page, branchCode, keywords, perPage)
    }

}