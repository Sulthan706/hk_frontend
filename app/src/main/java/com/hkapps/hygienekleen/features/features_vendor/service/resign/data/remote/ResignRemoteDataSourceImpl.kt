package com.hkapps.hygienekleen.features.features_vendor.service.resign.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.service.ResignService
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.detailresignvendor.DetailReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor.DateResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.ListResignVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.submitresign.SubmitResignVendorResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ResignRemoteDataSourceImpl @Inject constructor(private val service: ResignService):
 ResignRemoteDataSource {
   override fun getListResignVendor(employeeId: Int): Single<ListResignVendorResponseModel> {
    return service.getListResignVendor(employeeId)
   }

    override fun postSubmitResignVendor(
        employeeId: Int,
        tanggalPengajuan: String
    ): Single<SubmitResignVendorResponseModel> {
        return service.postSubmitResignVendor(employeeId, tanggalPengajuan)
    }

    override fun getDateResignVendor(projectCode: String): Single<DateResignResponseModel> {
        return service.getDateResignVendor(projectCode)
    }

    override fun getDetailResignVendor(idTurnOver: Int): Single<DetailReasonResignResponseModel> {
        return service.getDetailResignVendor(idTurnOver)
    }

}