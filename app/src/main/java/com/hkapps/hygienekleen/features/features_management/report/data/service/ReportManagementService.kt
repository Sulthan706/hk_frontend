package com.hkapps.hygienekleen.features.features_management.report.data.service

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
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportManagementService {


    //main report cftalk
    @GET("/api/v1/report/cftalk/list")
    fun getMainReportCftalkManagement(
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("statusComplaint") statusComplaint:String,
        @Query("listIdTitle") listIdTitle: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
    ): Single<ReportMainCftalkResponseModel>
    //main report cftalk low
    @GET("/api/v1/report/cftalk/list/low-level")
    fun getMainReportCftalkManagementLow(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("statusComplaint") statusComplaint:String,
        @Query("listIdTitle") listIdTitle: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
    ): Single<ReportMainCftalkResponseModel>
    //bot sheet search project
    @GET("/api/v1/project/search/all-new")
    fun getSearchProject(
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<BotSheetSearchProjectResponseModel>
    //recap total complaint daily
    @GET("/api/v1/report/cftalk/recap")
    fun getRecapTotalDailyMgmnt(
        @Query("projectCode") projectCode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): Single<RecapTotalComplaintResponseModel>

    //list all project high level
    @GET("/api/v1/project")
    fun getListAllProjectHigh(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ListAllProjectHighResponseModel>

    //list all project low level
    @GET("/api/v1/project/management/{adminMasterId}")
    fun getListAllLowProjectLow(
        @Path("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int
    ): Single<ListAllProjectLowResponseModel>

    //search low level
    @GET("/api/v1/project/search/own/new/{adminMasterId}")
    fun getSearchProjectLow(
        @Path("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("keywords") keywords: String
    ): Single<BotSheetSearchLowReponseModel>

    @GET("/api/v1/complaint/{complaintId}")
    fun getDetailReportCftalk(
        @Path("complaintId") complaintId:Int
    ): Single<DetailReportCftalkResponseModel>

    //get api main report ctalk
    @GET("/api/v1/report/ctalk/list")
    fun getMainReportCtalkManagement(
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("statusComplaint") statusComplaint:String,
        @Query("listIdTitle") listIdTitle: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
    ): Single<ReportMainCtalkResponseModel>
    //get api main report ctalk low
    @GET("/api/v1/report/ctalk/list/low-level")
    fun getMainReportCtalkManagementLow(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("page") page: Int,
        @Query("projectCode") projectCode: String,
        @Query("statusComplaint") statusComplaint:String,
        @Query("listIdTitle") listIdTitle: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("filterBy") filterBy: String,
    ): Single<ReportMainCtalkResponseModel>
    //get list branch
    @GET("/api/v1/report/branch/list/{date}/{filterBy}")
    fun getCardListBranch(
        @Path("date") date: String,
        @Path("filterBy") filterBy: String
    ): Single<CardListBranchResponseModel>
    //list report for report
    @GET("/api/v1/report/project/list")
    fun getListReportProject(
        @Query("branchCode") branchCode: String,
        @Query("date") date: String,
        @Query("page") page: Int
    ): Single<ListProjectForBranchReponseModel>
    //close complaint cftalk
    @PUT("/api/v1/complaint/internal/close/{complaintId}")
    fun putCloseComplaintCftalk(
        @Path("complaintId") complaintId: Int,
    ): Single<CloseComplaintCftalkResponseModel>
}