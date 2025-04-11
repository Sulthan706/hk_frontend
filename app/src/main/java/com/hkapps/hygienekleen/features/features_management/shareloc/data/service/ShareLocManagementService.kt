package com.hkapps.hygienekleen.features.features_management.shareloc.data.service

import com.hkapps.hygienekleen.features.features_management.shareloc.model.allsharelocmanagement.GetAllShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.detailmainsharelocmanagement.DetailShareLocMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.eventcalenderdetailmanagement.EventCalenderManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.listsearchgetmanagement.GetListSearchAllMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.shareloc.model.sharelocmanagement.PutShareLocManagementResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ShareLocManagementService {

    //MANAGEMENT
    @PUT("api/v1/lokasi/management")
    fun putShareLocManagement(
        @Query("managementId") managementId:Int,
        @Query("latitude") latitude:Double,
        @Query("longitude") longitude:Double,
        @Query("address") address:String,
    ): Single<PutShareLocManagementResponseModel>

    //BOD
    @GET("api/v1/lokasi/bod/today")
    fun getAllShareLocManagement(
    ): Single<GetAllShareLocMgmntResponseModel>

    @GET("api/v1/management/bod/list/employee/search")
    fun getListSearchAllManagement(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<GetListSearchAllMgmntResponseModel>

    @GET("api/v1/lokasi/management/get-location")
    fun getDetailManagementShareLoc(
        @Query("managementId") managementId: Int,
        @Query("date") date: String
    ): Single<DetailShareLocMgmntResponseModel>

    @GET("api/v1/lokasi/management/history/event-calendar")
    fun getEventCalendarManagement(
        @Query("managementId") managementId: Int,
        @Query("month") month: String,
        @Query("year") year : String
    ): Single<EventCalenderManagementResponseModel>


}