package com.hkapps.hygienekleen.features.features_management.damagereport.data.repository

import com.hkapps.hygienekleen.features.features_management.damagereport.model.detaildamagereport.DetailDamageReportMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listdamagereport.ListDamageReportResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.listprojectdamagereport.ListProjectBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploaddamageimagebak.PutDamageBakMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfotodamagereport.UploadFotoBakResponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadfrontimagebak.PutFrontBakMgmntReponseModel
import com.hkapps.hygienekleen.features.features_management.damagereport.model.uploadketerangan.UploadKeteranganResponseModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface DamageReportManagementRepository {

    fun getListDamageReportManagement(
        adminMasterId: Int,
        projectCode : String,
        date: String,
        filter: String,
        page: Int
    ): Single<ListDamageReportResponseModel>

    fun getDetailDamageReportManagement(
        idDetailBakMesin:Int
    ):Single<DetailDamageReportMgmntResponseModel>

    fun getListProjectDamageReportManagement(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<ListProjectBakResponseModel>

    fun putUploadBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file1: MultipartBody.Part,
        file2: MultipartBody.Part,
        keteranganAssets: String
    ): Single<UploadFotoBakResponseModel>

    fun putUploadFrontBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file1: MultipartBody.Part
    ): Single<PutFrontBakMgmntReponseModel>

    fun putUploadDamageBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file2: MultipartBody.Part
    ): Single<PutDamageBakMgmntResponseModel>

    fun putKeteranganBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        keteranganAssets: String
    ): Single<UploadKeteranganResponseModel>

    fun getDataListBAKMachine(
        projectCode : String,
        page : Int,
        perPage : Int,
    ): Single<BakMachineResponse>


}