package com.hkapps.hygienekleen.features.features_management.report.data.repository

import com.hkapps.hygienekleen.features.features_management.report.data.remote.ReportManagementDataSource
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
import javax.inject.Inject

class ReportManagementRepositoryImpl @Inject constructor(private val dataSource: ReportManagementDataSource) :
    ReportManagementRepository {

    override fun getMainReportCftalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCftalkResponseModel> {
        return dataSource.getMainReportCftalkManagement(page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy)
    }

    override fun getMainReportCftalkManagementLow(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCftalkResponseModel> {
        return dataSource.getMainReportCftalkManagementLow(adminMasterId, page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy)
    }

    override fun getSearchProject(
        page: Int,
        keywords: String
    ): Single<BotSheetSearchProjectResponseModel> {
        return dataSource.getSearchProject(page, keywords)
    }

    override fun getRecapTotalDailyMgmnt(
        projectCode: String,
        startDate: String,
        endDate: String
    ): Single<RecapTotalComplaintResponseModel> {
        return dataSource.getRecapTotalDailyMgmnt(projectCode, startDate, endDate)
    }

    override fun getListAllProjectHigh(page: Int, size: Int): Single<ListAllProjectHighResponseModel> {
        return dataSource.getListAllProjectHigh(page, size)
    }

    override fun getListAllLowProjectLow(
        adminMasterId: Int,
        page: Int
    ): Single<ListAllProjectLowResponseModel> {
        return dataSource.getListAllLowProjectLow(adminMasterId, page)
    }

    override fun getSearchProjectLow(
        adminMasterId: Int,
        page: Int,
        keywords: String
    ): Single<BotSheetSearchLowReponseModel> {
        return dataSource.getSearchProjectLow(adminMasterId, page, keywords)
    }

    override fun getDetailReportCftalk(complaintId: Int): Single<DetailReportCftalkResponseModel> {
        return dataSource.getDetailReportCftalk(complaintId)
    }

    override fun getMainReportCtalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCtalkResponseModel> {
        return dataSource.getMainReportCtalkManagement(page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy)
    }

    override fun getMainReportCtalkManagementLow(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ): Single<ReportMainCtalkResponseModel> {
        return dataSource.getMainReportCtalkManagementLow(adminMasterId, page, projectCode, statusComplaint, listIdTitle, startDate, endDate, filterBy)
    }

    override fun getCardListBranch(
        date: String,
        filterBy: String
    ): Single<CardListBranchResponseModel> {
        return dataSource.getCardListBranch(date, filterBy)
    }


    override fun getListReportProject(
        branchCode: String,
        date: String,
        page: Int
    ): Single<ListProjectForBranchReponseModel> {
        return dataSource.getListReportProject(branchCode, date, page)
    }

    override fun putCloseComplaintCftalk(complaintId: Int): Single<CloseComplaintCftalkResponseModel> {
        return dataSource.putCloseComplaintCftalk(complaintId)
    }


}