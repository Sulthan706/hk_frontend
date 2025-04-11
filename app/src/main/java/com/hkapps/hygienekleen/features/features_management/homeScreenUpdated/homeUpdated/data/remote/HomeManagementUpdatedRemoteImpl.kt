package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.service.HomeManagementUpdatedService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit.VisitHomeManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.RemainingVisitsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.RemainingVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.RemainingVisitsTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome.RkbHomeManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeManagementUpdatedRemoteImpl @Inject constructor(private val service: HomeManagementUpdatedService):
    HomeManagementUpdatedRemote {

    override fun getRkbBodHome(bodId: Int, date: String): Single<RkbHomeManagementResponse> {
        return service.getRkbBodHome(bodId, date)
    }

    override fun getLastVisitBod(
        adminMasterId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return service.getLastVisitBod(adminMasterId, date)
    }

    override fun getListRemainingVisitBod(
        bodId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsBodResponse> {
        return service.getListRemainingVisitBod(bodId, date, page, perPage)
    }

    override fun getRkbManagementHome(
        userId: Int,
        date: String
    ): Single<RkbHomeManagementResponse> {
        return service.getRkbManagementHome(userId, date)
    }

    override fun getLastVisitManagement(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return service.getLastVisitManagement(userId, date)
    }

    override fun getListRemainingVisitManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsManagementResponse> {
        return service.getListRemainingVisitManagement(userId, date, page, perPage)
    }

    override fun getRkbTeknisiHome(
        userId: Int,
        date: String
    ): Single<RkbHomeManagementResponse> {
        return service.getRkbTeknisiHome(userId, date)
    }

    override fun getLastVisitTeknisi(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return service.getLastVisitTeknisi(userId, date)
    }

    override fun getListRemainingVisitTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsTeknisiResponse> {
        return service.getListRemainingVisitTeknisi(userId, date, page, perPage)
    }

}