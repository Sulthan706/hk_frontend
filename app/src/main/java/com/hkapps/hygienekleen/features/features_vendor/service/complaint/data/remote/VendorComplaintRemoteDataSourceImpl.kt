package com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.service.VendorComplaintService
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class VendorComplaintRemoteDataSourceImpl @Inject constructor(private val service: VendorComplaintService) :
    VendorComplaintRemoteDataSource {
    override fun postComplaintRDS(
        userId: Int,
        projectId: String,
        title: String,
        description: String,
//        date: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part
    ): Single<VendorComplaintResponseModel> {
        return service.postComplaintApi(userId,projectId,title,description,locationId,subLocationId,image)
    }

    override fun getComplaintAreaRDS(projectId: String): Single<VendorComplaintAreaResponseModel> {
        return service.getComplaintAreaApi(projectId)
    }

    override fun getComplaintSubAreaRDS(projectId: String, locationId: Int): Single<VendorComplaintSubAreaResponseModel> {
        return service.getComplaintSubAreaApi(projectId, locationId)
    }

    override fun getHistoryComplaint(
        page: Int,
        projectId: String,
        complaintType: String
    ): Single<HistoryComplaintResponseModel> {
        return service.getHistoryComplaint(page, projectId, complaintType)
    }

    override fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse> {
        return service.getDetailHistoryComplaint(complaintId)
    }

    override fun getDashboardComplaint(projectId: String): Single<DashboardCtalkVendorResponseModel> {
        return service.getDashboardComplaint(projectId)
    }
}