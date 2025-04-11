package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.service.ScheduleService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ScheduleRemoteDataSourceImpl @Inject constructor(private val service: ScheduleService):
    ScheduleRemoteDataSource {
    override fun getScheduleData(employeeId: Int, date: String, page: Int): Single<ScheduleResponseModel> {
        return service.getScheduleService(employeeId,date, page)
    }

    override fun getMidScheduleData(
        employeeId: Int,
        date: String,
        page: Int
    ): Single<MidScheduleResponseModel> {
        return service.getMidScheduleService(employeeId,date, page)
    }
}