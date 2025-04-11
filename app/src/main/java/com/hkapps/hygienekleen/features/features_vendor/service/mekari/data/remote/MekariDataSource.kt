package com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.SubmitRegisMekariResponse
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.generatetokenmekari.TokenMekariResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari.TrialMekariResponseModel
import io.reactivex.Single

interface MekariDataSource {

    fun submitRegisMekari(
        employeeId: Int
    ): Single<SubmitRegisMekariResponse>

    fun getCheckMekari(
        employeeId: Int
    ): Single<SubmitRegisMekariResponse>

    fun generateTokenMekari(
        employeeId: Int
    ):Single<TokenMekariResponseModel>

    fun getTrialMekari(
        projectCode: String,
        employeeId: Int
    ):Single<TrialMekariResponseModel>

}