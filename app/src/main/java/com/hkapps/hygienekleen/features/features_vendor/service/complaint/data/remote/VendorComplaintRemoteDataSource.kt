package com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.VendorComplaintSubAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.dashboardctalkvendor.DashboardCtalkVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.detailHistoryComplaint.DetailHistoryComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.model.historyComplaint.HistoryComplaintResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface VendorComplaintRemoteDataSource {
    fun postComplaintRDS(userId: Int,
                         projectId: String,
                         title: String,
                         description: String,
//                         date: String,
                         locationId: Int,
                         subLocationId: Int,
                         image: MultipartBody.Part
    ): Single<VendorComplaintResponseModel>

    fun getComplaintAreaRDS(projectId: String
    ): Single<VendorComplaintAreaResponseModel>


    fun getComplaintSubAreaRDS(projectId: String, locationId: Int
    ): Single<VendorComplaintSubAreaResponseModel>

    fun getHistoryComplaint(page: Int, projectId: String, complaintType: String
    ): Single<HistoryComplaintResponseModel>

    fun getDetailHistoryComplaint(complaintId: Int): Single<DetailHistoryComplaintResponse>

    fun getDashboardComplaint(
        projectId: String
    ): Single<DashboardCtalkVendorResponseModel>
}