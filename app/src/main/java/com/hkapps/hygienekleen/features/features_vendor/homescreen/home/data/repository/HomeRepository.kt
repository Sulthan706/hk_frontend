package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository

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

interface HomeRepository {

    fun getProfileEmployee(userId: Int): Single<ProfileModel>

    fun updateLastProfile(userId : Int) : Single<LastUpdateProfileResponse>

    fun checkRenewal(userId : Int): Single<LastUpdateProfileResponse>

    fun getListProject(userId: Int): Single<ListProjectModel>

    fun getStatusAbsen(userId: Int, projectCode: String): Single<StatusAbsenResponseModelNew>

    fun getStatusAbsenMid(userId: Int, projectCode: String): Single<StatusAbsenResponseModel>

    fun getTimeInOut(userId: Int, projectCode: String): Single<StatusAttendanceTimeResponseModel>

    fun getDailyActHome(employeeId: Int, projectCode: String): Single<DailyActHomeResponseModel>

    fun getDACCount(userId: Int, projectCode: String): Single<DacCountModel>

    fun getChecklistCount(
        projectCode: String
    ): Single<ChecklistHomeVendorResponse>

    fun checkVersion(
        versionApp: String
    ): Single<VersionCheckResponse>

    fun getNewVersionApp(): Single<NewVersionResponse>

    fun getCheckProfile(
        employeeId: Int
    ): Single<CheckProfileEmployeeResponse>

    fun getBadgeNotification(
        employeeId: Int,
        projectCode: String
    ): Single<badgeNotifResponseModel>

    fun getBadgeNotificationOperator(
        employeeId: Int,
        projectCode: String
    ): Single<badgeNotifResponseModel>

    fun getCountPermissionMid(
        employeeId: Int,
        projectCode: String
    ): Single<CountPermissionMidResponse>

    fun PutNumberFamily(
        employeeId: Int,
        kkNumber: String
    ): Single<UpdateFamsNumberResponseModel>

    fun PutAccountEmployee(
        employeeId: Int,
        accountName: String,
        accountNumber: String
    ): Single<UpdateAccountNumbReponseModel>

    fun GetGreetingLate(
        employeeId: Int,
        projectCode: String
    ): Single<GreetingLateResponseModel>

    fun getCheckNews(
        userType: String,
        userId: Int
    ): Single<CheckNewsResponseModel>

    fun getListHomeNews(
        page: Int,
        userId: Int,
        userType: String
    ): Single<ListHomeNewsResponseModel>

    fun putReadNews(
        userType: String,
        userId: Int,
        newsId: Int
    ): Single<ReadNewsResponseModel>

    fun getDetailNews(
        idNews: Int
    ): Single<DetailNewsResponseModel>

    fun uploadDocument(
        employeeId: Int,
        imageDocument: MultipartBody.Part,
        typeDocument: String
    ): Single<UploadDocumentResponseModel>

    fun getListDocument(
        employeeId: Int
    ): Single<ListDocumentResponseModel>

    fun getListVaccine(
        employeeId: Int,
        page: Int
    ): Single<ListVaccineResponseModel>

    fun uploadVaccine(
        employeeId: Int,
        file: MultipartBody.Part,
        typeVaccine: String
    ): Single<UpdateVaccineResponseModel>

    fun changeVaccine(
        idVaccine: Int,
        file: MultipartBody.Part
    ): Single<ChangeVaccineResponseModel>

    fun getListTypeVaccine(
        employeeId: Int
    ):Single<ListVaccineTypeResponseModel>

    fun putUploadBpjs(
        employeeId: Int,
        file : MultipartBody.Part,
        number: String,
        typeBpjs: String
    ):Single<UpdateBpjsResponseModel>

    fun putLatLongArea(
        employeeId: Int,
        latitude: String,
        longitude: String,
        address: String
    ): Single<LatLongAreaResponseModel>

    fun getReportAttendance(
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<ReportAttendanceResponse>

    fun getComplaintValidate(
        projectCode: String,
        complaintType: ArrayList<String>
    ): Single<ComplaintValidateResponse>
  
    fun getCheckAttendance3times(
        employeeId: Int
    ): Single<CheckNotAttendanceResponseModel>

    fun getTimeShiftEmployee(
        employeeId: Int,
        projectId: String
    ):Single<GetTimeShiftResponseModel>

    fun getListSlipGaji(
        userId: Int,
        month: Int,
        year: Int
    ): Single<ListSlipGajiResponse>

    fun itemMr(
        projectCode : String,
        month : Int,
        year : Int,
        page : Int,
        size : Int
    ):Single<ItemMrResponse>

}