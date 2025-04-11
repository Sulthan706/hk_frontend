package com.hkapps.hygienekleen.features.features_vendor.damagereport.data.remote

import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.checkusertechnician.CheckUserVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.detailbakvendor.DetailBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakvendor.ListBakVendorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.uploadbakvendor.UploadBakVendorResponseModel
import io.reactivex.Single
import okhttp3.MultipartBody

interface DamageReportVendorDataSource {

    fun getListBakVendor(
        projectCode: String,
        date: String,
        page: Int
    ): Single<ListBakVendorResponseModel>

    fun getDetailBakVendor(
        idDetailBakMesin: Int
    ): Single<DetailBakVendorResponseModel>

    fun putUploadBakVendor(
        idDetailBakMesin: Int,
        employeeId: Int,
        file: MultipartBody.Part,
        keteranganBak: String
    ): Single<UploadBakVendorResponseModel>

    fun getCheckUserTechnician(
        employeeId: Int
    ): Single<CheckUserVendorResponseModel>

    fun getDataListBAKMachine(
        projectCode : String,
        page : Int,
        perPage : Int
    ): Single<BakMachineResponse>


}