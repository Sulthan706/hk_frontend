package com.hkapps.hygienekleen.features.features_management.shareloc.data.remote

import com.hkapps.hygienekleen.features.features_management.shareloc.data.service.ShareLocManagementService
import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.GetAllShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement.DetailShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.eventcalenderdetailmanagement.EventCalenderManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.GetListSearchAllMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.sharelocmanagement.PutShareLocManagementResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ShareLocManagementDataSourceImpl @Inject constructor(private val service: ShareLocManagementService):
ShareLocManagementDataSource {
    override fun putShareLocManagement(
        managementId: Int,
        latitude: Double,
        longitude: Double,
        address: String
    ): Single<PutShareLocManagementResponseModel> {
        return service.putShareLocManagement(managementId, latitude, longitude, address)
    }

    override fun getAllShareLocManagement(): Single<GetAllShareLocMgmntResponseModel> {
        return service.getAllShareLocManagement()
    }

    override fun getListSearchAllManagement(
        page: Int,
        keywords: String
    ): Single<GetListSearchAllMgmntResponseModel> {
        return service.getListSearchAllManagement(page, keywords)
    }

    override fun getDetailManagementShareLoc(
        managementId: Int,
        date: String
    ): Single<DetailShareLocMgmntResponseModel> {
        return service.getDetailManagementShareLoc(managementId, date)
    }

    override fun getEventCalendarManagement(
        managementId: Int,
        month: String,
        year: String
    ): Single<EventCalenderManagementResponseModel> {
        return service.getEventCalendarManagement(managementId, month, year)
    }


}