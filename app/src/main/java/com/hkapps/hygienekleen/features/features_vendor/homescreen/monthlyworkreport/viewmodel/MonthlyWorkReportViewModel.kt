package com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.repository.MonthlyWorkReportRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.approvejob.ApproveJobRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.createba.CreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.DailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.dailytarget.ListDailyTargetResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.detailchecklistrkb.DetailChecklistRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.escallationrkb.EscallationLowLevResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.eventcalendar.EventCalendarRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkb.HomeMonthlyWorkReportResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.homerkbnew.HomeRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.limitcreateba.LimitasiCreateBaResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.monthrkb.MonthRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.rkboperator.GetStatsRkbOperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.statusapprovejobs.StatusApprovalJobResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbydaterkb.TargetByDateRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.targetbystatus.TargetStatusRkbResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.model.uploadjob.succesupload.SuccessUploadPhotoResponseModel
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

class MonthlyWorkReportViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val getHomeMonthlyWorkReportModel = MutableLiveData<HomeMonthlyWorkReportResponseModel>()
    val getHomeRkbModel = MutableLiveData<HomeRkbResponseModel>()
    val getHomeListWorkModel = MutableLiveData<MonthRkbResponseModel>()

    val getTargetMonthRkbModel = MutableLiveData<MonthRkbResponseModel>()
    val getListStatusMonthRkbModel = MutableLiveData<TargetStatusRkbResponseModel>()
    val getTargetByDateRkbModel = MutableLiveData<TargetByDateRkbResponseModel>()
    val getDetailChecklistRkbModel = MutableLiveData<DetailChecklistRkbResponseModel>()
    val getCalendarEventRkbModel = MutableLiveData<EventCalendarRkbResponseModel>()
    //delete
    val getStatusApprovJobModel = MutableLiveData<StatusApprovalJobResponseModel>()
    val getEscalationLowLevelModel = MutableLiveData<EscallationLowLevResponseModel>()

    val putCreateBaRkbModel = MutableLiveData<CreateBaResponseModel>()
    val putJobRkbModel = MutableLiveData<SuccessUploadPhotoResponseModel>()
    val putApproveJobModel = MutableLiveData<ApproveJobRkbResponseModel>()
    val getLimitasiCreateBaModel = MutableLiveData<LimitasiCreateBaResponseModel>()
    val getStatsRkbOperatorModel = MutableLiveData<GetStatsRkbOperatorResponseModel>()

    val getRkbDailyTargetModel = MutableLiveData<DailyTargetResponse>()

    val getListRkbDailyTargetModel = MutableLiveData<ListDailyTargetResponse>()

    val getDetailRkbDailyTargetModel = MutableLiveData<DailyTargetResponse>()

    @Inject
    lateinit var repository: MonthlyWorkReportRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListStatusMonthRkb(
        employeeId: Int,
        projectCode: String,
        startDate: String,
        endDate: String,
        filterBy: String,
        page: Int,
        perPage: Int,
        locationId : Int

    ) {
        compositeDisposable.add(
            repository.getListStatusRkb(
                employeeId,
                projectCode,
                startDate,
                endDate,
                filterBy,
                page,
                perPage,
                locationId
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TargetStatusRkbResponseModel>() {
                    override fun onSuccess(t: TargetStatusRkbResponseModel) {
                        if (t.code == 200) {
                            getListStatusMonthRkbModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TargetStatusRkbResponseModel::class.java
                                )
                                getListStatusMonthRkbModel.value = error
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

    fun getTargetByDateRkb(employeeId: Int, projectCode: String, date: String, page: Int, perPage: Int){
        compositeDisposable.add(
            repository.getTargetByDateRkb(employeeId, projectCode, date, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TargetByDateRkbResponseModel>(){
                    override fun onSuccess(t: TargetByDateRkbResponseModel) {
                        getTargetByDateRkbModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    TargetByDateRkbResponseModel::class.java
                                )
                                getTargetByDateRkbModel.value = error
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

    fun getDetailChecklist(idJobs: Int){
        compositeDisposable.add(
            repository.getDetailChecklistRkb(idJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailChecklistRkbResponseModel>(){
                    override fun onSuccess(t: DetailChecklistRkbResponseModel) {
                        getDetailChecklistRkbModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailChecklistRkbResponseModel::class.java
                                )
                                getDetailChecklistRkbModel.value = error
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

    fun putCreateBaRkb(idJob: Int, employeeId: Int, description: String, date: String,divertedShift : Int, file: MultipartBody.Part){
        compositeDisposable.add(
            repository.putCreateBaRkb(idJob, employeeId, description, date,divertedShift, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateBaResponseModel>(){
                    override fun onSuccess(t: CreateBaResponseModel) {
                        putCreateBaRkbModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateBaResponseModel::class.java
                                )
                                putCreateBaRkbModel.value = error
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
    fun getCalendarEvent(projectCode: String, employeeId: Int, month: String, year: String){
        compositeDisposable.add(
            repository.getEventCalendarRkb(projectCode, employeeId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<EventCalendarRkbResponseModel>(){
                    override fun onSuccess(t: EventCalendarRkbResponseModel) {
                        if (t.code == 200){
                            getCalendarEventRkbModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EventCalendarRkbResponseModel::class.java
                                )
                                getCalendarEventRkbModel.value = error
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
    fun getHomeRkb(employeeId: Int, projectCode: String){
        compositeDisposable.add(
            repository.getHomeRkb(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HomeRkbResponseModel>(){
                    override fun onSuccess(t: HomeRkbResponseModel) {
                        getHomeRkbModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HomeRkbResponseModel::class.java
                                )
                                getHomeRkbModel.value = error
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
    fun getHomeListWork(projectCode: String, startDate: String, endDate: String){
        compositeDisposable.add(
            repository.getHomeListWork(projectCode, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MonthRkbResponseModel>(){
                    override fun onSuccess(t: MonthRkbResponseModel) {
                        getHomeListWorkModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    MonthRkbResponseModel::class.java
                                )
                                getHomeListWorkModel.value = error
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
    fun putJobRkb(idJobs: Int, employeeId: Int, file : MultipartBody.Part, uploadType: String, comment: String){
        compositeDisposable.add(
            repository.putUploadPhotoRkb(idJobs, employeeId, file, uploadType, comment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<SuccessUploadPhotoResponseModel>(){
                    override fun onSuccess(t: SuccessUploadPhotoResponseModel) {
                        putJobRkbModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SuccessUploadPhotoResponseModel::class.java
                                )
                                putJobRkbModel.value = error
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
    fun putApproveJob(idJobs: Int, employeeId: Int){
        compositeDisposable.add(
            repository.putApproveJobRkb(idJobs, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApproveJobRkbResponseModel>(){
                    override fun onSuccess(t: ApproveJobRkbResponseModel) {
                        if (t.code == 200){
                            putApproveJobModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ApproveJobRkbResponseModel::class.java
                                )
                                putApproveJobModel.value = error
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
    fun getStatusApprovalJobs(employeeId: Int){
        compositeDisposable.add(
            repository.getStatusApproveJob(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatusApprovalJobResponseModel>(){
                    override fun onSuccess(t: StatusApprovalJobResponseModel) {
                        if (t.code == 200){
                            getStatusApprovJobModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    StatusApprovalJobResponseModel::class.java
                                )
                                getStatusApprovJobModel.value = error
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

    fun getEscalationLowLevel(employeeId: Int, projectCode: String){
        compositeDisposable.add(
            repository.getEscalationRkb(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EscallationLowLevResponseModel>(){
                    override fun onSuccess(t: EscallationLowLevResponseModel) {
                        getEscalationLowLevelModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EscallationLowLevResponseModel::class.java
                                )
                                getEscalationLowLevelModel.value = error
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

    fun getLimitasiCreateBa(employeeId: Int, idJobs: Int){
        compositeDisposable.add(
            repository.getLimitasiCreateBa(employeeId, idJobs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LimitasiCreateBaResponseModel>(){
                    override fun onSuccess(t: LimitasiCreateBaResponseModel) {
                        getLimitasiCreateBaModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LimitasiCreateBaResponseModel::class.java
                                )
                                getLimitasiCreateBaModel.value = error
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

    fun getStatsRkbOperator(employeeId: Int, projectCode: String){
        compositeDisposable.add(
            repository.getStatsRkbOperator(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetStatsRkbOperatorResponseModel>(){
                    override fun onSuccess(t: GetStatsRkbOperatorResponseModel) {
                        getStatsRkbOperatorModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    GetStatsRkbOperatorResponseModel::class.java
                                )
                                getStatsRkbOperatorModel.value = error
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

    fun getRkbDailyTarget(adminMasterId : Int,projectCode : String){
        compositeDisposable.add(
            repository.getRkbDailyTarget(adminMasterId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyTargetResponse>(){
                    override fun onSuccess(t: DailyTargetResponse) {
                         getRkbDailyTargetModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyTargetResponse::class.java
                                )
                                getRkbDailyTargetModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                }
        ))
    }


    fun getRkbListDailyTarget(adminMasterId : Int,projectCode : String){
        compositeDisposable.add(
            repository.getRkbDailyTargetV2(adminMasterId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListDailyTargetResponse>(){
                    override fun onSuccess(t: ListDailyTargetResponse) {
                        getListRkbDailyTargetModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListDailyTargetResponse::class.java
                                )
                                getListRkbDailyTargetModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                }
                ))
    }


    fun getDetailRkbDailyTarget(idDetailEmployeeProject : Int,employeeId : Int){
        compositeDisposable.add(
            repository.getDetailRkbDailyTarget(idDetailEmployeeProject,employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyTargetResponse>(){
                    override fun onSuccess(t: DailyTargetResponse) {
                        getDetailRkbDailyTargetModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyTargetResponse::class.java
                                )
                                getDetailRkbDailyTargetModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                                isLoading?.value = false
                            }
                            else -> isLoading?.value = true
                        }
                    }

                }
                ))
    }



    //in activity
    fun getStatsRkbOperatorViewModel(): MutableLiveData<GetStatsRkbOperatorResponseModel>{
        return getStatsRkbOperatorModel
    }
    fun getLimitasiCreateBaViewModel(): MutableLiveData<LimitasiCreateBaResponseModel>{
        return getLimitasiCreateBaModel
    }
    fun getEscalationLowlevelViewModel(): MutableLiveData<EscallationLowLevResponseModel>{
        return getEscalationLowLevelModel
    }

    fun getStatusApprovalJobViewModel(): MutableLiveData<StatusApprovalJobResponseModel>{
        return getStatusApprovJobModel
    }

    fun putApproveJobViewModel(): MutableLiveData<ApproveJobRkbResponseModel>{
        return putApproveJobModel
    }

    fun putJobRkbViewModel(): MutableLiveData<SuccessUploadPhotoResponseModel>{
        return putJobRkbModel
    }
    fun getHomeListWorkViewModel(): MutableLiveData<MonthRkbResponseModel>{
        return getHomeListWorkModel
    }

    fun getHomeRkbViewModel(): MutableLiveData<HomeRkbResponseModel>{
        return getHomeRkbModel
    }

    fun getEventCalendarRkbViewModel(): MutableLiveData<EventCalendarRkbResponseModel>{
        return getCalendarEventRkbModel
    }

    fun putCreateBaViewModel(): MutableLiveData<CreateBaResponseModel>{
        return putCreateBaRkbModel
    }
    fun getDetailChecklistRkbViewModel(): MutableLiveData<DetailChecklistRkbResponseModel>{
        return getDetailChecklistRkbModel
    }

    fun getTargetByDateMonthRkbViewModel(): MutableLiveData<TargetByDateRkbResponseModel>{
        return getTargetByDateRkbModel
    }
    fun getListStatusMonthRkbViewModel(): MutableLiveData<TargetStatusRkbResponseModel> {
        return getListStatusMonthRkbModel
    }

    fun getTargetMonthRkbViewModel(): MutableLiveData<MonthRkbResponseModel> {
        return getTargetMonthRkbModel
    }

    fun getHomeMonthlyWorkViewModel(): MutableLiveData<HomeMonthlyWorkReportResponseModel> {
        return getHomeMonthlyWorkReportModel
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}