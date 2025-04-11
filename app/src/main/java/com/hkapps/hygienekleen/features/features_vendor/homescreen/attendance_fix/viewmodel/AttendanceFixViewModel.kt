package com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.repository.AttendanceFixRepository
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_already_absent.AttendanceAlreadyAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.attendance_not_absent.AttendanceNotAbsenResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_history_result.FabHistoryResultResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_person_model.FabPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fab_team_model.FabTeamResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.fabsearch.FabSearchPersonResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.mid_level_one_sch.MidLevelOneSchStatusResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.regisfacerecog.RegisFaceRecogResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.userFlyingOperator.AttendanceUserFlyingResponse
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.verifyfacerecog.VerifyFaceResponseModel
import com.hkapps.hygienekleen.features.splash.ui.activity.SplashActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
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

class AttendanceFixViewModel(application: Application) : AndroidViewModel(application) {
    val chooseStaffResponseModel = MutableLiveData<ChooseStaffResponseModel>()

    val historyResponseModel = MutableLiveData<HistoryResponseModel>()
    val historyByDateResponseModel = MutableLiveData<HistoryByDateResponseModel>()

    val attStatus = MutableLiveData<AttendanceStatusResponseModelNew>()

    val attStatusFail = MutableLiveData<Int?>()

    val attCheck = MutableLiveData<AttendanceCheckResponseModel>()
    val attCheckOut = MutableLiveData<AttendanceCheckOutResponseModel>()
    private val isConnectionTimeout = MutableLiveData<Boolean>()

    val shift = MutableLiveData<AttendanceShiftStaffNotAttendanceResponseModel>()

    //yg baru
    val listAttendanceNotAbsentViewModel =
        MutableLiveData<AttendanceNotAbsenResponseModel>()
    val listAttendanceAlreadyAbsentViewModel =
        MutableLiveData<AttendanceAlreadyAbsentResponseModel>()

    //old
    val notAttendanceResponseModel =
        MutableLiveData<AttendanceNotAbsenResponseModel>()
    val notAttendanceSPVResponseModel =
        MutableLiveData<AttendanceListStaffNotAttendanceSPVResponseModel>()
    val alreadyAttendanceSPVResponseModel =
        MutableLiveData<AttendanceListStaffAlreadyAttendanceSPVResponseModel>()
    val notAttendanceCSPVResponseModel =
        MutableLiveData<AttendanceListStaffNotAttendanceCSPVResponseModel>()
    val alreadyAttendanceResponseModel =
        MutableLiveData<AttendanceListStaffAlreadyAttendanceResponseModel>()
    val alreadyAttendanceCSPVResponseModel =
        MutableLiveData<AttendanceListStaffAlreadyAttendanceCSPVResponseModel>()

    val employeeSchResponseModel = MutableLiveData<EmployeeSchResponseModel>()
    val employeeSchMidResponseModel = MutableLiveData<EmployeeSchMidResponseModel>()
    val employeeSchByIdResponseModel = MutableLiveData<EmployeeSchByIdResponseModel>()
    val getListStaffAlreadyAttendance =
        MutableLiveData<AttendanceListStaffAlreadyAttendanceResponseModel>()


    val qrModel = MutableLiveData<QRCodeResponseModel>()
    val postSelfie = MutableLiveData<SelfiePostResponseModel>()
    val postSelfieGeo = MutableLiveData<GeoSelfiePostResponseModel>()
    val postSelfieGeoMid = MutableLiveData<GeoSelfiePostMidResponseModel>()


    val midLevelOneSchResponseModel = MutableLiveData<MidLevelOneSchResponseModel>()
    val statusAttendanceMidLevelOneSchResponseModel =
        MutableLiveData<MidLevelOneSchStatusResponseModel>()

    val attCheckMidOneSch = MutableLiveData<AttendanceCheckResponseModel>()
    val attCheckOutMidOneSch = MutableLiveData<AttendanceCheckOutResponseModel>()

    val att = MutableLiveData<MidLevelOneSchStatusResponseModel>()

    val myTeamViewModel = MutableLiveData<FabPersonResponseModel>()
    val myTeamSPVViewModel = MutableLiveData<FabPersonResponseModel>()
    val myTeamCSPVViewModel = MutableLiveData<FabPersonResponseModel>()

    val searchPersonViewModel = MutableLiveData<FabSearchPersonResponseModel>()

    val myHistoryTeamLeadViewModel = MutableLiveData<FabTeamResponseModel>()
    val myHistorySPVViewModel = MutableLiveData<FabTeamResponseModel>()
    val myHistoryFMViewModel = MutableLiveData<FabTeamResponseModel>()
    val myHistoryResultViewModel = MutableLiveData<FabHistoryResultResponseModel>()

    val attendanceInUserFlyingModel = MutableLiveData<AttendanceUserFlyingResponse>()
    val attendanceOutUserFlyingModel = MutableLiveData<AttendanceUserFlyingResponse>()
    val getStatusUserFlyingModel = MutableLiveData<AttendanceUserFlyingResponse>()
    val getAccessUserFlyingModel = MutableLiveData<AttendanceUserFlyingResponse>()

    val regisFaceRecogModel = MutableLiveData<RegisFaceRecogResponseModel>()
    val verifyFaceRecogModel = MutableLiveData<VerifyFaceResponseModel>()

    private val compositeDisposable = CompositeDisposable()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: AttendanceFixRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    //    lowLevell
    fun getQRCodeViewModel(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getQRCode(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<QRCodeResponseModel>() {
                    override fun onSuccess(t: QRCodeResponseModel) {
                        qrModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                QRCodeResponseModel::class.java
                            )
                            qrModel.value = error
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

    fun postAttendance(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.postImageSelfie(employeeId, projectCode, barcodeKey, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SelfiePostResponseModel>() {
                    override fun onSuccess(t: SelfiePostResponseModel) {
                        postSelfie.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                SelfiePostResponseModel::class.java
                            )
                            postSelfie.value = error
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


    fun postAttendanceGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.postImageSelfieGeo(employeeId, projectCode, scheduleId, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GeoSelfiePostResponseModel>() {
                    override fun onSuccess(t: GeoSelfiePostResponseModel) {
                        postSelfieGeo.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GeoSelfiePostResponseModel::class.java
                            )
                            postSelfieGeo.value = error
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

    fun putAttendance(
        employeeId: Int,
        projectCode: String,
        barcodeKey: String,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.putImageSelfie(employeeId, projectCode, barcodeKey, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SelfiePostResponseModel>() {
                    override fun onSuccess(t: SelfiePostResponseModel) {
                        postSelfie.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                SelfiePostResponseModel::class.java
                            )
                            postSelfie.value = error
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

    fun putAttendanceGeo(
        employeeId: Int,
        projectCode: String,
        scheduleId: Int,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.putImageSelfieGeo(employeeId, projectCode, scheduleId, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GeoSelfiePostResponseModel>() {
                    override fun onSuccess(t: GeoSelfiePostResponseModel) {
                        postSelfieGeo.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GeoSelfiePostResponseModel::class.java
                            )
                            postSelfieGeo.value = error
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


    fun getStatusAttendance(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAttendance(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceStatusResponseModelNew>() {
                    override fun onSuccess(t: AttendanceStatusResponseModelNew) {
                        attStatus.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            isLoading?.value = false
                            if (e.response()!!.code() == 401 || e.response()!!.code() == 403) {
                                Toast.makeText(
                                    context,
                                    "Sesi anda telah berakhir.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                CarefastOperationPref.logout()
                                val i = Intent(context, SplashActivity::class.java)
                                context.startActivity(i)
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    AttendanceStatusResponseModelNew::class.java
                                )
                                attStatus.value = error
                            }
                            Log.d("Profile", "onError: ${e.response()!!.code()}")
                        } else if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException) {
                            isConnectionTimeout.postValue(true)
                            isLoading?.value = false
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan." + attStatus.value,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        )
    }


    fun getCheckAttendance(id: Int, employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getCheckAttendance(id, employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceCheckResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckResponseModel) {
                        attCheck.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFail.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckResponseModel::class.java
                            )
                            attCheck.value = error
                            Log.d("Profile", "onError: $error")
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


    fun getCheckAttendanceOut(employeeId: Int, scheduleId: Int) {
        compositeDisposable.add(
            repository.getCheckAttendanceOut(employeeId, scheduleId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceCheckOutResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckOutResponseModel) {
                        attCheckOut.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFailOut.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckOutResponseModel::class.java
                            )
                            attCheckOut.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getHistoryAttendance(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getHistory(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryResponseModel>() {
                    override fun onSuccess(t: HistoryResponseModel) {
                        historyResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                HistoryResponseModel::class.java
                            )
                            historyResponseModel.value = error
                            Log.d("History Attendance", "onError: $error")
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


    fun getHistoryAttendanceByDate(
        employeeId: Int,
        projectCode: String,
        datePrefix: String,
        dateSuffix: String
    ) {
        compositeDisposable.add(
            repository.getHistoryByDate(employeeId, projectCode, datePrefix, dateSuffix)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HistoryByDateResponseModel>() {
                    override fun onSuccess(t: HistoryByDateResponseModel) {
                        historyByDateResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                HistoryByDateResponseModel::class.java
                            )
                            historyByDateResponseModel.value = error
                            Log.d("History Att By Date", "onError: $error")
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


    fun getShift(projectId: String) {
        compositeDisposable.add(
            repository.getShift(projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceShiftStaffNotAttendanceResponseModel>() {
                    override fun onSuccess(t: AttendanceShiftStaffNotAttendanceResponseModel) {
                        shift.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceShiftStaffNotAttendanceResponseModel::class.java
                            )
                            shift.value = error
                            Log.d("Not Attendance", "onError: $error")
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


    fun getListStaffNotAttendance(employeeId: Int, projectCode: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffNotAttendance(employeeId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceNotAbsenResponseModel>() {
                    override fun onSuccess(t: AttendanceNotAbsenResponseModel) {
                        notAttendanceResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceNotAbsenResponseModel::class.java
                            )
                            notAttendanceResponseModel.value = error
                            Log.d("Not Attendance", "onError: $error")
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

    fun getListStaffNotAttendanceSPV(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffNotAttendanceSPV(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceListStaffNotAttendanceSPVResponseModel>() {
                    override fun onSuccess(t: AttendanceListStaffNotAttendanceSPVResponseModel) {
                        notAttendanceSPVResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceListStaffNotAttendanceSPVResponseModel::class.java
                            )
                            notAttendanceSPVResponseModel.value = error
                            Log.d("Not Attendance", "onError: $error")
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

    fun getListStaffNotAttendanceCSPV(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffNotAttendanceCSPV(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceListStaffNotAttendanceCSPVResponseModel>() {
                    override fun onSuccess(t: AttendanceListStaffNotAttendanceCSPVResponseModel) {
                        notAttendanceCSPVResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceListStaffNotAttendanceCSPVResponseModel::class.java
                            )
                            notAttendanceCSPVResponseModel.value = error
                            Log.d("Not Attendance", "onError: $error")
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


    fun getListStaffAlreadyAttendance(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffAlreadyAttendance(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceListStaffAlreadyAttendanceResponseModel>() {
                    override fun onSuccess(t: AttendanceListStaffAlreadyAttendanceResponseModel) {
                        alreadyAttendanceResponseModel.value = t
                        Log.d("natu", "$t")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceListStaffAlreadyAttendanceResponseModel::class.java
                            )
                            alreadyAttendanceResponseModel.value = error
                            Log.d("Already Attendance", "onError: $error")
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

    fun getListStaffSPVAlreadyAttendance(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffAlreadyAttendanceSPV(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceListStaffAlreadyAttendanceSPVResponseModel>() {
                    override fun onSuccess(t: AttendanceListStaffAlreadyAttendanceSPVResponseModel) {
                        alreadyAttendanceSPVResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceListStaffAlreadyAttendanceSPVResponseModel::class.java
                            )
                            alreadyAttendanceSPVResponseModel.value = error
                            Log.d("Already Attendance", "onError: $error")
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

    fun getListStaffCSPVAlreadyAttendance(projectCode: String, employeeId: Int, shiftId: Int) {
        compositeDisposable.add(
            repository.getListStaffAlreadyAttendanceCSPV(projectCode, employeeId, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceListStaffAlreadyAttendanceCSPVResponseModel>() {
                    override fun onSuccess(t: AttendanceListStaffAlreadyAttendanceCSPVResponseModel) {
                        alreadyAttendanceCSPVResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceListStaffAlreadyAttendanceCSPVResponseModel::class.java
                            )
                            alreadyAttendanceCSPVResponseModel.value = error
                            Log.d("Already Attendance", "onError: $error")
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


    fun getSch(employeeId: Int, projectId: String) {
        compositeDisposable.add(
            repository.getSch(employeeId, projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmployeeSchResponseModel>() {
                    override fun onSuccess(t: EmployeeSchResponseModel) {
                        employeeSchResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                EmployeeSchResponseModel::class.java
                            )
                            employeeSchResponseModel.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getSchById(id: Int, employeeId: Int, projectId: String) {
        compositeDisposable.add(
            repository.getSchById(id, employeeId, projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmployeeSchByIdResponseModel>() {
                    override fun onSuccess(t: EmployeeSchByIdResponseModel) {
                        employeeSchByIdResponseModel.value = t
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 400) {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EmployeeSchByIdResponseModel::class.java
                                )
                                employeeSchByIdResponseModel.value = error
                                isLoading?.value = false
                                Log.d("Profile", "onError: $error")
                            } else {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    EmployeeSchByIdResponseModel::class.java
                                )
                                employeeSchByIdResponseModel.value = error
                                isLoading?.value = false
                                Log.d("Profile", "onError: $error")
                            }
                        } else {
                            Log.d("Profile", "onError:")
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

    fun getQRModel(): MutableLiveData<QRCodeResponseModel> {
        return qrModel
    }

    fun getSelfie(): MutableLiveData<GeoSelfiePostResponseModel> {
        return postSelfieGeo
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

//    fun attStatusFail(): MutableLiveData<Int?> {
//        return attStatusFail
//    }


    //midLevel
    fun postAttendanceGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.postImageSelfieGeoMid(employeeId, projectCode, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GeoSelfiePostMidResponseModel>() {
                    override fun onSuccess(t: GeoSelfiePostMidResponseModel) {
                        postSelfieGeoMid.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GeoSelfiePostMidResponseModel::class.java
                            )
                            postSelfieGeoMid.value = error
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

    fun putAttendanceGeoMid(
        employeeId: Int,
        projectCode: String,
        imageSelfie: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.putImageSelfieGeoMid(employeeId, projectCode, imageSelfie)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GeoSelfiePostMidResponseModel>() {
                    override fun onSuccess(t: GeoSelfiePostMidResponseModel) {
                        postSelfieGeoMid.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GeoSelfiePostMidResponseModel::class.java
                            )
                            postSelfieGeoMid.value = error
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

    fun getSchMid(employeeId: Int, projectId: String) {
        compositeDisposable.add(
            repository.getSchMid(employeeId, projectId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmployeeSchMidResponseModel>() {
                    override fun onSuccess(t: EmployeeSchMidResponseModel) {
                        employeeSchMidResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                EmployeeSchMidResponseModel::class.java
                            )
                            employeeSchMidResponseModel.value = error
                            Log.d("Profile", "onError: $error")
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


    fun getCheckAttendanceMid(id: Int, employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getCheckAttendance(id, employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceCheckResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckResponseModel) {
                        attCheck.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFail.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckResponseModel::class.java
                            )
                            attCheck.value = error
                            Log.d("Profile", "onError: $error")
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


    fun getCheckAttendanceOutMid(employeeId: Int, scheduleId: Int) {
        compositeDisposable.add(
            repository.getCheckAttendanceOut(employeeId, scheduleId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceCheckOutResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckOutResponseModel) {
                        attCheckOut.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFailOut.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckOutResponseModel::class.java
                            )
                            attCheckOut.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getSelfieMid(): MutableLiveData<GeoSelfiePostMidResponseModel> {
        return postSelfieGeoMid
    }


    //Mid level one sch
    fun getAttendanceMidOneSch(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getStatusAttendanceMidOneSch(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<MidLevelOneSchStatusResponseModel>() {
                    override fun onSuccess(t: MidLevelOneSchStatusResponseModel) {
                        statusAttendanceMidLevelOneSchResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                MidLevelOneSchStatusResponseModel::class.java
                            )
                            statusAttendanceMidLevelOneSchResponseModel.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getStatusAttendanceMidOneSch(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getAttendanceMidOneSch(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MidLevelOneSchResponseModel>() {
                    override fun onSuccess(t: MidLevelOneSchResponseModel) {
                        midLevelOneSchResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                MidLevelOneSchResponseModel::class.java
                            )
                            midLevelOneSchResponseModel.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getCheckAttendanceMidOneSch(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getCheckAttendanceMidLevelOneSch(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceCheckResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckResponseModel) {
                        attCheckMidOneSch.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFail.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckResponseModel::class.java
                            )
                            attCheckMidOneSch.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getCheckAttendanceOutMidOneSch(employeeId: Int, projectCode: String) {
        compositeDisposable.add(
            repository.getCheckAttendanceOutMidLevelOneSch(employeeId, projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<AttendanceCheckOutResponseModel>() {
                    override fun onSuccess(t: AttendanceCheckOutResponseModel) {
                        attCheckOutMidOneSch.value = t
                        Log.d("Viewmodelattendance", "onSuccess: ")
                    }

                    override fun onError(e: Throwable) {
//                        if (e is SocketException || e is UnknownHostException || e is SocketTimeoutException || e is HttpException) {
//                            isConnectionTimeout.postValue(true)
//                            val body = (e as HttpException).response()?.code()
//                            attCheckFailOut.value = body
//                        }
                        if (e is HttpException) {
                            isLoading?.value = false
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceCheckOutResponseModel::class.java
                            )
                            attCheckOutMidOneSch.value = error
                            Log.d("Profile", "onError: $error")
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

    fun getMyTeam(employeeId: Int, projectId: String, page: Int) {
        compositeDisposable.add(
            repository.getMyTeam(employeeId, projectId  ,page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<FabPersonResponseModel>() {
                    override fun onSuccess(t: FabPersonResponseModel) {
                        myTeamViewModel.value = t
                        Log.d("red", "$t")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabPersonResponseModel::class.java
                            )
                            myTeamViewModel.value = error
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

    fun getMyTeamSpv(projectId: String, page: Int){
        compositeDisposable.add(
            repository.getMyTeamSpv(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                DisposableSingleObserver<FabPersonResponseModel>(){
                    override fun onSuccess(t: FabPersonResponseModel) {
                        myTeamSPVViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabPersonResponseModel::class.java
                            )
                            myTeamSPVViewModel.value = error
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        )
    }

    fun getMyTeamCspv(projectId: String, page: Int){
        compositeDisposable.add(
            repository.getMyTeamCspv(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<FabPersonResponseModel>(){
                    override fun onSuccess(t: FabPersonResponseModel) {
                        myTeamCSPVViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabPersonResponseModel::class.java
                            )
                            myTeamCSPVViewModel.value = error
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        )
    }

    fun getHistoryTeamLead(employeeId: Int, projectCode: String, date: String, shiftId: Int) {
        compositeDisposable.add(
            repository.getHistoryTeamLead(employeeId, projectCode, date, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<FabTeamResponseModel>() {
                    override fun onSuccess(t: FabTeamResponseModel) {
                        myHistoryTeamLeadViewModel.value = t
                        Log.d("fabTeam", "$t")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabTeamResponseModel::class.java
                            )
                            myHistoryTeamLeadViewModel.value = error
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        )
    }

    fun getHistorySPV(employeeId: Int, projectCode: String, date: String, shiftId: Int){
        compositeDisposable.add(
            repository.getHistorySPV(employeeId, projectCode, date, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                DisposableSingleObserver<FabTeamResponseModel>(){
                    override fun onSuccess(t: FabTeamResponseModel) {
                        myHistorySPVViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabTeamResponseModel::class.java
                            )
                            myHistorySPVViewModel.value = error
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        )
    }

    fun getHistoryFM(employeeId: Int, projectCode: String, date: String, shiftId: Int){
        compositeDisposable.add(
            repository.getHistoryFM(employeeId, projectCode, date, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                DisposableSingleObserver<FabTeamResponseModel>(){
                    override fun onSuccess(t: FabTeamResponseModel) {
                        myHistoryFMViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabTeamResponseModel::class.java
                            )
                            myHistoryFMViewModel.value = error
                        } else {
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                })
        )
    }

    fun getHistoryResult(employeeId: Int, month: Int, year: Int){
        compositeDisposable.add(
            repository.getHistoryResult(employeeId, month, year)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                DisposableSingleObserver<FabHistoryResultResponseModel>(){
                    override fun onSuccess(t: FabHistoryResultResponseModel) {
                        myHistoryResultViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException){
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabHistoryResultResponseModel::class.java
                            )
                            myHistoryResultViewModel.value = error
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getSearchPerson(projectId: String, page: Int, keywords: String){
        compositeDisposable.add(
            repository.getSearchPerson(projectId, page, keywords)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                DisposableSingleObserver<FabSearchPersonResponseModel>(){
                    override fun onSuccess(t: FabSearchPersonResponseModel) {
                        searchPersonViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException){
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                FabSearchPersonResponseModel::class.java
                            )
                            searchPersonViewModel.value = error
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }
    fun getListAttendanceNotAbsent(employeeId: Int, projectCode: String, shiftId: Int){
        compositeDisposable.add(
            repository.getListAttendanceNotAbsent(employeeId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceNotAbsenResponseModel>(){
                    override fun onSuccess(t: AttendanceNotAbsenResponseModel) {
                        listAttendanceNotAbsentViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceNotAbsenResponseModel::class.java
                            )
                            listAttendanceNotAbsentViewModel.value = error
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

    fun getListAttendanceAlreadyAbsent(employeeId: Int, projectCode: String, shiftId: Int){
        compositeDisposable.add(
            repository.getListAttendanceAlreadyAbsent(employeeId, projectCode, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceAlreadyAbsentResponseModel>(){
                    override fun onSuccess(t: AttendanceAlreadyAbsentResponseModel) {
                        listAttendanceAlreadyAbsentViewModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceAlreadyAbsentResponseModel::class.java
                            )
                            listAttendanceAlreadyAbsentViewModel.value = error
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

    // attendance user flying
    fun postUserFlyingIn(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ) {
        compositeDisposable.add(
            repository.postUserFlyingIn(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceUserFlyingResponse>() {
                    override fun onSuccess(t: AttendanceUserFlyingResponse) {
                        attendanceInUserFlyingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceUserFlyingResponse::class.java
                            )
                            attendanceInUserFlyingModel.value = error
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

    fun postUserFlyingOut(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        latitude: String,
        longitude: String,
        address: String,
        radius: String,
        description: String,
        deviceInfo: String
    ) {
        compositeDisposable.add(
            repository.postUserFlyingOut(userId, idSchedule, projectCode, latitude, longitude, address, radius, description, deviceInfo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceUserFlyingResponse>() {
                    override fun onSuccess(t: AttendanceUserFlyingResponse) {
                        attendanceOutUserFlyingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceUserFlyingResponse::class.java
                            )
                            attendanceOutUserFlyingModel.value = error
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

    fun getStatusUserFlying(
        userId: Int,
        idSchedule: Int,
        projectCode: String,
        type: String
    ) {
        compositeDisposable.add(
            repository.getStatusUserFlying(userId, idSchedule, projectCode, type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceUserFlyingResponse>() {
                    override fun onSuccess(t: AttendanceUserFlyingResponse) {
                        getStatusUserFlyingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceUserFlyingResponse::class.java
                            )
                            getStatusUserFlyingModel.value = error
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

    fun getAccessUserFlying(
        projectCode: String
    ) {
        compositeDisposable.add(
            repository.getAccessUserFlying(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<AttendanceUserFlyingResponse>() {
                    override fun onSuccess(t: AttendanceUserFlyingResponse) {
                        getAccessUserFlyingModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                AttendanceUserFlyingResponse::class.java
                            )
                            getAccessUserFlyingModel.value = error
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
    fun regisFaceRecog(employeeId: Int, file: MultipartBody.Part){
        compositeDisposable.add(
            repository.regisFaceRecog(employeeId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisFaceRecogResponseModel>(){
                    override fun onSuccess(t: RegisFaceRecogResponseModel) {
                            regisFaceRecogModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                RegisFaceRecogResponseModel::class.java
                            )
                            regisFaceRecogModel.value = error
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

    fun verifyFaceRecog(file: MultipartBody.Part, employeeId: Int){
        compositeDisposable.add(
            repository.verifyFaceRecog(file, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VerifyFaceResponseModel>(){
                    override fun onSuccess(t: VerifyFaceResponseModel) {

                            verifyFaceRecogModel.value = t

                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                VerifyFaceResponseModel::class.java
                            )
                            verifyFaceRecogModel.value = error
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

    fun verifyFaceRecogViewModel(): MutableLiveData<VerifyFaceResponseModel>{
        return verifyFaceRecogModel
    }

    fun regisFaceRecogViewModel(): MutableLiveData<RegisFaceRecogResponseModel>{
        return regisFaceRecogModel
    }

    fun listAttendanceAlreadyAbsent(): MutableLiveData<AttendanceAlreadyAbsentResponseModel>{
        return listAttendanceAlreadyAbsentViewModel
    }

    fun listAttendanceNotAbsent(): MutableLiveData<AttendanceNotAbsenResponseModel>{
        return listAttendanceNotAbsentViewModel
    }

    fun getSearchPerson(): MutableLiveData<FabSearchPersonResponseModel>{
        return searchPersonViewModel
    }

    fun getHistoryResultPerson(): MutableLiveData<FabHistoryResultResponseModel>{
        return myHistoryResultViewModel
    }

    fun getHistoryTeamSPV(): MutableLiveData<FabTeamResponseModel>{
        return myHistorySPVViewModel
    }

    fun getHistoryTeamFM(): MutableLiveData<FabTeamResponseModel>{
        return myHistoryFMViewModel
    }

    fun getHistoryTeamLeads(): MutableLiveData<FabTeamResponseModel>{
        return myHistoryTeamLeadViewModel
    }

    fun getMyTeam(): MutableLiveData<FabPersonResponseModel> {
        return myTeamViewModel
    }

    fun getMyTeamSPV(): MutableLiveData<FabPersonResponseModel>{
        return myTeamSPVViewModel
    }

    fun getMyTeamCspv(): MutableLiveData<FabPersonResponseModel>{
        return myTeamCSPVViewModel
    }





}