package com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.remote.VendorComplaintRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class VendorComplaintRepositoryImpl @Inject constructor(private val remoteDataSource: VendorComplaintRemoteDataSource) :
    VendorComplaintRepository {
    override fun postComplaint(
        userId: Int,
        projectId: String,
        title: String,
        description: String,
//        date: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part
    ): Single<VendorComplaintResponseModel> {
        return remoteDataSource.postComplaintRDS(userId,projectId,title,description,locationId,subLocationId,image)
    }

    override fun getAreaComplaint(projectId: String): Single<VendorComplaintAreaResponseModel> {
        return remoteDataSource.getComplaintAreaRDS(projectId)
    }

    override fun getSubAreaComplaint(
        projectId: String,
        locationId: Int
    ): Single<VendorComplaintSubAreaResponseModel> {
        return remoteDataSource.getComplaintSubAreaRDS(projectId, locationId)
    }

    override fun getHistoryComplaint(
        page: Int,
        projectId: String,
        complaintType: String
    ): Single<HistoryComplaintResponseModel> {
        return remoteDataSource.getHistoryComplaint(page, projectId, complaintType)
    }

    override fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse> {
        return remoteDataSource.getDetailHistoryComplaint(complaintId)
    }

    override fun getDashboardComplaint(projectId: String): Single<DashboardCtalkVendorResponseModel> {
        return remoteDataSource.getDashboardComplaint(projectId)
    }
}