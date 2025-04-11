package com.hkapps.hygienekleen.features.features_client.report.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.report.data.repository.ReportClientRepository
import com.hkapps.hygienekleen.features.features_client.report.model.dashboardreport.DashboardReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.detailArea.DetailAreaReportClientResponse
import com.hkapps.hygienekleen.features.features_client.report.model.detailPlotting.DetailPlottingReportResponse
import com.hkapps.hygienekleen.features.features_client.report.model.detailkondisiarea.DetailKondisiAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.jadwalkerja.WorkHourReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listArea.ListAreaReportClientReponse
import com.hkapps.hygienekleen.features.features_client.report.model.listShift.ListShiftReportClientResponse
import com.hkapps.hygienekleen.features.features_client.report.model.listkondisiarea.ListKondisiAreaResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listlocation.ListLocationResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listshiftreport.ListShiftReportResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listsublocation.ListSublocationResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.reportabsensi.ReportAbsensiResponseModel
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

class ReportClientViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    val listShiftResponseModel = MutableLiveData<ListShiftReportClientResponse>()
    val listAreaResponseModel = MutableLiveData<ListAreaReportClientReponse>()
    val detailAreaResponseModel = MutableLiveData<DetailAreaReportClientResponse>()
    val detailPlottingResponseModel = MutableLiveData<DetailPlottingReportResponse>()

    //new
    val listLocationResponseModel = MutableLiveData<ListLocationResponseModel>()
    val listSubLocationResponseModel = MutableLiveData<ListSublocationResponseModel>()
    val listShiftReportResponseModel = MutableLiveData<ListShiftReportResponseModel>()
    val workHourResponseModel = MutableLiveData<WorkHourReportResponseModel>()
    val listKondisiAreaResponseModel = MutableLiveData<ListKondisiAreaResponseModel>()
    val dashboardReportResponseModel = MutableLiveData<DashboardReportResponseModel>()
    val getReportAbsensiResponseModel = MutableLiveData<ReportAbsensiResponseModel>()
    val getDetailKondisiAreaResponseModel = MutableLiveData<DetailKondisiAreaResponseModel>()


    @Inject
    lateinit var repository: ReportClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListShift(projectId: String) {
        compositeDisposable.add(
            repository.getListShiftReport(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftReportClientResponse>() {
                    override fun onSuccess(t: ListShiftReportClientResponse) {
                        listShiftResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListShiftReportClientResponse::class.java
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
            repository.getListAreaReport(projectCode, shiftId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListAreaReportClientReponse>() {
                    override fun onSuccess(t: ListAreaReportClientReponse) {
                        listAreaResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListAreaReportClientReponse::class.java
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

    fun getDetailAreaReport(projectCode: String, shiftId: Int, plottingId: Int) {
        compositeDisposable.add(
            repository.getDetailAreaReport(projectCode, shiftId, plottingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailAreaReportClientResponse>() {
                    override fun onSuccess(t: DetailAreaReportClientResponse) {
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
                                    DetailAreaReportClientResponse::class.java
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

    fun getDetailPlottingReport(plottingId: Int) {
        compositeDisposable.add(
            repository.getDetailPlottingReport(plottingId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailPlottingReportResponse>() {
                    override fun onSuccess(t: DetailPlottingReportResponse) {
                        detailPlottingResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailPlottingReportResponse::class.java
                                )
                                detailPlottingResponseModel.value = error
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

    fun getListLocation(projectCode: String) {
        compositeDisposable.add(
            repository.getListLocationReport(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListLocationResponseModel>() {
                    override fun onSuccess(t: ListLocationResponseModel) {
                        listLocationResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLocationResponseModel::class.java
                                )
                                listLocationResponseModel.value = error
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

    fun getListSublocation(projectCode: String, locationId: Int) {
        compositeDisposable.add(
            repository.getListSubLocationReport(projectCode, locationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListSublocationResponseModel>() {
                    override fun onSuccess(t: ListSublocationResponseModel) {
                        listSubLocationResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListLocationResponseModel::class.java
                                )
                                listLocationResponseModel.value = error
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

    fun getListShiftReports(projectCode: String, locationId: Int, subLocationId: Int) {
        compositeDisposable.add(
            repository.getListShiftReports(projectCode, locationId, subLocationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftReportResponseModel>() {
                    override fun onSuccess(t: ListShiftReportResponseModel) {
                        if (t.code == 200) {
                            listShiftReportResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListShiftReportResponseModel::class.java
                                )
                                listShiftReportResponseModel.value = error
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

    fun getWorkHour(
        projectCode: String,
        date: String,
        shiftId: Int,
        locationId: Int,
        subLocationId: Int
    ) {
        compositeDisposable.add(
            repository.getWorkHourReport(projectCode, date, shiftId, locationId, subLocationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WorkHourReportResponseModel>() {
                    override fun onSuccess(t: WorkHourReportResponseModel) {
                        if (t.code == 200) {
                            workHourResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    WorkHourReportResponseModel::class.java
                                )
                                workHourResponseModel.value = error
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

    fun getListKondisiArea(projectCode: String, page: Int) {
        compositeDisposable.add(
            repository.getListKondisiArea(projectCode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListKondisiAreaResponseModel>() {
                    override fun onSuccess(t: ListKondisiAreaResponseModel) {
                        if (t.code == 200) {
                            listKondisiAreaResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListKondisiAreaResponseModel::class.java
                                )
                                listKondisiAreaResponseModel.value = error
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

    fun getDashBoardReport(projectCode: String, month: String, year: String) {
        compositeDisposable.add(
            repository.getDashboardReport(projectCode, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DashboardReportResponseModel>() {
                    override fun onSuccess(t: DashboardReportResponseModel) {
                        if (t.code == 200) {
                            dashboardReportResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DashboardReportResponseModel::class.java
                                )
                                dashboardReportResponseModel.value = error
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

    fun getReportAbsensi(projectCode: String, date: String) {
        compositeDisposable.add(
            repository.getReportAbsensi(projectCode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReportAbsensiResponseModel>() {
                    override fun onSuccess(t: ReportAbsensiResponseModel) {
                        if (t.code == 200) {
                            getReportAbsensiResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ReportAbsensiResponseModel::class.java
                                )
                                getReportAbsensiResponseModel.value = error
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

    fun getDetailKondisiArea(
        projectCode: String,
        locationId: Int,
        subLocationId: Int,
        shiftId: Int,
        date: String
    ) {
        compositeDisposable.add(
            repository.getDetailKondisiArea(projectCode, locationId, subLocationId, shiftId, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailKondisiAreaResponseModel>() {
                    override fun onSuccess(t: DetailKondisiAreaResponseModel) {
                        if (t.code == 200) {
                            getDetailKondisiAreaResponseModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = (e as HttpException).response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    DetailKondisiAreaResponseModel::class.java
                                )
                                getDetailKondisiAreaResponseModel.value = error
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




    fun getDetailKondisiAreaViewModel(): MutableLiveData<DetailKondisiAreaResponseModel>{
        return getDetailKondisiAreaResponseModel
    }

    fun getReportAbsensiviewModel(): MutableLiveData<ReportAbsensiResponseModel> {
        return getReportAbsensiResponseModel
    }


    fun getDashboardReportViewModel(): MutableLiveData<DashboardReportResponseModel> {
        return dashboardReportResponseModel
    }

    fun getListKondisiAreaViewModel(): MutableLiveData<ListKondisiAreaResponseModel> {
        return listKondisiAreaResponseModel
    }

    fun getWorkHoutViewModel(): MutableLiveData<WorkHourReportResponseModel> {
        return workHourResponseModel
    }

    fun getListShiftReportViewModel(): MutableLiveData<ListShiftReportResponseModel> {
        return listShiftReportResponseModel
    }

    fun getListSublocationViewModel(): MutableLiveData<ListSublocationResponseModel> {
        return listSubLocationResponseModel
    }

    fun getListLocationViewModel(): MutableLiveData<ListLocationResponseModel> {
        return listLocationResponseModel
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}