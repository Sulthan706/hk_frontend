package com.hkapps.hygienekleen.features.features_vendor.myteam.data.remote

import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsent.CountAbsentResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.countAbsentMidLevel.CountAbsentMidLevelResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listOperatorModel.OperatorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listShiftModel.ShiftResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listSpvModel.SupervisorResponseModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.model.listTeamleadModel.TeamleadResponseModel
import io.reactivex.Single

interface TimkuRemoteDataSource {

    fun getListShiftTimku(projectCode: String): Single<ShiftResponseModel>

    fun getListOperator(userId: Int, projectCode: String, shiftId: Int): Single<OperatorResponseModel>

    fun getListOperatorByDate(userId: Int, projectCode: String, shiftId: Int, date: String): Single<OperatorResponseModel>

    fun getListLeader(employeeId: Int, projectCode: String, shiftId: Int): Single<TeamleadResponseModel>

    fun getListSpvTimku(projectCode: String): Single<SupervisorResponseModel>

    fun getCountAbsentOperator(projectCode: String, employeeId: Int, shiftId: Int): Single<CountAbsentResponseModel>

    fun getCountAbsentLeader(projectCode: String, employeeId: Int, shiftId: Int): Single<CountAbsentMidLevelResponseModel>

    fun getCountAbsentSpv(projectCode: String, employeeId: Int): Single<CountAbsentMidLevelResponseModel>

    fun getListLeaderNew(projectCode: String, shiftId: Int): Single<TeamleadResponseModel>

}