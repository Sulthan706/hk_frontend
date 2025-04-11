package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.repository

import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.remote.ScheduleManagementDataSource
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.divertionSchedule.DivertionScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProject.ProjectAllScheduleResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listProjectSchedule.ProjectsScheduleManagementModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listSchedule.SchedulesManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.listScheduleBod.SchedulesBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiBod
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiManagement
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.selectedProjectsSchedule.ProjectsScheduleApiTeknisi
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleBod.SubmitCreateScheduleBodResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleManagement.SubmitCreateScheduleManagementResponse
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.model.submitCreateScheduleTeknisi.SubmitCreateScheduleTeknisiResponse
import io.reactivex.Single
import javax.inject.Inject

class ScheduleManagementRepositoryImpl @Inject constructor(private val dataSource: ScheduleManagementDataSource):
    ScheduleManagementRepository {
    override fun getScheduleManagement(
        userId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ): Single<SchedulesManagementResponse> {
        return dataSource.getScheduleManagement(userId, date, type, page, size)
    }

    override fun getProjectsSchedule(userId: Int, page: Int, size : Int): Single<ProjectAllScheduleResponse> {
        return dataSource.getProjectsSchedule(userId, page, size)
    }

    override fun getAllProjectSchedule(page: Int, size: Int): Single<ProjectAllScheduleResponse> {
        return dataSource.getAllProjectSchedule(page, size)
    }

    override fun divertionSchedule(
        userId: Int,
        idRkbOperation: Int,
        divertedTo: String,
        reason: String
    ): Single<DivertionScheduleManagementResponse> {
        return dataSource.divertionSchedule(userId, idRkbOperation, divertedTo, reason)
    }

    override fun getScheduleBod(userId: Int, date: String): Single<SchedulesBodResponse> {
        return dataSource.getScheduleBod(userId, date)
    }

    override fun diversionScheduleBod(
        userId: Int,
        idRkbBod: Int,
        divertedTo: String,
        reason: String
    ): Single<DivertionScheduleManagementResponse> {
        return dataSource.diversionScheduleBod(userId, idRkbBod, divertedTo, reason)
    }

    override fun getProjectVp(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ProjectAllScheduleResponse> {
        return dataSource.getProjectVp(branchCode, page, perPage)
    }

    override fun submitCreateScheduleBod(data: ArrayList<ProjectsScheduleApiBod>): Single<SubmitCreateScheduleBodResponse> {
        return dataSource.submitCreateScheduleBod(data)
    }

    override fun submitCreateScheduleManagement(data: ArrayList<ProjectsScheduleApiManagement>): Single<SubmitCreateScheduleManagementResponse> {
        return dataSource.submitCreateScheduleManagement(data)
    }

    override fun submitCreateScheduleTeknisi(data: ArrayList<ProjectsScheduleApiTeknisi>): Single<SubmitCreateScheduleTeknisiResponse> {
        return dataSource.submitCreateScheduleTeknisi(data)
    }

    override fun getProjectsScheduleVp(
        userId: Int,
        date: String,
        branchCode: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel> {
        return dataSource.getProjectsScheduleVp(userId, date, branchCode, query, page, perPage)
    }

    override fun getProjectsScheduleBod(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel> {
        return dataSource.getProjectsScheduleBod(userId, date, query, page, perPage)
    }

    override fun getProjectsScheduleTeknisi(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel> {
        return dataSource.getProjectsScheduleTeknisi(userId, date, query, page, perPage)
    }

    override fun getProjectsScheduleManagement(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel> {
        return dataSource.getProjectsScheduleManagement(userId, date, query, page, perPage)
    }

}