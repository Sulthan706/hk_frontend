package com.hkapps.hygienekleen.features.features_management.report.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.report.data.repository.ReportManagementRepository
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
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ReportManagementViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //val viewmodel
    private val getMainReportCftalkModel =
        MutableLiveData<ReportMainCftalkResponseModel>()
    private val getMainReportLowCftalkModel =
        MutableLiveData<ReportMainCftalkResponseModel>()
    private val getSearchProjectModelBotSheet =
        MutableLiveData<BotSheetSearchProjectResponseModel>()
    private val getRecapTotalDailyComplaintModel =
        MutableLiveData<RecapTotalComplaintResponseModel>()
    private val getListAllProjectHighModel =
        MutableLiveData<ListAllProjectHighResponseModel>()
    private val getSearchLowProjectModel =
        MutableLiveData<BotSheetSearchLowReponseModel>()
    //cftalk
    private val getDetailReportCftalkModel =
        MutableLiveData<DetailReportCftalkResponseModel>()
    private val getListAllProjectLowModel =
        MutableLiveData<ListAllProjectLowResponseModel>()

    //ctalk
    private val getMainReportCtalkModel =
        MutableLiveData<ReportMainCtalkResponseModel>()
    private val getMainReportLowCtalkModel =
        MutableLiveData<ReportMainCtalkResponseModel>()
    //branch
    private val getCardListBranchModel =
        MutableLiveData<CardListBranchResponseModel>()

    private val getListProjectReportModel =
        MutableLiveData<ListProjectForBranchReponseModel>()
    //close complaint
    private val putCloseComplaintCftalkModel =
        MutableLiveData<CloseComplaintCftalkResponseModel>()


    @Inject
    lateinit var repository: ReportManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }


    fun getMainReportCftalk(
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ) {
        compositeDisposable.add(
            repository.getMainReportCftalkManagement(
                page,
                projectCode,
                statusComplaint,
                listIdTitle,
                startDate,
                endDate,
                filterBy
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportMainCftalkResponseModel>() {
                    override fun onSuccess(t: ReportMainCftalkResponseModel) {
                        if (t.code == 200) {
                            getMainReportCftalkModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportMainCftalkResponseModel::class.java
                                )
                                getMainReportCftalkModel.value = error
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

    fun getMainReportLowCftalk(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ) {
        compositeDisposable.add(
            repository.getMainReportCftalkManagementLow(
                adminMasterId,
                page,
                projectCode,
                statusComplaint,
                listIdTitle,
                startDate,
                endDate,
                filterBy
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportMainCftalkResponseModel>() {
                    override fun onSuccess(t: ReportMainCftalkResponseModel) {
                        if (t.code == 200) {
                            getMainReportLowCftalkModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportMainCftalkResponseModel::class.java
                                )
                                getMainReportLowCftalkModel.value = error
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

    fun getSearchProjectBotSheet(page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchProject(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<BotSheetSearchProjectResponseModel>() {
                    override fun onSuccess(t: BotSheetSearchProjectResponseModel) {
                        if (t.code == 200) {
                            getSearchProjectModelBotSheet.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BotSheetSearchProjectResponseModel::class.java
                                )
                                getSearchProjectModelBotSheet.value = error
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

    fun getRecapTotalDailyComplaint(projectCode: String, startDate: String, endDate: String) {
        compositeDisposable.add(
            repository.getRecapTotalDailyMgmnt(projectCode, startDate, endDate)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<RecapTotalComplaintResponseModel>() {
                    override fun onSuccess(t: RecapTotalComplaintResponseModel) {
                        if (t.code == 200) {
                            getRecapTotalDailyComplaintModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    RecapTotalComplaintResponseModel::class.java
                                )
                                getRecapTotalDailyComplaintModel.value = error
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

    fun getListAllProjectHigh(page: Int, size: Int) {
        compositeDisposable.add(
            repository.getListAllProjectHigh(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<ListAllProjectHighResponseModel>() {
                    override fun onSuccess(t: ListAllProjectHighResponseModel) {
                        if (t.code == 200) {
                            getListAllProjectHighModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllProjectHighResponseModel::class.java
                                )
                                getListAllProjectHighModel.value = error
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

    fun getListAllProjectLow(adminMasterId: Int, page: Int) {
        compositeDisposable.add(
            repository.getListAllLowProjectLow(adminMasterId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAllProjectLowResponseModel>() {
                    override fun onSuccess(t: ListAllProjectLowResponseModel) {
                        if (t.code == 200) {
                            getListAllProjectLowModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllProjectLowResponseModel::class.java
                                )
                                getListAllProjectLowModel.value = error
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

    fun getSearchLowProject(adminMasterId: Int, page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchProjectLow(adminMasterId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BotSheetSearchLowReponseModel>() {
                    override fun onSuccess(t: BotSheetSearchLowReponseModel) {
                        if (t.code == 200) {
                            getSearchLowProjectModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BotSheetSearchLowReponseModel::class.java
                                )
                                getSearchLowProjectModel.value = error
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

    fun getReportDetailCftalk(complaintId: Int) {
        compositeDisposable.add(
            repository.getDetailReportCftalk(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<DetailReportCftalkResponseModel>() {
                    override fun onSuccess(t: DetailReportCftalkResponseModel) {
                        if (t.code == 200) {
                            getDetailReportCftalkModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailReportCftalkResponseModel::class.java
                                )
                                getDetailReportCftalkModel.value = error
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

    fun getMainReportCtalkManagement(
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ) {
        compositeDisposable.add(
            repository.getMainReportCtalkManagement(
                page,
                projectCode,
                statusComplaint,
                listIdTitle,
                startDate,
                endDate,
                filterBy
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportMainCtalkResponseModel>() {
                    override fun onSuccess(t: ReportMainCtalkResponseModel) {
                        if (t.code == 200) {
                            getMainReportCtalkModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportMainCtalkResponseModel::class.java
                                )
                                getMainReportCtalkModel.value = error
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

    fun getMainReportLowCtalk(
        adminMasterId: Int,
        page: Int,
        projectCode: String,
        statusComplaint: String,
        listIdTitle: Int,
        startDate: String,
        endDate: String,
        filterBy: String
    ) {
        compositeDisposable.add(
            repository.getMainReportCtalkManagementLow(
                adminMasterId,
                page,
                projectCode,
                statusComplaint,
                listIdTitle,
                startDate,
                endDate,
                filterBy
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportMainCtalkResponseModel>() {
                    override fun onSuccess(t: ReportMainCtalkResponseModel) {
                       if (t.code == 200){
                           getMainReportLowCtalkModel.value = t
                       }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportMainCtalkResponseModel::class.java
                                )
                                getMainReportLowCtalkModel.value = error
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



    fun getListProjectReport(branchCode: String, date: String, page: Int){
        compositeDisposable.add(
            repository.getListReportProject(branchCode, date, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListProjectForBranchReponseModel>(){
                    override fun onSuccess(t: ListProjectForBranchReponseModel) {
                        if (t.code == 200){
                            getListProjectReportModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListProjectForBranchReponseModel::class.java
                                )
                                getListProjectReportModel.value = error
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

    fun closeComplaintCftalk(complaintId: Int){
        compositeDisposable.add(
            repository.putCloseComplaintCftalk(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CloseComplaintCftalkResponseModel>(){
                    override fun onSuccess(t: CloseComplaintCftalkResponseModel) {
                        if (t.code == 200){
                            putCloseComplaintCftalkModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CloseComplaintCftalkResponseModel::class.java
                                )
                                putCloseComplaintCftalkModel.value = error
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

    fun getCardListBranch(date: String, filterBy: String){
        compositeDisposable.add(
            repository.getCardListBranch(date,filterBy)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CardListBranchResponseModel>(){
                    override fun onSuccess(t: CardListBranchResponseModel) {
                        if (t.code == 200){
                            getCardListBranchModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CardListBranchResponseModel::class.java
                                )
                                getCardListBranchModel.value = error
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

    //call viewmodel for ui screen

    fun getCardListBranchViewModel(): MutableLiveData<CardListBranchResponseModel>{
        return getCardListBranchModel
    }
    fun putCloseComplaintCftalkViewModel(): MutableLiveData<CloseComplaintCftalkResponseModel>{
        return putCloseComplaintCftalkModel
    }

    fun getListProjectReportViewModel(): MutableLiveData<ListProjectForBranchReponseModel>{
        return getListProjectReportModel
    }


    fun getMainReportLowCtalkViewModel(): MutableLiveData<ReportMainCtalkResponseModel>{
        return getMainReportLowCtalkModel
    }

    fun getMainReportCtalkViewModel(): MutableLiveData<ReportMainCtalkResponseModel> {
        return getMainReportCtalkModel
    }

    fun getReportDetailCftalkViewModel(): MutableLiveData<DetailReportCftalkResponseModel> {
        return getDetailReportCftalkModel
    }

    fun getSearchLowProjectViewModel(): MutableLiveData<BotSheetSearchLowReponseModel> {
        return getSearchLowProjectModel
    }

    fun getListProjectHighViewModel(): MutableLiveData<ListAllProjectHighResponseModel> {
        return getListAllProjectHighModel
    }

    fun getListProjectLowViewModel(): MutableLiveData<ListAllProjectLowResponseModel> {
        return getListAllProjectLowModel
    }

    fun getSearchProjectBotSheetViewModel(): MutableLiveData<BotSheetSearchProjectResponseModel> {
        return getSearchProjectModelBotSheet
    }

    fun getMainReportCftalkViewModel(): MutableLiveData<ReportMainCftalkResponseModel> {
        return getMainReportCftalkModel
    }

    fun getMainReportLowCftalkViewModel(): MutableLiveData<ReportMainCftalkResponseModel> {
        return getMainReportLowCftalkModel
    }

    fun getRecapTotalDailyViewModel(): MutableLiveData<RecapTotalComplaintResponseModel> {
        return getRecapTotalDailyComplaintModel
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}