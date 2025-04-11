package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.service

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.StatusAttendanceTimeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.badgenotif.badgeNotifResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.checknews.CheckNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.countChecklist.ChecklistHomeVendorResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.countProgressBar.CountPermissionMidResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DacCountModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DailyActHomeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.detailnews.DetailNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.editProfileCheck.CheckProfileEmployeeResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.greetinglate3days.GreetingLateResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listProject.ListProjectModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listdocument.ListDocumentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews.ListHomeNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.ListVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.newVersion.NewVersionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile.ProfileModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.readnews.ReadNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.StatusAbsenResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.statusAbsen.new_.StatusAbsenResponseModelNew
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updateAccountNumber.UpdateAccountNumbReponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updateFamsNumber.UpdateFamsNumberResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.changevaccine.UpdateVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.complaintValidate.ComplaintValidateResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.checknotattendance.CheckNotAttendanceResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dropdowntypevaccine.ListVaccineTypeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.editVaccine.ChangeVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.getTimeShift.GetTimeShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.latlongarea.LatLongAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listSlipGaji.ListSlipGajiResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile.LastUpdateProfileResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.reportAttendance.ReportAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updatebpjs.UpdateBpjsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.uploaddocument.UploadDocumentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.versionCek.VersionCheckResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    @GET("api/v1/employee/profile")
    fun getProfileEmployee(@Query("employeeId") userId: Int): Single<ProfileModel>

    @PATCH("/api/v1/employee/patch/last-updated/{employeeId}")
    fun updateLastProfile(@Path("employeeId") userId : Int) : Single<LastUpdateProfileResponse>

    @GET("api/v1/employee/profile/check/renewal")
    fun checkDataRenewal(@Query("employeeId")userId : Int) : Single<LastUpdateProfileResponse>

    @GET("api/v1/employee/projects")
    fun getListProject(@Query("employeeId") userId: Int): Single<ListProjectModel>

    @GET("api/v1/employee/attendance/status")
    fun getStatusAbsen(
        @Query("employeeId") userId: Int,
        @Query("projectCode") projectCode: String
    ): Single<StatusAbsenResponseModel>

    @GET("/api/v1/employee/attendance/geo/status")
    fun getAttendanceStatusOperatorApi(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String,
    ): Single<StatusAbsenResponseModelNew>

    @GET("api/v1/employee/attendance/non-operator/geo/status")
    fun getStatusAbsenMid(
        @Query("employeeId") userId: Int,
        @Query("projectCode") projectCode: String
    ): Single<StatusAbsenResponseModel>

    @GET("api/v1/employee/schedule/today")
    fun getDailyActHome(
        @Query("employeeId") userId: Int,
        @Query("projectId") projectId: String
    ): Single<DailyActHomeResponseModel>

    @GET("api/v1/employee/attendance")
    fun getTimeInOut(
        @Query("employeeId") userId: Int,
        @Query("projectCode") projectCode: String
    ): Single<StatusAttendanceTimeResponseModel>

    @GET("api/v1/employee/dac/info/count")
    fun getDacCount(
        @Query("employeeId") userId: Int,
        @Query("projectId") projectCode: String
    ): Single<DacCountModel>

    @GET("api/v1/checklist/project/count/info/{projectCode}")
    fun getChecklistCount(
        @Path("projectCode") projectCode: String
    ): Single<ChecklistHomeVendorResponse>

    @POST("api/v1/version/check-version")
    fun checkVersion(
        @Query("versionApp") versionApp: String
    ): Single<VersionCheckResponse>

    @GET("api/v1/version/check-newest-version")
    fun getNewVersionApp(): Single<NewVersionResponse>

    @GET("api/v1/employee/profile/check")
    fun getCheckProfile(
        @Query("employeeId") employeeId: Int
    ): Single<CheckProfileEmployeeResponse>

    //badgeNotif
    @GET("api/v1/notification/history/count")
    fun getBadgeNotification(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ):Single<badgeNotifResponseModel>

    @GET("api/v1/notification/history/count-operator")
    fun getBadgeNotificationOperator(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ):Single<badgeNotifResponseModel>

    @GET("api/v2/permission/count-status")
    fun getCountPermissionMid(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ): Single<CountPermissionMidResponse>
    //update nomer kk
    @PUT("api/v1/employee/profile/update-kk")
    fun PutNumberFamily(
        @Query("employeeId") employeeId: Int,
        @Query("kkNumber") kkNumber: String
    ): Single<UpdateFamsNumberResponseModel>
    //update bank account
    @PUT("api/v1/employee/profile/update-banking")
    fun PutAccountEmployee(
        @Query("employeeId") employeeId: Int,
        @Query("accountName") accountName: String,
        @Query("accountNumber") accountNumber: String
    ): Single<UpdateAccountNumbReponseModel>
    //greeting not absent 3 day
    @GET("api/v1/employee/check-attendance")
    fun GetGreetingLate(
        @Query("employeeId") employeeId: Int,
        @Query("projectCode") projectCode: String
    ):Single<GreetingLateResponseModel>
    //checknews
    @GET("api/v1/news/check-news")
    fun getCheckNews(
        @Query("userType") userType:String,
        @Query("userId") userId: Int
    ): Single<CheckNewsResponseModel>
    //list home news
    @GET("api/v1/news/list-news")
    fun getListHomeNews(
        @Query("page") page: Int,
        @Query("userId") userId: Int,
        @Query("userType") userType: String
    ):Single<ListHomeNewsResponseModel>
    //read news
    @PUT("api/v1/news/read-news")
    fun putReadNews(
        @Query("userType") userType: String,
        @Query("userId") userId: Int,
        @Query("newsId") newsId: Int
    ): Single<ReadNewsResponseModel>
    //get detail news
    @GET("api/v1/news/detail")
    fun getDetailNews(
        @Query("idNews") idNews:Int
    ): Single<DetailNewsResponseModel>
    //upload dokumen profile

    @Multipart
    @PUT("api/v1/employee/profile/update-document")
    fun uploadDocument(
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part,
        @Query("typeDocument") typeDocument: String
    ) :Single<UploadDocumentResponseModel>
    //get list document
    @GET("api/v1/employee/profile/list-document")
    fun getListDocument(
        @Query("employeeId") employeeId: Int
    ): Single<ListDocumentResponseModel>

    //getList Vaksin
    @GET("api/v1/employee/profile/list-vaccine")
    fun getListVaccine(
        @Query("employeeId") employeeId: Int,
        @Query("page") page: Int
    ): Single<ListVaccineResponseModel>

    //update vaccine
    @Multipart
    @PUT("api/v1/employee/profile/update-vaccine")
    fun uploadVaccine(
        @Query("employeeId") employeeId: Int,
        @Part file: MultipartBody.Part,
        @Query("typeVaccine") typeVaccine: String
    ): Single<UpdateVaccineResponseModel>

    //change vaccine
    @Multipart
    @PUT("api/v1/employee/profile/change-vaccine")
    fun changeVaccine(
        @Query("idVaccine") idVaccine: Int,
        @Part file: MultipartBody.Part
    ): Single<ChangeVaccineResponseModel>

    //list type vaccine
    @GET("api/v1/employee/profile/list-vaccine-type")
    fun listTypeVaccine(
        @Query("employeeId") employeeId: Int
    ):Single<ListVaccineTypeResponseModel>

    @Multipart
    @PUT("api/v1/employee/profile/update-bpjs")
    fun putUploadBpjs(
        @Query("employeeId") employeeId: Int,
        @Part file : MultipartBody.Part,
        @Query("number") number: String,
        @Query("typeBpjs") typeBpjs: String
    ):Single<UpdateBpjsResponseModel>
    //get latlong home
    @PUT("api/v1/lokasi/area")
    fun putLatLongArea(
        @Query("employeeId") employeeId: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("address") address: String
    ): Single<LatLongAreaResponseModel>

    @GET("api/v1/attendance/employee/report-attendance")
    fun getReportAttendance(
        @Query("employeeId") employeeId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<ReportAttendanceResponse>

    @GET("api/v1/complaint/check-red-complaint")
    fun getComplaintValidate(
        @Query("projectCode") projectCode: String,
        @Query("complaintType") complaintType: ArrayList<String>
    ): Single<ComplaintValidateResponse>
  
    @GET("api/v1/attendance/employee/check-absence")
    fun getCheckAttendance3times(
        @Query("employeeId") employeeId: Int
    ): Single<CheckNotAttendanceResponseModel>

    @GET("api/v1/employee/schedule/today")
    fun getTimeShiftEmployee(
        @Query("employeeId") employeeId: Int,
        @Query("projectId") projectId: String
    ): Single<GetTimeShiftResponseModel>

    @GET("api/v1/ops/payroll/get/paychecks")
    fun getListSlipGaji(
        @Query("employeeId") userId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Single<ListSlipGajiResponse>

    @GET("api/v1/materialrequest/home/list")
    fun getDataMR(
        @Query("projectCode") projectCode : String,
        @Query("month") month : Int,
        @Query("year") year : Int,
        @Query("page") page : Int,
        @Query("perPage") size : Int
    ):Single<ItemMrResponse>

}