package com.hkapps.hygienekleen.features.features_client.visitreport.data.remote

import com.hkapps.hygienekleen.features.features_client.visitreport.data.service.VisitReportService
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.MainVisitReportResponseModel
import io.reactivex.Single
import javax.inject.Inject

class VisitReportRemoteDataSourceImpl @Inject constructor(private val service: VisitReportService):
VisitReportRemoteDataSource {

    override fun getMainVisitReport(
        projectCode: String,
        date: String,
        page: Int
    ): Single<MainVisitReportResponseModel> {
        return service.getMainVisitReport(projectCode, date, page)
    }

}