package com.hkapps.hygienekleen.features.features_client.visitreport.data.repository

import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.MainVisitReportResponseModel
import io.reactivex.Single

interface VisitReportRepository {

    fun getMainVisitReport(
        projectCode:String,
        date: String,
        page:Int
    ): Single<MainVisitReportResponseModel>

}