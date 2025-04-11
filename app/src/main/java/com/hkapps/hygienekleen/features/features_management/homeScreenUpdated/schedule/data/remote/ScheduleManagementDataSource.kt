package com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.remote

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

interface ScheduleManagementDataSource {

    fun getScheduleManagement(
        userId: Int,
        date: String,
        type: String,
        page: Int,
        size: Int
    ): Single<SchedulesManagementResponse>

    fun getProjectsSchedule(
        userId: Int,
        page: Int,
        size : Int
    ): Single<ProjectAllScheduleResponse>

    fun getAllProjectSchedule(
        page: Int,
        size: Int
    ): Single<ProjectAllScheduleResponse>

    fun divertionSchedule(
        userId: Int,
        idRkbOperation: Int,
        divertedTo: String,
        reason: String
    ): Single<DivertionScheduleManagementResponse>

    fun getScheduleBod(
        userId: Int,
        date: String
    ): Single<SchedulesBodResponse>

    fun diversionScheduleBod(
        userId: Int,
        idRkbBod: Int,
        divertedTo: String,
        reason: String
    ): Single<DivertionScheduleManagementResponse>

    fun getProjectVp(
        branchCode: String,
        page: Int,
        perPage: Int
    ): Single<ProjectAllScheduleResponse>

    fun submitCreateScheduleBod(
        data: ArrayList<ProjectsScheduleApiBod>
    ): Single<SubmitCreateScheduleBodResponse>

    fun submitCreateScheduleManagement(
        data: ArrayList<ProjectsScheduleApiManagement>
    ): Single<SubmitCreateScheduleManagementResponse>

    fun submitCreateScheduleTeknisi(
        data: ArrayList<ProjectsScheduleApiTeknisi>
    ): Single<SubmitCreateScheduleTeknisiResponse>

    fun getProjectsScheduleVp(
        userId: Int,
        date: String,
        branchCode: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    fun getProjectsScheduleBod(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    fun getProjectsScheduleTeknisi(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel>

    fun getProjectsScheduleManagement(
        userId: Int,
        date: String,
        query: String,
        page: Int,
        perPage: Int
    ): Single<ProjectsScheduleManagementModel>

}