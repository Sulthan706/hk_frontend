package com.hkapps.hygienekleen.features.features_management.report.data.remote

import com.hkapps.hygienekleen.features.features_management.report.model.cardlistbranch.CardListBranchResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.closecomplaintcftalk.CloseComplaintCftalkResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.detailcftalk.DetailReportCftalkResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.listallprojecthigh.ListAllProjectHighResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.listallprojectlow.ListAllProjectLowResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.listprojectforbranch.ListProjectForBranchReponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.mainreportcftalk.ReportMainCftalkResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.mainreportctalk.ReportMainCtalkResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.recaptotaldaily.RecapTotalComplaintResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.searchproject.BotSheetSearchProjectResponseModel
import com.hkapps.hygienekleen.features.features_management.report.model.searchprojectlowlevel.BotSheetSearchLowReponseModel
import io.reactivex.Single

interface ReportManagementDataSource {

    fun getMainReportCftalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint:String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String,
    ): Single<ReportMainCftalkResponseModel>

    fun getMainReportCftalkManagementLow(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String,
    ): Single<ReportMainCftalkResponseModel>

    fun getSearchProject(
        page: Int,
        keywords: String
    ): Single<BotSheetSearchProjectResponseModel>

    fun getRecapTotalDailyMgmnt(
        projectCode: String,
        startDate: String,
        endDate: String,
    ): Single<RecapTotalComplaintResponseModel>

    fun getListAllProjectHigh(
        page: Int,
        size: Int
    ): Single<ListAllProjectHighResponseModel>

    fun getListAllLowProjectLow(
        adminMasterId: Int,
        page: Int
    ): Single<ListAllProjectLowResponseModel>

    fun getSearchProjectLow(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<BotSheetSearchLowReponseModel>

    fun getDetailReportCftalk(
        complaintId:Int
    ): Single<DetailReportCftalkResponseModel>
    //ctalk
    fun getMainReportCtalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint:String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCtalkResponseModel>

    fun getMainReportCtalkManagementLow(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint:String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCtalkResponseModel>

    fun getCardListBranch(
        date: String,
        filterBy: String
    ): Single<CardListBranchResponseModel>

    fun getListReportProject(
        branchCode: String,
        date: String,
        page: Int
    ): Single<ListProjectForBranchReponseModel>

    fun putCloseComplaintCftalk(
        complaintId: Int,
    ): Single<CloseComplaintCftalkResponseModel>
}