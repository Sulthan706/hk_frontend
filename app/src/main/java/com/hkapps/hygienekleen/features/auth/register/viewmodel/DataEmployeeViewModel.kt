package com.hkapps.hygienekleen.features.auth.register.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.auth.register.data.repository.DaftarRepository
import com.hkapps.hygienekleen.features.auth.register.model.getDataEmployee.DataEmployeeResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class DataEmployeeViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()
    private val isConnectionTimeout = MutableLiveData<Boolean>()
    private val dataEmployeeModel = MutableLiveData<DataEmployeeResponseModel>()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()


    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: DaftarRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getDataEmployee(employeeNuc: String, employeeNik: String) {
        compositeDisposable.add(
            repository.getEmployeeData(employeeNuc, employeeNik)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DataEmployeeResponseModel>() {
                    override fun onSuccess(t: DataEmployeeResponseModel) {
                        if (t.code == 200) {
                            dataEmployeeModel.value = t
                            isLoading?.value = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("getDataEmployee", "onError: error get data employee")
                        if (e is HttpException) {
                            if (e.response()!!.code() == 401 || e.response()!!.code() == 403){
                                Toast.makeText(context, "Terjadi kesalahan.", Toast.LENGTH_LONG).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), DataEmployeeResponseModel::class.java)
                                dataEmployeeModel.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getDataEmployeeModel(): MutableLiveData<DataEmployeeResponseModel> {
        return dataEmployeeModel
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}