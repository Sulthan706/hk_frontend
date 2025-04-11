package com.hkapps.academy.features.features_trainer.myclass.data.service

import com.hkapps.academy.features.features_trainer.myclass.model.createClass.CreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.detailTraining.DetailTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.inviteParticipant.InviteParticipantTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listModule.ModulesCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ParticipantsClassTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.ProjectsCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.ParticipantsTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.putTrainingDetailTrainer.UpdateTrainingTrainerResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ClassTrainerService {

    @GET("api/v1/academy/user/get-users")
    fun getListParticipantClass(
        @Query("name") name: String,
        @Query("projectCode") projectCode: String,
        @Query("levelJabatan") position: String,
        @Query("page") page: Int
    ): Single<ParticipantsClassTrainerResponse>

    @GET("api/v1/academy/module/get/all")
    fun getListModuleCreateClass(): Single<ModulesCreateClassResponse>

    @GET("api/v1/academy/projects/get/all")
    fun getListProjectCreateClass(
        @Query("projectName") projectName: String
    ): Single<ProjectsCreateClassResponse>

    @POST("api/v1/academy/trainer/training/post/create")
    fun createClassTrainer(
        @Query("trainerNuc") trainerNuc: String,
        @Query("trainingName") trainingName: String,
        @Query("participant") participant: String,
        @Query("moduleId") moduleId: Int,
        @Query("assignOnlineTest") assignOnlineTest: String,
        @Query("category") category: String,
        @Query("quota") quota: Int,
        @Query("projectCode") projectCode: String,
        @Query("trainingDate") trainingDate: String,
        @Query("trainingStart") trainingStart: String,
        @Query("trainingEnd") trainingEnd: String,
        @Query("region") region: String,
        @Query("jenisKelas") jenisKelas: String,
        @Query("trainingLocationCode") trainingLocationCode: String,
        @Query("locationDescription") locationDescription: String,
        @Query("alternateLocation") alternateLocation: String,
        @Query("appName") appName: String,
        @Query("appLink") appLink: String
    ): Single<CreateClassResponse>

    @POST("api/v1/academy/trainer/training/post/invite")
    fun inviteParticipant(
        @Query("userNuc") userNuc: ArrayList<String>,
        @Query("trainingId") trainingId: Int
    ): Single<InviteParticipantTrainerResponse>

    @GET("api/v1/academy/trainer/training/get/detail")
    fun getDetailTraining(
        @Query("trainingId") trainingId: Int,
        @Query("region") region: String
    ): Single<DetailTrainingTrainerResponse>

    @GET("api/v1/academy/trainer/training/get/participants")
    fun getParticipantsTraining(
        @Query("trainingId") trainingId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<ParticipantsTrainingTrainerResponse>

    @PUT("api/v1/academy/trainer/training/put/attendance")
    fun updateAttendanceParticipant(
        @Query("participantId") participantId: Int,
        @Query("statusAttendance") statusAttendance: String,
        @Query("userNuc") userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

    @PUT("api/v1/academy/trainer/training/put/end-class")
    fun updateEndClassTraining(
        @Query("trainingId") trainingId: Int,
        @Query("userNuc") userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

    @PUT("api/v1/academy/trainer/training/put/score")
    fun updateScoreTraining(
        @Query("participantId") participantId: Int,
        @Query("nilai") nilai: Int,
        @Query("komentar") komentar: String,
        @Query("userNuc") userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

}