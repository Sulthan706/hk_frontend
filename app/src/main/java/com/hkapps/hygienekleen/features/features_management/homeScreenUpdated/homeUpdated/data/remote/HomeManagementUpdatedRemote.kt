package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.lastVisit.VisitHomeManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitBod.RemainingVisitsBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitManagement.RemainingVisitsManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.listRemainingVisitTeknisi.RemainingVisitsTeknisiResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.model.rkbHome.RkbHomeManagementResponse
import io.reactivex.Single

interface HomeManagementUpdatedRemote {

    fun getRkbBodHome(
        bodId: Int,
        date: String
    ): Single<RkbHomeManagementResponse>

    fun getLastVisitBod(
        adminMasterId: Int,
        date: String
    ): Single<VisitHomeManagementResponse>

    fun getListRemainingVisitBod(
        bodId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsBodResponse>

    fun getRkbManagementHome(
        userId: Int,
        date: String
    ): Single<RkbHomeManagementResponse>

    fun getLastVisitManagement(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse>

    fun getListRemainingVisitManagement(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsManagementResponse>

    fun getRkbTeknisiHome(
        userId: Int,
        date: String
    ): Single<RkbHomeManagementResponse>

    fun getLastVisitTeknisi(
        userId: Int,
        date: String
    ): Single<VisitHomeManagementResponse>

    fun getListRemainingVisitTeknisi(
        userId: Int,
        date: String,
        page: Int,
        perPage: Int
    ): Single<RemainingVisitsTeknisiResponse>

}