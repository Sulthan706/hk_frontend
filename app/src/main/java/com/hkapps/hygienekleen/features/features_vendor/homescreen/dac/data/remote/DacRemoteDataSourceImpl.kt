package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.service.DacService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.CheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.CheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.DACCheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.PutCheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import javax.inject.Inject

class DacRemoteDataSourceImpl @Inject constructor(private val service: DacService) :
    DacRemoteDataSource {
    override fun getDailyActRDS(
        employeeId: Int,
        projectCode: String
    ): Single<DailyActResponseModel> {
        return service.getDailyDataApi(employeeId, projectCode)
    }

    override fun getDailyActNewRDS(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int
    ): Single<DailyActNewResponseModel> {
        return service.getDailyDataNewApi(employeeId, projectCode, idDetailProject)
    }

    override fun getCountAct(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int
    ): Single<DACCheckResponseModel> {
        return service.getCountDACApi(employeeId, projectCode, idDetailProject)
    }

    override fun getChecklistDacLowLevel(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int,
        activityId: Int,
        plottingId: Int
    ): Single<CheckDACResponseModel> {
        return service.getChecklistDacLowLevelApi(
            employeeId,
            projectCode,
            idDetailProject,
            activityId,
            plottingId
        )
    }

    override fun postChecklistDacLow(
        employeeId: Int,
        idDetailEmployeeProject: Int,
        projectId: String,
        plottingId: Int,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int,
        activityId: Int
    ): Single<CheckResponseModel> {
        return service.postChecklistDacLowApi(
            employeeId,
            idDetailEmployeeProject,
            projectId,
            plottingId,
            shiftId,
            locationId,
            subLocationId,
            activityId
        )
    }

    override fun putChecklistDacLow(idDetailEmployeeProject: Int): Single<PutCheckDACResponseModel> {
        return service.putChecklistDacLowApi(idDetailEmployeeProject)
    }
}