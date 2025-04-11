package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.remote.WeeklyProgressDataSource
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.createweekly.CreateWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.detailweekly.DetailWeeklyResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.ListWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation.ValidationResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class WeeklyProgressRepositoryImpl
@Inject constructor(
    private val weeklyProgressDataSource: WeeklyProgressDataSource
) : WeeklyProgressRepository {
    override fun getListWeeklyProgress(
        projectCode: String,
        adminMasterId: Int,
        startAt: String,
        endAt : String,
        status : String,
        page: Int,
        size: Int
    ): Single<ListWeeklyProgressResponse> {
        return weeklyProgressDataSource.getListWeeklyProgress(
            projectCode,
            adminMasterId,
            startAt,
            endAt,
            status,
            page,
            size
        )
    }

    override fun getDetailWeeklyProgress(idWeekly: Int): Single<DetailWeeklyResponse> {
        return weeklyProgressDataSource.getDetailWeeklyProgress(idWeekly)
    }

    override fun checkValidation(adminMasterId: Int, date: String): Single<ValidationResponse> {
        return weeklyProgressDataSource.checkValidation(adminMasterId, date)
    }

    override fun createWeeklyProgress(
        adminMasterId: Int,
        projectCode: String,
        location: String,
        materialType: String,
        chemical: String,
        volumeChemical: Int,
        cleaningMethod: String,
        frequency: Int,
        areaSize: Int,
        totalPic: Int,
        fileBefore: MultipartBody.Part,
        fileAfter: MultipartBody.Part?,
        status: String,
    ): Single<CreateWeeklyProgressResponse> {
        return weeklyProgressDataSource.createWeeklyProgress(
            adminMasterId,
            projectCode,
            location,
            materialType,
            chemical,
            volumeChemical,
            cleaningMethod,
            frequency,
            areaSize,
            totalPic,
            fileBefore,
            fileAfter,
            status,
        )
    }

    override fun updateWeeklyProgress(
        idWeekly: Int,
        adminMasterId: Int,
        projectCode: String,
        location: String,
        materialType: String,
        chemical: String,
        volumeChemical: Int,
        cleaningMethod: String,
        frequency: Int,
        areaSize: Int,
        totalPic: Int,
        fileBefore: MultipartBody.Part,
        fileAfter: MultipartBody.Part?,
        status: String
    ): Single<CreateWeeklyProgressResponse> {
        return weeklyProgressDataSource.updateWeeklyProgress(
            idWeekly,
            adminMasterId,
            projectCode,
            location,
            materialType,
            chemical,
            volumeChemical,
            cleaningMethod,
            frequency,
            areaSize,
            totalPic,
            fileBefore,
            fileAfter,
            status
        )
    }
}