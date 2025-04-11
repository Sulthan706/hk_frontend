package com.hkapps.academy.features.features_trainer.myclass.data.repository

import com.hkapps.academy.features.features_trainer.myclass.model.createClass.CreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.detailTraining.DetailTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.inviteParticipant.InviteParticipantTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listModule.ModulesCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ParticipantsClassTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.ProjectsCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.ParticipantsTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.putTrainingDetailTrainer.UpdateTrainingTrainerResponse
import io.reactivex.Single

interface ClassTrainerRepository {

    fun getListParticipantClass(
        name: String,
        projectCode: String,
        position: String,
        page: Int
    ): Single<ParticipantsClassTrainerResponse>

    fun getListModuleCreateClass(): Single<ModulesCreateClassResponse>

    fun getListProjectCreateClass(
        projectName: String
    ): Single<ProjectsCreateClassResponse>

    fun createClassTrainer(
        trainerNuc: String,
        trainingName: String,
        participant: String,
        moduleId: Int,
        assignOnlineTest: String,
        category: String,
        quota: Int,
        projectCode: String,
        trainingDate: String,
        trainingStart: String,
        trainingEnd: String,
        region: String,
        jenisKelas: String,
        trainingLocationCode: String,
        locationDescription: String,
        alternateLocation: String,
        appName: String,
        appLink: String
    ): Single<CreateClassResponse>

    fun inviteParticipant(
        userNuc: ArrayList<String>,
        trainingId: Int
    ): Single<InviteParticipantTrainerResponse>

    fun getDetailTraining(
        trainingId: Int,
        region: String
    ): Single<DetailTrainingTrainerResponse>

    fun getParticipantsTraining(
        trainingId: Int,
        page: Int,
        size: Int
    ): Single<ParticipantsTrainingTrainerResponse>

    fun updateAttendanceParticipant(
        participantId: Int,
        statusAttendance: String,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

    fun updateEndClassTraining(
        trainingId: Int,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

    fun updateScoreTraining(
        participantId: Int,
        nilai: Int,
        komentar: String,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse>

}