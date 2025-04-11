package com.hkapps.hygienekleen.features.features_client.setting.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.setting.data.repository.SettingClientRepository
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePassClientRequestModel
import com.hkapps.hygienekleen.features.features_client.setting.model.changePass.ChangePasswordClientResponse
import com.hkapps.hygienekleen.features.features_client.setting.model.editProfile.EditProfileClientResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class SettingClientViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val changePassModel = MutableLiveData<ChangePasswordClientResponse>()
    private val editProfileModel = MutableLiveData<EditProfileClientResponse>()
    private val profileResponseModel = MutableLiveData<EditProfileClientResponse>()

    @Inject
    lateinit var repository: SettingClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun changePassClient(
        clientId: Int,
        oldPassword: String,
        password: String,
        confirmPassword: String
    ) {
        val changePass = ChangePassClientRequestModel(clientId, oldPassword, password, confirmPassword)
        compositeDisposable.add(
            repository.changePassClient(changePass)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ChangePasswordClientResponse>() {
                    override fun onSuccess(t: ChangePasswordClientResponse) {
                        changePassModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ChangePasswordClientResponse::class.java
                            )
                            changePassModel.value = error
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

    fun updateProfileClient(
        clientId: Int,
        clientEmail: String,
        clientName: String,
        file: MultipartBody.Part
    ) {
        compositeDisposable.add(
            repository.updateProfileClient(clientId, clientEmail, clientName, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EditProfileClientResponse>() {
                    override fun onSuccess(t: EditProfileClientResponse) {
                        editProfileModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                EditProfileClientResponse::class.java
                            )
                            editProfileModel.value = error
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

    fun getProfileClient(clientId: Int) {
        compositeDisposable.add(
            repository.getProfileClient(clientId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EditProfileClientResponse>() {
                    override fun onSuccess(t: EditProfileClientResponse) {
                        profileResponseModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                EditProfileClientResponse::class.java
                            )
                            profileResponseModel.value = error
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

    fun changePassClientModel(): MutableLiveData<ChangePasswordClientResponse> {
        return changePassModel
    }

    fun updateProfileClientModel(): MutableLiveData<EditProfileClientResponse> {
        return editProfileModel
    }

    fun getProfileClientModel(): MutableLiveData<EditProfileClientResponse> {
        return profileResponseModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}