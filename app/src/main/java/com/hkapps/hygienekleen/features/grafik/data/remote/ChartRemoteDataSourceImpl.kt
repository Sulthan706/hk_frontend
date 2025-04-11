package com.hkapps.hygienekleen.features.grafik.data.remote

import com.hkapps.hygienekleen.features.grafik.data.service.ChartService
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.ListTurnOverResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.TurnOverResponse
import io.reactivex.Single

class ChartRemoteDataSourceImpl(private val chartService: ChartService): ChartRemoteDataSource {
    override fun getDataAbsenceStaff(projectCode: String) : Single<ChartAbsenceStaffResponse> {
        return chartService.getDataAbsenceStaff(projectCode)
    }

    override fun getDataTimeSheet(projectCode: String): Single<TimeSheetResponse> {
        return chartService.getDataTimeSheet(projectCode)
    }

    override fun getDataListTimeSheet(projectCode: String): Single<ListTimeSheetResponse> {
        return chartService.getDataListTimeSheet(projectCode)
    }

    override fun getDataTurnOver(projectCode: String): Single<TurnOverResponse> {
        return chartService.getDataTurnOver(projectCode)
    }

    override fun getListDataTurnOver(projectCode: String): Single<ListTurnOverResponse> {
        return chartService.getListDataTurnOver(projectCode)
    }

}