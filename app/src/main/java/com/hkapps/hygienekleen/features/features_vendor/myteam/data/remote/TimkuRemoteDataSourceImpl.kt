package com.hkapps.hygienekleen.features.features_vendor.myteam.data.remote

import com.hkapps.hygienekleen.features.features_vendor.myteam.data.service.TimkuService
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsent.CountAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel.CountAbsentMidLevelResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.TeamleadResponseModel
import io.reactivex.Single
import javax.inject.Inject

class TimkuRemoteDataSourceImpl @Inject constructor(private val service: TimkuService): TimkuRemoteDataSource {

    override fun getListShiftTimku(projectCode: String): Single<ShiftResponseModel> {
        return service.getListShiftTimku(projectCode)
    }

    override fun getListOperator(
        userId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<OperatorResponseModel> {
        return service.getListOperatorTimku(userId, projectCode, shiftId)
    }

    override fun getListOperatorByDate(
        userId: Int,
        projectCode: String,
        shiftId: Int,
        date: String
    ): Single<OperatorResponseModel> {
        return service.getListOperatorByDate(userId, projectCode, shiftId, date)
    }

    override fun getListLeader(
        employeeId: Int,
        projectCode: String,
        shiftId: Int
    ): Single<TeamleadResponseModel> {
        return service.getListLeaderTimku(employeeId, projectCode, shiftId)
    }

    override fun getListSpvTimku(
        projectCode: String
    ): Single<SupervisorResponseModel> {
        return service.getListSpvTimku(projectCode)
    }

    override fun getCountAbsentOperator(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<CountAbsentResponseModel> {
        return service.getCountAbsentOperator(projectCode, employeeId, shiftId)
    }

    override fun getCountAbsentLeader(
        projectCode: String,
        employeeId: Int,
        shiftId: Int
    ): Single<CountAbsentMidLevelResponseModel> {
        return service.getCountAbsentLeader(projectCode, employeeId, shiftId)
    }

    override fun getCountAbsentSpv(
        projectCode: String,
        employeeId: Int
    ): Single<CountAbsentMidLevelResponseModel> {
        return service.getCountAbsentSpv(projectCode, employeeId)
    }

    override fun getListLeaderNew(
        projectCode: String,
        shiftId: Int
    ): Single<TeamleadResponseModel> {
        return service.getListLeaderTimkuNew(projectCode, shiftId)
    }


}