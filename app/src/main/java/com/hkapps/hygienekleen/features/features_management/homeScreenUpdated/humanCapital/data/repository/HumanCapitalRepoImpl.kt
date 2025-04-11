package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.remote.HumanCapitalRemote
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.BranchesHumanCapitalResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.ManPowerBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.ManPowerManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating.SubmitRatingResponse
import io.reactivex.Single
import javax.inject.Inject

class HumanCapitalRepoImpl @Inject constructor(private val dataSource: HumanCapitalRemote): HumanCapitalRepository {

    override fun getManPowerManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerManagementResponse> {
        return dataSource.getManPowerManagement(userId, keywords, filter, page, perPage)
    }

    override fun getBranchesHumanCapital(
        page: Int,
        perPage: Int
    ): Single<BranchesHumanCapitalResponse> {
        return dataSource.getBranchesHumanCapital(page, perPage)
    }

    override fun getManPowerBod(
        branchCode: String,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerBodResponse> {
        return dataSource.getManPowerBod(branchCode, keywords, filter, page, perPage)
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
        return dataSource.submitRating(ratingByUserId, roleUser, employeeId, rating, feedback, projectCode, jobCode)
    }

}