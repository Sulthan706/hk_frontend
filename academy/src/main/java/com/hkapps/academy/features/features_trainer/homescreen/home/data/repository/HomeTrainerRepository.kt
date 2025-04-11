package com.hkapps.academy.features.features_trainer.homescreen.home.data.repository

import com.hkapps.academy.features.features_trainer.homescreen.home.model.listClass.ClassesHomeTrainerResponse
import io.reactivex.Single

interface HomeTrainerRepository {

    fun getListClassHomeTrainer(
        userNuc: String,
        date: String,
        region: String,
        page: Int,
        size: Int
    ): Single<ClassesHomeTrainerResponse>

}