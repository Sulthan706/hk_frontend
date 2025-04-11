package com.hkapps.hygienekleen.features.features_management.complaint.data.repository

import com.hkapps.hygienekleen.features.features_management.complaint.data.remote.ComplaintManagementDataSource
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
import javax.inject.Inject

class ComplaintManagementRepositoryImpl @Inject constructor(private val dataSource: ComplaintManagementDataSource) :
    ComplaintManagementRepository {

    override fun getListProjectFmGM(employeeId: Int): Single<ListProjectFmGmResponse> {
        return dataSource.getListProjectFmGM(employeeId)
    }

    override fun getProjectCountCtalkManage(
        userId: Int,
        projectId: String
    ): Single<ProjectCountCtalkManageResponse> {
        return dataSource.getProjectCountCtalkManage(userId, projectId)
    }

    override fun getComplaintManagement(
        page: Int,
        projectCode: String,
        complaintType: String
    ): Single<ListComplaintManagementResponse> {
        return dataSource.getComplaintManagement(page, projectCode, complaintType)
    }

    override fun getDetailComplaintManagement(complaintId: Int): Single<DetailComplaintManagementResponse> {
        return dataSource.getDetailComplaintManagement(complaintId)
    }

    override fun getAllListProjectManagement(page: Int, size: Int): Single<ListProjectAllResponse> {
        return dataSource.getAllListProjectManagement(page, size)
    }

    override fun getListBranch(): Single<ListBranchResponseModel> {
        return dataSource.getListBranch()
    }

    override fun getListProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ListProjectAllResponse> {
        return dataSource.getListProjectByBranch(branchCode, page, perPage)
    }

    override fun getSearchProjectAll(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ): Single<ListProjectAllResponse> {
        return dataSource.getSearchProjectAll(page, branchCode, keywords, perPage)
    }

    override fun postComplaintManagement(
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
    ): Single<CreateComplaintManagementResponse> {
        return dataSource.postComplaintManagement(userId, projectId, title, description, locationId, subLocationId, file, file2, file3, file4, complaintType)
    }

    override fun getTitleCreateComplaint(): Single<TitleCreateComplaintManagementResponse> {
        return dataSource.getTitleCreateComplaint()
    }

    override fun getAreaCreateComplaint(projectId: String): Single<AreaCreatComplaintManagementResponse> {
        return dataSource.getAreaCreateComplaint(projectId)
    }

    override fun getSubAreaCreateComplaint(
        projectId: String,
        locationId: Int
    ): Single<SubAreaCreateComplaintManagementResponse> {
        return dataSource.getSubAreaCreateComplaint(projectId, locationId)
    }

    override fun getCtalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint: String
    ): Single<ListCtalkManagementResponseModel> {
        return dataSource.getCtalkManagement(page, projectCode, statusComplaint)
    }

    override fun getDashboardComplaintManagement(projectId: String): Single<DashboardManagementComplaintResponseModel> {
        return dataSource.getDashboardComplaintManagement(projectId)
    }

}