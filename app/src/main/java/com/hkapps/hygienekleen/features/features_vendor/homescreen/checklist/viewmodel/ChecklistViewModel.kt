package com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.repository.ChecklistRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailArea.DetailAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.detailOperator.DetailOperatorChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listArea.ListAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listOperator.ListOperatorChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listReview.ListReviewAreaChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.dailyAct.DailyActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.listStaffBertugas.StaffResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.listShift.ListShiftChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.onCheckPlotting.CheckPlottingChecklistResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.new_.submitChecklist.SubmitChecklistResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.objectActivity.ObjectActivityResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.penilaianObj.PenilaianObjResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusAbsen.AttendanceStatusResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.statusPenilaianDac.StatusDacResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.model.old.otoritasChecklist.OtoritasChecklistResponse
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

class ChecklistViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val dailyActivityResponseModel = MutableLiveData<DailyActivityResponseModel>()
    private val statusAttendanceResponse = MutableLiveData<AttendanceStatusResponse>()
    private val staffResponseModel = MutableLiveData<StaffResponseModel>()
    private val objectDacResponseModel = MutableLiveData<ObjectActivityResponseModel>()
    private val penilaianObjResponse = MutableLiveData<PenilaianObjResponse>()
    private val statusDacResponseModel = MutableLiveData<StatusDacResponseModel>()
    private val otoritasChecklistResponse = MutableLiveData<OtoritasChecklistResponse>()

    val listShiftResponseModel = MutableLiveData<ListShiftChecklistResponse>()
    val listAreaResponseModel = MutableLiveData<ListAreaChecklistResponse>()
    val checkPlottingResponseModel = MutableLiveData<CheckPlottingChecklistResponse>()
    val detailAreaResponseModel = MutableLiveData<DetailAreaChecklistResponse>()
    val listReviewAreaResponseModel = MutableLiveData<ListReviewAreaChecklistResponse>()
    val submitChecklistResponseModel = MutableLiveData<SubmitChecklistResponseModel>()
    val listOperatorResponseModel = MutableLiveData<ListOperatorChecklistResponse>()
    val detailOperatorResponseModel = MutableLiveData<DetailOperatorChecklistResponse>()
    val searchListAreaResponseModel = MutableLiveData<ListAreaChecklistResponse>()
    val searchListOperatorResponseModel = MutableLiveData<ListOperatorChecklistResponse>()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    @Inject
    lateinit var repository: ChecklistRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getOtoritasChecklist(projectCode: String) {
        compositeDisposable.add(
            repository.getOtoritasChecklist(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OtoritasChecklistResponse>() {
                    override fun onSuccess(t: OtoritasChecklistResponse) {
                        if (t.code == 200) {
                            otoritasChecklistResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    OtoritasChecklistResponse::class.java
                                )
                                otoritasChecklistResponse.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }
    fun getStatusDac(employeeId: Int, projectCode: String, plottingId: Int, activityId: Int) {
        compositeDisposable.add(
            repository.getStatusDac(employeeId, projectCode, plottingId, activityId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatusDacResponseModel>() {
                    override fun onSuccess(t: StatusDacResponseModel) {
                        if (t.code == 200) {
                            statusDacResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    StatusDacResponseModel::class.java
                                )
                                statusDacResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun postPenilaianObj(employeeId: Int,
                         projectCode: String,
                         activityId: Int,
                         userId: Int,
                         plottingId: Int,
                         firstObject: String?,
                         secondObject: String?,
                         thirdObject: String?,
                         fourthObject: String?,
                         fifthObject: String?,
                         firstValue: String?,
                         secondValue: String?,
                         thirdValue: String?,
                         fourthValue: String?,
                         fifthValue: String?,
                         notes: String,
                         uploadImage: MultipartBody.Part) {
        compositeDisposable.add(
            repository.postPenilaianObj(employeeId, projectCode, activityId, userId, plottingId, firstObject, secondObject, thirdObject, fourthObject, fifthObject, firstValue, secondValue, thirdValue, fourthValue, fifthValue, notes, uploadImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PenilaianObjResponse>() {
                    override fun onSuccess(t: PenilaianObjResponse) {
                        if (t.code == 200) {
                            penilaianObjResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    PenilaianObjResponse::class.java
                                )
                                penilaianObjResponse.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getObjectDac(activityId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getObjectDac(activityId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ObjectActivityResponseModel>() {
                    override fun onSuccess(t: ObjectActivityResponseModel) {
                        if (t.code == 200) {
                            objectDacResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ObjectActivityResponseModel::class.java
                                )
                                objectDacResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getStaffBertugas(userId: Int, projectCode: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaff(userId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StaffResponseModel>() {
                    override fun onSuccess(t: StaffResponseModel) {
                        if (t.code == 200) {
                            staffResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    StaffResponseModel::class.java
                                )
                                staffResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getAttendanceStatus(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAttendanceCheck(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceStatusResponse>() {
                    override fun onSuccess(t: AttendanceStatusResponse) {
                        if (t.code == 200) {
                            statusAttendanceResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceStatusResponse::class.java
                                )
                                statusAttendanceResponse.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDailyAct(userId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getDailyAct(userId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DailyActivityResponseModel>() {
                    override fun onSuccess(t: DailyActivityResponseModel) {
                        if (t.code == 200) {
                            dailyActivityResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DailyActivityResponseModel::class.java
                                )
                                dailyActivityResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun otoritasChecklistResponse(): MutableLiveData<OtoritasChecklistResponse> {
        return otoritasChecklistResponse
    }

    fun penilaianObjectResponse(): MutableLiveData<PenilaianObjResponse> {
        return penilaianObjResponse
    }

    fun dailyActResponseModel(): MutableLiveData<DailyActivityResponseModel> {
        return dailyActivityResponseModel
    }

    fun attendanceStatusResponse(): MutableLiveData<AttendanceStatusResponse> {
        return statusAttendanceResponse
    }

    fun staffResponseModel(): MutableLiveData<StaffResponseModel>{
        return staffResponseModel
    }

    fun objectDacResponseModel(): MutableLiveData<ObjectActivityResponseModel>{
        return objectDacResponseModel
    }

    fun statusDacResponseModel(): MutableLiveData<StatusDacResponseModel>{
        return statusDacResponseModel
    }


    fun getListShift(projectId: String) {
        compositeDisposable.add(
            repository.getListShift(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftChecklistResponse>() {
                    override fun onSuccess(t: ListShiftChecklistResponse) {
                        if (t.code == 200) {
                            listShiftResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListShiftChecklistResponse::class.java
                                )
                                listShiftResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListArea(projectCode: String, shiftId: Int, page: Int) {
        compositeDisposable.add(
            repository.getListArea(projectCode, shiftId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAreaChecklistResponse>() {
                    override fun onSuccess(t: ListAreaChecklistResponse) {
                        if (t.code == 200) {
                            listAreaResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAreaChecklistResponse::class.java
                                )
                                listAreaResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getCheckPlotting(plottingId: Int) {
        compositeDisposable.add(
            repository.getCheckPlotting(plottingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CheckPlottingChecklistResponse>() {
                    override fun onSuccess(t: CheckPlottingChecklistResponse) {
                        checkPlottingResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAreaChecklistResponse::class.java
                                )
                                listAreaResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailArea(projectCode: String, shiftId: Int, plottingId: Int) {
        compositeDisposable.add(
            repository.getDetailAreaChecklist(projectCode, shiftId, plottingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAreaChecklistResponse>() {
                    override fun onSuccess(t: DetailAreaChecklistResponse) {
                        detailAreaResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailAreaChecklistResponse::class.java
                                )
                                detailAreaResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListReviewArea(employeeId: Int, projectId: String, plottingId: Int, activityId: Int) {
        compositeDisposable.add(
            repository.getListReviewArea(employeeId, projectId, plottingId, activityId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListReviewAreaChecklistResponse>() {
                    override fun onSuccess(t: ListReviewAreaChecklistResponse) {
                        listReviewAreaResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListReviewAreaChecklistResponse::class.java
                                )
                                listReviewAreaResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun submitChecklist(
        projectId: String,
        submitBy: Int,
        pengawasId: Int,
        plottingId: Int,
        shiftId: Int,
        checklistReviewId: Int,
        notes: String,
        file: MultipartBody.Part,
        operationalsId: ArrayList<Int>
    ) {
        compositeDisposable.add(
            repository.submitChecklist(projectId, submitBy, pengawasId, plottingId, shiftId, checklistReviewId, notes, file, operationalsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SubmitChecklistResponseModel>() {
                    override fun onSuccess(t: SubmitChecklistResponseModel) {
                        submitChecklistResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    SubmitChecklistResponseModel::class.java
                                )
                                submitChecklistResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListOperator(projectId: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListOperator(projectId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperatorChecklistResponse>() {
                    override fun onSuccess(t: ListOperatorChecklistResponse) {
                        if (t.code == 200) {
                            listOperatorResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperatorChecklistResponse::class.java
                                )
                                listOperatorResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDetailOperatorChecklist(employeeId: Int, projectId: String, idDetailEmployeeProject: Int) {
        compositeDisposable.add(
            repository.getDetailOperatorChecklist(employeeId, projectId, idDetailEmployeeProject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailOperatorChecklistResponse>() {
                    override fun onSuccess(t: DetailOperatorChecklistResponse) {
                        if (t.code == 200) {
                            detailOperatorResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailOperatorChecklistResponse::class.java
                                )
                                detailOperatorResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getSearchListArea(projectCode: String, shiftId: Int, page: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchListArea(projectCode, shiftId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAreaChecklistResponse>() {
                    override fun onSuccess(t: ListAreaChecklistResponse) {
                        if (t.code == 200) {
                            searchListAreaResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAreaChecklistResponse::class.java
                                )
                                searchListAreaResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
                            }
                            else -> isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getSearchListOperator(projectId: String, shiftId: Int, keywords: String) {
        compositeDisposable.add(
            repository.getSearchListOperator(projectId, shiftId, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListOperatorChecklistResponse>() {
                    override fun onSuccess(t: ListOperatorChecklistResponse) {
                        if (t.code == 200) {
                            searchListOperatorResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListOperatorChecklistResponse::class.java
                                )
                                searchListOperatorResponseModel.value = error
                                isLoading?.value = false
                            }
                            is SocketException, is UnknownHostException, is SocketTimeoutException -> {
                                isConnectionTimeout.postValue(true)
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