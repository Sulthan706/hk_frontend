package com.hkapps.hygienekleen.features.features_management.shareloc.data.remote

import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.GetAllShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement.DetailShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.eventcalenderdetailmanagement.EventCalenderManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.GetListSearchAllMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.sharelocmanagement.PutShareLocManagementResponseModel
import io.reactivex.Single

interface ShareLocManagementDataSource {

    fun putShareLocManagement(
        managementId:Int,
        latitude:Double,
        longitude:Double,
        address:String,
    ): Single<PutShareLocManagementResponseModel>

    fun getAllShareLocManagement(
    ): Single<GetAllShareLocMgmntResponseModel>

    fun getListSearchAllManagement(
        page: Int,
        keywords: String
    ): Single<GetListSearchAllMgmntResponseModel>

    fun getDetailManagementShareLoc(
        managementId: Int,
        date: String
    ): Single<DetailShareLocMgmntResponseModel>

    fun getEventCalendarManagement(
        managementId: Int,
        month: String,
        year : String
    ): Single<EventCalenderManagementResponseModel>

}