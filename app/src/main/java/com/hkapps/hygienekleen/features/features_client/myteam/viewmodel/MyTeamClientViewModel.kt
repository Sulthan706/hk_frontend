package com.hkapps.hygienekleen.features.features_client.myteam.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hkapps.hygienekleen.di.DaggerAppComponent
import com.hkapps.hygienekleen.features.features_client.myteam.data.repository.MyTeamClientRepository
import com.hkapps.hygienekleen.features.features_client.myteam.model.ManagementStructuralClientResponse
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamcount.CfTeamCountResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetail.DetailCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamdetailhistoryperson.DetailHistoryPersonCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlist.ListEmployeeCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamlistmanagement.ListManagementCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.cfteamscheduleteam.ScheduleTeamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.historyattendance.HistoryAttendanceCfteamResponseModel
import com.hkapps.hygienekleen.features.features_client.myteam.model.listwithshiftemployee.ListCftemByShiftResponseModel
import com.hkapps.hygienekleen.features.features_client.report.model.listshiftreport.ListShiftReportResponseModel
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class MyTeamClientViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDispossable = CompositeDisposable()
    var isLoading: MutableLiveData<Boolean?>? = MutableLiveData<Boolean?>()
    val listManagementStructuralClientResponse = MutableLiveData<ManagementStructuralClientResponse>()
    val getListCountCfTeam = MutableLiveData<CfTeamCountResponseModel>()
    val getListEmployeeCfteamResponse = MutableLiveData<ListEmployeeCfteamResponseModel>()
    val getListManagementCfteamResponse = MutableLiveData<ListManagementCfteamResponseModel>()
    val getDetailListCfteamResponse = MutableLiveData<DetailCfteamResponseModel>()
    val getDetailHistoryPersonCfteamResponse = MutableLiveData<DetailHistoryPersonCfteamResponseModel>()
    val getHistoryAttendanceCfteamResponse = MutableLiveData<HistoryAttendanceCfteamResponseModel>()
    val getScheduleCfteamResponse = MutableLiveData<ScheduleTeamResponseModel>()
    val getListShiftCfteamResponse = MutableLiveData<ListShiftReportResponseModel>()
    //list cfteam operator managemnnt by shift
    val getCfteamByShiftReponse = MutableLiveData<ListCftemByShiftResponseModel>()

    //buat dapetin contextnya
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    @Inject
    lateinit var repository: MyTeamClientRepository

    init {
        DaggerAppComponent.create().injectRepository(this)
    }

    fun getListManagementStructural(projectCode: String) {
        compositeDispossable.add(
            repository.getListManagement(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ManagementStructuralClientResponse>() {
                    override fun onSuccess(t: ManagementStructuralClientResponse) {
                        if (t.code == 200) {
                            listManagementStructuralClientResponse.value = t
                        }
                        isLoading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            if (e.response()!!.code() == 403 || e.response()!!.code() == 401){
                                Toast.makeText(
                                    context,
                                    "UNAUTHORIZED.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                val errorBody = e.response()?.errorBody()
                                val gson = Gson()
                                val error = gson.fromJson(errorBody?.string(), ManagementStructuralClientResponse::class.java)
                                listManagementStructuralClientResponse.value = error
                            }
                            isLoading?.value = false
                        } else {
                            isLoading?.value = true
                        }
                    }

                })
        )
    }

    fun getListCountCfTeam(projectCode: String){
        compositeDispossable.add(
            repository.getListCountCfTeam(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CfTeamCountResponseModel>(){
                    override fun onSuccess(t: CfTeamCountResponseModel) {
                        if (t.code == 200) {
                            getListCountCfTeam.value = t
                            Log.d("APIA CFTEAM","$t")
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                CfTeamCountResponseModel::class.java
                            )
                            getListCountCfTeam.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getListEmployeeCfteam(projectCode: String, role: String, page: Int) {
        compositeDispossable.add(
            repository.getListEmployeeCfteam(projectCode, role, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListEmployeeCfteamResponseModel>() {
                    override fun onSuccess(t: ListEmployeeCfteamResponseModel) {
                        if (t.code == 200) {
                            getListEmployeeCfteamResponse.value = t
                            Log.d("API LIST CFTEAM", "$t")
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                ListEmployeeCfteamResponseModel::class.java
                            )
                            getListEmployeeCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getListManagementCfteam(projectCode: String, page: Int){
        compositeDispossable.add(
            repository.getListManagementCfteam(projectCode, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListManagementCfteamResponseModel>(){
                    override fun onSuccess(t: ListManagementCfteamResponseModel) {
                        if (t.code == 200){
                            getListManagementCfteamResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                ListManagementCfteamResponseModel::class.java
                            )
                            getListManagementCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getDetailListCfteam(projectCode: String, idEmployee:Int){
        compositeDispossable.add(
            repository.getDetailEmployeeCfteam(projectCode, idEmployee)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailCfteamResponseModel>(){
                    override fun onSuccess(t: DetailCfteamResponseModel) {
                        if (t.code == 200){
                            getDetailListCfteamResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                DetailCfteamResponseModel::class.java
                            )
                            getDetailListCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getDetailHistoryPersonCfteam(projectCode: String, idEmployee: Int, localDate: String, month: String, year: String, jobCode: String){
        compositeDispossable.add(
            repository.getDetailHistoryPersonCfteam(projectCode, idEmployee, localDate, month, year, jobCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailHistoryPersonCfteamResponseModel>(){
                    override fun onSuccess(t: DetailHistoryPersonCfteamResponseModel) {
                        if (t.code == 200){
                            getDetailHistoryPersonCfteamResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                DetailHistoryPersonCfteamResponseModel::class.java
                            )
                            getDetailHistoryPersonCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getHistoryAttendanceCfteam(projectCode: String,date: String){
        compositeDispossable.add(
            repository.getHistoryAttendanceCfteam(projectCode, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<HistoryAttendanceCfteamResponseModel>(){
                    override fun onSuccess(t: HistoryAttendanceCfteamResponseModel) {
                        if (t.code == 200){
                            getHistoryAttendanceCfteamResponse.value= t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                HistoryAttendanceCfteamResponseModel::class.java
                            )
                            getHistoryAttendanceCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getScheduleCfteam(projectCode: String, date: String, shiftId: Int){
        compositeDispossable.add(
            repository.getScheduleCfteam(projectCode, date, shiftId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ScheduleTeamResponseModel>(){
                    override fun onSuccess(t: ScheduleTeamResponseModel) {
                        getScheduleCfteamResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                ScheduleTeamResponseModel::class.java
                            )
                            getScheduleCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )

    }

    fun getListShiftCfteam(projectCode: String){
        compositeDispossable.add(
            repository.getListShiftCfteam(projectCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ListShiftReportResponseModel>(){
                    override fun onSuccess(t: ListShiftReportResponseModel) {
                        if (t.code == 200){
                            getListShiftCfteamResponse.value = t
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (e is HttpException) {
                            val errorBody =  e.response()?.errorBody()
                            val gson = Gson()
                            val error  =gson.fromJson(
                                errorBody?.string(),
                                ListShiftReportResponseModel::class.java
                            )
                            getListShiftCfteamResponse.value = error
                            Log.d("cfCount", "onError: $error")
                        }
                    }

                })
        )
    }

    fun getListCfteamByShift(projectCode: String, role:String, shiftId: Int, page: Int){
       compositeDispossable.add(
           repository.getListCfteamByShift(projectCode, role, shiftId, page)
               .subscribeOn(Schedulers.newThread())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object : DisposableSingleObserver<ListCftemByShiftResponseModel>(){
                   override fun onSuccess(t: ListCftemByShiftResponseModel) {
                       if (t.code == 200){
                           getCfteamByShiftReponse.value = t
                       }
                   }

                   override fun onError(e: Throwable) {
                       if (e is HttpException) {
                           val errorBody =  e.response()?.errorBody()
                           val gson = Gson()
                           val error  =gson.fromJson(
                               errorBody?.string(),
                               ListCftemByShiftResponseModel::class.java
                           )
                           getCfteamByShiftReponse.value = error
                           Log.d("cfCount", "onError: $error")
                       }
                   }

               })
       )
    }

    fun getListCfteamByShiftViewModel(): MutableLiveData<ListCftemByShiftResponseModel>{
        return getCfteamByShiftReponse
    }

    fun getListShiftCfteamViewModel(): MutableLiveData<ListShiftReportResponseModel>{
        return getListShiftCfteamResponse
    }


    fun getScheduleCfteamViewModel():MutableLiveData<ScheduleTeamResponseModel>{
        return getScheduleCfteamResponse
    }

    fun getHistoryDetailAttedanceViewModel(): MutableLiveData<HistoryAttendanceCfteamResponseModel>{
        return getHistoryAttendanceCfteamResponse
    }

    fun getDetailHistoryPersonViewModel(): MutableLiveData<DetailHistoryPersonCfteamResponseModel>{
        return getDetailHistoryPersonCfteamResponse
    }
    fun getDetailCfteamViewModel(): MutableLiveData<DetailCfteamResponseModel>{
        return getDetailListCfteamResponse
    }

    fun getListManagementCfteamViewModel(): MutableLiveData<ListManagementCfteamResponseModel>{
        return getListManagementCfteamResponse
    }

    fun getListEmployeeCfteamViewModel(): MutableLiveData<ListEmployeeCfteamResponseModel>{
        return getListEmployeeCfteamResponse
    }

    fun getCountCfTeamViewModel(): MutableLiveData<CfTeamCountResponseModel>{
        return getListCountCfTeam
    }

    override fun onCleared() {
        super.onCleared()
        compositeDispossable.dispose()
    }
}