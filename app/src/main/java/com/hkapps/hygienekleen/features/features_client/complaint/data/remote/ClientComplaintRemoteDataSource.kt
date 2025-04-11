package com.hkapps.hygienekleen.features.features_client.complaint.data.remote
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

interface ClientComplaintRemoteDataSource {
    fun postComplaintRDS(userId: Int,
                         projectId: String,
                         title: Int,
                         description: String,
//                         date: String,
                         locationId: Int,
                         subLocationId: Int,
                         image: MultipartBody.Part,
                         image2: MultipartBody.Part,
                         image3: MultipartBody.Part,
                         image4: MultipartBody.Part
    ): Single<ClientComplaintResponseModel>

    fun getComplaintAreaRDS(projectId: String): Single<ClientComplaintAreaResponseModel>

    fun getComplaintSubAreaRDS(
        projectId: String,
        locationId: Int
    ): Single<ClientComplaintSubAreaResponseModel>

    fun getHistoryComplaint(
        page: Int,
        projectId: String,
        clientId: Int,
        complaintType: ArrayList<String>
    ): Single<HistoryComplaintResponseModel>

    fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse>

    fun putCloseComplaint(complaintId: Int): Single<CloseComplaintResponse>

    fun getTitleCreateComplaint(): Single<ListTitleCreateComplaintClientResponse>

    fun getValidateCreateCtalk(projectCode: String): Single<ValidateCreateComplaintResponse>

    fun getListCtalkVisitorClient(
        page: Int,
        projectCode: String,
        filter: String
    ): Single<ListCtalkVisitorClientResponseModel>

    fun getDashboardCtalkClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkClientResponseModel>

    fun getDashboardCtalkVisitorClient(
        projectCode: String,
        beginDate: String,
        endDate: String
    ): Single<DashboardCtalkVisitorResponseModel>

}