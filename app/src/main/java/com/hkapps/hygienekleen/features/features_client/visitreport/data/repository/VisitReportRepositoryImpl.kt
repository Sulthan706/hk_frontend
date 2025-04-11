package com.hkapps.hygienekleen.features.features_client.visitreport.data.repository

import com.hkapps.hygienekleen.features.features_client.visitreport.data.remote.VisitReportRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.visitreport.model.mainvisitreport.MainVisitReportResponseModel
import io.reactivex.Single
import javax.inject.Inject

class VisitReportRepositoryImpl @Inject constructor(private val remoteDataSource: VisitReportRemoteDataSource):
VisitReportRepository {

    override fun getMainVisitReport(
        projectCode: String,
        date: String,
        page: Int
    ): Single<MainVisitReportResponseModel> {
        return remoteDataSource.getMainVisitReport(projectCode, date, page)
    }

}