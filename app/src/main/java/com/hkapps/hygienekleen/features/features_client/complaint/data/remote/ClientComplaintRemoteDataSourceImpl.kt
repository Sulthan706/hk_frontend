package com.hkapps.hygienekleen.features.features_client.complaint.data.remote

import com.hkapps.hygienekleen.features.features_client.complaint.data.service.ClientComplaintService
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

class ClientComplaintRemoteDataSourceImpl @Inject constructor(private val service: ClientComplaintService) :
    ClientComplaintRemoteDataSource {
    override fun postComplaintRDS(
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
        return service.postComplaintApi(userId,projectId,title,description,locationId,subLocationId,image,image2,image3,image4)
    }

    override fun getComplaintAreaRDS(projectId: String): Single<ClientComplaintAreaResponseModel> {
        return service.getComplaintAreaApi(projectId)
    }

    override fun getComplaintSubAreaRDS(projectId: String, locationId: Int): Single<ClientComplaintSubAreaResponseModel> {
        return service.getComplaintSubAreaApi(projectId, locationId)
    }

    override fun getHistoryComplaint(
        page: Int,
        projectId: String,
        clientId: Int,
        complaintType: ArrayList<String>
    ): Single<HistoryComplaintResponseModel> {
        return service.getHistoryComplaint(page, projectId, clientId, complaintType)
    }

    override fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse> {
        return service.getDetailHistoryComplaint(complaintId)
    }

    override fun putCloseComplaint(complaintId: Int): Single<CloseComplaintResponse> {
        return service.putCloseComplaint(complaintId)
    }

    override fun getTitleCreateComplaint(): Single<ListTitleCreateComplaintClientResponse> {
        return service.getTitleCreateComplaint()
    }

    override fun getValidateCreateCtalk(projectCode: String): Single<ValidateCreateComplaintResponse> {
        return service.getValidateCreateCtalk(projectCode)
    }

    override fun getListCtalkVisitorClient(
        page: Int,
        projectCode: String,
        filter: String
    ): Single<ListCtalkVisitorClientResponseModel> {
        return service.getListCtalkVisitorClient(page, projectCode, filter)
    }

    override fun getDashboardCtalkClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkClientResponseModel> {
        return service.getDashboardCtalkClient(projectCode, beginDate, endDate)
    }

    override fun getDashboardCtalkVisitorClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkVisitorResponseModel> {
        return service.getDashboardCtalkVisitorClient(projectCode, beginDate, endDate)
    }

}