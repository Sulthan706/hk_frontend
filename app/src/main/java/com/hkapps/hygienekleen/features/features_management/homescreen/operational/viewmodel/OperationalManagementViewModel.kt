package com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.repository.OperationalManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.branchoperational.BranchOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailattendance.DetailAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailmanagement.DetailManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.detailoperational.DetailOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.historyattendance.HistoryAttendanceOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listalloperational.ListAllOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listceomanagement.ListCEOManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listemployee.ListOprtEmployeOperationalResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listgmfmom.ListGmOmFmResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listmanagement.ListOprtManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalbyprojectcode.ListOperationalByProjectCodeResponseModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.model.listoperationalproject.ListOperationalProjectResponseModel
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

class OperationalManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val listOperationalModel = MutableLiveData<ListAllOperationalResponseModel>()
    private val listCeoModel = MutableLiveData<ListCEOManagementResponseModel>()
    private val listBodModel = MutableLiveData<ListCEOManagementResponseModel>()
    private val listGmOmFm = MutableLiveData<ListGmOmFmResponseModel>()
    private val detailOperationalModel = MutableLiveData<DetailOperationalResponseModel>()
    private val detailManagementModel = MutableLiveData<DetailManagementResponseModel>()
    private val detailOperationalAttendance = MutableLiveData<DetailAttendanceOperationalResponseModel>()
    private val historyAttendanceOperational = MutableLiveData<HistoryAttendanceOperationalResponseModel>()
    private val listOperationalProjectModel = MutableLiveData<ListOperationalProjectResponseModel>()
    private val searchOperationalCeoBodModel = MutableLiveData<ListAllOperationalResponseModel>()
    private val searchOperationalGmOmFmModel = MutableLiveData<ListOperationalProjectResponseModel>()
    private val searchManagementUserCeo = MutableLiveData<ListCEOManagementResponseModel>()
    private val searchManagementUserBod = MutableLiveData<ListCEOManagementResponseModel>()
    private val searchManagementUserGmOmFm = MutableLiveData<ListGmOmFmResponseModel>()
    private val listOperationalByProjectCode = MutableLiveData<ListOperationalByProjectCodeResponseModel>()
    val listOperationalByRoleCeoModel = MutableLiveData<ListAllOperationalResponseModel>()
    val listOperationalByRoleProjectModel = MutableLiveData<ListOperationalProjectResponseModel>()
    val listManagementByRoleCeoBodModel = MutableLiveData<ListGmOmFmResponseModel>()
    val listManagementByRoleFmGMOmModel = MutableLiveData<ListGmOmFmResponseModel>()

    private val branchOperationalModel = MutableLiveData<BranchOperationalResponseModel>()
    private val branchManagementModel = MutableLiveData<BranchOperationalResponseModel>()
    private val getManagementOperationalModel = MutableLiveData<ListOprtManagementResponseModel>()
    private val getEmployeeOperationalModel = MutableLiveData<ListOprtEmployeOperationalResponseModel>()


    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    @Inject
    lateinit var repository: OperationalManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListAllOperational(page: Int){
        compositeDisposable.add(
            repository.getListAllOperational(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAllOperationalResponseModel>(){

                    override fun onSuccess(t: ListAllOperationalResponseModel) {
                        if (t.code == 200) {
                            listOperationalModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllOperationalResponseModel::class.java
                                )
                                listOperationalModel.value = error
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

    fun getListOperationalByRoleCEO(jobRole: String, page: Int) {
        compositeDisposable.add(
            repository.getListOperationalByRoleCEO(jobRole, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAllOperationalResponseModel>() {
                    override fun onSuccess(t: ListAllOperationalResponseModel) {
                        if (t.code == 200) {
                            listOperationalByRoleCeoModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllOperationalResponseModel::class.java
                                )
                                listOperationalByRoleCeoModel.value = error
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

    fun getListCEO(page: Int){
        compositeDisposable.add(
            repository.getListAllOperationalCEO(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCEOManagementResponseModel>(){

                    override fun onSuccess(t: ListCEOManagementResponseModel) {
                        if (t.code == 200) {
                            listCeoModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCEOManagementResponseModel::class.java
                                )
                                listCeoModel.value = error
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

    fun getListBOD(page: Int){
        compositeDisposable.add(
            repository.getListAllOperationalBOD(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCEOManagementResponseModel>(){

                    override fun onSuccess(t: ListCEOManagementResponseModel) {
                        if (t.code == 200) {
                            listBodModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCEOManagementResponseModel::class.java
                                )
                                listBodModel.value = error
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

    fun getGmOmFm(employeeId: Int){
        compositeDisposable.add(
            repository.getListGmOmFm(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListGmOmFmResponseModel>(){

                    override fun onSuccess(t: ListGmOmFmResponseModel) {
                        if (t.code == 200) {
                            listGmOmFm.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListGmOmFmResponseModel::class.java
                                )
                                listGmOmFm.value = error
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

    fun getDetailOperational(employeeId: Int){
        compositeDisposable.add(
            repository.getDetailOperational(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailOperationalResponseModel>(){

                    override fun onSuccess(t: DetailOperationalResponseModel) {
                        if (t.code == 200) {
                            detailOperationalModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailOperationalResponseModel::class.java
                                )
                                detailOperationalModel.value = error
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

    fun getDetailManagement(adminMasterId: Int){
        compositeDisposable.add(
            repository.getDetailManagement(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailManagementResponseModel>(){

                    override fun onSuccess(t: DetailManagementResponseModel) {
                        if (t.code == 200) {
                            detailManagementModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailManagementResponseModel::class.java
                                )
                                detailManagementModel.value = error
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

    fun getDetailOperationalAttendance(projectCode: String, employeeId: Int, month: Int, year: Int){
        compositeDisposable.add(
            repository.getDetailOperationalAttendance(projectCode, employeeId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAttendanceOperationalResponseModel>(){

                    override fun onSuccess(t: DetailAttendanceOperationalResponseModel) {
                        if (t.code == 200) {
                            detailOperationalAttendance.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAttendanceOperationalResponseModel::class.java
                                )
                                detailOperationalAttendance.value = error
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

    fun getHistoryAttendanceOperational(employeeId: Int, projectCode: String){
        compositeDisposable.add(
            repository.getHistoryAttendance(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryAttendanceOperationalResponseModel>(){

                    override fun onSuccess(t: HistoryAttendanceOperationalResponseModel) {
                        if (t.code == 200) {
                            historyAttendanceOperational.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    HistoryAttendanceOperationalResponseModel::class.java
                                )
                                historyAttendanceOperational.value = error
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

    fun getListOperationalByRoleProject(employeeId: Int, jobRole: String, page: Int) {
        compositeDisposable.add(
            repository.getListOperationalByRoleProject(employeeId, jobRole, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperationalProjectResponseModel>() {
                    override fun onSuccess(t: ListOperationalProjectResponseModel) {
                        if (t.code == 200) {
                            listOperationalByRoleProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperationalProjectResponseModel::class.java
                                )
                                listOperationalByRoleProjectModel.value = error
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

    fun getListOperationalProject(employeeId: Int, page: Int){
        compositeDisposable.add(
            repository.getListOperationalProject(employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperationalProjectResponseModel>(){

                    override fun onSuccess(t: ListOperationalProjectResponseModel) {
                        if (t.code == 200) {
                            listOperationalProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException ->{
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperationalProjectResponseModel::class.java
                                )
                                listOperationalProjectModel.value = error
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

    fun getSearchOperationalCeoBod(page: Int, keywords: String){
        compositeDisposable.add(
            repository.searchOperationalCeoBod(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAllOperationalResponseModel>() {
                    override fun onSuccess(t: ListAllOperationalResponseModel) {
                        if(t.code == 200){
                            searchOperationalCeoBodModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAllOperationalResponseModel::class.java
                                )
                                searchOperationalCeoBodModel.value = error
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

    fun getSearchOperationalGmOmFm(employeeId: Int, page: Int, keywords: String){
        compositeDisposable.add(
            repository.searchOperationalGmOmFm(employeeId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperationalProjectResponseModel>() {
                    override fun onSuccess(t: ListOperationalProjectResponseModel) {
                        if(t.code == 200){
                            searchOperationalGmOmFmModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperationalProjectResponseModel::class.java
                                )
                                searchOperationalGmOmFmModel.value = error
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

    fun getSearchManagementUserCeo(page: Int, keywords: String){
        compositeDisposable.add(
            repository.searchManagementUserCeo(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCEOManagementResponseModel>() {
                    override fun onSuccess(t: ListCEOManagementResponseModel) {
                        if(t.code == 200){
                            searchManagementUserCeo.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCEOManagementResponseModel::class.java
                                )
                                searchManagementUserCeo.value = error
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

    fun getSearchManagementUserBod(page: Int, keywords: String){
        compositeDisposable.add(
            repository.searchManagementUserBod(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListCEOManagementResponseModel>() {
                    override fun onSuccess(t: ListCEOManagementResponseModel) {
                        if(t.code == 200){
                            searchManagementUserBod.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListCEOManagementResponseModel::class.java
                                )
                                searchManagementUserBod.value = error
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

    fun getSearchManagementUserGmOmFm(employeeId: Int, keywords: String){
        compositeDisposable.add(
            repository.searchManagementUserGmOmFm(employeeId, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListGmOmFmResponseModel>() {
                    override fun onSuccess(t: ListGmOmFmResponseModel) {
                        if(t.code == 200){
                            searchManagementUserGmOmFm.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListGmOmFmResponseModel::class.java
                                )
                                searchManagementUserGmOmFm.value = error
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

    fun getListOperationalByProjectCode(jobRole : String, projectCode: String, page: Int){
        compositeDisposable.add(
            repository.getListOperationalByProjectCode(jobRole, projectCode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperationalByProjectCodeResponseModel>() {
                    override fun onSuccess(t: ListOperationalByProjectCodeResponseModel) {
                        if(t.code == 200){
                            listOperationalByProjectCode.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperationalByProjectCodeResponseModel::class.java
                                )
                                listOperationalByProjectCode.value = error
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

    fun getListManagementByRoleCeoBod(jabatan: String) {
        compositeDisposable.add(
            repository.getListManagementByRoleCeoBod(jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListGmOmFmResponseModel>() {
                    override fun onSuccess(t: ListGmOmFmResponseModel) {
                        if (t.code == 200) {
                            listManagementByRoleCeoBodModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListGmOmFmResponseModel::class.java
                                )
                                listManagementByRoleCeoBodModel.value = error
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

    fun getListManagementByRoleFmGmOm(employeeId: Int, jabatan: String) {
        compositeDisposable.add(
            repository.getListManagementByRoleFmGmOm(employeeId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListGmOmFmResponseModel>() {
                    override fun onSuccess(t: ListGmOmFmResponseModel) {
                        if (t.code == 200) {
                            listManagementByRoleFmGMOmModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListGmOmFmResponseModel::class.java
                                )
                                listManagementByRoleFmGMOmModel.value = error
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

    fun getBranchEmployeeOperational(page: Int, size: Int){
        compositeDisposable.add(
            repository.getBranchOperational(page,size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchOperationalResponseModel>(){
                    override fun onSuccess(t: BranchOperationalResponseModel) {
                        if (t.code == 200) {
                            branchOperationalModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchOperationalResponseModel::class.java
                                )
                                branchOperationalModel.value = error
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

    fun getBranchManagementOperational(page: Int, size: Int){
        compositeDisposable.add(
            repository.getBranchManagementOperational(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BranchOperationalResponseModel>(){
                    override fun onSuccess(t: BranchOperationalResponseModel) {
                        if (t.code == 200) {
                            branchManagementModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    BranchOperationalResponseModel::class.java
                                )
                                branchManagementModel.value = error
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

    fun getManagementOpertional(idCabang: Int, role: String, page: Int, size: Int){
        compositeDisposable.add(
            repository.getListManagementOperational(idCabang, role, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOprtManagementResponseModel>(){
                    override fun onSuccess(t: ListOprtManagementResponseModel) {
                        if (t.code == 200) {
                            getManagementOperationalModel.value = t
                        }
                        isLoading?.value = false
                    }


                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOprtManagementResponseModel::class.java
                                )
                                getManagementOperationalModel.value = error
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

    fun getEmployeeOperational(branchCode: String, role: String, page: Int, size: Int) {
        compositeDisposable.add(
            repository.getListEmployeeOperational(branchCode, role, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOprtEmployeOperationalResponseModel>(){
                    override fun onSuccess(t: ListOprtEmployeOperationalResponseModel) {
                        if (t.code == 200) {
                            getEmployeeOperationalModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOprtEmployeOperationalResponseModel::class.java
                                )
                                getEmployeeOperationalModel.value = error
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

    fun getEmployeeOpertionalViewModel():MutableLiveData<ListOprtEmployeOperationalResponseModel>{
        return getEmployeeOperationalModel
    }

    fun getManagementOperationalViewModel(): MutableLiveData<ListOprtManagementResponseModel>{
        return getManagementOperationalModel
    }

    fun getBranchManagementOperationalViewModel(): MutableLiveData<BranchOperationalResponseModel>{
        return branchManagementModel
    }

    fun getBranchOperationalEmployeeViewModel(): MutableLiveData<BranchOperationalResponseModel>{
        return branchOperationalModel
    }

    fun getListAllOperationalResponse(): MutableLiveData<ListAllOperationalResponseModel> {
        return listOperationalModel
    }

    fun getListCEOResponse(): MutableLiveData<ListCEOManagementResponseModel> {
        return listCeoModel
    }

    fun getListBODResponse(): MutableLiveData<ListCEOManagementResponseModel> {
        return listBodModel
    }

    fun getListGmOmFmResponse(): MutableLiveData<ListGmOmFmResponseModel> {
        return listGmOmFm
    }

    fun getDetailOperationalResponse(): MutableLiveData<DetailOperationalResponseModel> {
        return detailOperationalModel
    }

    fun getDetailManagementResponse(): MutableLiveData<DetailManagementResponseModel> {
        return detailManagementModel
    }

    fun getDetailOperationalAttendanceResponse(): MutableLiveData<DetailAttendanceOperationalResponseModel> {
        return detailOperationalAttendance
    }

    fun getHistoryAttendanceOperationalResponse(): MutableLiveData<HistoryAttendanceOperationalResponseModel> {
        return historyAttendanceOperational
    }

    fun getListOperationalProjectResponse(): MutableLiveData<ListOperationalProjectResponseModel> {
        return listOperationalProjectModel
    }

    fun getSearchOperationalCeoBodResponse(): MutableLiveData<ListAllOperationalResponseModel> {
        return searchOperationalCeoBodModel
    }

    fun getSearchOperationalGmOmFmResponse(): MutableLiveData<ListOperationalProjectResponseModel> {
        return searchOperationalGmOmFmModel
    }

    fun getSearchManagementUserCeoResponse(): MutableLiveData<ListCEOManagementResponseModel> {
        return searchManagementUserCeo
    }
    fun getSearchManagementUserBodResponse(): MutableLiveData<ListCEOManagementResponseModel> {
        return searchManagementUserBod
    }
    fun getSearchManagementUserGmOmFmResponse(): MutableLiveData<ListGmOmFmResponseModel> {
        return searchManagementUserGmOmFm
    }

    fun getListOperationalByProjectCodeResponse(): MutableLiveData<ListOperationalByProjectCodeResponseModel> {
        return listOperationalByProjectCode
    }

}