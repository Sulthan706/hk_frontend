package com.hkapps.hygienekleen.features.grafik.data.repository

import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.ListTimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.ListTurnOverResponse
import com.hkapps.hygienekleen.features.grafik.model.turnover.TurnOverResponse
import io.reactivex.Single

interface ChartRepository {

    fun getDataAbsenceStaff(projectCode : String) : Single<ChartAbsenceStaffResponse>

    fun getDataTimeSheet(projectCode : String) : Single<TimeSheetResponse>

    fun getDataListTimeSheet(projectCode : String) : Single<ListTimeSheetResponse>

    fun getDataTurnOver(projectCode : String) : Single<TurnOverResponse>

    fun getListDataTurnOver(
        projectCode : String
    ):Single<ListTurnOverResponse>
}