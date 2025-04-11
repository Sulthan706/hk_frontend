package com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.repository.HomeManagementRepository
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
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeManagementViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    val isErrorProfileManagementModel = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val getProfileResponseManagementModel = MutableLiveData<GetProfileManagementReponseModel>()
    val updateLastManagementModel = MutableLiveData<LastUpdateManagementProfileResponse>()
    val checkRenewalManagementModel = MutableLiveData<LastUpdateManagementProfileResponse>()
    val updateProfileManagementResponseModel = MutableLiveData<UpdateProfilemanagementResponse>()
    val attendanceActivityModel = MutableLiveData<ProjectAttendanceActivityResponse>()
    val updateProfilePictureModel = MutableLiveData<UpdateProfilemanagementResponse>()
    val updateProfileFmGmOmModel = MutableLiveData<UpdateProfilemanagementResponse>()
    val checkEditProfileModel = MutableLiveData<CheckProfileManagementResponse>()
    val countProgressPermissionModel = MutableLiveData<ProgressPermissionManagementResponse>()
    val todayLastAttendanceModel = MutableLiveData<TodayLastAttendanceResponse>()
    //edit profile
    val putNumbFamsManagementModel = MutableLiveData<UpdateNumbFamManagementResponseModel>()
    val putNumbAccManagementModel = MutableLiveData<UpdateNumbAccountMngntResponseModel>()
    val getListDocumentManagementModel = MutableLiveData<ListDocumentManagementResponseModel>()
    val getListVaccineManagementModel = MutableLiveData<ListVaccineManagementResponseModel>()
    val putUploadDocumentManagementModel = MutableLiveData<UploadDocumentMngmtResponseModel>()
    val putUploadVaccineManagementModel = MutableLiveData<UploadVaccineMngmtResponseModel>()
    val getListTypeVaccineModel = MutableLiveData<ListTypeVaccineMngmtResponseModel>()
    val putChangeVaccineManagementModel = MutableLiveData<ChangeVaccineMgmntResponseModel>()
    //report
    val getMainReportHighModel = MutableLiveData<MainReportResponseModel>()
    val getMainReportLowModel = MutableLiveData<MainReportResponseModel>()
    //bpjs
    val putBpjsTkKesMgmntModel = MutableLiveData<UpdateBpjsTkKesResponseModel>()
    //put lat long
    val putLatLongManagementModel = MutableLiveData<LatLongManagementResponseModel>()
    //news
    val getListNewsManagementModel = MutableLiveData<ListNewsManagementResponseModel>()
    val detailNewsManagementModel = MutableLiveData<DetailNewsManagementResponseModel>()
    val getCheckNewsManagementModel = MutableLiveData<GetCheckNewsResponseModel>()
    val readNewsManagementModel = MutableLiveData<ReadNewsManagementResponseModel>()
    //profile
    val changePassManagementModel = MutableLiveData<ChangePassManagementResponseModel>()

    val attendanceFeatureAvailabilityModel = MutableLiveData<AttendanceFeatureAvailabilityModel>()

    val getDataMr = MutableLiveData<ItemMrResponse>()

    @Inject
    lateinit var repository: HomeManagementRepository


    private val model = MutableLiveData<ProjectCodeManagementNewResponse>()

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getProject(userId: Int) {
        compositeDisposable.add(repository.getListProjectManagement(userId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ProjectCodeManagementNewResponse>() {
                override fun onSuccess(t: ProjectCodeManagementNewResponse) {
                    if (t.code == 200) {
                        model.value = t
                    }
                    isLoading?.value = false
                }

                override fun onError(e: Throwable) {
                    when (e) {
                        is HttpException -> {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProjectCodeManagementNewResponse::class.java
                            )
                            model.value = error
                        }
                        is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                            isConnectionTimeout.postValue(true)
                        }
                    }
                }
            })
        )
    }

    fun updateLastManagementProfile(adminMasterId: Int){
        compositeDisposable.add(
            repository.updateLastProfileManagement(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LastUpdateManagementProfileResponse>(){
                    override fun onSuccess(t: LastUpdateManagementProfileResponse) {
                        updateLastManagementModel.value = t
                        isLoading?.value = false

                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LastUpdateManagementProfileResponse::class.java
                                )
                                updateLastManagementModel.value = error
                                isLoading?.value = false
                            }

                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }

                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun checkRenewalManagement(adminId: Int){
        compositeDisposable.add(
            repository.checkDataRenewalManagement(adminId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LastUpdateManagementProfileResponse>(){
                    override fun onSuccess(t: LastUpdateManagementProfileResponse) {
                        Log.d("TESTED","$t")
                        checkRenewalManagementModel.value = t
                        isLoading?.value = false

                    }

                    override fun onError(e: Throwable) {
                        Log.d("TESTEDD","$e")
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LastUpdateManagementProfileResponse::class.java
                                )
                                checkRenewalManagementModel.value = error
                                isLoading?.value = false
                            }

                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }

                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getProfileManagement(adminmasterid : Int){
        compositeDisposable.add(
            repository.getProfileManagement(adminmasterid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetProfileManagementReponseModel>(){
                    override fun onSuccess(t: GetProfileManagementReponseModel) {
                        getProfileResponseManagementModel.value = t
                        Log.d("getManagementProfile", "$t")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GetProfileManagementReponseModel::class.java
                            )
                            getProfileResponseManagementModel.value = error
                            Log.d("Profile", "onError: $error")
                        }
                    }

                })
        )
    }
    fun updateProfileManagement(
        adminMasterId : Int,
        adminMasterEmail : String,
        adminMasterPhone : String,
        adminMasterPhone2 : String
    ){
        compositeDisposable.add(
            repository.updateProfileManagement(adminMasterId,adminMasterEmail,adminMasterPhone,adminMasterPhone2)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<UpdateProfilemanagementResponse>(){
                override fun onSuccess(t: UpdateProfilemanagementResponse) {
                    updateProfileManagementResponseModel.value = t
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val errorBody = e.response()?.errorBody()
                        val gson = Gson()
                        val error = gson.fromJson(
                            errorBody?.string(),
                            UpdateProfilemanagementResponse::class.java
                        )
                        updateProfileManagementResponseModel.value = error
                        Log.d("Profile", "onError: $error")
                    }
                }

            })
        )
    }

    fun getAttendanceActivity(userId: Int) {
        compositeDisposable.add(
            repository.getAttendanceActivity(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectAttendanceActivityResponse>() {
                    override fun onSuccess(t: ProjectAttendanceActivityResponse) {
                        if (t.code == 200) {
                            attendanceActivityModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectAttendanceActivityResponse::class.java
                                )
                                attendanceActivityModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun updateProfilePicture(
        adminMasterId: Int,
        file: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.updateProfilePicture(adminMasterId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfilemanagementResponse>() {
                    override fun onSuccess(t: UpdateProfilemanagementResponse) {
                        updateProfilePictureModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfilemanagementResponse::class.java
                            )
                            updateProfilePictureModel.value = error
                        }
                    }

                })
        )
    }

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
        adminMasterKtp: String
        ) {
        compositeDisposable.add(
            repository.updateProfileFmGmOm(adminId, adminEmail, adminPhone, adminPhone2, adminAddress, adminBirthDate, adminPlaceOfBirth, adminGender, adminMarriageStatus, adminMotherName, religion, adminAddressKtp, adminMasterCountChildren, adminMasterKtp)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfilemanagementResponse>() {
                    override fun onSuccess(t: UpdateProfilemanagementResponse) {
                        updateProfileFmGmOmModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfilemanagementResponse::class.java
                            )
                            updateProfileFmGmOmModel.value = error
                        }
                    }

                })
        )
    }

    fun getCheckProfile(
        adminId: Int
    ) {
        compositeDisposable.add(
            repository.getCheckProfile(adminId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckProfileManagementResponse>() {
                    override fun onSuccess(t: CheckProfileManagementResponse) {
                        checkEditProfileModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                CheckProfileManagementResponse::class.java
                            )
                            checkEditProfileModel.value = error
                        }
                    }

                })
        )
    }

    fun getCountProgressPermission(userId: Int) {
        compositeDisposable.add(
            repository.getCountProgressPermission(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProgressPermissionManagementResponse>() {
                    override fun onSuccess(t: ProgressPermissionManagementResponse) {
                        countProgressPermissionModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProgressPermissionManagementResponse::class.java
                            )
                            countProgressPermissionModel.value = error
                        }
                    }

                })
        )
    }

    fun getTodayLastAttendance(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getTodayLastAttendance(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TodayLastAttendanceResponse>() {
                    override fun onSuccess(t: TodayLastAttendanceResponse) {
                        todayLastAttendanceModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                TodayLastAttendanceResponse::class.java
                            )
                            todayLastAttendanceModel.value = error
                        }
                    }

                })
        )
    }

    fun updateNumbFamManagement(adminMasterId: Int, kkNumber:String){
        compositeDisposable.add(
            repository.putNumberFamilyManagement(adminMasterId, kkNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateNumbFamManagementResponseModel>(){
                    override fun onSuccess(t: UpdateNumbFamManagementResponseModel) {
                        if (t.code == 200){
                            putNumbFamsManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateNumbFamManagementResponseModel::class.java
                            )
                            putNumbFamsManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun updateNumbAccManagement(adminMasterId: Int, accountName: String, accountNumber:String){
        compositeDisposable.add(
            repository.putNumberAccountManagement(adminMasterId, accountName, accountNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<UpdateNumbAccountMngntResponseModel>(){
                    override fun onSuccess(t: UpdateNumbAccountMngntResponseModel) {
                        if (t.code ==200){
                            putNumbAccManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateNumbAccountMngntResponseModel::class.java
                            )
                            putNumbAccManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getListDocumentManagement(adminMasterId: Int){
        compositeDisposable.add(
            repository.getListDocumentManagement(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListDocumentManagementResponseModel>(){
                    override fun onSuccess(t: ListDocumentManagementResponseModel) {
                        if (t.code == 200){
                            getListDocumentManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListDocumentManagementResponseModel::class.java
                            )
                            getListDocumentManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getListVaccineManagement(adminMasterId: Int, page:Int){
        compositeDisposable.add(
            repository.getListVaccineManagement(adminMasterId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListVaccineManagementResponseModel>(){
                    override fun onSuccess(t: ListVaccineManagementResponseModel) {
                        if (t.code == 200){
                            getListVaccineManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListVaccineManagementResponseModel::class.java
                            )
                            getListVaccineManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun putUploadDocumentManagement(adminMasterId: Int, file: MultipartBody.Part, typeDocument:String){
        compositeDisposable.add(
            repository.putUploadDocumentMngmt(adminMasterId, file, typeDocument)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadDocumentMngmtResponseModel>(){
                    override fun onSuccess(t: UploadDocumentMngmtResponseModel) {
                        if (t.code == 200){
                            putUploadDocumentManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UploadDocumentMngmtResponseModel::class.java
                            )
                            putUploadDocumentManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun putUploadVaccineManagement(adminMasterId: Int, file: MultipartBody.Part, typeVaccine:String){
        compositeDisposable.add(
            repository.putUploadVaccineMngmt(adminMasterId, file, typeVaccine)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadVaccineMngmtResponseModel>(){
                    override fun onSuccess(t: UploadVaccineMngmtResponseModel) {
                        if (t.code == 200){
                            putUploadVaccineManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UploadVaccineMngmtResponseModel::class.java
                            )
                            putUploadVaccineManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getListTypeVaccineManagement(adminMasterId: Int){
        compositeDisposable.add(
            repository.getListTypeVaccineMngmt(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListTypeVaccineMngmtResponseModel>(){

                    override fun onSuccess(t: ListTypeVaccineMngmtResponseModel) {
                        if (t.code == 200) {
                            getListTypeVaccineModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListTypeVaccineMngmtResponseModel::class.java
                            )
                            getListTypeVaccineModel.value = error
                        }
                    }

                })
        )
    }

    fun putChangeVaccineManagement(idVaccine:Int, file: MultipartBody.Part){
        compositeDisposable.add(
            repository.putChangeVaccineMngmt(idVaccine, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChangeVaccineMgmntResponseModel>(){
                    override fun onSuccess(t: ChangeVaccineMgmntResponseModel) {
                        if (t.code == 200){
                            putChangeVaccineManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ChangeVaccineMgmntResponseModel::class.java
                            )
                            putChangeVaccineManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getMainReportHigh(){
        compositeDisposable.add(
            repository.getMainReportHigh()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MainReportResponseModel>(){
                    override fun onSuccess(t: MainReportResponseModel) {
                        if (t.code == 200){
                            getMainReportHighModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                MainReportResponseModel::class.java
                            )
                            getMainReportHighModel.value = error
                        }
                    }

                })
        )
    }

    fun getMainReportLow(adminMasterId: Int){
        compositeDisposable.add(
            repository.getMainReportLow(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MainReportResponseModel>(){
                    override fun onSuccess(t: MainReportResponseModel) {
                        if (t.code == 200){
                            getMainReportLowModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                MainReportResponseModel::class.java
                            )
                            getMainReportLowModel.value = error
                        }
                    }

                })
        )
    }

    fun putBpjsTkKes(adminMasterId: Int, file: MultipartBody.Part, number: String, typjeBpjs: String){
        compositeDisposable.add(
            repository.putBpjsMgmnt(adminMasterId, file, number, typjeBpjs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<UpdateBpjsTkKesResponseModel>(){
                    override fun onSuccess(t: UpdateBpjsTkKesResponseModel) {
                        if (t.code == 200){
                            putBpjsTkKesMgmntModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateBpjsTkKesResponseModel::class.java
                            )
                            putBpjsTkKesMgmntModel.value = error
                        }
                    }

                })
        )
    }

    fun putLatLongManagement(managementId: Int, latitude: String, longitude: String, address: String){
        compositeDisposable.add(
            repository.putLatLongManagement(managementId, latitude, longitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object  :DisposableSingleObserver<LatLongManagementResponseModel>(){
                    override fun onSuccess(t: LatLongManagementResponseModel) {
                        if (t.code == 200){
                            putLatLongManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                LatLongManagementResponseModel::class.java
                            )
                            putLatLongManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getListNewsManagement(page: Int, userId: Int, userType: String){
        compositeDisposable.add(
            repository.getListNewsManagement(page, userId, userType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListNewsManagementResponseModel>(){
                    override fun onSuccess(t: ListNewsManagementResponseModel) {
                        getListNewsManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ListNewsManagementResponseModel::class.java
                            )
                            getListNewsManagementModel.value = error
                        }
                    }
                })
        )
    }

    fun getDetailNewsManagement(idNews: Int){
        compositeDisposable.add(
            repository.getDetailNewsManagement(idNews)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailNewsManagementResponseModel>(){
                    override fun onSuccess(t: DetailNewsManagementResponseModel) {
                        detailNewsManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DetailNewsManagementResponseModel::class.java
                            )
                            detailNewsManagementModel.value = error
                        }
                    }

                })
        )
    }

    fun getCheckNewsManagement(userType: String, userId: Int){
        compositeDisposable.add(
            repository.getCheckNewsManagement(userType, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetCheckNewsResponseModel>(){
                    override fun onSuccess(t: GetCheckNewsResponseModel) {
                        getCheckNewsManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GetCheckNewsResponseModel::class.java
                            )
                            getCheckNewsManagementModel.value = error
                        }
                    }

                }
        )
        )
    }

    fun readNewsManagement(userType: String, userId: Int, newsId: Int){
        compositeDisposable.add(
            repository.putReadNewsManagement(userType, userId, newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReadNewsManagementResponseModel>(){
                    override fun onSuccess(t: ReadNewsManagementResponseModel) {
                        readNewsManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ReadNewsManagementResponseModel::class.java
                            )
                            readNewsManagementModel.value = error
                        }
                    }

                })
        )
    }


    fun getAttendanceFeature(
        adminMasterId: Int
    ) {
        compositeDisposable.add(
            repository.getAttendanceFeature(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceFeatureAvailabilityModel>() {
                    override fun onSuccess(t: AttendanceFeatureAvailabilityModel) {
                        attendanceFeatureAvailabilityModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceFeatureAvailabilityModel::class.java
                            )
                            attendanceFeatureAvailabilityModel.value = error
                        }
                    }

                })
        )
    }


    fun putChangePassManagement(adminMasterId: Int,
                                passOld: String,
                                passNew: String,
                                passConfirm: String){
        val changepass = ChangePassMgmntResponseModel(passOld, passNew, passConfirm)
        compositeDisposable.add(repository.putChangePassword(adminMasterId, changepass)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ChangePassManagementResponseModel>(){
                override fun onSuccess(t: ChangePassManagementResponseModel) {
                    changePassManagementModel.value = t
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val errorBody = e.response()?.errorBody()
                        val gson = Gson()
                        val error = gson.fromJson(
                            errorBody?.string(),
                            ChangePassManagementResponseModel::class.java
                        )
                        changePassManagementModel.value = error
                    }
                }

            })
        )
    }

    fun getDataMr(
        projectCode : String,
        month : Int,
        year : Int,
        page : Int,
        size : Int
    ){
        compositeDisposable.add(
            repository.itemMr(projectCode, month, year, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ItemMrResponse>(){
                    override fun onSuccess(t: ItemMrResponse) {
                        getDataMr.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Log.d("ERROR","$e")
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ItemMrResponse::class.java
                                )
                                getDataMr.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putChangePassManagementViewModel(): MutableLiveData<ChangePassManagementResponseModel>{
        return changePassManagementModel
    }


    fun putReadNewsManagementViewModel(): MutableLiveData<ReadNewsManagementResponseModel>{
        return readNewsManagementModel
    }

    fun getCheckNewsManagementViewModel(): MutableLiveData<GetCheckNewsResponseModel>{
        return getCheckNewsManagementModel
    }

    fun getDetailNewsManagementViewModel(): MutableLiveData<DetailNewsManagementResponseModel>{
        return detailNewsManagementModel
    }

    fun getListHomeNewsViewModel(): MutableLiveData<ListNewsManagementResponseModel>{
        return getListNewsManagementModel
    }

    fun putLatLongManagementViewModel(): MutableLiveData<LatLongManagementResponseModel>{
        return putLatLongManagementModel
    }

    fun putBpjsTkKesMgmntViewModel(): MutableLiveData<UpdateBpjsTkKesResponseModel>{
        return putBpjsTkKesMgmntModel
    }

    fun getCheckEditProfileViewModel(): MutableLiveData<CheckProfileManagementResponse>{
        return checkEditProfileModel
    }

    fun getMainReportLowViewModel(): MutableLiveData<MainReportResponseModel>{
        return getMainReportLowModel
    }

    fun getMainReportHighViewModel(): MutableLiveData<MainReportResponseModel>{
        return getMainReportHighModel
    }

    fun putChangeVaccineManagementViewModel(): MutableLiveData<ChangeVaccineMgmntResponseModel>{
        return putChangeVaccineManagementModel
    }

    fun getListTypeVaccineViewModel(): MutableLiveData<ListTypeVaccineMngmtResponseModel>{
        return getListTypeVaccineModel
    }

    fun uploadVaccineManagementViewModel(): MutableLiveData<UploadVaccineMngmtResponseModel>{
        return putUploadVaccineManagementModel
    }

    fun uploadDocumentManagementViewModel(): MutableLiveData<UploadDocumentMngmtResponseModel>{
        return putUploadDocumentManagementModel
    }

    fun getListVaccineManagementViewModel():MutableLiveData<ListVaccineManagementResponseModel>{
        return getListVaccineManagementModel
    }

    fun getListDocumentManagementViewModel(): MutableLiveData<ListDocumentManagementResponseModel>{
        return getListDocumentManagementModel
    }

    fun updateNumbAccManagementViewModel(): MutableLiveData<UpdateNumbAccountMngntResponseModel>{
        return putNumbAccManagementModel
    }

    fun updateNumbFamManagementViewModel(): MutableLiveData<UpdateNumbFamManagementResponseModel>{
        return putNumbFamsManagementModel
    }

    fun getProjectModel(): MutableLiveData<ProjectCodeManagementNewResponse> {
        return model
    }

    fun getProfileManagementModel(): MutableLiveData<GetProfileManagementReponseModel>{
        return getProfileResponseManagementModel
    }

    fun updateProfileManagementObs(): MutableLiveData<UpdateProfilemanagementResponse>{
        return updateProfileManagementResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}