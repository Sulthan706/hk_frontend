package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import io.reactivex.Single

interface ScheduleRepository {
    fun getSchedule(employeeId: Int, date: String, page: Int): Single<ScheduleResponseModel>
    fun getMidSchedule(employeeId: Int, date: String, page: Int): Single<MidScheduleResponseModel>
}