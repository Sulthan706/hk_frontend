package com.hkapps.hygienekleen.features.features_management.homescreen.home.data.service

import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.CheckProfileManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.UpdateProfilemanagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.changepassmanagement.ChangePassManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.changepassmanagement.ChangePassMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.changevaccinemanagement.ChangeVaccineMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.checknews.GetCheckNewsResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.detailnewsmanagement.DetailNewsManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.editnumberaccount.UpdateNumbAccountMngntResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.editnumberfamily.UpdateNumbFamManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.featureAvailability.AttendanceFeatureAvailabilityModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.getlistdocument.ListDocumentManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.latlongmanagement.LatLongManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listnewsmanagement.ListNewsManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.listvaccinemanagement.ListVaccineManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.mainreport.MainReportResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.profileManagement.LastUpdateManagementProfileResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.profilemain.GetProfileManagementReponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.progressStatusPermission.ProgressPermissionManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectActivity.ProjectAttendanceActivityResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.projectCode.ProjectCodeManagementNewResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.readnewsmanagement.ReadNewsManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.todayLastAttendance.TodayLastAttendanceResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.typevaccine.ListTypeVaccineMngmtResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.updatebpjstkkes.UpdateBpjsTkKesResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.uploaddocument.UploadDocumentMngmtResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.model.uploadvaccine.UploadVaccineMngmtResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface HomeManagementService {

    @GET("api/v1/management/project/v2/{id}")
    fun getProjectCode(
        @Path("id") userId: Int
    ): Single<ProjectCodeManagementNewResponse>

    @PATCH("/api/v1/management/patch/last-updated/{adminMasterId}")
    fun updateLastManagementProfile(@Path("adminMasterId") positionId: Int): Single<LastUpdateManagementProfileResponse>

    @GET("/api/v1/management/profile/check/renewal")
    fun checkDataRenewalManagement(@Query("adminId") userId : Int) : Single<LastUpdateManagementProfileResponse>

    @GET("/api/v1/management/{adminMasterId}")
    fun getProfileManagement(
        @Path("adminMasterId") adminMasterId: Int
    ): Single<GetProfileManagementReponseModel>

    @PUT("/api/v1/management/profile/update")
    fun updateProfileManagementAPI(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("adminMasterEmail") adminMasterEmail: String,
        @Query("adminMasterPhone") adminMasterPhone: String,
        @Query("adminMasterPhone2") adminMasterPhone2: String
    ): Single<UpdateProfilemanagementResponse>

    @GET("/api/v1/employee/management/attendance/activities")
    fun getAttendanceActivity(
        @Query("adminMasterId") userId: Int
    ): Single<ProjectAttendanceActivityResponse>

    @Multipart
    @PUT("/api/v1/management/profile/update/image")
    fun updateProfilePicture(
        @Query("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part
    ): Single<UpdateProfilemanagementResponse>

    @PUT("/api/v1/management/profile/update/v2")
    fun updateProfileFmGmOm(
        @Query("adminId") adminId: Int,
        @Query("adminEmail") adminEmail: String,
        @Query("adminPhone") adminPhone: String,
        @Query("adminPhone2") adminPhone2: String,
        @Query("adminAddress") adminAddress: String,
        @Query("adminBirthDate") adminBirthDate: String,
        @Query("adminPlaceOfBirth") adminPlaceOfBirth: String,
        @Query("adminGender") adminGender: String,
        @Query("adminMarriageStatus") adminMarriageStatus: String,
        @Query("adminMotherName") adminMotherName: String,
        @Query("religion") religion: String,
        @Query("adminAddressKtp") adminAddressKtp: String,
        @Query("adminMasterCountChildren") adminMasterCountChildren: String,
        @Query("adminMasterNik") adminMasterNik: String
        ): Single<UpdateProfilemanagementResponse>

    @GET("/api/v1/management/profile/check")
    fun getCheckProfile(
        @Query("adminId") adminId: Int
    ): Single<CheckProfileManagementResponse>

    @GET("/api/v2/permission/management/count-status")
    fun getCountProgressPermission(
        @Query("adminMasterId") userId: Int
    ): Single<ProgressPermissionManagementResponse>

    @GET("/api/v1/inspection/main-screen")
    fun getTodayLastAttendance(
        @Query("adminMasterId") userId: Int,
        @Query("date") date: String
    ): Single<TodayLastAttendanceResponse>

    //edit number family management
    @PUT("/api/v1/management/profile/update-kk")
    fun putNumberFamilyManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("kkNumber") kkNumber: String
    ): Single<UpdateNumbFamManagementResponseModel>
    //edit number account management
    @PUT("/api/v1/management/profile/update-banking")
    fun putNumberAccountManagement(
        @Query("adminMasterId") adminMasterId: Int,
        @Query("accountName") accountName: String,
        @Query("accountNumber") accountNumber: String
    ): Single<UpdateNumbAccountMngntResponseModel>
    //list document management
    @GET("/api/v1/management/profile/list-document")
    fun getListDocumentManagement(
        @Query("adminMasterId") adminMasterId: Int
    ): Single<ListDocumentManagementResponseModel>
    //list vaccine management
    @GET("/api/v1/management/profile/list-vaccine")
    fun getListVaccineManagement(
        @Query("adminMasterId") adminMasterId:Int,
        @Query("page") page:Int
    ): Single<ListVaccineManagementResponseModel>
    //upload document management
    @Multipart
    @PUT("/api/v1/management/profile/update-document")
    fun putUploadDocumentMngmt(
        @Query("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part,
        @Query("typeDocument") typeDocument: String
    ):Single<UploadDocumentMngmtResponseModel>
    //upload vaccine management
    @Multipart
    @PUT("/api/v1/management/profile/update-vaccine")
    fun putUploadVaccineMngmt(
        @Query("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part,
        @Query("typeVaccine") typeVaccine: String
    ):Single<UploadVaccineMngmtResponseModel>

    //type vaccine
    @GET("/api/v1/management/profile/list-vaccine-type")
    fun getListTypeVaccineMngmt(
        @Query("adminMasterId") adminMasterId:Int
    ):Single<ListTypeVaccineMngmtResponseModel>

    //change vaccine
    @Multipart
    @PUT("/api/v1/management/profile/change-vaccine")
    fun putChangeVaccineMngmt(
        @Query("idVaccine") idVaccine: Int,
        @Part file: MultipartBody.Part
    ): Single<ChangeVaccineMgmntResponseModel>
    //main report
    @GET("/api/v1/report/main-report/high-level")
    fun getMainReportHigh(

    ): Single<MainReportResponseModel>

    @GET("/api/v1/report/main-report/low-level")
    fun getMainReportLow(
        @Query("adminMasterId") adminMasterId: Int
    ): Single<MainReportResponseModel>

    //update bpjs
    @Multipart
    @PUT("/api/v1/management/profile/update-bpjs")
    fun putBpjsMgmnt(
        @Query("adminMasterId") adminMasterId: Int,
        @Part file: MultipartBody.Part,
        @Query("number") number: String,
        @Query("typeBpjs") typeBpjs: String
    ): Single<UpdateBpjsTkKesResponseModel>

    //put lat long management
    @PUT("api/v1/lokasi/management")
    fun putLatLongManagement(
        @Query("managementId") managementId: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("address") address: String
    ): Single<LatLongManagementResponseModel>

    //news
    @GET("api/v1/news/list-news")
    fun getListNewsManagement(
        @Query("page") page: Int,
        @Query("userId") userId: Int,
        @Query("userType") userType: String
    ): Single<ListNewsManagementResponseModel>

    @GET("api/v1/news/detail")
    fun getDetailNewsManagement(
        @Query("idNews") idNews: Int
    ): Single<DetailNewsManagementResponseModel>

    @GET("api/v1/news/check-news")
    fun getCheckNewsManagement(
        @Query("userType") userType: String,
        @Query("userId") userId: Int
    ) : Single<GetCheckNewsResponseModel>

    @PUT("api/v1/news/read-news")
    fun putReadNewsManagement(
        @Query("userType") userType: String,
        @Query("userId") userId: Int,
        @Query("newsId") newsId: Int
    ): Single<ReadNewsManagementResponseModel>

    @GET("api/v2/employee/management/get/feature-availability")
    fun getAttendanceFeature(
        @Query("adminMasterId") adminMasterId: Int
    ): Single<AttendanceFeatureAvailabilityModel>

    @PUT("api/v1/management/change-password")
    fun putChangePassword(
        @Query("adminMasterId") adminMasterId: Int,
        @Body changePassMgmnt: ChangePassMgmntResponseModel
    ): Single<ChangePassManagementResponseModel>

    @GET("api/v1/materialrequest/home/list")
    fun getDataMR(
        @Query("projectCode") projectCode : String,
        @Query("month") month : Int,
        @Query("year") year : Int,
        @Query("page") page : Int,
        @Query("perPage") size : Int
    ):Single<ItemMrResponse>

}