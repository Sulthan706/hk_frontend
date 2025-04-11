package com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.remote

import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.createweekly.CreateWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.detailweekly.DetailWeeklyResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.listweeklyresponse.ListWeeklyProgressResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.model.validation.ValidationResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface WeeklyProgressDataSource {


    fun getListWeeklyProgress(
        projectCode: String,
        adminMasterId: Int,
        startAt: String,
        endAt: String,
        status : String,
        page: Int,
        size: Int
    ): Single<ListWeeklyProgressResponse>

    fun checkValidation(
        adminMasterId: Int,
        date : String,
    ): Single<ValidationResponse>

    fun getDetailWeeklyProgress(
        idWeekly: Int,
    ): Single<DetailWeeklyResponse>

    fun createWeeklyProgress(
        adminMasterId: Int,
        projectCode: String,
        location : String,
        materialType : String,
        chemical : String,
        volumeChemical : Int,
        cleaningMethod : String,
        frequency : Int,
        areaSize : Int,
        totalPic : Int,
        fileBefore : MultipartBody.Part,
        fileAfter : MultipartBody.Part?,
        status : String,
    ):Single<CreateWeeklyProgressResponse>

    fun updateWeeklyProgress(
        idWeekly: Int,
        adminMasterId: Int,
        projectCode: String,
        location : String,
        materialType : String,
        chemical : String,
        volumeChemical : Int,
        cleaningMethod : String,
        frequency : Int,
        areaSize : Int,
        totalPic : Int,
        fileBefore : MultipartBody.Part,
        fileAfter : MultipartBody.Part?,
        status : String,
    ):Single<CreateWeeklyProgressResponse>


}