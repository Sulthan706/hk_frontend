package com.hkapps.hygienekleen.features.grafik.data.repository

import com.hkapps.hygienekleen.features.grafik.data.remote.ChartRemoteDataSource
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.ListTurnOverResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.TurnOverResponse
import io.reactivex.Single

class ChartRepositoryImpl(private val remoteDataSource : ChartRemoteDataSource): ChartRepository {
    override fun getDataAbsenceStaff(projectCode: String): Single<ChartAbsenceStaffResponse> {
        return remoteDataSource.getDataAbsenceStaff(projectCode)
    }

    override fun getDataTimeSheet(projectCode: String): Single<TimeSheetResponse> {
        return remoteDataSource.getDataTimeSheet(projectCode)
    }

    override fun getDataListTimeSheet(projectCode: String): Single<ListTimeSheetResponse> {
        return remoteDataSource.getDataListTimeSheet(projectCode)
    }

    override fun getDataTurnOver(projectCode: String): Single<TurnOverResponse> {
        return remoteDataSource.getDataTurnOver(projectCode)
    }

    override fun getListDataTurnOver(projectCode: String): Single<ListTurnOverResponse> {
        return remoteDataSource.getListDataTurnOver(projectCode)
    }


}