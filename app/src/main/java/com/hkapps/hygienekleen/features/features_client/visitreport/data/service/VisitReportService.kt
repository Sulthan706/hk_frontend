package com.hkapps.hygienekleen.features.features_client.visitreport.data.service

import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.MainVisitReportResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VisitReportService {
    @GET("api/v1/client/visit-report/main-visit")
    fun getMainVisitReport(
        @Query("projectCode") projectCode: String,
        @Query("date") date: String,
        @Query("page") page: Int
    ):Single<MainVisitReportResponseModel>
}