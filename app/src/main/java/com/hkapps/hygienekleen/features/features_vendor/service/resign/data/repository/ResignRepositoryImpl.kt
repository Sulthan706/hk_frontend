package com.hkapps.hygienekleen.features.features_vendor.service.resign.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.remote.ResignRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.detailresignvendor.DetailReasonResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.getdatevendor.DateResignResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.listresignvendor.ListResignVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.model.submitresign.SubmitResignVendorResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ResignRepositoryImpl @Inject constructor(private val remoteDataSource: ResignRemoteDataSource):
ResignRepository {
    override fun getListResignVendor(employeeId: Int): Single<ListResignVendorResponseModel> {
        return remoteDataSource.getListResignVendor(employeeId)
    }

    override fun postSubmitResignVendor(
        employeeId: Int,
        tanggalPengajuan: String
    ): Single<SubmitResignVendorResponseModel> {
        return remoteDataSource.postSubmitResignVendor(employeeId, tanggalPengajuan)
    }

    override fun getDateResignVendor(projectCode: String): Single<DateResignResponseModel> {
        return remoteDataSource.getDateResignVendor(projectCode)
    }

    override fun getDetailResignVendor(idTurnOver: Int): Single<DetailReasonResignResponseModel> {
        return remoteDataSource.getDetailResignVendor(idTurnOver)
    }

}