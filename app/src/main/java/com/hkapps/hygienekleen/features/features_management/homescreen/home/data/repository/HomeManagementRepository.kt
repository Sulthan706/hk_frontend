package com.hkapps.hygienekleen.features.features_management.homescreen.home.data.repository

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

interface HomeManagementRepository {

    fun getListProjectManagement(
        userId: Int
    ): Single<ProjectCodeManagementNewResponse>

    fun getProfileManagement(
        id: Int
    ): Single<GetProfileManagementReponseModel>

    fun updateLastProfileManagement(
        adminMasterId: Int
    ): Single<LastUpdateManagementProfileResponse>

    fun checkDataRenewalManagement(
        adminId : Int
    ): Single<LastUpdateManagementProfileResponse>

    fun updateProfileManagement(
        adminMasterId : Int,
        adminMasterEmail : String,
        adminMasterPhone : String,
        adminMasterPhone2 : String
    ): Single<UpdateProfilemanagementResponse>

    fun getAttendanceActivity(
        userId: Int
    ): Single<ProjectAttendanceActivityResponse>

    fun updateProfilePicture(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<UpdateProfilemanagementResponse>

    fun updateProfileFmGmOm(
        adminId: Int,
        adminEmail: String,
        adminPhone: String,
        adminPhone2: String,
        adminAddress: String,
        adminBirthDate: String,
        adminPlaceOfBirth: String,
        adminGender: String,
        adminMarriageStatus: String,
        adminMotherName: String,
        religion: String,
        adminAddressKtp: String,
        adminMasterCountChildren: String,
        adminMasterNik: String
        ): Single<UpdateProfilemanagementResponse>

    fun getCheckProfile(
        adminId: Int
    ): Single<CheckProfileManagementResponse>

    fun getCountProgressPermission(
        userId: Int
    ): Single<ProgressPermissionManagementResponse>

    fun getTodayLastAttendance(
        userId: Int,
        date: String
    ): Single<TodayLastAttendanceResponse>

    fun putNumberFamilyManagement(
        adminMasterId: Int,
        kkNumber: String
    ): Single<UpdateNumbFamManagementResponseModel>

    fun putNumberAccountManagement(
        adminMasterId: Int,
        accountName: String,
        accountNumber: String
    ): Single<UpdateNumbAccountMngntResponseModel>

    fun getListDocumentManagement(
        adminMasterId: Int
    ): Single<ListDocumentManagementResponseModel>

    fun getListVaccineManagement(
        adminMasterId:Int,
        page:Int
    ): Single<ListVaccineManagementResponseModel>

    fun putUploadDocumentMngmt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        typeDocument: String
    ):Single<UploadDocumentMngmtResponseModel>

    fun putUploadVaccineMngmt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        typeVaccine: String
    ):Single<UploadVaccineMngmtResponseModel>

    fun getListTypeVaccineMngmt(
        adminMasterId:Int
    ):Single<ListTypeVaccineMngmtResponseModel>

    fun putChangeVaccineMngmt(
       idVaccine: Int,
       file: MultipartBody.Part
    ): Single<ChangeVaccineMgmntResponseModel>

    fun getMainReportHigh(

    ): Single<MainReportResponseModel>

    fun getMainReportLow(
        adminMasterId: Int
    ): Single<MainReportResponseModel>

    fun putBpjsMgmnt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        number: String,
        typeBpjs: String
    ): Single<UpdateBpjsTkKesResponseModel>

    fun putLatLongManagement(
        managementId: Int,
        latitude: String,
        longitude: String,
        address: String
    ): Single<LatLongManagementResponseModel>

    fun getListNewsManagement(
        page: Int,
        userId: Int,
        userType: String
    ): Single<ListNewsManagementResponseModel>

    fun getDetailNewsManagement(
        idNews: Int
    ): Single<DetailNewsManagementResponseModel>

    fun getCheckNewsManagement(
        userType: String,
        userId: Int
    ) : Single<GetCheckNewsResponseModel>

    fun putReadNewsManagement(
        userType: String,
        userId: Int,
        newsId: Int
    ): Single<ReadNewsManagementResponseModel>

    fun getAttendanceFeature(
        adminMasterId: Int
    ): Single<AttendanceFeatureAvailabilityModel>

    fun putChangePassword(
        adminMasterId: Int,
        changePassMgmnt: ChangePassMgmntResponseModel
    ): Single<ChangePassManagementResponseModel>

    fun itemMr(
        projectCode : String,
        month : Int,
        year : Int,
        page : Int,
        size : Int
    ):Single<ItemMrResponse>
}