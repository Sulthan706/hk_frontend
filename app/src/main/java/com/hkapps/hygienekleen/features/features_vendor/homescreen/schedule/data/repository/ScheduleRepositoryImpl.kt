package com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.remote.ScheduleRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.lowlevel.ScheduleResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.model.midlevel.MidScheduleResponseModel
import io.reactivex.Single
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(private val remoteDataSource: ScheduleRemoteDataSource) :
    ScheduleRepository {

    override fun getSchedule(employeeId: Int, date: String, page: Int): Single<ScheduleResponseModel> {
        return remoteDataSource.getScheduleData(employeeId, date, page)
    }

    override fun getMidSchedule(
        employeeId: Int,
        date: String,
        page: Int
    ): Single<MidScheduleResponseModel> {
        return remoteDataSource.getMidScheduleData(employeeId, date, page)
    }
}