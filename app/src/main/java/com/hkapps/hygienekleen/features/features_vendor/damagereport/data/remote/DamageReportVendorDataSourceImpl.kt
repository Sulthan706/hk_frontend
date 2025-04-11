package com.hkapps.hygienekleen.features.features_vendor.damagereport.data.remote

import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.service.DamageReportVendorService
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.checkusertechnician.CheckUserVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor.DetailBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.uploadbakvendor.UploadBakVendorResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class DamageReportVendorDataSourceImpl @Inject constructor(private val service: DamageReportVendorService):
DamageReportVendorDataSource {
    override fun getListBakVendor(
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListBakVendorResponseModel> {
        return service.getListBakVendor(projectCode, date, page)
    }

    override fun getDetailBakVendor(idDetailBakMesin: Int): Single<DetailBakVendorResponseModel> {
        return service.getDetailBakVendor(idDetailBakMesin)
    }

    override fun putUploadBakVendor(
        idDetailBakMesin: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        keteranganBak: String
    ): Single<UploadBakVendorResponseModel> {
        return service.putUploadBakVendor(idDetailBakMesin, employeeId, file, keteranganBak)
    }

    override fun getCheckUserTechnician(employeeId: Int): Single<CheckUserVendorResponseModel> {
        return service.getCheckUserTechnician(employeeId)
    }

    override fun getDataListBAKMachine(projectCode: String,page : Int,perPage : Int): Single<BakMachineResponse> {
        return service.getDataListBAKMachine(projectCode,page,perPage)
    }


}