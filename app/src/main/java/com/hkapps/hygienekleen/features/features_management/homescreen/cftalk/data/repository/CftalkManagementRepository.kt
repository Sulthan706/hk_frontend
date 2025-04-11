package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.ComplaintsByEmployeeResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.createComplaint.CreateCftalkManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.ProjectsAllCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listLocation.LocationsProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.ProjectsEmployeeIdResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listSubLoc.SubLocationProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listTitleComplaint.TitlesCftalkManagementResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface CftalkManagementRepository {

    fun getProjectsByEmployeeId(
        employeeId: Int
    ): Single<ProjectsEmployeeIdResponse>

    fun getAllProject(
        page: Int,
        size: Int
    ): Single<ProjectsAllCftalkResponse>

    fun getComplaintByEmployee(
        employeeId: Int,
        page: Int
    ): Single<ComplaintsByEmployeeResponse>

    fun getComplaintByProject(
        projectId: String,
        page: Int
    ): Single<ComplaintsByEmployeeResponse>

    fun getTitleComplaint(): Single<TitlesCftalkManagementResponse>

    fun getLocationComplaint(
        projectId: String
    ): Single<LocationsProjectCftalkResponse>

    fun getSubLocationComplaint(
        projectId: String,
        locationId: Int
    ): Single<SubLocationProjectCftalkResponse>

    fun createCftalkManagement(
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
    ): Single<CreateCftalkManagementResponse>

    fun getOtherProjects(
        userId: Int,
        page: Int
    ): Single<ProjectsAllCftalkResponse>

    fun getSearchOtherProjects(
        userId: Int,
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse>

    fun getSearchAllProject(
        page: Int,
        keywords: String
    ): Single<ProjectsAllCftalkResponse>

    fun getProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse>

    fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ProjectsAllCftalkResponse>

}