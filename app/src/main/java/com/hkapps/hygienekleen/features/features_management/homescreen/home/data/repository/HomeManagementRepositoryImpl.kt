package com.hkapps.hygienekleen.features.features_management.homescreen.home.data.repository

import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.remote.HomeManagementDataSource
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
import javax.inject.Inject

class HomeManagementRepositoryImpl @Inject constructor(private val dataSource: HomeManagementDataSource): HomeManagementRepository {

    override fun getListProjectManagement(userId: Int): Single<ProjectCodeManagementNewResponse> {
        return dataSource.getProject(userId)
    }

    override fun getProfileManagement(id: Int): Single<GetProfileManagementReponseModel> {
        return dataSource.getProfileManagement(id)
    }

    override fun updateLastProfileManagement(adminMasterId: Int): Single<LastUpdateManagementProfileResponse> {
        return dataSource.updateLastProfileManagement(adminMasterId)
    }

    override fun checkDataRenewalManagement(adminId: Int): Single<LastUpdateManagementProfileResponse> {
        return dataSource.checkDataRenewalManagement(adminId)
    }

    override fun updateProfileManagement(
        adminMasterId : Int,
        adminMasterEmail : String,
        adminMasterPhone : String,
        adminMasterPhone2 : String
    ): Single<UpdateProfilemanagementResponse> {
        return dataSource.updateProfileManagement(
            adminMasterId,
            adminMasterEmail,
            adminMasterPhone,
            adminMasterPhone2
        )
    }

    override fun getAttendanceActivity(userId: Int): Single<ProjectAttendanceActivityResponse> {
        return dataSource.getAttendanceActivity(userId)
    }

    override fun updateProfilePicture(
        adminMasterId: Int,
        file: MultipartBody.Part
    ): Single<UpdateProfilemanagementResponse> {
        return dataSource.updateProfilePicture(adminMasterId, file)
    }

    override fun updateProfileFmGmOm(
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
        ): Single<UpdateProfilemanagementResponse> {
        return dataSource.updateProfileFmGmOm(adminId, adminEmail, adminPhone, adminPhone2, adminAddress, adminBirthDate, adminPlaceOfBirth, adminGender, adminMarriageStatus, adminMotherName, religion, adminAddressKtp, adminMasterCountChildren, adminMasterNik)
    }

    override fun getCheckProfile(
        adminId: Int
    ): Single<CheckProfileManagementResponse> {
        return dataSource.getCheckProfile(adminId)
    }

    override fun getCountProgressPermission(userId: Int): Single<ProgressPermissionManagementResponse> {
        return dataSource.getCountProgressPermission(userId)
    }

    override fun getTodayLastAttendance(
        userId: Int,
        date: String
    ): Single<TodayLastAttendanceResponse> {
        return dataSource.getTodayLastAttendance(userId, date)
    }

    override fun putNumberFamilyManagement(
        adminMasterId: Int,
        kkNumber: String
    ): Single<UpdateNumbFamManagementResponseModel> {
        return dataSource.putNumberFamilyManagement(adminMasterId, kkNumber)
    }

    override fun putNumberAccountManagement(
        adminMasterId: Int,
        accountName: String,
        accountNumber: String
    ): Single<UpdateNumbAccountMngntResponseModel> {
        return dataSource.putNumberAccountManagement(adminMasterId, accountName, accountNumber)
    }

    override fun getListDocumentManagement(adminMasterId: Int): Single<ListDocumentManagementResponseModel> {
        return dataSource.getListDocumentManagement(adminMasterId)
    }

    override fun getListVaccineManagement(
        adminMasterId: Int,
        page: Int
    ): Single<ListVaccineManagementResponseModel> {
        return dataSource.getListVaccineManagement(adminMasterId, page)
    }

    override fun putUploadDocumentMngmt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        typeDocument: String
    ): Single<UploadDocumentMngmtResponseModel> {
        return dataSource.putUploadDocumentMngmt(adminMasterId, file, typeDocument)
    }

    override fun putUploadVaccineMngmt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        typeVaccine: String
    ): Single<UploadVaccineMngmtResponseModel> {
        return dataSource.putUploadVaccineMngmt(adminMasterId, file, typeVaccine)
    }

    override fun getListTypeVaccineMngmt(adminMasterId: Int): Single<ListTypeVaccineMngmtResponseModel> {
        return dataSource.getListTypeVaccineMngmt(adminMasterId)
    }

    override fun putChangeVaccineMngmt(
        idVaccine: Int,
        file: MultipartBody.Part
    ): Single<ChangeVaccineMgmntResponseModel> {
        return dataSource.putChangeVaccineMngmt(idVaccine, file)
    }

    override fun getMainReportHigh(): Single<MainReportResponseModel> {
        return dataSource.getMainReportHigh()
    }

    override fun getMainReportLow(adminMasterId: Int): Single<MainReportResponseModel> {
        return dataSource.getMainReportLow(adminMasterId)
    }

    override fun putBpjsMgmnt(
        adminMasterId: Int,
        file: MultipartBody.Part,
        number: String,
        typeBpjs: String
    ): Single<UpdateBpjsTkKesResponseModel> {
        return dataSource.putBpjsMgmnt(adminMasterId, file, number, typeBpjs)
    }

    override fun putLatLongManagement(
        managementId: Int,
        latitude: String,
        longitude: String,
        address: String
    ): Single<LatLongManagementResponseModel> {
        return dataSource.putLatLongManagement(managementId, latitude, longitude, address)
    }

    override fun getListNewsManagement(
        page: Int,
        userId: Int,
        userType: String
    ): Single<ListNewsManagementResponseModel> {
        return dataSource.getListNewsManagement(page, userId, userType)
    }

    override fun getDetailNewsManagement(idNews: Int): Single<DetailNewsManagementResponseModel> {
        return dataSource.getDetailNewsManagement(idNews)
    }

    override fun getCheckNewsManagement(
        userType: String,
        userId: Int
    ): Single<GetCheckNewsResponseModel> {
        return dataSource.getCheckNewsManagement(userType, userId)
    }

    override fun putReadNewsManagement(
        userType: String,
        userId: Int,
        newsId: Int
    ): Single<ReadNewsManagementResponseModel> {
        return dataSource.putReadNewsManagement(userType, userId, newsId)
    }


    override fun getAttendanceFeature(adminMasterId: Int): Single<AttendanceFeatureAvailabilityModel> {
        return dataSource.getAttendanceFeature(adminMasterId)
    }


    override fun putChangePassword(
        adminMasterId: Int,
        changePassMgmnt: ChangePassMgmntResponseModel
    ): Single<ChangePassManagementResponseModel> {
        return dataSource.putChangePassword(adminMasterId, changePassMgmnt)
    }

    override fun itemMr(
        projectCode: String,
        month: Int,
        year: Int,
        page: Int,
        size: Int
    ): Single<ItemMrResponse> {
        return dataSource.itemMr(projectCode, month, year, page, size)
    }

}