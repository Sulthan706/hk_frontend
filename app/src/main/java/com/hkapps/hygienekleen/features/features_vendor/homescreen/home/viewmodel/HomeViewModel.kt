package com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.repository.HomeRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.badgenotif.badgeNotifResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.checknews.CheckNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.countChecklist.ChecklistHomeVendorResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.countProgressBar.CountPermissionMidResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DacCountModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dacHome.DailyActHomeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.detailnews.DetailNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.editProfileCheck.CheckProfileEmployeeResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.greetinglate3days.GreetingLateResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listdocument.ListDocumentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listhomenews.ListHomeNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.listvaccine.ListVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.newVersion.NewVersionResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile.ProfileModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.readnews.ReadNewsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updateAccountNumber.UpdateAccountNumbReponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updateFamsNumber.UpdateFamsNumberResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.changevaccine.UpdateVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.complaintValidate.ComplaintValidateResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.checknotattendance.CheckNotAttendanceResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.dropdowntypevaccine.ListVaccineTypeResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.editVaccine.ChangeVaccineResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.getTimeShift.GetTimeShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.latlongarea.LatLongAreaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMrResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.profile.LastUpdateProfileResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.updatebpjs.UpdateBpjsResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.uploaddocument.UploadDocumentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.versionCek.VersionCheckResponse
import com.hkapps.hygienekleen.features.splash.ui.activity.SplashActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.google.gson.Gson
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.CreateMRResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.CreateMaterialRequest
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRData
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ItemMRDataResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ListHistoryStockResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.ListHistoryUsedResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.MRDashboardResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.model.mr.SatuanResponse
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


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val profileModel = MutableLiveData<ProfileModel>()
    val updateLastModel = MutableLiveData<LastUpdateProfileResponse>()
    val checkRenewalModel = MutableLiveData<LastUpdateProfileResponse>()
    private val dacCountModel = MutableLiveData<DacCountModel>()
    val dailyActHomeResponseModel = MutableLiveData<DailyActHomeResponseModel>()
    val checklistCountModel = MutableLiveData<ChecklistHomeVendorResponse>()
    val checkVersionAppModel = MutableLiveData<VersionCheckResponse>()
    val newVersionAppModel = MutableLiveData<NewVersionResponse>()
    val cekEditProfileModel = MutableLiveData<CheckProfileEmployeeResponse>()
    val getBadgeNotification = MutableLiveData<badgeNotifResponseModel>()
    val getBadgeNotificationOperator = MutableLiveData<badgeNotifResponseModel>()
    val countPermissionMidModel = MutableLiveData<CountPermissionMidResponse>()

    val updateFamsNumberModel = MutableLiveData<UpdateFamsNumberResponseModel>()
    val updateAccountNumberModel = MutableLiveData<UpdateAccountNumbReponseModel>()
    val getGrettingLateModel = MutableLiveData<GreetingLateResponseModel>()
    val getCheckAttendanceModel = MutableLiveData<CheckNotAttendanceResponseModel>()
    val getCheckHomeNewsModel = MutableLiveData<CheckNewsResponseModel>()
    val getListHomeNewsModel = MutableLiveData<ListHomeNewsResponseModel>()
    val putReadNewsModel = MutableLiveData<ReadNewsResponseModel>()
    val getDetailNewsModel = MutableLiveData<DetailNewsResponseModel>()
    val putUploadDocumentModel = MutableLiveData<UploadDocumentResponseModel>()
    val getListDocumentModel = MutableLiveData<ListDocumentResponseModel>()
    val putChangeVaccineModel = MutableLiveData<ChangeVaccineResponseModel>()
    val getListTypeVaccine = MutableLiveData<ListVaccineTypeResponseModel>()

    val getListVaccineModel = MutableLiveData<ListVaccineResponseModel>()
    val updateVaccineModel = MutableLiveData<UpdateVaccineResponseModel>()

    val updateBpjsModel = MutableLiveData<UpdateBpjsResponseModel>()
    val putLatLongAreaModel = MutableLiveData<LatLongAreaResponseModel>()
    val getTimeShiftModel = MutableLiveData<GetTimeShiftResponseModel>()

    val getDataMr = MutableLiveData<ItemMrResponse>()
    val dashboardMR = MutableLiveData<MRDashboardResponse>()
    val createMRResponse = MutableLiveData<CreateMRResponse>()
    val getItemMR = MutableLiveData<ItemMRDataResponse>()
    val getUnitMR = MutableLiveData<SatuanResponse>()
    val approveMR = MutableLiveData<CreateMRResponse>()
    val getMRUsed = MutableLiveData<ListHistoryUsedResponse>()
    val getMRStock = MutableLiveData<ListHistoryStockResponse>()
    val getListUsed = MutableLiveData<ListHistoryUsedResponse>()
    val createUsed = MutableLiveData<CreateMRResponse>()

    val complaintValidateModel = MutableLiveData<ComplaintValidateResponse>()

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


    @Inject
    lateinit var repository: HomeRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDailyActHome(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getDailyActHome(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyActHomeResponseModel>() {
                    override fun onSuccess(t: DailyActHomeResponseModel) {
                        dailyActHomeResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DailyActHomeResponseModel::class.java
                            )
                            dailyActHomeResponseModel.value = error
                            Log.d("DailyAct", "onError: $error")
                        } else {
//                            Toast.makeText(
//                                context,
//                                "Terjadi Kesalahan.",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    }
                })
        )
    }

    fun getProfileEmployee(userId: Int) {
        compositeDisposable.add(
            repository.getProfileEmployee(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProfileModel>() {
                    override fun onSuccess(t: ProfileModel) {
                        if (t.code == 200) {
                            profileModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
//                            isConnectionTimeout.postValue(true)
//                            isLoading?.value = false
//                        }else{
//                            Toast.makeText(
//                                context,
//                                "Your session is expired.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            CarefastOperationPref.logout()
//                            val i = Intent(context, SplashActivity::class.java)
//                            context.startActivity(i)
//                            Log.d("HomeViewModel", "errorHomeViewModel")
//                        }

                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                CarefastOperationPref.logout()
                                val i = Intent(context, SplashActivity::class.java)
                                context.startActivity(i)
                                Toast.makeText(
                                    context,
                                    "Sesi anda telah berakhir.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (e.response()!!.code() == 500) {
                                Toast.makeText(
                                    context,
                                    "Jadwal anda belum tersedia.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProfileModel::class.java
                            )
                            profileModel.value = error
                            Log.d("Profile", "onError: $error + ${e.response()!!.code()}")
                        } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
                            isConnectionTimeout.postValue(true)
                            isLoading?.value = false
                        } else {

                        }
                    }

                })
        )
    }

    fun updateLastProfile(userId : Int){
        compositeDisposable.add(
            repository.updateLastProfile(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LastUpdateProfileResponse>(){
                    override fun onSuccess(t: LastUpdateProfileResponse) {
                        updateLastModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LastUpdateProfileResponse::class.java
                                )
                                updateLastModel.value = error
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

    fun checkRenewalProfile(userId : Int){
        compositeDisposable.add(
            repository.checkRenewal(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LastUpdateProfileResponse>(){
                    override fun onSuccess(t: LastUpdateProfileResponse) {
                        Log.d("TESTED","$t")
                        checkRenewalModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.d("TESTED","$e")
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LastUpdateProfileResponse::class.java
                                )
                                checkRenewalModel.value = error
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


    fun getDacCount(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getDACCount(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DacCountModel>() {
                    override fun onSuccess(t: DacCountModel) {
                        if (t.code == 200) {
                            dacCountModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                CarefastOperationPref.logout()
                                val i = Intent(context, SplashActivity::class.java)
                                context.startActivity(i)
                                Toast.makeText(
                                    context,
                                    "Sesi anda telah berakhir.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (e.response()!!.code() == 500) {
                                Toast.makeText(
                                    context,
                                    "Jadwal anda belum tersedia.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DacCountModel::class.java
                            )
                            dacCountModel.value = error
                            Log.d("DAC", "onError: $error + ${e.response()!!.code()}")
                        } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
                            isConnectionTimeout.postValue(true)
                            isLoading?.value = false
                        } else {

                        }
                    }

                })
        )
    }

    fun getChecklistCount(projectCode: String) {
        compositeDisposable.add(
            repository.getChecklistCount(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChecklistHomeVendorResponse>() {
                    override fun onSuccess(t: ChecklistHomeVendorResponse) {
                        if (t.code == 200) {
                            checklistCountModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChecklistHomeVendorResponse::class.java
                                )
                                checklistCountModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun checkVersion(versionApp: String) {
        compositeDisposable.add(
            repository.checkVersion(versionApp)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VersionCheckResponse>() {
                    override fun onSuccess(t: VersionCheckResponse) {
                        checkVersionAppModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error =
                                gson.fromJson(errorBody?.string(), VersionCheckResponse::class.java)
                            checkVersionAppModel.value = error
                        }
                    }

                })
        )
    }

    fun getNewVersionApp() {
        compositeDisposable.add(
            repository.getNewVersionApp()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NewVersionResponse>() {
                    override fun onSuccess(t: NewVersionResponse) {
                        newVersionAppModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChecklistHomeVendorResponse::class.java
                                )
                                checklistCountModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCheckProfile(
        employeeId: Int
    ) {
        compositeDisposable.add(
            repository.getCheckProfile(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckProfileEmployeeResponse>() {
                    override fun onSuccess(t: CheckProfileEmployeeResponse) {
                        cekEditProfileModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CheckProfileEmployeeResponse::class.java
                                )
                                cekEditProfileModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getBadgeNotification(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getBadgeNotification(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<badgeNotifResponseModel>() {
                    override fun onSuccess(t: badgeNotifResponseModel) {
                        getBadgeNotification.value = t

                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                badgeNotifResponseModel::class.java
                            )
                            getBadgeNotification.value = error
                            Log.d("DailyAct", "onError: $error")
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                })
        )
    }

    fun getBadgeNotificationOperator(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getBadgeNotificationOperator(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<badgeNotifResponseModel>() {
                    override fun onSuccess(t: badgeNotifResponseModel) {
                        getBadgeNotificationOperator.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                badgeNotifResponseModel::class.java
                            )
                            getBadgeNotificationOperator.value = error
                            Log.d("DailyAct", "onError: $error")
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        )
    }

    fun getCountPermissionMid(
        employeeId: Int,
        projectCode: String
    ) {
        compositeDisposable.add(
            repository.getCountPermissionMid(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CountPermissionMidResponse>() {
                    override fun onSuccess(t: CountPermissionMidResponse) {
                        countPermissionMidModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CountPermissionMidResponse::class.java
                                )
                                countPermissionMidModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun updateFamsNumber(employeeId: Int, kkNumber: String) {
        compositeDisposable.add(
            repository.PutNumberFamily(employeeId, kkNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateFamsNumberResponseModel>() {
                    override fun onSuccess(t: UpdateFamsNumberResponseModel) {
                        if (t.code == 200) {
                            updateFamsNumberModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateFamsNumberResponseModel::class.java
                                )
                                updateFamsNumberModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun updateAccountNumber(employeeId: Int, accountName: String, accountNumber: String) {
        compositeDisposable.add(
            repository.PutAccountEmployee(employeeId, accountName, accountNumber)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateAccountNumbReponseModel>() {
                    override fun onSuccess(t: UpdateAccountNumbReponseModel) {
                        if (t.code == 200) {
                            updateAccountNumberModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateAccountNumbReponseModel::class.java
                                )
                                updateAccountNumberModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getGreetingLateEmployee(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.GetGreetingLate(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GreetingLateResponseModel>() {
                    override fun onSuccess(t: GreetingLateResponseModel) {
                        if (t.code == 200) {
                            getGrettingLateModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    GreetingLateResponseModel::class.java
                                )
                                getGrettingLateModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCheckNews(userType: String, userId: Int) {
        compositeDisposable.add(
            repository.getCheckNews(userType, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckNewsResponseModel>() {
                    override fun onSuccess(t: CheckNewsResponseModel) {
                        if (t.code == 200) {
                            getCheckHomeNewsModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CheckNewsResponseModel::class.java
                                )
                                getCheckHomeNewsModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListHomeNews(page: Int, userId: Int, userType: String) {
        compositeDisposable.add(
            repository.getListHomeNews(page, userId, userType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListHomeNewsResponseModel>() {
                    override fun onSuccess(t: ListHomeNewsResponseModel) {
                        if (t.code == 200) {
                            getListHomeNewsModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListHomeNewsResponseModel::class.java
                                )
                                getListHomeNewsModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putReadNews(userType: String, userId: Int, newsId: Int) {
        compositeDisposable.add(
            repository.putReadNews(userType, userId, newsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReadNewsResponseModel>() {
                    override fun onSuccess(t: ReadNewsResponseModel) {
                        if (t.code == 200) {
                            putReadNewsModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReadNewsResponseModel::class.java
                                )
                                putReadNewsModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailNews(idNews: Int) {
        compositeDisposable.add(
            repository.getDetailNews(idNews)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailNewsResponseModel>() {
                    override fun onSuccess(t: DetailNewsResponseModel) {
                        if (t.code == 200) {
                            getDetailNewsModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailNewsResponseModel::class.java
                                )
                                getDetailNewsModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun UploadDocument(employeeId: Int, imageDocument: MultipartBody.Part, typeDocument: String) {
        compositeDisposable.add(
            repository.uploadDocument(employeeId, imageDocument, typeDocument)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UploadDocumentResponseModel>() {
                    override fun onSuccess(t: UploadDocumentResponseModel) {
                        if (t.code == 200) {
                            putUploadDocumentModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UploadDocumentResponseModel::class.java
                                )
                                putUploadDocumentModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListDocument(employeeId: Int) {
        compositeDisposable.add(
            repository.getListDocument(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListDocumentResponseModel>() {
                    override fun onSuccess(t: ListDocumentResponseModel) {
                        if (t.code == 200) {
                            getListDocumentModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListDocumentResponseModel::class.java
                                )
                                getListDocumentModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListVaccine(employeeId: Int, page: Int) {
        compositeDisposable.add(
            repository.getListVaccine(employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListVaccineResponseModel>() {
                    override fun onSuccess(t: ListVaccineResponseModel) {
                        if (t.code == 200) {
                            getListVaccineModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListVaccineResponseModel::class.java
                                )
                                getListVaccineModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun updateVaccine(employeeId: Int, file: MultipartBody.Part, typeVaccine: String) {
        compositeDisposable.add(
            repository.uploadVaccine(employeeId, file, typeVaccine)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateVaccineResponseModel>() {
                    override fun onSuccess(t: UpdateVaccineResponseModel) {
                        if (t.code == 200) {
                            updateVaccineModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateVaccineResponseModel::class.java
                                )
                                updateVaccineModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun changeCertificateVaccine(idNews: Int, file: MultipartBody.Part) {
        compositeDisposable.add(
            repository.changeVaccine(idNews, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChangeVaccineResponseModel>() {
                    override fun onSuccess(t: ChangeVaccineResponseModel) {
                        if (t.code == 200) {
                            putChangeVaccineModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChangeVaccineResponseModel::class.java
                                )
                                putChangeVaccineModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListTypeVaccine(employeeId: Int) {
        compositeDisposable.add(
            repository.getListTypeVaccine(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListVaccineTypeResponseModel>() {
                    override fun onSuccess(t: ListVaccineTypeResponseModel) {
                        if (t.code == 200) {
                            getListTypeVaccine.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListVaccineTypeResponseModel::class.java
                                )
                                getListTypeVaccine.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putUpdateBpjs(employeeId: Int, file: MultipartBody.Part, number: String, typeBpjs: String) {
        compositeDisposable.add(
            repository.putUploadBpjs(employeeId, file, number, typeBpjs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateBpjsResponseModel>() {
                    override fun onSuccess(t: UpdateBpjsResponseModel) {
                        if (t.code == 200) {
                            updateBpjsModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    UpdateBpjsResponseModel::class.java
                                )
                                updateBpjsModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }


    fun putLatLongArea(employeeId: Int, latitude: String, longitude: String, address: String) {
        compositeDisposable.add(
            repository.putLatLongArea(employeeId, latitude, longitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LatLongAreaResponseModel>() {
                    override fun onSuccess(t: LatLongAreaResponseModel) {
                        if (t.code == 200) {
                            putLatLongAreaModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LatLongAreaResponseModel::class.java
                                )
                                putLatLongAreaModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getComplaintValidate(
        projectCode: String,
        complaintType: ArrayList<String>
    ) {
        compositeDisposable.add(
            repository.getComplaintValidate(projectCode, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ComplaintValidateResponse>(){
                    override fun onSuccess(t: ComplaintValidateResponse) {
                        complaintValidateModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ComplaintValidateResponse::class.java
                                )
                                complaintValidateModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })

        )
    }

    fun getCheckAttendance(employeeId: Int){
        compositeDisposable.add(
            repository.getCheckAttendance3times(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckNotAttendanceResponseModel>(){
                    override fun onSuccess(t: CheckNotAttendanceResponseModel) {
                        getCheckAttendanceModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CheckNotAttendanceResponseModel::class.java
                                )
                                getCheckAttendanceModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getTimeShiftEmployee(employeeId: Int, projectCode: String){
       compositeDisposable.add(
           repository.getTimeShiftEmployee(employeeId, projectCode)
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object : DisposableSingleObserver<GetTimeShiftResponseModel>(){
                   override fun onSuccess(t: GetTimeShiftResponseModel) {
                       getTimeShiftModel.value = t
                   }

                   override fun onError(e: Throwable) {
                       if (e is HttpException) {
                           if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                               Toast.makeText(
                                   context,
                                   "UNAUTHORIZED.",
                                   Toast.LENGTH_SHORT
                               ).show()
                           } else {
                               val errorBody = e.response()?.errorBody()
                               val gson = Gson()
                               val error = gson.fromJson(
                                   errorBody?.string(),
                                   CheckNotAttendanceResponseModel::class.java
                               )
                               getCheckAttendanceModel.value = error
                           }
                           isLoading?.value = false
                       } else {
                           isLoading?.value = true
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
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    fun dashboardMR(
        projectCode : String,
        page : Int,
        size : Int
    ){
        compositeDisposable.add(
            repository.dashboardMR(projectCode, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MRDashboardResponse>(){
                    override fun onSuccess(t: MRDashboardResponse) {
                        dashboardMR.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    MRDashboardResponse::class.java
                                )
                                dashboardMR.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun createMR(
        createMaterialRequest: CreateMaterialRequest
    ){
        compositeDisposable.add(
            repository.createMR(createMaterialRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateMRResponse>(){
                    override fun onSuccess(t: CreateMRResponse) {
                        createMRResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateMRResponse::class.java
                                )
                                createMRResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun createMRFollowUp(
        createMaterialRequest: CreateMaterialRequest
    ){
        compositeDisposable.add(
            repository.createMRFollowUp(createMaterialRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateMRResponse>(){
                    override fun onSuccess(t: CreateMRResponse) {
                        createMRResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateMRResponse::class.java
                                )
                                createMRResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getItemMR(
       filter : String
    ){
        compositeDisposable.add(
            repository.getItemMR(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ItemMRDataResponse>(){
                    override fun onSuccess(t: ItemMRDataResponse) {
                        getItemMR.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ItemMRDataResponse::class.java
                                )
                                getItemMR.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getUnitMR(
        filter : String
    ){
        compositeDisposable.add(
            repository.getUnitMR(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SatuanResponse>(){
                    override fun onSuccess(t: SatuanResponse) {
                        getUnitMR.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SatuanResponse::class.java
                                )
                                getUnitMR.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun approveMr(
        employeeId : Int,
        idMaterial : Int,
    ){
        compositeDisposable.add(
            repository.approveMR(employeeId,idMaterial)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateMRResponse>(){
                    override fun onSuccess(t: CreateMRResponse) {
                        approveMR.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateMRResponse::class.java
                                )
                                approveMR.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListUsedMR(
        projectCode : String
    ){
        compositeDisposable.add(
            repository.getDataListHistoryUsed(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListHistoryUsedResponse>(){
                    override fun onSuccess(t: ListHistoryUsedResponse) {
                        getMRUsed.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListHistoryUsedResponse::class.java
                                )
                                getMRUsed.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListStockMR(
        projectCode : String
    ){
        compositeDisposable.add(
            repository.getDataListHistoryStock(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListHistoryStockResponse>(){
                    override fun onSuccess(t: ListHistoryStockResponse) {
                        getMRStock.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListHistoryStockResponse::class.java
                                )
                                getMRStock.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListUsed(
        projectCode : String,
        date: String,
    ){
        compositeDisposable.add(
            repository.getDataListUsed(projectCode,date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListHistoryUsedResponse>(){
                    override fun onSuccess(t: ListHistoryUsedResponse) {
                        getListUsed.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListHistoryUsedResponse::class.java
                                )
                                getListUsed.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun createUsed(
        idProject: String,idItem : Int,quantity : Int,unit : String,userId : Int
    ){
        compositeDisposable.add(
            repository.createMRUsed(idProject,idItem,quantity,unit,userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateMRResponse>(){
                    override fun onSuccess(t: CreateMRResponse) {
                        createUsed.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401) {
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateMRResponse::class.java
                                )
                                createUsed.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCheckProfileViewModels(): MutableLiveData<CheckProfileEmployeeResponse>{
        return cekEditProfileModel
    }

    fun getTimeShiftEmployeeViewModel(): MutableLiveData<GetTimeShiftResponseModel>{
        return getTimeShiftModel
    }

    fun getCheckAttendanceViewModel(): MutableLiveData<CheckNotAttendanceResponseModel> {
        return getCheckAttendanceModel
    }

    fun putLatLongAreaViewModel(): MutableLiveData<LatLongAreaResponseModel> {
        return putLatLongAreaModel
    }

    fun putUpdateBpjsViewModel(): MutableLiveData<UpdateBpjsResponseModel> {
        return updateBpjsModel
    }

    fun getListTypeVaccineViewModel(): MutableLiveData<ListVaccineTypeResponseModel> {
        return getListTypeVaccine
    }

    fun changeVaccineViewModel(): MutableLiveData<ChangeVaccineResponseModel> {
        return putChangeVaccineModel
    }

    fun updateVaccineViewModel(): MutableLiveData<UpdateVaccineResponseModel> {
        return updateVaccineModel
    }

    fun getListVaccineViewModel(): MutableLiveData<ListVaccineResponseModel> {
        return getListVaccineModel
    }

    fun getListDocumentViewModel(): MutableLiveData<ListDocumentResponseModel> {
        return getListDocumentModel
    }

    fun uploadDocumentViewModel(): MutableLiveData<UploadDocumentResponseModel> {
        return putUploadDocumentModel
    }

    fun getDetailNewsViewModel(): MutableLiveData<DetailNewsResponseModel> {
        return getDetailNewsModel
    }

    fun putReadNewsViewModel(): MutableLiveData<ReadNewsResponseModel> {
        return putReadNewsModel
    }

    fun getListNewsViewModel(): MutableLiveData<ListHomeNewsResponseModel> {
        return getListHomeNewsModel
    }

    fun getCheckHomeNewsViewModel(): MutableLiveData<CheckNewsResponseModel> {
        return getCheckHomeNewsModel
    }

    fun getGreetingLateViewModel(): MutableLiveData<GreetingLateResponseModel> {
        return getGrettingLateModel
    }

    fun updateAccountNumbViewModel(): MutableLiveData<UpdateAccountNumbReponseModel> {
        return updateAccountNumberModel
    }

    fun updateNumberFamsViewModel(): MutableLiveData<UpdateFamsNumberResponseModel> {
        return updateFamsNumberModel
    }

    fun getBadgeNotifOperator(): MutableLiveData<badgeNotifResponseModel> {
        return getBadgeNotificationOperator
    }

    fun getBadgeNotifPengawas(): MutableLiveData<badgeNotifResponseModel> {
        return getBadgeNotification
    }

    fun getDacCount(): MutableLiveData<DacCountModel> {
        return dacCountModel
    }

    fun getProfileModel(): MutableLiveData<ProfileModel> {
        return profileModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}