package com.hkapps.hygienekleen.features.features_vendor.damagereport.data.repository

import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.remote.DamageReportVendorDataSource
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.checkusertechnician.CheckUserVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor.DetailBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.uploadbakvendor.UploadBakVendorResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class DamageReportVendorRepositoryImpl @Inject constructor(private val dataSource: DamageReportVendorDataSource):
DamageReportVendorRepository {
    override fun getListBakVendor(
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListBakVendorResponseModel> {
        return dataSource.getListBakVendor(projectCode, date, page)
    }

    override fun getDetailBakVendor(idDetailBakMesin: Int): Single<DetailBakVendorResponseModel> {
        return dataSource.getDetailBakVendor(idDetailBakMesin)
    }

    override fun putUploadBakVendor(
        idDetailBakMesin: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        keteranganBak: String
    ): Single<UploadBakVendorResponseModel> {
        return dataSource.putUploadBakVendor(idDetailBakMesin, employeeId, file, keteranganBak)
    }

    override fun getCheckUserTechnician(employeeId: Int): Single<CheckUserVendorResponseModel> {
        return dataSource.getCheckUserTechnician(employeeId)
    }

    override fun getDataListBAKMachine(projectCode: String,page :Int,perPage : Int): Single<BakMachineResponse> {
        return dataSource.getDataListBAKMachine(projectCode,page,perPage)
    }


}