package com.hkapps.hygienekleen.features.features_client.home.data.remote

import com.hkapps.hygienekleen.features.features_client.home.data.service.HomeClientService
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

class HomeClientRemoteDataSourceImpl @Inject constructor(private val service: HomeClientService) :
    HomeClientRemoteDataSource {

    override fun getProfileClient(userId: Int): Single<ProfileClientResponse> {
        return service.getProfileClient(userId)
    }

    override fun getInfoComplaintClient(
        projectId: String,
        userId: Int
    ): Single<InfoComplaintClientResponse> {
        return service.getInfoComplaintClient(projectId, userId)
    }

    override fun getCountChecklistClient(projectCode: String): Single<CountChecklistHomeClientResponse> {
        return service.getCountChecklistClient(projectCode)
    }

    override fun getProjectInfo(projectCode: String): Single<ProjectInfoClientResponse> {
        return service.getProjectInfo(projectCode)
    }

    override fun getCountNotifClient(clientId: Int, projectCode: String): Single<BadgeNotifClient> {
        return service.getCountNotifClient(clientId,projectCode)
    }

    override fun getDataAbsenceStaff(projectCode: String) : Single<ChartAbsenceStaffResponse> {
        return service.getDataAbsenceStaff(projectCode)
    }

    override fun getDataTimeSheet(projectCode: String): Single<TimeSheetResponse> {
        return service.getDataTimeSheet(projectCode)
    }

    override fun getDataListBAKMachine(projectCode: String,page : Int,perPage : Int): Single<BakMachineResponse> {
        return service.getDataListBAKMachine(projectCode,page,perPage)
    }

    override fun itemMr(
        projectCode: String,
        month: Int,
        year: Int,
        page: Int,
        size: Int
    ): Single<ItemMrResponse> {
        return service.getDataMR(projectCode, month, year, page, size)
    }



}