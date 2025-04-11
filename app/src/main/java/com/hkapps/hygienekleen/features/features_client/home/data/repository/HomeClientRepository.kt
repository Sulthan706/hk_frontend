package com.hkapps.hygienekleen.features.features_client.home.data.repository

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
import retrofit2.http.Query

interface HomeClientRepository {

    fun getProfileClient(userId: Int): Single<ProfileClientResponse>

    fun getInfoComplaintClient(
        projectId: String,
        userId: Int
    ): Single<InfoComplaintClientResponse>

    fun getCountChecklistClient(
        projectCode: String
    ): Single<CountChecklistHomeClientResponse>

    fun getProjectInfo(
        projectCode: String
    ): Single<ProjectInfoClientResponse>
    //badgenotifclient
    fun getCountNotifClient(
        @Query("clientId") clientId: Int,
        @Query("projectCode") projectCode: String
    ): Single<BadgeNotifClient>

    fun getDataAbsenceStaff(projectCode : String) : Single<ChartAbsenceStaffResponse>

    fun getDataTimeSheet(projectCode : String) : Single<TimeSheetResponse>

    fun getDataListBAKMachine(
        projectCode : String,
        page : Int,
        perPage : Int
    ): Single<BakMachineResponse>

    fun itemMr(
        projectCode : String,
        month : Int,
        year : Int,
        page : Int,
        size : Int
    ):Single<ItemMrResponse>


}