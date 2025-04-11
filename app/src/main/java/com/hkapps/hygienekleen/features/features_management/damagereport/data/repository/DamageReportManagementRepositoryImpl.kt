package com.hkapps.hygienekleen.features.features_management.damagereport.data.repository

import com.hkapps.hygienekleen.features.features_management.damagereport.data.remote.DamageReportManagementDataSource
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
import javax.inject.Inject

class DamageReportManagementRepositoryImpl @Inject constructor(private val dataSource: DamageReportManagementDataSource):
DamageReportManagementRepository {
    override fun getListDamageReportManagement(
        adminMasterId: Int,
        projectCode : String,
        date: String,
        filter: String,
        page: Int
    ): Single<ListDamageReportResponseModel> {
        return dataSource.getListDamageReportManagement(adminMasterId,projectCode, date, filter, page)
    }

    override fun getDetailDamageReportManagement(idDetailBakMesin: Int): Single<DetailDamageReportMgmntResponseModel> {
        return dataSource.getDetailDamageReportManagement(idDetailBakMesin)
    }

    override fun getListProjectDamageReportManagement(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<ListProjectBakResponseModel> {
        return dataSource.getListProjectDamageReportManagement(adminMasterId, page, keywords)
    }

    override fun putUploadBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file1: MultipartBody.Part,
        file2: MultipartBody.Part,
        keteranganAssets: String
    ): Single<UploadFotoBakResponseModel> {
        return dataSource.putUploadBakManagement(idDetailBakMesin, adminMasterId, file1, file2, keteranganAssets)
    }

    override fun putUploadFrontBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file1: MultipartBody.Part
    ): Single<PutFrontBakMgmntReponseModel> {
        return dataSource.putUploadFrontBakManagement(idDetailBakMesin, adminMasterId, file1)
    }

    override fun putUploadDamageBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        file2: MultipartBody.Part
    ): Single<PutDamageBakMgmntResponseModel> {
        return dataSource.putUploadDamageBakManagement(idDetailBakMesin, adminMasterId, file2)
    }

    override fun putKeteranganBakManagement(
        idDetailBakMesin: Int,
        adminMasterId: Int,
        keteranganAssets: String
    ): Single<UploadKeteranganResponseModel> {
        return dataSource.putKeteranganBakManagement(idDetailBakMesin, adminMasterId, keteranganAssets)
    }

    override fun getDataListBAKMachine(projectCode: String,page :Int,perPage : Int): Single<BakMachineResponse> {
        return dataSource.getDataListBAKMachine(projectCode,page,perPage)
    }


}