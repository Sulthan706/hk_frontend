package com.hkapps.hygienekleen.features.facerecog.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.facerecog.data.repository.FaceRecogRepository
import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.CheckUserStatusResponse
import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.FaceRecogResponse
import com.hkapps.hygienekleen.features.facerecog.model.facerecognew.ListRandomGestureResponse
import com.hkapps.hygienekleen.features.facerecog.model.loginforfailure.ErrorFailureFaceResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.profilevendor.GetProfileUserResponseModel
import com.hkapps.hygienekleen.features.facerecog.model.statsregisface.StatsRegisFaceResponseModel
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

class FaceRecogViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()


    private val statsRegisFaceRecogModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val regisFaceModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val verifyFaceRecogModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val profileUserModel = MutableLiveData<GetProfileUserResponseModel>()
    private val loginFailureFaceModel = MutableLiveData<ErrorFailureFaceResponseModel>()
    //management
    private val statsRegisManagementFaceRecogModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val regisFaceManagementModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val verifyManagementFaceRecogModel = MutableLiveData<StatsRegisFaceResponseModel>()
    private val loginManagementFailureFaceModel = MutableLiveData<ErrorFailureFaceResponseModel>()

    // Compreface
    val registerNew = MutableLiveData<StatsRegisFaceResponseModel>()
    val verifyNew = MutableLiveData<StatsRegisFaceResponseModel>()

    val registerManagementNew = MutableLiveData<StatsRegisFaceResponseModel>()
    val verifyManagementNew = MutableLiveData<StatsRegisFaceResponseModel>()


    // Both
    val registerFaceBoth = MutableLiveData<StatsRegisFaceResponseModel>()
    val verifyFaceBoth = MutableLiveData<StatsRegisFaceResponseModel>()

    val registerFaceManagementBoth = MutableLiveData<StatsRegisFaceResponseModel>()
    val verifyFaceManagementBoth = MutableLiveData<StatsRegisFaceResponseModel>()


    val checkStatusUser = MutableLiveData<FaceRecogResponse<CheckUserStatusResponse>>()
    val listGesture = MutableLiveData<ListRandomGestureResponse>()

    //failure validation
    private val _errorCounter = MutableLiveData<Int>().apply { value = 0 }
    val errorCounter: LiveData<Int> get() = _errorCounter


    @Inject
    lateinit var repository: FaceRecogRepository

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun incrementErrorCounter() {
        _errorCounter.value = (_errorCounter.value ?: 0) + 1
    }

    fun resetErrorCounter() {
        _errorCounter.value = 0
    }

    // BOTH MANAGEMENT
    fun employeeRegisterBoth(
        employeeId : Int,
        file : MultipartBody.Part
    ){

        compositeDisposable.add(
            repository.apiRegisterBoth(employeeId,file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        registerFaceBoth.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    registerFaceBoth.value = error
                                    isLoading?.value = false
                                }
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

    fun employeeVerifyBoth(
        employeeId : Int,
        file : MultipartBody.Part
    ){

        compositeDisposable.add(
            repository.apiVerifyBoth(employeeId,file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        verifyFaceBoth.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    verifyFaceBoth.value = error
                                    isLoading?.value = false
                                }
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

    fun managementRegisterBoth(
        adminMasterId : Int,
        file : MultipartBody.Part
    ){
        compositeDisposable.add(
            repository.apiRegisterManagementBoth(adminMasterId,file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        registerFaceManagementBoth.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    registerFaceManagementBoth.value = error
                                    isLoading?.value = false
                                }
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

    fun managementVerifyBoth(
        adminMasterId: Int,
        file: MultipartBody.Part
    ){

        compositeDisposable.add(
            repository.apiVerifyManagementBoth(adminMasterId,file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        verifyFaceManagementBoth.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    verifyFaceManagementBoth.value = error
                                    isLoading?.value = false
                                }
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

    // COMPREFACE EMPLOYEE
    fun registerNewFaceRecognition(employeeId : Int,image : MultipartBody.Part){
        compositeDisposable.add(
            repository.registerFaceNewEmployee(employeeId,image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        registerNew.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    registerNew.value = error
                                    isLoading?.value = false
                                }
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

    fun verifyNewFaceRecognition(employeeId : Int,image : MultipartBody.Part){
        compositeDisposable.add(
            repository.verifyUserFaceNewEmployee(employeeId,image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        verifyNew.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    verifyNew.value = error
                                    isLoading?.value = false
                                }
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

    // COMPREFACE MANAGEMENT
    fun registerNewFaceRecognitionManagement(adminMasterId : Int,image : MultipartBody.Part){
        compositeDisposable.add(
            repository.registerFaceNewManagement(adminMasterId,image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        registerManagementNew.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    registerManagementNew.value = error
                                    isLoading?.value = false
                                }
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

    fun verifyNewFaceRecognitionManagement(adminMasterId : Int,image : MultipartBody.Part){
        compositeDisposable.add(
            repository.verifyUserFaceNewManagement(adminMasterId,image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        verifyManagementNew.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                if (errorBody != null) {
                                    val gson = Gson()
                                    val error = gson.fromJson(
                                        errorBody.string(),
                                        StatsRegisFaceResponseModel::class.java
                                    )
                                    verifyManagementNew.value = error
                                    isLoading?.value = false
                                }
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

    fun getListGesture(){
        compositeDisposable.add(
            repository.getListGesture()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListRandomGestureResponse>(){
                    override fun onSuccess(t: ListRandomGestureResponse) {
                        listGesture.value = t
                    }

                    override fun onError(e: Throwable) {
                        when (e) {
                            is HttpException -> {
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(
                                    errorBody?.string(),
                                    ListRandomGestureResponse::class.java
                                )
                                listGesture.value = error
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

    fun getStatsRegisFaceRecog(employeeId: Int){
        compositeDisposable.add(
            repository.getStatsRegisFaceRecog(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        statsRegisFaceRecogModel.value = t
                        Log.d("UHUYYT","$t")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("UHUYY","$e $employeeId")
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            statsRegisFaceRecogModel.value = error
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

    // ALFABETA EMPLOYEE
    fun postRegisFaceRecog(employeeId: Int, file: MultipartBody.Part){
        compositeDisposable.add(
            repository.postRegisFaceRecog(employeeId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        regisFaceModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            regisFaceModel.value = error
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

    fun postVerifyFaceRecog(file: MultipartBody.Part, employeeId: Int){
        compositeDisposable.add(
            repository.verifyFaceRecog(file, employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        Log.d("ERROR HARI INII","$t")
                        verifyFaceRecogModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.d("ERROR HARI INI","$e")
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            verifyFaceRecogModel.value = error
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

    fun getProfileUser(employeeId: Int){
        compositeDisposable.add(
            repository.getProfileUser(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<GetProfileUserResponseModel>(){
                    override fun onSuccess(t: GetProfileUserResponseModel) {
                        profileUserModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                GetProfileUserResponseModel::class.java
                            )
                            profileUserModel.value = error
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
    fun postLoginFailureFace(employeeId: Int, employeePassword: String){
        compositeDisposable.add(
            repository.postLoginForErrorFace(employeeId, employeePassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ErrorFailureFaceResponseModel>(){
                    override fun onSuccess(t: ErrorFailureFaceResponseModel) {
                        if (t.code == 200) {
                            loginFailureFaceModel.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ErrorFailureFaceResponseModel::class.java
                            )
                            loginFailureFaceModel.value = error
                        }
                    }

                })
        )
    }

    //management

    fun getStatsRegisManagementFaceRecog(adminMaster: Int){
        compositeDisposable.add(
            repository.getStatsRegisManagementRecog(adminMaster)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        statsRegisManagementFaceRecogModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            statsRegisManagementFaceRecogModel.value = error
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

    // ALFABETA MANAGEMENT
    fun postRegisManagementFaceRecog(adminMasterId: Int, file: MultipartBody.Part){
        compositeDisposable.add(
            repository.postRegisManagementFaceRecog(adminMasterId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        regisFaceManagementModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            regisFaceManagementModel.value = error
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

    fun postVerifyManagementFaceRecog(file: MultipartBody.Part, adminMasterId: Int){
        compositeDisposable.add(
            repository.verifyManagementFaceRecog(file, adminMasterId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<StatsRegisFaceResponseModel>(){
                    override fun onSuccess(t: StatsRegisFaceResponseModel) {
                        verifyManagementFaceRecogModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                StatsRegisFaceResponseModel::class.java
                            )
                            verifyManagementFaceRecogModel.value = error
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

    fun postLoginManagementFailureFace(adminMasterId: Int, adminMasterPassword: String){
        compositeDisposable.add(
            repository.postManagementLoginForErrorFace(adminMasterId, adminMasterPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ErrorFailureFaceResponseModel>(){
                    override fun onSuccess(t: ErrorFailureFaceResponseModel) {
                            loginManagementFailureFaceModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = e.response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ErrorFailureFaceResponseModel::class.java
                            )
                            loginManagementFailureFaceModel.value = error
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


    //implemented in activity

    fun postVerifyFaceRecogViewModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return verifyFaceRecogModel
    }
    fun postRegisFaceViewModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return regisFaceModel
    }

    fun getStatsFaceRecogViewModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return statsRegisFaceRecogModel
    }

    fun getProfileViewModel(): MutableLiveData<GetProfileUserResponseModel>{
        return profileUserModel
    }

    fun postLoginFailureFaceViewModel(): MutableLiveData<ErrorFailureFaceResponseModel>{
        return loginFailureFaceModel
    }
    //management
    fun postVerifyManagementFaceRecogModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return verifyManagementFaceRecogModel
    }
    fun postRegisManagementFaceViewModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return regisFaceManagementModel
    }

    fun getStatsManagementFaceRecogViewModel(): MutableLiveData<StatsRegisFaceResponseModel>{
        return statsRegisManagementFaceRecogModel
    }

    fun postLoginFailureManagementFaceRecogViewModel(): MutableLiveData<ErrorFailureFaceResponseModel>{
        return loginManagementFailureFaceModel
    }


}