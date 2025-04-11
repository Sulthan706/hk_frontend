package com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.repository

import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.remote.MekariDataSource
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.SubmitRegisMekariResponse
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.generatetokenmekari.TokenMekariResponseModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.model.trialmekari.TrialMekariResponseModel
import io.reactivex.Single
import javax.inject.Inject

class MekariRepositoryImpl @Inject constructor(private val dataSource: MekariDataSource) : MekariRepository {

    override fun submitRegisMekari(employeeId: Int): Single<SubmitRegisMekariResponse> {
        return dataSource.submitRegisMekari(employeeId)
    }

    override fun getCheckMekari(employeeId: Int): Single<SubmitRegisMekariResponse> {
        return dataSource.getCheckMekari(employeeId)
    }

    override fun generateTokenMekari(employeeId: Int): Single<TokenMekariResponseModel> {
        return dataSource.generateTokenMekari(employeeId)
    }

    override fun getTrialMekari(projectCode: String, employeeId: Int): Single<TrialMekariResponseModel> {
        return dataSource.getTrialMekari(projectCode, employeeId)
    }

}