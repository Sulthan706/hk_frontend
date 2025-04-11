package com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.repository.OvertimeManagementRepository
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.changeemployeemanagement.ChangeEmployeeMgmntResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.createOvertimeChange.CreateOvertimeChangeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.employeereplace.EmployeeReplaceResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.getlocationmanagement.LocationManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.getsublocationmanagement.SubLocationManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listOvertimeChange.OvertimeChangeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listProjectManagement.ProjectsOvertimeResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.listshift.ListShiftManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.plottingOperationalResponse.PlottingOperationalResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.searchProjectManagement.SearchProjectManagementResponseModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectEmployee.OperationalOvertimeManagementResponse
import com.hkapps.hygienekleen.features.features_management.service.overtime.model.selectemployeemanagement.SelectEmployeeMgmntResponseModel
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

class OvertimeManagementViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listOvertimeChangeResponse = MutableLiveData<OvertimeChangeManagementResponse>()
    val operationalOvertimeChangeResponse = MutableLiveData<OperationalOvertimeManagementResponse>()
    val replaceOperationalOvertimeChangeResponse = MutableLiveData<OperationalOvertimeManagementResponse>()
    val createOvertimeChangeResponse = MutableLiveData<CreateOvertimeChangeManagementResponse>()
    val projectsManagementOvertimeResponse = MutableLiveData<ProjectsOvertimeResponse>()
    val operationalOvertimeResignResponse = MutableLiveData<OperationalOvertimeManagementResponse>()
    val createOvertimeResignResponse = MutableLiveData<CreateOvertimeChangeManagementResponse>()
    val getSearchProjectManagementModel = MutableLiveData<SearchProjectManagementResponseModel>()

    val getEmployeeReplaceManagementModel = MutableLiveData<EmployeeReplaceResponseModel>()
    val getListShiftManagementModel = MutableLiveData<ListShiftManagementResponseModel>()
    val getEmployeeManagementModel = MutableLiveData<SelectEmployeeMgmntResponseModel>()
    val getChangeEmployeeManagementModel = MutableLiveData<ChangeEmployeeMgmntResponseModel>()

    val getLocationManagementModel = MutableLiveData<LocationManagementResponseModel>()
    val getSublocationManagementModel = MutableLiveData<SubLocationManagementResponseModel>()
    val getPlottingOperationalResponse = MutableLiveData<PlottingOperationalResponse>()


    @Inject
    lateinit var repository: OvertimeManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListOvertimeChange(
        employeeId: Int,
        jabatan: String,
        startDate: String,
        endDate: String,
        page: Int
    ) {
        compositeDisposable.add(
            repository.getListOvertimeChange(employeeId, jabatan, startDate, endDate, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OvertimeChangeManagementResponse>(){
                    override fun onSuccess(t: OvertimeChangeManagementResponse) {
                        listOvertimeChangeResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OvertimeChangeManagementResponse::class.java
                                )
                                listOvertimeChangeResponse.value = error
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

    fun getOperationalOvertimeChange(
        projectId: String,
        date: String,
        overtimeType: String,
        shiftId: Int,
        jabatan: String
    ) {
        compositeDisposable.add(
            repository.getOperationalOvertimeChange(projectId, date, overtimeType, shiftId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperationalOvertimeManagementResponse>() {
                    override fun onSuccess(t: OperationalOvertimeManagementResponse) {
                        operationalOvertimeChangeResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OperationalOvertimeManagementResponse::class.java
                                )
                                operationalOvertimeChangeResponse.value = error
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

    fun getReplaceOperationalOvertimeChange(
        projectId: String,
        date: String,
        shiftId: Int,
        jobCode: String,
        jabatan: String
    ) {
        compositeDisposable.add(
            repository.getReplaceOperationalOvertimeChange(projectId, date, shiftId, jobCode, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperationalOvertimeManagementResponse>(){
                    override fun onSuccess(t: OperationalOvertimeManagementResponse) {
                        replaceOperationalOvertimeChangeResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OperationalOvertimeManagementResponse::class.java
                                )
                                replaceOperationalOvertimeChangeResponse.value = error
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

    fun createOvertimeChangeManagement(
        createdById: Int,
        replaceOprId: Int,
        operationalId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ) {
        compositeDisposable.add(
            repository.createOvertimeChangeManagement(createdById, replaceOprId, operationalId, projectId, title, description, date, shiftId, overtimeType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateOvertimeChangeManagementResponse>(){
                    override fun onSuccess(t: CreateOvertimeChangeManagementResponse) {
                        createOvertimeChangeResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateOvertimeChangeManagementResponse::class.java
                                )
                                createOvertimeChangeResponse.value = error
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

    fun getProjectsOvertimeManagement(
        adminMasterId: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsOvertimeManagement(adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsOvertimeResponse>() {
                    override fun onSuccess(t: ProjectsOvertimeResponse) {
                        projectsManagementOvertimeResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsOvertimeResponse::class.java
                                )
                                projectsManagementOvertimeResponse.value = error
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

    fun getOperationalOvertimeResign(
        projectId: String,
        date: String,
        shiftId: Int,
        jabatan: String
    ) {
        compositeDisposable.add(
            repository.getOperationalOvertimeResign(projectId, date, shiftId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperationalOvertimeManagementResponse>() {
                    override fun onSuccess(t: OperationalOvertimeManagementResponse) {
                        operationalOvertimeResignResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OperationalOvertimeManagementResponse::class.java
                                )
                                operationalOvertimeResignResponse.value = error
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

    fun createOvertimeResignManagement(
        createdById: Int,
        replaceOprId: Int,
        locationId: Int,
        subLocationId: Int,
        projectId: String,
        title: String,
        description: String,
        date: String,
        shiftId: Int,
        overtimeType: String
    ) {
        compositeDisposable.add(
            repository.createOvertimeResignManagement(createdById, replaceOprId, locationId, subLocationId, projectId, title, description, date, shiftId, overtimeType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateOvertimeChangeManagementResponse>() {
                    override fun onSuccess(t: CreateOvertimeChangeManagementResponse) {
                        createOvertimeResignResponse.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    CreateOvertimeChangeManagementResponse::class.java
                                )
                                createOvertimeResignResponse.value = error
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

    fun getSearchProjectManagement(adminMasterId: Int, page: Int, keywords: String){
        compositeDisposable.add(
            repository.getSearchProjectManagement(adminMasterId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith( object : DisposableSingleObserver<SearchProjectManagementResponseModel>(){
                    override fun onSuccess(t: SearchProjectManagementResponseModel) {
                        if (t.code == 200){
                            getSearchProjectManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SearchProjectManagementResponseModel::class.java
                                )
                                getSearchProjectManagementModel.value = error
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

    fun getEmployeeReplace(projectId: String, date: String, shiftId: Int, jobCode: String, jabatan: String){
        compositeDisposable.add(
            repository.getEmployeeReplaceManagement(projectId, date, shiftId, jobCode, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmployeeReplaceResponseModel>(){
                    override fun onSuccess(t: EmployeeReplaceResponseModel) {
                        if (t.code == 200){
                            getEmployeeReplaceManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EmployeeReplaceResponseModel::class.java
                                )
                                getEmployeeReplaceManagementModel.value = error
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

    fun getListShiftManagement(projectId: String){
        compositeDisposable.add(
            repository.getListShiftManagement(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftManagementResponseModel>(){
                    override fun onSuccess(t: ListShiftManagementResponseModel) {
                        if (t.code == 200){
                            getListShiftManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListShiftManagementResponseModel::class.java
                                )
                                getListShiftManagementModel.value = error
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

    fun getEmployeeManagement(projectId: String, date: String, overtimeType: String, shiftId: Int, jabatan: String){
        compositeDisposable.add(
            repository.getEmployeeManagement(projectId, date, overtimeType, shiftId, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SelectEmployeeMgmntResponseModel>(){
                    override fun onSuccess(t: SelectEmployeeMgmntResponseModel) {
                        if (t.code == 200){
                            getEmployeeManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SelectEmployeeMgmntResponseModel::class.java
                                )
                                getEmployeeManagementModel.value = error
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

    fun getChangeEmployeeManagement(projectId: String, date: String, shiftId: Int, jobCode: String, jabatan: String){
        compositeDisposable.add(
            repository.getChangeEmployeeManagement(projectId, date, shiftId, jobCode, jabatan)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChangeEmployeeMgmntResponseModel>(){
                    override fun onSuccess(t: ChangeEmployeeMgmntResponseModel) {
                        if (t.code == 200){
                            getChangeEmployeeManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ChangeEmployeeMgmntResponseModel::class.java
                                )
                                getChangeEmployeeManagementModel.value = error
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

    fun getLocationManagement(projectId: String, shiftId: Int, date: String){
        compositeDisposable.add(
            repository.getLocationManagement(projectId, shiftId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LocationManagementResponseModel>(){
                    override fun onSuccess(t: LocationManagementResponseModel) {
                        if (t.code == 200){
                            getLocationManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LocationManagementResponseModel::class.java
                                )
                                getLocationManagementModel.value = error
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

    fun getSublocationManagement(projectId: String, locationId: Int, shiftId: Int){
        compositeDisposable.add(
            repository.getSublocationManagement(projectId, locationId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubLocationManagementResponseModel>(){
                    override fun onSuccess(t: SubLocationManagementResponseModel) {
                        if(t.code == 200){
                            getSublocationManagementModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    LocationManagementResponseModel::class.java
                                )
                                getLocationManagementModel.value = error
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

    fun getPlottingOperational(
        idEmployeeProject: Int
    ) {
        compositeDisposable.add(
            repository.getPlottingOperational(idEmployeeProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PlottingOperationalResponse>() {
                    override fun onSuccess(t: PlottingOperationalResponse) {
                        getPlottingOperationalResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PlottingOperationalResponse::class.java
                                )
                                getPlottingOperationalResponse.value = error
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


    //for activity

    fun getSublocationManagementViewModel(): MutableLiveData<SubLocationManagementResponseModel>{
        return getSublocationManagementModel
    }

    fun getLocationManagementViewModel(): MutableLiveData<LocationManagementResponseModel>{
        return getLocationManagementModel
    }


    //api resign
    fun postReplaceCreateOvertimeViewModel(): MutableLiveData<CreateOvertimeChangeManagementResponse>{
        return createOvertimeResignResponse
    }

    fun getChangeEmployeeResignViewModel(): MutableLiveData<OperationalOvertimeManagementResponse> {
        return operationalOvertimeResignResponse
    }
    //api alfa
    fun postCreateOvertimeViewModel():MutableLiveData<CreateOvertimeChangeManagementResponse>{
        return createOvertimeChangeResponse
    }

    fun getChangeEmployeeManagementViewModel(): MutableLiveData<ChangeEmployeeMgmntResponseModel>{
        return  getChangeEmployeeManagementModel
    }

    fun getEmployeeManagementViewModel(): MutableLiveData<SelectEmployeeMgmntResponseModel>{
        return getEmployeeManagementModel
    }

    fun getListShiftManagementViewModel(): MutableLiveData<ListShiftManagementResponseModel>{
        return getListShiftManagementModel
    }

    fun getEmployeeReplaceManagementViewModel(): MutableLiveData<EmployeeReplaceResponseModel>{
        return getEmployeeReplaceManagementModel
    }

    fun getSearchProjectManagementViewModel(): MutableLiveData<SearchProjectManagementResponseModel>{
        return getSearchProjectManagementModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}