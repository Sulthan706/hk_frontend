package com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.repository.CftalkManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.complaintsByEmployee.ComplaintsByEmployeeResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.createComplaint.CreateCftalkManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listAllProject.ProjectsAllCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listLocation.LocationsProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listProjectByUserId.ProjectsEmployeeIdResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listSubLoc.SubLocationProjectCftalkResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.model.listTitleComplaint.TitlesCftalkManagementResponse
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

class CftalkManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    private val isNull = MutableLiveData<Boolean>()

    val projectsByEmployeeIdModel = MutableLiveData<ProjectsEmployeeIdResponse>()
    val allProjectsCftalkModel = MutableLiveData<ProjectsAllCftalkResponse>()
    val complaintsByEmployeeModel = MutableLiveData<ComplaintsByEmployeeResponse>()
    val complaintsByProjectModel = MutableLiveData<ComplaintsByEmployeeResponse>()
    val titlesCftalkManagementModel = MutableLiveData<TitlesCftalkManagementResponse>()
    val locationsCftalkManagementModel = MutableLiveData<LocationsProjectCftalkResponse>()
    val subLocationCftalkManagementModel = MutableLiveData<SubLocationProjectCftalkResponse>()
    val createCftalkManagementModel = MutableLiveData<CreateCftalkManagementResponse>()
    val otherProjectsCftalkModel = MutableLiveData<ProjectsAllCftalkResponse>()
    val searchOtherProjectsModel = MutableLiveData<ProjectsAllCftalkResponse>()
    val searchAllProjectsModel = MutableLiveData<ProjectsAllCftalkResponse>()
    val projectsByBranchCftalkModel = MutableLiveData<ProjectsAllCftalkResponse>()
    val searchProjectsBranchModel = MutableLiveData<ProjectsAllCftalkResponse>()

    @Inject
    lateinit var repository: CftalkManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getProjectsByEmployeeId(employeeId: Int) {
        compositeDisposable.add(
            repository.getProjectsByEmployeeId(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsEmployeeIdResponse>() {
                    override fun onSuccess(t: ProjectsEmployeeIdResponse) {
                        if (t.code == 200) {
                            projectsByEmployeeIdModel.value = t
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
                                    ProjectsEmployeeIdResponse::class.java
                                )
                                projectsByEmployeeIdModel.value = error
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

    fun getAllProjects(page: Int, size: Int) {
        compositeDisposable.add(
            repository.getAllProject(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        if (t.code == 200) {
                            allProjectsCftalkModel.value = t
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
                                    ProjectsAllCftalkResponse::class.java
                                )
                                allProjectsCftalkModel.value = error
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

    fun getComplaintByEmployee(employeeId: Int, page: Int) {
        compositeDisposable.add(
            repository.getComplaintByEmployee(employeeId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ComplaintsByEmployeeResponse>() {
                    override fun onSuccess(t: ComplaintsByEmployeeResponse) {
                        if (t.code == 200) {
                            complaintsByEmployeeModel.value = t
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
                                    ComplaintsByEmployeeResponse::class.java
                                )
                                complaintsByEmployeeModel.value = error
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

    fun getComplaintsProject(projectId: String, page: Int) {
        compositeDisposable.add(
            repository.getComplaintByProject(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ComplaintsByEmployeeResponse>() {
                    override fun onSuccess(t: ComplaintsByEmployeeResponse) {
                        if (t.code == 200) {
                            complaintsByProjectModel.value = t
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
                                    ComplaintsByEmployeeResponse::class.java
                                )
                                complaintsByProjectModel.value = error
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

    fun getTitleComplaint() {
        compositeDisposable.add(
            repository.getTitleComplaint()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TitlesCftalkManagementResponse>() {
                    override fun onSuccess(t: TitlesCftalkManagementResponse) {
                        if (t.code == 200) {
                            titlesCftalkManagementModel.value = t
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
                                    TitlesCftalkManagementResponse::class.java
                                )
                                titlesCftalkManagementModel.value = error
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

    fun getLocationsComplaint(projectId: String) {
        compositeDisposable.add(
            repository.getLocationComplaint(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LocationsProjectCftalkResponse>() {
                    override fun onSuccess(t: LocationsProjectCftalkResponse) {
                        if (t.code == 200) {
                            locationsCftalkManagementModel.value = t
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
                                    LocationsProjectCftalkResponse::class.java
                                )
                                locationsCftalkManagementModel.value = error
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

    fun getSubLocComplaint(projectId: String, locationId: Int) {
        compositeDisposable.add(
            repository.getSubLocationComplaint(projectId, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubLocationProjectCftalkResponse>() {
                    override fun onSuccess(t: SubLocationProjectCftalkResponse) {
                        if (t.code == 200) {
                            subLocationCftalkManagementModel.value = t
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
                                    SubLocationProjectCftalkResponse::class.java
                                )
                                subLocationCftalkManagementModel.value = error
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

    fun createComplaintManagement(
        userId: Int,
        projectId: String,
        title: Int,
        description: String,
        locationId: Int,
        subLocationId: Int,
        file: MultipartBody.Part,
        file2: MultipartBody.Part,
        file3: MultipartBody.Part,
        file4: MultipartBody.Part,
        complaintType: String
    ) {
        compositeDisposable.add(
            repository.createCftalkManagement(userId, projectId, title, description, locationId, subLocationId, file, file2, file3, file4, complaintType)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CreateCftalkManagementResponse>() {
                    override fun onSuccess(t: CreateCftalkManagementResponse) {
                        if (t.code == 200) {
                            createCftalkManagementModel.value = t
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
                                    CreateCftalkManagementResponse::class.java
                                )
                                createCftalkManagementModel.value = error
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

    fun getOtherProjectsCftalk(userId: Int, page: Int) {
        compositeDisposable.add(
            repository.getOtherProjects(userId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        if (t.code == 200) {
                            otherProjectsCftalkModel.value = t
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
                                    ProjectsAllCftalkResponse::class.java
                                )
                                otherProjectsCftalkModel.value = error
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

    fun getSearchOtherProjects(userId: Int, page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchOtherProjects(userId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        if (t.code == 200) {
                            searchOtherProjectsModel.value = t
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
                                    ProjectsAllCftalkResponse::class.java
                                )
                                searchOtherProjectsModel.value = error
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

    fun getSearchAllProject(page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchAllProject(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        if (t.code == 200) {
                            searchAllProjectsModel.value = t
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
                                    ProjectsAllCftalkResponse::class.java
                                )
                                searchAllProjectsModel.value = error
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

    fun getProjectByBranch(
        branchCode: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectByBranch(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        projectsByBranchCftalkModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllCftalkResponse::class.java
                                )
                                projectsByBranchCftalkModel.value = error
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

    fun getSearchProjectBranch(
        page: Int,
        branchCode: String,
        keywords: String,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getSearchProjectBranch(page, branchCode, keywords, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllCftalkResponse>() {
                    override fun onSuccess(t: ProjectsAllCftalkResponse) {
                        searchProjectsBranchModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when(e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllCftalkResponse::class.java
                                )
                                searchProjectsBranchModel.value = error
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

    fun checkNull(desc: String) {
        isNull.value = desc.isEmpty()
    }

    fun getDesc(): MutableLiveData<Boolean> {
        return isNull
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}