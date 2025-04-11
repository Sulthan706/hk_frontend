package com.hkapps.hygienekleen.features.features_vendor.service.resign.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.detailresignvendor.DetailReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor.DateResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.ListResignVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.submitresign.SubmitResignVendorResponseModel
import io.reactivex.Single

interface ResignRepository {

    fun getListResignVendor(
        employeeId: Int
    ): Single<ListResignVendorResponseModel>

    fun postSubmitResignVendor(
        employeeId: Int,
        tanggalPengajuan: String
    ): Single<SubmitResignVendorResponseModel>

    fun getDateResignVendor(
        projectCode: String
    ): Single<DateResignResponseModel>

    fun getDetailResignVendor(
        idTurnOver: Int
    ): Single<DetailReasonResignResponseModel>


}