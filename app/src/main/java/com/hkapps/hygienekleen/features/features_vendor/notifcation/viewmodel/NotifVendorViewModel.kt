package com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.repository.NotifRepository
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.NotifResponseModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.model.*
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.detailProcessComplaint.DetailProcessComplaintResponse
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.listOperator.OperatorComplaintResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifComplaintMidLevel.NotifMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.NotificationMidHistory
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.NotificationMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.notifmidhistory.readnotifmid.ReadNotificationMidResponseModel
import com.hkapps.hygienekleen.features.features_vendor.notifcation.model.processComplaint.ProcessComplaintResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class NotifVendorViewModel(application: Application) : AndroidViewModel(application) {
    val chooseStaffResponseModel = MutableLiveData<ChooseStaffResponseModel>()

    val historyResponseModel = MutableLiveData<HistoryResponseModel>()
    val historyByDateResponseModel = MutableLiveData<HistoryByDateResponseModel>()
    val isConnectionTimeout = MutableLiveData<Boolean>()
    val shift = MutableLiveData<AttendanceShiftStaffNotAttendanceResponseModel>()
    val employeeSchResponseModel = MutableLiveData<EmployeeSchResponseModel>()


    val notifModel = MutableLiveData<NotifResponseModel>()
    val notifMidModel = MutableLiveData<NotifMidResponseModel>()

    val detailProcessResponseModel = MutableLiveData<DetailProcessComplaintResponse>()
    val processComplaintModel = MutableLiveData<ProcessComplaintResponseModel>()
    val operatorResponseModel = MutableLiveData<OperatorComplaintResponseModel>()
    val submitOperatorResponseModel = MutableLiveData<ProcessComplaintResponseModel>()
    val uploadBeforeResponseModel = MutableLiveData<ProcessComplaintResponseModel>()
    val uploadProgressResponseModel = MutableLiveData<ProcessComplaintResponseModel>()
    val uploadAfterResponseModel = MutableLiveData<ProcessComplaintResponseModel>()
    val submitComplaintResponseModel = MutableLiveData<ProcessComplaintResponseModel>()
    //old
    val getNotificationHistoryLeader = MutableLiveData<NotificationMidHistory>()
    val getNotificationHistoryOperator = MutableLiveData<NotificationMidHistory>()
    //new
    val getNotifHistoryLeaderResponseModel = MutableLiveData<NotificationMidResponseModel>()
    val getNotifHistoryOperatorResponseModel = MutableLiveData<NotificationMidResponseModel>()

    val putReadNotificationMid = MutableLiveData<ReadNotificationMidResponseModel>()

    private val compositeDisposable = CompositeDisposable()

    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: NotifRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getNotif(projectId: String, page: Int) {
        compositeDisposable.add(
            repository.getNotif(projectId, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotifResponseModel>() {
                    override fun onSuccess(t: NotifResponseModel) {
                        notifModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotifResponseModel::class.java
                            )
                            notifModel.value = error
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


    fun getNotifMid(projectId: String, jobCode: String, page: Int) {
        compositeDisposable.add(
            repository.getNotifMid(projectId, jobCode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotifMidResponseModel>() {
                    override fun onSuccess(t: NotifMidResponseModel) {
                        notifMidModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotifMidResponseModel::class.java
                            )
                            notifMidModel.value = error
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

    fun getDetailProcessComplaint(complaintId: Int) {
        compositeDisposable.add(
            repository.getDetailProcessComplaint(complaintId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailProcessComplaintResponse>() {
                    override fun onSuccess(t: DetailProcessComplaintResponse) {
                        detailProcessResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                DetailProcessComplaintResponse::class.java
                            )
                            detailProcessResponseModel.value = error
                        } else {
                            Log.e("NorifVendorVM", "onError: can't get detail complaint", )
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

    fun putProcessComplaint(complaintId: Int, employeeId: Int, comments: String) {
        compositeDisposable.add(
            repository.putProcessComplaint(complaintId, employeeId, comments)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        processComplaintModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            processComplaintModel.value = error
                        }else{
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

    fun getOperatorComplaint(projectCode: String) {
        compositeDisposable.add(
            repository.getOperatorComplaint(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<OperatorComplaintResponseModel>() {
                    override fun onSuccess(t: OperatorComplaintResponseModel) {
                        if (t.code == 200) {
                            operatorResponseModel.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                OperatorComplaintResponseModel::class.java
                            )
                            operatorResponseModel.value = error
                            isLoading?.value = false
                        }else{
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan.",
                                Toast.LENGTH_SHORT
                            ).show()
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun putSubmitOperator(complaintId: Int, workerId: Int) {
        compositeDisposable.add(
            repository.putSubmitOperator(complaintId, workerId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        submitOperatorResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            submitOperatorResponseModel.value = error
                        }else{
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

    fun putBeforeImageComplaint(complaintId: Int, employeeId: Int, beforeImage: MultipartBody.Part) {
        compositeDisposable.add(
            repository.putBeforeImageComplaint(complaintId, employeeId, beforeImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        uploadBeforeResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            uploadBeforeResponseModel.value = error
                        }else{
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

    fun putProgressImageComplaint(complaintId: Int, employeeId: Int, progressImage: MultipartBody.Part) {
        compositeDisposable.add(
            repository.putProgressImageComplaint(complaintId, employeeId, progressImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        uploadProgressResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            uploadProgressResponseModel.value = error
                        }else{
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

    fun putAfterImageComplaint(complaintId: Int, employeeId: Int, afterImage: MultipartBody.Part) {
        compositeDisposable.add(
            repository.putAfterImageComplaint(complaintId, employeeId, afterImage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        uploadAfterResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            uploadAfterResponseModel.value = error
                        }else{
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

    fun putSubmitComplaint(complaintId: Int, employeeId: Int) {
        compositeDisposable.add(
            repository.putSubmitComplaint(complaintId, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProcessComplaintResponseModel>() {
                    override fun onSuccess(t: ProcessComplaintResponseModel) {
                        submitComplaintResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProcessComplaintResponseModel::class.java
                            )
                            submitComplaintResponseModel.value = error
                        }else{
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

    fun getNotificationLeader(employeeId: Int, projectCode: String, category: String){
        compositeDisposable.add(
            repository.getNotifHistoryLeader(employeeId, projectCode, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotificationMidHistory>() {
                    override fun onSuccess(t: NotificationMidHistory) {
                        getNotificationHistoryLeader.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotificationMidHistory::class.java
                            )
                            getNotificationHistoryLeader.value = error
                        }else{
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

    fun getNotificationOperator(employeeId: Int, projectCode: String, category: String){
        compositeDisposable.add(
            repository.getNotifHistoryOperator(employeeId, projectCode, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotificationMidHistory>(){
                    override fun onSuccess(t: NotificationMidHistory) {
                        getNotificationHistoryOperator.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotificationMidHistory::class.java
                            )
                            getNotificationHistoryOperator.value = error
                        }else{
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

    fun getNotifMidLeader(employeeId: Int, projectCode: String, category: String, page: Int){
        compositeDisposable.add(
            repository.getNotificationHistoryLeader(employeeId, projectCode, category, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotificationMidResponseModel>(){
                    override fun onSuccess(t: NotificationMidResponseModel) {
                        getNotifHistoryLeaderResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotificationMidResponseModel::class.java
                            )
                            getNotifHistoryLeaderResponseModel.value = error
                        }else{
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

    fun getNotifMidOperator(employeeId: Int, projectCode: String, category: String, page: Int){
        compositeDisposable.add(
            repository.getNotificationHistoryOperator(employeeId, projectCode, category, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<NotificationMidResponseModel>(){
                    override fun onSuccess(t: NotificationMidResponseModel) {
                        getNotifHistoryOperatorResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                NotificationMidResponseModel::class.java
                            )
                            getNotifHistoryOperatorResponseModel.value = error
                        }else{
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

    fun putReadNotificationMid(employeeId: Int, notificationId: Int){
        compositeDisposable.add(
            repository.putReadNotificationMid(employeeId, notificationId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ReadNotificationMidResponseModel>(){
                    override fun onSuccess(t: ReadNotificationMidResponseModel) {
                        putReadNotificationMid.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException){
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ReadNotificationMidResponseModel::class.java
                            )
                            putReadNotificationMid.value = error
                        } else {
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        )
    }

    fun getNotificationLeaderViewModel(): MutableLiveData<NotificationMidResponseModel>{
        return getNotifHistoryLeaderResponseModel
    }

    fun getNotificationOperatorViewModel(): MutableLiveData<NotificationMidResponseModel>{
        return getNotifHistoryOperatorResponseModel
    }

    fun putReadNotifMid(): MutableLiveData<ReadNotificationMidResponseModel>{
        return putReadNotificationMid
    }


    fun getNotificationLeader(): MutableLiveData<NotificationMidHistory>{
        return getNotificationHistoryLeader
    }

    fun getNotificationOperator(): MutableLiveData<NotificationMidHistory>{
        return getNotificationHistoryOperator
    }

    fun getNotificationResponseModel(): MutableLiveData<NotifResponseModel> {
        return notifModel
    }

    fun getNotificationMidResponseModel(): MutableLiveData<NotifMidResponseModel> {
        return notifMidModel
    }

    fun getDetailComplaintModel(): MutableLiveData<DetailProcessComplaintResponse> {
        return detailProcessResponseModel
    }

    fun getProcessModel(): MutableLiveData<ProcessComplaintResponseModel>{
        return processComplaintModel
    }

    fun getOperatorModel(): MutableLiveData<OperatorComplaintResponseModel>{
        return operatorResponseModel
    }

    fun getSubmitComplaintModel(): MutableLiveData<ProcessComplaintResponseModel>{
        return submitOperatorResponseModel
    }

    fun getBeforeImageModel(): MutableLiveData<ProcessComplaintResponseModel> {
        return uploadBeforeResponseModel
    }

    fun getProgressImageModel(): MutableLiveData<ProcessComplaintResponseModel> {
        return uploadProgressResponseModel
    }

    fun getAfterImageModel(): MutableLiveData<ProcessComplaintResponseModel> {
        return uploadAfterResponseModel
    }

    fun getSubmitProcessModel(): MutableLiveData<ProcessComplaintResponseModel> {
        return submitComplaintResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}