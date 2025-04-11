package com.hkapps.hygienekleen.features.features_vendor.myteam.data.repository

import com.hkapps.hygienekleen.features.features_vendor.myteam.data.remote.TimkuRemoteDataSource
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsent.CountAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel.CountAbsentMidLevelResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.TeamleadResponseModel
import io.reactivex.Single
import javax.inject.Inject

class TimkuRepositoryImpl @Inject constructor(private val remoteDataSource: TimkuRemoteDataSource)
    : TimkuRepository {

    override fun getListShiftTimku(projectCode: String): Single<ShiftResponseModel> {
        return remoteDataSource.getListShiftTimku(projectCode)
    }

    override fun getListOperator(
        userId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<OperatorResponseModel> {
        return remoteDataSource.getListOperator(userId, projectCode, shiftId)
    }

    override fun getListOperatorByDate(
        userId: Int,
        projectCode: String,
        shiftId: Int,
        date: String
    ): Single<OperatorResponseModel> {
        return remoteDataSource.getListOperatorByDate(userId, projectCode, shiftId, date)
    }

    override fun getListLeader(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<TeamleadResponseModel> {
        return remoteDataSource.getListLeader(employeeId, projectCode, shiftId)
    }

    override fun getListSpvTimku(
        projectCode: String
    ): Single<SupervisorResponseModel> {
        return remoteDataSource.getListSpvTimku(projectCode)
    }

    override fun getCountAbsentOperator(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<CountAbsentResponseModel> {
        return remoteDataSource.getCountAbsentOperator(projectCode, employeeId, shiftId)
    }

    override fun getCountAbsentLeader(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<CountAbsentMidLevelResponseModel> {
        return remoteDataSource.getCountAbsentLeader(projectCode, employeeId, shiftId)
    }

    override fun getCountAbsentSpv(
        projectCode: String,
        employeeId: Int
    ): Single<CountAbsentMidLevelResponseModel> {
        return remoteDataSource.getCountAbsentSpv(projectCode, employeeId)
    }

    override fun getListLeaderNew(
        projectCode: String,
        shiftId: Int
    ): Single<TeamleadResponseModel> {
        return remoteDataSource.getListLeaderNew(projectCode, shiftId)
    }

}