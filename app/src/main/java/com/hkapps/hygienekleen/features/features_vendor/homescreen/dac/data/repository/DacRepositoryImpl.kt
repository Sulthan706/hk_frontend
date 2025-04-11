package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.remote.DacRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.CheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.CheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.DACCheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.PutCheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single
import javax.inject.Inject

class DacRepositoryImpl @Inject constructor(private val remoteDataSource: DacRemoteDataSource) :
    DacRepository {

    override fun getDailyAct(employeeId: Int, projectCode: String): Single<DailyActResponseModel> {
        return remoteDataSource.getDailyActRDS(employeeId, projectCode)
    }

    override fun getDailyNewAct(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int
    ): Single<DailyActNewResponseModel> {
        return remoteDataSource.getDailyActNewRDS(employeeId, projectCode, idDetailProject)
    }

    override fun getCountAct(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int
    ): Single<DACCheckResponseModel> {
        return remoteDataSource.getCountAct(employeeId, projectCode, idDetailProject)
    }

    override fun getChecklistDacLowLevel(
        employeeId: Int,
        projectCode: String,
        idDetailProject: Int,
        activityId: Int,
        plottingId: Int
    ): Single<CheckDACResponseModel> {
        return remoteDataSource.getChecklistDacLowLevel(employeeId, projectCode, idDetailProject, activityId, plottingId)
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
        return remoteDataSource.postChecklistDacLow(
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
        return remoteDataSource.putChecklistDacLow(
            idDetailEmployeeProject
        )
    }
}