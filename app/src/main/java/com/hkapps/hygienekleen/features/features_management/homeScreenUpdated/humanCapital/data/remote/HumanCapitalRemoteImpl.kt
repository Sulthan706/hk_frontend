package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.remote

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.service.HumanCapitalService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.BranchesHumanCapitalResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.ManPowerBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.ManPowerManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating.SubmitRatingResponse
import io.reactivex.Single
import javax.inject.Inject

class HumanCapitalRemoteImpl @Inject constructor(private val service: HumanCapitalService): HumanCapitalRemote {

    override fun getManPowerManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerManagementResponse> {
        return service.getManPowerManagement(userId, keywords, filter, page, perPage)
    }

    override fun getBranchesHumanCapital(
        page: Int,
        perPage: Int
    ): Single<BranchesHumanCapitalResponse> {
        return service.getBranchesHumanCapital(page, perPage)
    }

    override fun getManPowerBod(
        branchCode: String,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerBodResponse> {
        return service.getManPowerBod(branchCode, keywords, filter, page, perPage)
    }

    override fun submitRating(
        ratingByUserId: Int,
        roleUser: String,
        employeeId: Int,
        rating: Int,
        feedback: String,
        projectCode: String,
        jobCode: String
    ): Single<SubmitRatingResponse> {
        return service.submitRating(ratingByUserId, roleUser, employeeId, rating, feedback, projectCode, jobCode)
    }

}