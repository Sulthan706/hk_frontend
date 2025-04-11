package com.hkapps.academy.features.features_trainer.homescreen.home.data.remote

import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
import io.reactivex.Single

interface HomeTrainerDataSource {

    fun getListClassHomeTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeTrainerResponse>

}