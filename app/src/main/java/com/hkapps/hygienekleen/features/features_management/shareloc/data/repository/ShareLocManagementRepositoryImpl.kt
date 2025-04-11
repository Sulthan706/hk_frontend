package com.hkapps.hygienekleen.features.features_management.shareloc.data.repository

import com.hkapps.hygienekleen.features.features_management.shareloc.data.remote.ShareLocManagementDataSource
import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.GetAllShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement.DetailShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.eventcalenderdetailmanagement.EventCalenderManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.GetListSearchAllMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.sharelocmanagement.PutShareLocManagementResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ShareLocManagementRepositoryImpl @Inject constructor(private val dataSource: ShareLocManagementDataSource):
ShareLocManagementRepository {
    override fun putShareLocManagement(
        managementId: Int,
        latitude: Double,
        longitude: Double,
        address: String
    ): Single<PutShareLocManagementResponseModel> {
        return dataSource.putShareLocManagement(managementId, latitude, longitude, address)
    }

    override fun getAllShareLocManagement(): Single<GetAllShareLocMgmntResponseModel> {
        return dataSource.getAllShareLocManagement()
    }

    override fun getListSearchAllManagement(
        page: Int,
        keywords: String
    ): Single<GetListSearchAllMgmntResponseModel> {
        return dataSource.getListSearchAllManagement(page, keywords)
    }

    override fun getDetailManagementShareLoc(
        managementId: Int,
        date: String
    ): Single<DetailShareLocMgmntResponseModel> {
        return dataSource.getDetailManagementShareLoc(managementId, date)
    }

    override fun getEventCalendarManagement(
        managementId: Int,
        month: String,
        year: String
    ): Single<EventCalenderManagementResponseModel> {
        return dataSource.getEventCalendarManagement(managementId, month, year)
    }


}