package com.hkapps.hygienekleen.features.features_client.home.data.repository

import com.hkapps.hygienekleen.features.features_client.home.data.remote.HomeClientRemoteDataSource
import com.hkapps.hygienekleen.features.features_client.home.model.ProfileClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.badgenotif.BadgeNotifClient
import com.hkapps.hygienekleen.features.features_client.home.model.countChecklist.CountChecklistHomeClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.countComplaint.InfoComplaintClientResponse
import com.hkapps.hygienekleen.features.features_client.home.model.projectDashboard.ProjectInfoClientResponse
import com.hkapps.hygienekleen.features.features_vendor.damagereport.model.listbakmesin.BakMachineResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.grafik.model.absence.ChartAbsenceStaffResponse
import com.hkapps.hygienekleen.features.grafik.model.timesheet.TimeSheetResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeClientRepositoryImpl @Inject constructor(private val remoteDataSource: HomeClientRemoteDataSource) :
    HomeClientRepository {

    override fun getProfileClient(userId: Int): Single<ProfileClientResponse> {
        return remoteDataSource.getProfileClient(userId)
    }

    override fun getInfoComplaintClient(
        projectId: String,
        userId: Int
    ): Single<InfoComplaintClientResponse> {
        return remoteDataSource.getInfoComplaintClient(projectId, userId)
    }

    override fun getCountChecklistClient(projectCode: String): Single<CountChecklistHomeClientResponse> {
        return remoteDataSource.getCountChecklistClient(projectCode)
    }

    override fun getProjectInfo(projectCode: String): Single<ProjectInfoClientResponse> {
        return remoteDataSource.getProjectInfo(projectCode)
    }

    override fun getCountNotifClient(clientId: Int, projectCode: String): Single<BadgeNotifClient> {
        return remoteDataSource.getCountNotifClient(clientId, projectCode)
    }

    override fun getDataAbsenceStaff(projectCode: String): Single<ChartAbsenceStaffResponse> {
        return remoteDataSource.getDataAbsenceStaff(projectCode)
    }

    override fun getDataTimeSheet(projectCode: String): Single<TimeSheetResponse> {
        return remoteDataSource.getDataTimeSheet(projectCode)
    }

    override fun getDataListBAKMachine(projectCode: String,page :Int,perPage : Int): Single<BakMachineResponse> {
        return remoteDataSource.getDataListBAKMachine(projectCode,page,perPage)
    }

    override fun itemMr(
        projectCode: String,
        month: Int,
        year: Int,
        page: Int,
        size: Int
    ): Single<ItemMrResponse> {
        return remoteDataSource.itemMr(projectCode, month, year, page, size)
    }

}