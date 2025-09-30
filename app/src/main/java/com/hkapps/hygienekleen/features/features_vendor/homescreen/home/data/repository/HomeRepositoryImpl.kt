package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.StatusAttendanceTimeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.remote.HomeRemoteDataSource
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
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.CreateMRResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.CreateMaterialRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRDataResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ListHistoryStockResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ListHistoryUsedResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MRDashboardResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile.LastUpdateProfileResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.reportAttendance.ReportAttendanceResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updatebpjs.UpdateBpjsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.uploaddocument.UploadDocumentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.versionCek.VersionCheckResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val remoteDataSource: HomeRemoteDataSource) :
    HomeRepository {

    override fun getProfileEmployee(userId: Int): Single<ProfileModel> {
        return remoteDataSource.getProfileEmployee(userId)
    }

    override fun updateLastProfile(userId: Int): Single<LastUpdateProfileResponse> {
        return remoteDataSource.updateLastProfile(userId)
    }

    override fun checkRenewal(userId: Int): Single<LastUpdateProfileResponse> {
        return remoteDataSource.checkRenewal(userId)
    }

    override fun getListProject(userId: Int): Single<ListProjectModel> {
        return remoteDataSource.getListProject(userId)
    }

    override fun getStatusAbsen(
        userId: Int,
        projectCode: String
    ): Single<StatusAbsenResponseModelNew> {
        return remoteDataSource.getStatusAbsen(userId, projectCode)
    }

    override fun getStatusAbsenMid(
        userId: Int,
        projectCode: String
    ): Single<StatusAbsenResponseModel> {
        return remoteDataSource.getStatusAbsenMid(userId, projectCode)
    }

    override fun getTimeInOut(
        userId: Int,
        projectCode: String
    ): Single<StatusAttendanceTimeResponseModel> {
        return remoteDataSource.getTimeInOut(userId, projectCode)
    }

    override fun getDailyActHome(
        employeeId: Int,
        projectCode: String
    ): Single<DailyActHomeResponseModel> {
        return remoteDataSource.getDailyActHome(employeeId, projectCode)
    }

    override fun getDACCount(userId: Int, projectCode: String): Single<DacCountModel> {
        return remoteDataSource.getDacCount(userId, projectCode)
    }

    override fun getChecklistCount(projectCode: String): Single<ChecklistHomeVendorResponse> {
        return remoteDataSource.getChecklistCount(projectCode)
    }

    override fun checkVersion(versionApp: String): Single<VersionCheckResponse> {
        return remoteDataSource.checkVersion(versionApp)
    }

    override fun getNewVersionApp(): Single<NewVersionResponse> {
        return remoteDataSource.getNewVersionApp()
    }

    override fun getCheckProfile(
        employeeId: Int
    ): Single<CheckProfileEmployeeResponse> {
        return remoteDataSource.getCheckProfile(employeeId)
    }

    override fun getBadgeNotification(
        employeeId: Int,
        projectCode: String
    ): Single<badgeNotifResponseModel> {
        return remoteDataSource.getBadgeNotification(employeeId, projectCode)
    }

    override fun getBadgeNotificationOperator(
        employeeId: Int,
        projectCode: String
    ): Single<badgeNotifResponseModel> {
        return remoteDataSource.getBadgeNotificationOperator(employeeId, projectCode)
    }

    override fun getCountPermissionMid(
        employeeId: Int,
        projectCode: String
    ): Single<CountPermissionMidResponse> {
        return remoteDataSource.getCountPermissionMid(employeeId, projectCode)
    }

    override fun PutNumberFamily(
        employeeId: Int,
        kkNumber: String
    ): Single<UpdateFamsNumberResponseModel> {
        return remoteDataSource.PutNumberFamily(employeeId, kkNumber)
    }

    override fun PutAccountEmployee(
        employeeId: Int,
        accountName: String,
        accountNumber: String
    ): Single<UpdateAccountNumbReponseModel> {
        return remoteDataSource.PutAccountEmployee(employeeId, accountName, accountNumber)
    }

    override fun GetGreetingLate(
        employeeId: Int,
        projectCode: String
    ): Single<GreetingLateResponseModel> {
        return remoteDataSource.GetGreetingLate(employeeId, projectCode)
    }

    override fun getCheckNews(userType: String, userId: Int): Single<CheckNewsResponseModel> {
        return remoteDataSource.getCheckNews(userType, userId)
    }

    override fun getListHomeNews(
        page: Int,
        userId: Int,
        userType: String
    ): Single<ListHomeNewsResponseModel> {
        return remoteDataSource.getListHomeNews(page, userId, userType)
    }

    override fun putReadNews(
        userType: String,
        userId: Int,
        newsId: Int
    ): Single<ReadNewsResponseModel> {
        return remoteDataSource.putReadNews(userType, userId, newsId)
    }

    override fun getDetailNews(idNews: Int): Single<DetailNewsResponseModel> {
        return remoteDataSource.getDetailNews(idNews)
    }

    override fun uploadDocument(
        employeeId: Int,
        imageDocument: MultipartBody.Part,
        typeDocument: String
    ): Single<UploadDocumentResponseModel> {
        return remoteDataSource.uploadDocument(employeeId, imageDocument, typeDocument)
    }

    override fun getListDocument(
        employeeId: Int
    ): Single<ListDocumentResponseModel> {
        return remoteDataSource.getListDocument(employeeId)
    }

    override fun getListVaccine(employeeId: Int, page: Int): Single<ListVaccineResponseModel> {
        return remoteDataSource.getListVaccine(employeeId, page)
    }

    override fun uploadVaccine(
        employeeId: Int,
        file: MultipartBody.Part,
        typeVaccine: String
    ): Single<UpdateVaccineResponseModel> {
        return remoteDataSource.uploadVaccine(employeeId, file, typeVaccine)
    }

    override fun changeVaccine(
        idVaccine: Int,
        file: MultipartBody.Part
    ): Single<ChangeVaccineResponseModel> {
        return remoteDataSource.changeVaccine(idVaccine, file)
    }

    override fun getListTypeVaccine(employeeId: Int): Single<ListVaccineTypeResponseModel> {
        return remoteDataSource.getListTypeVaccine(employeeId)
    }

    override fun putUploadBpjs(
        employeeId: Int,
        file: MultipartBody.Part,
        number: String,
        typeBpjs: String
    ): Single<UpdateBpjsResponseModel> {
        return remoteDataSource.putUploadBpjs(employeeId, file, number, typeBpjs)
    }

    override fun putLatLongArea(
        employeeId: Int,
        latitude: String,
        longitude: String,
        address: String
    ): Single<LatLongAreaResponseModel> {
        return remoteDataSource.putLatLongArea(employeeId, latitude, longitude, address)
    }

    override fun getReportAttendance(
        employeeId: Int,
        month: Int,
        year: Int
    ): Single<ReportAttendanceResponse> {
        return remoteDataSource.getReportAttendance(employeeId, month, year)
    }

    override fun getComplaintValidate(
        projectCode: String,
        complaintType: ArrayList<String>
    ): Single<ComplaintValidateResponse> {
        return remoteDataSource.getComplaintValidate(projectCode, complaintType)
    }

    override fun getCheckAttendance3times(employeeId: Int): Single<CheckNotAttendanceResponseModel> {
        return remoteDataSource.getCheckAttendance3times(employeeId)
    }

    override fun getTimeShiftEmployee(
        employeeId: Int,
        projectId: String
    ): Single<GetTimeShiftResponseModel> {
        return remoteDataSource.getTimeShiftEmployee(employeeId, projectId)
    }

    override fun getListSlipGaji(userId: Int, month: Int, year: Int): Single<ListSlipGajiResponse> {
        return remoteDataSource.getListSlipGaji(userId, month, year)
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

    override fun dashboardMR(
        projectCode: String,
        page: Int,
        size: Int
    ): Single<MRDashboardResponse> {
        return remoteDataSource.dashboardMR(projectCode, page, size)
    }

    override fun createMR(createMaterialRequest: CreateMaterialRequest): Single<CreateMRResponse> {
        return remoteDataSource.createMR(createMaterialRequest)
    }

    override fun createMRFollowUp(createMaterialRequest: CreateMaterialRequest): Single<CreateMRResponse> {
        return remoteDataSource.createMRFollowUp(createMaterialRequest)
    }

    override fun getItemMR(filter: String): Single<ItemMRDataResponse> {
        return remoteDataSource.getItemMR(filter)
    }

    override fun getUnitMR(filter: String): Single<SatuanResponse> {
        return remoteDataSource.getUnitMR(filter)
    }

    override fun approveMR(
        employeeId: Int,
        idMaterialRequest: Int
    ): Single<CreateMRResponse> {
        return remoteDataSource.approveMR(employeeId,idMaterialRequest)
    }

    override fun getDataListHistoryUsed(idProject: String): Single<ListHistoryUsedResponse> {
        return remoteDataSource.getDataListHistoryUsed(idProject)
    }

    override fun getDataListHistoryStock(idProject: String): Single<ListHistoryStockResponse> {
        return remoteDataSource.getDataListHistoryStock(idProject)
    }

    override fun getDataListUsed(
        idProject: String,
        date: String
    ): Single<ListHistoryUsedResponse> {
        return remoteDataSource.getDataListUsed(idProject, date)
    }

    override fun createMRUsed(
        idProject: String,
        idItem: Int,
        quantity: Int,
        unit: String,
        userId: Int
    ): Single<CreateMRResponse> {
        return remoteDataSource.createMRUsed(idProject, idItem, quantity, unit, userId)
    }

}