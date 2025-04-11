package com.hkapps.academy.features.features_trainer.homescreen.home.data.repository

import com.hkapps.academy.features.features_trainer.homescreen.home.data.remote.HomeTrainerDataSource
import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeTrainerRepositoryImpl @Inject constructor(private val dataSource: HomeTrainerDataSource):
    HomeTrainerRepository {
    override fun getListClassHomeTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeTrainerResponse> {
        return dataSource.getListClassHomeTrainer(userNuc, date, region, page, size)
    }

}