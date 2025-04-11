package com.hkapps.hygienekleen.features.features_vendor.profile.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_vendor.profile.data.repository.ProfileRepository
import com.hkapps.hygienekleen.features.features_vendor.profile.model.*
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    val isErrorProfileEmployeeModel = MutableLiveData<Boolean>()

    val profileResponseModel = MutableLiveData<ProfileEmployeeResponseModel>()
    val updateProfileResponseModel = MutableLiveData<UpdateProfileResponseModel>()
    val changePassResponseModel = MutableLiveData<ChangePassResponseModel>()
    val updatePhotoProfileModel = MutableLiveData<UpdateProfileResponseModel>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: ProfileRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getProfile(employeeId: Int) {
        compositeDisposable.add(
            repository.getProfile(employeeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProfileEmployeeResponseModel>() {
                    override fun onSuccess(t: ProfileEmployeeResponseModel) {
                        profileResponseModel.value = t
                        Log.d("fritz","")
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ProfileResponseModel::class.java
                            )
                            isErrorProfileEmployeeModel.value = true
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



    fun updateProfile(
        employeeId: Int,
        employeeEmail: String,
        employeePhone: String,
        employeePhone2: String,
        employeeAddress: String,
        employeeKtpAddress: String,
        employeeBirthDate: String,
        employeePlaceOfBirth: String,
        employeeGender: String,
        employeeMarriageStatus: String,
        employeeMotherName: String,
        employeeReligion: String,
        employeeCountChildren: String,
        employeeNik: String
    ) {
        compositeDisposable.add(
            repository.updateProfile(employeeId, employeeEmail, employeePhone, employeePhone2, employeeAddress, employeeKtpAddress, employeeBirthDate, employeePlaceOfBirth, employeeGender, employeeMarriageStatus, employeeMotherName, employeeReligion, employeeCountChildren, employeeNik)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfileResponseModel>() {
                    override fun onSuccess(t: UpdateProfileResponseModel) {
                        updateProfileResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfileResponseModel::class.java
                            )
                            updateProfileResponseModel.value = error
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

    fun changePass(
        employeeId: Int,
        passOld: String,
        passNew: String,
        passConfirm: String
    ) {
        val changepass = ChangePassRequestModel(passOld, passNew, passConfirm)
        compositeDisposable.add(
            repository.changePass(employeeId, changepass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChangePassResponseModel>() {
                    override fun onSuccess(t: ChangePassResponseModel) {
                        changePassResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ChangePassResponseModel::class.java
                            )
                            changePassResponseModel.value = error
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

    fun updatePhotoProfile(
        userId: Int,
        img: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.updatePhotoProfile(userId, img)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfileResponseModel>() {
                    override fun onSuccess(t: UpdateProfileResponseModel) {
                        updatePhotoProfileModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfileResponseModel::class.java
                            )
                            updatePhotoProfileModel.value = error
                        }
                    }

                })
        )
    }

    fun getProfileEmployee(): MutableLiveData<ProfileEmployeeResponseModel> {
        return profileResponseModel
    }

    fun isErrorEmployeeProfile(): MutableLiveData<Boolean>{
        return isErrorProfileEmployeeModel
    }

    fun updateProfileObs(): MutableLiveData<UpdateProfileResponseModel> {
        return updateProfileResponseModel
    }

    fun changepassObs(): MutableLiveData<ChangePassResponseModel> {
        return changePassResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}