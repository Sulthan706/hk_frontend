package com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.remote
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.DailyActNewResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.CheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.CheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.DACCheckResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.new_.checklist.check.PutCheckDACResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.model.old.DailyActResponseModel
import io.reactivex.Single

interface DacRemoteDataSource {
    fun getDailyActRDS(employeeId: Int, projectCode: String): Single<DailyActResponseModel>
    fun getDailyActNewRDS(employeeId: Int, projectCode: String, idDetailProject: Int): Single<DailyActNewResponseModel>
    fun getCountAct(employeeId: Int, projectCode: String, idDetailProject: Int): Single<DACCheckResponseModel>
    fun getChecklistDacLowLevel(employeeId: Int, projectCode: String, idDetailProject: Int, activityId: Int, plottingId: Int): Single<CheckDACResponseModel>
    fun postChecklistDacLow(employeeId: Int, idDetailEmployeeProject: Int, projectId: String, plottingId: Int, shiftId: Int, locationId: Int, subLocationId: Int, activityId: Int): Single<CheckResponseModel>
    fun putChecklistDacLow(idDetailEmployeeProject: Int): Single<PutCheckDACResponseModel>
}