package com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.repository.AttendanceManagementRepository
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceCheckInOut.AttendanceGeoManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceInOutBod.AttendanceGeoBodResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.attendanceStatus.AttendanceStatusManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.detailSchedule.DetailScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.extendVisitDuration.ExtendDurationVisitResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.listAllProject.ProjectsAllAttendanceResponse
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.model.schedulesAttendanceManagement.SchedulesAttendanceManagementModel
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

class AttendanceManagementViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val attendanceCheckInGeoModel = MutableLiveData<AttendanceGeoManagementResponse>()
    val attendanceCheckOutGeoModel = MutableLiveData<AttendanceGeoManagementResponse>()
    val attendanceStatusModel = MutableLiveData<AttendanceStatusManagementResponse>()
    val projectManagementModel = MutableLiveData<ProjectsAllAttendanceResponse>()
    val allProjectModel = MutableLiveData<ProjectsAllAttendanceResponse>()
    val searchAllProjectModel = MutableLiveData<ProjectsAllAttendanceResponse>()
    val searchManagementProjectModel = MutableLiveData<ProjectsAllAttendanceResponse>()
    val scheduleAttendanceModel = MutableLiveData<SchedulesAttendanceManagementModel>()
    val attendanceStatusV2Model = MutableLiveData<AttendanceStatusManagementResponse>()
    val attendanceInGeoV2Model = MutableLiveData<AttendanceGeoManagementResponse>()
    val attendanceOutGeoV2Model = MutableLiveData<AttendanceGeoManagementResponse>()
    val extendVisitDuration = MutableLiveData<ExtendDurationVisitResponse>()
    val detailScheduleManagementModel = MutableLiveData<DetailScheduleManagementResponse>()
    val attendanceInGeoBodV2Response = MutableLiveData<AttendanceGeoBodResponse>()
    val attendanceStatusBodResponse = MutableLiveData<AttendanceStatusManagementResponse>()
    val attendanceOutGeoBodV2Response = MutableLiveData<AttendanceGeoBodResponse>()
    val searchBranchProjectModel = MutableLiveData<ProjectsAllAttendanceResponse>()

    @Inject
    lateinit var repository: AttendanceManagementRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun attendanceCheckInGeo(
        userId: Int,
        projectCode: String,
        jabatan: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceInGeo(userId, projectCode, jabatan, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoManagementResponse>() {
                    override fun onSuccess(t: AttendanceGeoManagementResponse) {
                        attendanceCheckInGeoModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceGeoManagementResponse::class.java
                            )
                            attendanceCheckInGeoModel.value = error
                        }
                    }

                })
        )
    }

    fun attendanceOutGeo(
        userId: Int,
        projectCode: String,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceOutGeo(userId, projectCode, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoManagementResponse>() {
                    override fun onSuccess(t: AttendanceGeoManagementResponse) {
                        attendanceCheckOutGeoModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceGeoManagementResponse::class.java
                            )
                            attendanceCheckOutGeoModel.value = error
                        }
                    }

                })
        )
    }

    fun getAttendanceStatus(
        userId: Int,
        projectCode: String
    ) {
        compositeDisposable.add(
            repository.getStatusAttendance(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceStatusManagementResponse>() {
                    override fun onSuccess(t: AttendanceStatusManagementResponse) {
                        attendanceStatusModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceStatusManagementResponse::class.java
                                )
                                attendanceStatusModel.value = error
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

    fun getProjectsManagement(userId: Int, page: Int,size: Int) {
        compositeDisposable.add(
            repository.getProjectsManagement(userId, page,size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllAttendanceResponse>() {
                    override fun onSuccess(t: ProjectsAllAttendanceResponse) {
                        if (t.code == 200) {
                            projectManagementModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllAttendanceResponse::class.java
                                )
                                projectManagementModel.value = error
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

    fun getAllProject(page: Int, size: Int) {
        compositeDisposable.add(
            repository.getAllProject(page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllAttendanceResponse>() {
                    override fun onSuccess(t: ProjectsAllAttendanceResponse) {
                        if (t.code == 200) {
                            allProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllAttendanceResponse::class.java
                                )
                                allProjectModel.value = error
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

    fun getSearchProjectAll(page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchProjectAll(page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllAttendanceResponse>() {
                    override fun onSuccess(t: ProjectsAllAttendanceResponse) {
                        if (t.code == 200) {
                            searchAllProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllAttendanceResponse::class.java
                                )
                                searchAllProjectModel.value = error
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

    fun getSearchProjectManagement(
        userId: Int,
        page: Int,
        keywords: String
    ) {
        compositeDisposable.add(
            repository.getSearchProjectManagement(userId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllAttendanceResponse>() {
                    override fun onSuccess(t: ProjectsAllAttendanceResponse) {
                        if (t.code == 200) {
                            searchManagementProjectModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllAttendanceResponse::class.java
                                )
                                searchManagementProjectModel.value = error
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

    fun getSchedulesAttendance(
        adminMasterId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ) {
        compositeDisposable.add(
            repository.getSchedulesAttendance(adminMasterId, date, type, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SchedulesAttendanceManagementModel>() {
                    override fun onSuccess(t: SchedulesAttendanceManagementModel) {
                        scheduleAttendanceModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SchedulesAttendanceManagementModel::class.java
                                )
                                scheduleAttendanceModel.value = error
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

    fun getAttendanceStatusV2(
        userId: Int,
        idRkbOperation: Int
    ) {
        compositeDisposable.add(
            repository.getAttendanceStatusV2(userId, idRkbOperation)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceStatusManagementResponse>(){
                    override fun onSuccess(t: AttendanceStatusManagementResponse) {
                        attendanceStatusV2Model.value = t
                    }

                    override fun onError(e: Throwable) {

                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceStatusManagementResponse::class.java
                                )
                                attendanceStatusV2Model.value = error
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

    fun attendanceInGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceInGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoManagementResponse>() {
                    override fun onSuccess(t: AttendanceGeoManagementResponse) {
                        attendanceInGeoV2Model.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceGeoManagementResponse::class.java
                                )
                                attendanceInGeoV2Model.value = error
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

    fun attendanceOutGeoV2(
        userId: Int,
        idRkbOperation: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceOutGeoV2(userId, idRkbOperation, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoManagementResponse>() {
                    override fun onSuccess(t: AttendanceGeoManagementResponse) {
                        attendanceOutGeoV2Model.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceGeoManagementResponse::class.java
                                )
                                attendanceOutGeoV2Model.value = error
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

    fun submitExtendVisitDuration(
        userId: Int,
        idRkb: Int,
        duration: Int,
        extendReason: String
    ) {
        compositeDisposable.add(
            repository.submitExtendVisitDuration(userId, idRkb, duration, extendReason)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ExtendDurationVisitResponse>() {
                    override fun onSuccess(t: ExtendDurationVisitResponse) {
                        extendVisitDuration.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ExtendDurationVisitResponse::class.java
                                )
                                extendVisitDuration.value = error
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

    fun getDetailScheduleManagement(
        idRkb: Int,
        userId: Int
    ) {
        compositeDisposable.add(
            repository.getDetailScheduleManagement(idRkb, userId)
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : DisposableSingleObserver<DetailScheduleManagementResponse>() {
                    override fun onSuccess(t: DetailScheduleManagementResponse) {
                        detailScheduleManagementModel.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailScheduleManagementResponse::class.java
                                )
                                detailScheduleManagementModel.value = error
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

    fun attendanceInGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceInGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoBodResponse>() {
                    override fun onSuccess(t: AttendanceGeoBodResponse) {
                        attendanceInGeoBodV2Response.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceGeoBodResponse::class.java
                                )
                                attendanceInGeoBodV2Response.value = error
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

    fun getAttendanceStatusBod(
        userId: Int,
        idRkbOperation: Int
    ) {
        compositeDisposable.add(
            repository.getAttendanceStatusBod(userId, idRkbOperation)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceStatusManagementResponse>() {
                    override fun onSuccess(t: AttendanceStatusManagementResponse) {
                        attendanceStatusBodResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceStatusManagementResponse::class.java
                                )
                                attendanceStatusBodResponse.value = error
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

    fun attendanceOutGeoBodV2(
        userId: Int,
        idRkbBod: Int,
        file: MultipartBody.Part,
        longitude: Double,
        latitude: Double,
        address :String
    ) {
        compositeDisposable.add(
            repository.attendanceOutGeoBodV2(userId, idRkbBod, file, longitude, latitude, address)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceGeoBodResponse>() {
                    override fun onSuccess(t: AttendanceGeoBodResponse) {
                        attendanceOutGeoBodV2Response.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceGeoBodResponse::class.java
                                )
                                attendanceOutGeoBodV2Response.value = error
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
                .subscribeWith(object : DisposableSingleObserver<ProjectsAllAttendanceResponse>() {
                    override fun onSuccess(t: ProjectsAllAttendanceResponse) {
                        searchBranchProjectModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e){
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ProjectsAllAttendanceResponse::class.java
                                )
                                searchBranchProjectModel.value = error
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}