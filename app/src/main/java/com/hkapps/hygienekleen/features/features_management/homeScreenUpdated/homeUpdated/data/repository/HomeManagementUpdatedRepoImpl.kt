package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.remote.HomeManagementUpdatedRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit.VisitHomeManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.RemainingVisitsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.RemainingVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.RemainingVisitsTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome.RkbHomeManagementResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeManagementUpdatedRepoImpl @Inject constructor(private val dataSource: HomeManagementUpdatedRemote):
    HomeManagementUpdateRepository {

    override fun getRkbBodHome(bodId: Int, date: String): Single<RkbHomeManagementResponse> {
        return dataSource.getRkbBodHome(bodId, date)
    }

    override fun getLastVisitBod(
        adminMasterId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return dataSource.getLastVisitBod(adminMasterId, date)
    }

    override fun getListRemainingVisitBod(
        bodId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsBodResponse> {
        return dataSource.getListRemainingVisitBod(bodId, date, page, perPage)
    }

    override fun getRkbManagementHome(
        userId: Int,
        date: String
    ): Single<RkbHomeManagementResponse> {
        return dataSource.getRkbManagementHome(userId, date)
    }

    override fun getLastVisitManagement(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return dataSource.getLastVisitManagement(userId, date)
    }

    override fun getListRemainingVisitManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsManagementResponse> {
        return dataSource.getListRemainingVisitManagement(userId, date, page, perPage)
    }

    override fun getRkbTeknisiHome(userId: Int, date: String): Single<RkbHomeManagementResponse> {
        return dataSource.getRkbTeknisiHome(userId, date)
    }

    override fun getLastVisitTeknisi(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse> {
        return dataSource.getLastVisitTeknisi(userId, date)
    }

    override fun getListRemainingVisitTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsTeknisiResponse> {
        return dataSource.getListRemainingVisitTeknisi(userId, date, page, perPage)
    }

}