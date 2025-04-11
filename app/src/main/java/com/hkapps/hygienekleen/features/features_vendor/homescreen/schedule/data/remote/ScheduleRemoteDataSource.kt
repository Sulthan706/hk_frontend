package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.remote

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import io.reactivex.Single

interface ScheduleRemoteDataSource {
    fun getScheduleData(employeeId: Int, date: String, page: Int): Single<ScheduleResponseModel>
    fun getMidScheduleData(employeeId: Int, date: String, page: Int): Single<MidScheduleResponseModel>
}