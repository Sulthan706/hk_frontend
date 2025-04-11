package com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.remote

import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.service.MekariService
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.SubmitRegisMekariResponse
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.generatetokenmekari.TokenMekariResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari.TrialMekariResponseModel
import io.reactivex.Single
import javax.inject.Inject

class MekariDataSourceImpl @Inject constructor(private val service: MekariService) : MekariDataSource {

    override fun submitRegisMekari(employeeId: Int): Single<SubmitRegisMekariResponse> {
        return service.submitRegisMekari(employeeId)
    }

    override fun getCheckMekari(employeeId: Int): Single<SubmitRegisMekariResponse> {
        return service.getCheckMekari(employeeId)
    }

    override fun generateTokenMekari(employeeId: Int): Single<TokenMekariResponseModel> {
        return service.generateTokenMekari(employeeId)
    }

    override fun getTrialMekari(projectCode: String, employeeId: Int): Single<TrialMekariResponseModel> {
        return service.getTrialMekari(projectCode, employeeId)
    }

}