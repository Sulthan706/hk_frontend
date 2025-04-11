package com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface VendorComplaintRepository {
    fun postComplaint(
        userId: Int,
        projectId: String,
        title: String,
        description: String,
//        date: String,
        locationId: Int,
        subLocationId: Int,
        image: MultipartBody.Part
    ): Single<VendorComplaintResponseModel>

    fun getAreaComplaint(
        projectId: String
    ): Single<VendorComplaintAreaResponseModel>

    fun getSubAreaComplaint(
        projectId: String,
        locationId: Int
    ): Single<VendorComplaintSubAreaResponseModel>

    fun getHistoryComplaint(page: Int, projectId: String, complaintType: String): Single<HistoryComplaintResponseModel>

    fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse>

    fun getDashboardComplaint(
        projectId: String
    ): Single<DashboardCtalkVendorResponseModel>

}