package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.repository.ScheduleManagementRepository
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.divertionSchedule.DivertionScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProject.ProjectAllScheduleResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule.ProjectsScheduleManagementModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule.SchedulesManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listScheduleBod.SchedulesBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiBod
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiManagement
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiTeknisi
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleBod.SubmitCreateScheduleBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleManagement.SubmitCreateScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleTeknisi.SubmitCreateScheduleTeknisiResponse
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

class ScheduleManagementViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val calendarScheduleManagement = MutableLiveData<SchedulesManagementResponse>()
    val divertedScheduleManagementModel = MutableLiveData<SchedulesManagementResponse>()
    val divertedToScheduleManagementModel = MutableLiveData<SchedulesManagementResponse>()
    val projectsScheduleManagementModel = MutableLiveData<ProjectAllScheduleResponse>()
    val allProjectScheduleModel = MutableLiveData<ProjectAllScheduleResponse>()
    val divertionScheduleManagementModel = MutableLiveData<DivertionScheduleManagementResponse>()
    val divertedScheduleBodResponse = MutableLiveData<SchedulesBodResponse>()
    val diversionScheduleBodResponse = MutableLiveData<DivertionScheduleManagementResponse>()
    val projectScheduleVpResponse = MutableLiveData<ProjectAllScheduleResponse>()
    val submitCreateScheduleBodResponse = MutableLiveData<SubmitCreateScheduleBodResponse>()
    val submitCreateScheduleManagementResponse = MutableLiveData<SubmitCreateScheduleManagementResponse>()
    val submitCreateScheduleTeknisiResponse = MutableLiveData<SubmitCreateScheduleTeknisiResponse>()
    val projectsScheduleVpResponse = MutableLiveData<ProjectsScheduleManagementModel>()
    val projectsScheduleBodResponse = MutableLiveData<ProjectsScheduleManagementModel>()
    val projectsScheduleTeknisiResponse = MutableLiveData<ProjectsScheduleManagementModel>()
    val projectsScheduleManagementResponse = MutableLiveData<ProjectsScheduleManagementModel>()

    @Inject
    lateinit var repository: ScheduleManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getScheduleManagement(
        userId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getScheduleManagement(userId, date, type, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SchedulesManagementResponse>() {
                    override fun onSuccess(t: SchedulesManagementResponse) {
                        calendarScheduleManagement.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SchedulesManagementResponse::class.java
                                )
                                calendarScheduleManagement.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getDivertedScheduleManagement(
        userId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getScheduleManagement(userId, date, type, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SchedulesManagementResponse>() {
                    override fun onSuccess(t: SchedulesManagementResponse) {
                        divertedScheduleManagementModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SchedulesManagementResponse::class.java
                                )
                                divertedScheduleManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getDivertedToScheduleManagement(
        userId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getScheduleManagement(userId, date, type, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SchedulesManagementResponse>() {
                    override fun onSuccess(t: SchedulesManagementResponse) {
                        divertedToScheduleManagementModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SchedulesManagementResponse::class.java
                                )
                                divertedToScheduleManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectsSchedule(
        userId: Int,
        page: Int,
        size : Int
    ) {
        compositeDisposable.add(
            repository.getProjectsSchedule(userId, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectAllScheduleResponse>() {
                    override fun onSuccess(t: ProjectAllScheduleResponse) {
                        projectsScheduleManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectAllScheduleResponse::class.java
                                )
                                projectsScheduleManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getAllProjectSchedule(
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getAllProjectSchedule(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectAllScheduleResponse>() {
                    override fun onSuccess(t: ProjectAllScheduleResponse) {
                        allProjectScheduleModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectAllScheduleResponse::class.java
                                )
                                allProjectScheduleModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun divertionSchedule(
        userId: Int,
        idRkbOperation: Int,
        divertedTo: String,
        reason: String
    ) {
        compositeDisposable.add(
            repository.divertionSchedule(userId, idRkbOperation, divertedTo, reason)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DivertionScheduleManagementResponse>() {
                    override fun onSuccess(t: DivertionScheduleManagementResponse) {
                        divertionScheduleManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DivertionScheduleManagementResponse::class.java
                                )
                                divertionScheduleManagementModel.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getDivertedScheduleBod(
        userId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getScheduleBod(userId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SchedulesBodResponse>() {
                    override fun onSuccess(t: SchedulesBodResponse) {
                        divertedScheduleBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SchedulesBodResponse::class.java
                                )
                                divertedScheduleBodResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun diversionScheduleBod(
        userId: Int,
        idRkbBod: Int,
        divertedTo: String,
        reason: String
    ) {
        compositeDisposable.add(
            repository.diversionScheduleBod(userId, idRkbBod, divertedTo, reason)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DivertionScheduleManagementResponse>() {
                    override fun onSuccess(t: DivertionScheduleManagementResponse) {
                        diversionScheduleBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DivertionScheduleManagementResponse::class.java
                                )
                                diversionScheduleBodResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectVp(
        branchCode: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectVp(branchCode, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectAllScheduleResponse>() {
                    override fun onSuccess(t: ProjectAllScheduleResponse) {
                        projectScheduleVpResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectAllScheduleResponse::class.java
                                )
                                projectScheduleVpResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun submitCreateScheduleBod(
        data: ArrayList<ProjectsScheduleApiBod>
    ) {
        compositeDisposable.add(
            repository.submitCreateScheduleBod(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitCreateScheduleBodResponse>() {
                    override fun onSuccess(t: SubmitCreateScheduleBodResponse) {
                        submitCreateScheduleBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitCreateScheduleBodResponse::class.java
                                )
                                submitCreateScheduleBodResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun submitCreateScheduleManagement(
        data: ArrayList<ProjectsScheduleApiManagement>
    ) {
        compositeDisposable.add(
            repository.submitCreateScheduleManagement(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitCreateScheduleManagementResponse>() {
                    override fun onSuccess(t: SubmitCreateScheduleManagementResponse) {
                        submitCreateScheduleManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitCreateScheduleManagementResponse::class.java
                                )
                                submitCreateScheduleManagementResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun submitCreateScheduleTeknisi(
        data: ArrayList<ProjectsScheduleApiTeknisi>
    ) {
        compositeDisposable.add(
            repository.submitCreateScheduleTeknisi(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitCreateScheduleTeknisiResponse>() {
                    override fun onSuccess(t: SubmitCreateScheduleTeknisiResponse) {
                        submitCreateScheduleTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitCreateScheduleTeknisiResponse::class.java
                                )
                                submitCreateScheduleTeknisiResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectsScheduleVp(
        userId: Int,
        date: String,
        branchCode: String,
        query: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsScheduleVp(userId, date, branchCode, query, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsScheduleManagementModel>() {
                    override fun onSuccess(t: ProjectsScheduleManagementModel) {
                        projectsScheduleVpResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsScheduleManagementModel::class.java
                                )
                                projectsScheduleVpResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectsScheduleBod(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsScheduleBod(userId, date, query, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsScheduleManagementModel>() {
                    override fun onSuccess(t: ProjectsScheduleManagementModel) {
                        projectsScheduleBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsScheduleManagementModel::class.java
                                )
                                projectsScheduleBodResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectsScheduleTeknisi(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsScheduleTeknisi(userId, date, query, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsScheduleManagementModel>() {
                    override fun onSuccess(t: ProjectsScheduleManagementModel) {
                        projectsScheduleTeknisiResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsScheduleManagementModel::class.java
                                )
                                projectsScheduleTeknisiResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    fun getProjectsScheduleManagement(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ) {
        compositeDisposable.add(
            repository.getProjectsScheduleManagement(userId, date, query, page, perPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsScheduleManagementModel>() {
                    override fun onSuccess(t: ProjectsScheduleManagementModel) {
                        projectsScheduleManagementResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsScheduleManagementModel::class.java
                                )
                                projectsScheduleManagementResponse.value = error
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}