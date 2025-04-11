package com.hkapps.hygienekleen.features.features_management.complaint.data.repository

import com.hkapps.hygienekleen.features.features_management.complaint.model.createComplaint.CreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.dashboardcomplaintmanagement.DashboardManagementComplaintResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.detailComplaint.DetailComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listAreaComplaint.AreaCreatComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listBranch.ListBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.listComplaint.ListComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProject.ListProjectFmGmResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listProjectAll.ListProjectAllResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listSubAreaComplaint.SubAreaCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listTitleComplaint.TitleCreateComplaintManagementResponse
import com.hkapps.hygienekleen.features.features_management.complaint.model.listcomplaintctalk.ListCtalkManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.complaint.model.projectCount.ProjectCountCtalkManageResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface ComplaintManagementRepository {

    fun getListProjectFmGM(employeeId: Int): Single<ListProjectFmGmResponse>

    fun getProjectCountCtalkManage(
        userId: Int,
        projectId: String
    ): Single<ProjectCountCtalkManageResponse>

    fun getComplaintManagement(
        page: Int,
        projectCode: String,
        complaintType: String
    ): Single<ListComplaintManagementResponse>

    fun getDetailComplaintManagement(
        complaintId: Int
    ): Single<DetailComplaintManagementResponse>

    fun getAllListProjectManagement(
        page: Int,
        size: Int
    ): Single<ListProjectAllResponse>

    fun getListBranch(): Single<ListBranchResponseModel>

    fun getListProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectAllResponse>

    fun getSearchProjectAll(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectAllResponse>

    fun postComplaintManagement(
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
    ): Single<CreateComplaintManagementResponse>

    fun getTitleCreateComplaint(): Single<TitleCreateComplaintManagementResponse>

    fun getAreaCreateComplaint(projectId: String
    ): Single<AreaCreatComplaintManagementResponse>

    fun getSubAreaCreateComplaint(projectId: String, locationId: Int
    ): Single<SubAreaCreateComplaintManagementResponse>

    fun getCtalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint: String
    ): Single<ListCtalkManagementResponseModel>

    fun getDashboardComplaintManagement(
        projectId: String
    ): Single<DashboardManagementComplaintResponseModel>

}