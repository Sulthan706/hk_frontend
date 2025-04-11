package com.hkapps.academy.features.features_trainer.homescreen.home.data.remote

import com.hkapps.academy.features.features_trainer.homescreen.home.data.service.HomeTrainerService
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeTrainerDataSourceImpl @Inject constructor(private val service: HomeTrainerService):
    HomeTrainerDataSource {
    override fun getListClassHomeTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeTrainerResponse> {
        return service.getListClassHomeTrainer(userNuc, date, region, page, size)
    }
}