package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listBranch.BranchesHumanCapitalResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerBod.ManPowerBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.listManPowerManagement.ManPowerManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.model.submitRating.SubmitRatingResponse
import io.reactivex.Single

interface HumanCapitalRepository {

    fun getManPowerManagement(
        userId: Int,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerManagementResponse>

    fun getBranchesHumanCapital(
        page: Int,
        perPage: Int
    ): Single<BranchesHumanCapitalResponse>

    fun getManPowerBod(
        branchCode: String,
        keywords: String,
        filter: String,
        page: Int,
        perPage: Int
    ): Single<ManPowerBodResponse>

    fun submitRating(
        ratingByUserId: Int,
        roleUser: String,
        employeeId: Int,
        rating: Int,
        feedback: String,
        projectCode: String,
        jobCode: String
    ): Single<SubmitRatingResponse>

}