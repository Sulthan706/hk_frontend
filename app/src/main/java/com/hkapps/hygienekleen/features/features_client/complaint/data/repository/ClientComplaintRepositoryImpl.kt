package com.hkapps.hygienekleen.features.features_client.complaint.data.repository

import com.hkapps.hygienekleen.features.features_client.complaint.data.remote.ClientComplaintRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.ClientComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.CloseComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.complaintvisitorclient.ListCtalkVisitorClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkclient.DashboardCtalkClientResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.dashboardctalkvisitorclient.DashboardCtalkVisitorResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.historyComplaint.HistoryComplaintResponseModel
import com.hkapps.hygienekleen.features.features_client.complaint.model.statusCreateComplaint.ValidateCreateComplaintResponse
import com.hkapps.hygienekleen.features.features_client.complaint.model.titleCreateComplaint.ListTitleCreateComplaintClientResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class ClientComplaintRepositoryImpl @Inject constructor(private val remoteDataSource: ClientComplaintRemoteDataSource) :
    ClientComplaintRepository {
    override fun postComplaint(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part,
        image2: MultipartBody.Part,
        image3: MultipartBody.Part,
        image4: MultipartBody.Part
    ): Single<ClientComplaintResponseModel> {
        return remoteDataSource.postComplaintRDS(userId,projectId,title,description,locationId,subLocationId,image,image2,image3,image4)
    }

    override fun getAreaComplaint(projectId: String): Single<ClientComplaintAreaResponseModel> {
        return remoteDataSource.getComplaintAreaRDS(projectId)
    }

    override fun getSubAreaComplaint(
        projectId: String,
        locationId: Int
    ): Single<ClientComplaintSubAreaResponseModel> {
        return remoteDataSource.getComplaintSubAreaRDS(projectId, locationId)
    }

    override fun getHistoryComplaint(
        page: Int,
        projectId: String,
        clientId: Int,
        complaintType: ArrayList<String>
    ): Single<HistoryComplaintResponseModel> {
        return remoteDataSource.getHistoryComplaint(page, projectId, clientId, complaintType)
    }

    override fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse> {
        return remoteDataSource.getDetailHistoryComplaint(complaintId)
    }

    override fun putCloseComplaint(complaintId: Int): Single<CloseComplaintResponse> {
        return remoteDataSource.putCloseComplaint(complaintId)
    }

    override fun getTitleCreateComplaint(): Single<ListTitleCreateComplaintClientResponse> {
        return remoteDataSource.getTitleCreateComplaint()
    }

    override fun getValidateCreateCtalk(projectCode: String): Single<ValidateCreateComplaintResponse> {
        return remoteDataSource.getValidateCreateCtalk(projectCode)
    }

    override fun getListCtalkVisitorClient(
        page: Int,
        projectCode: String,
        filter: String
    ): Single<ListCtalkVisitorClientResponseModel> {
        return remoteDataSource.getListCtalkVisitorClient(page, projectCode, filter)
    }

    override fun getDashboardCtalkClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkClientResponseModel> {
        return remoteDataSource.getDashboardCtalkClient(projectCode, beginDate, endDate)
    }

    override fun getDashboardCtalkVisitorClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkVisitorResponseModel> {
        return remoteDataSource.getDashboardCtalkVisitorClient(projectCode, beginDate, endDate)
    }

}