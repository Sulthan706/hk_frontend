package com.hkapps.academy.features.features_trainer.myclass.data.remote

import com.hkapps.academy.features.features_trainer.myclass.data.service.ClassTrainerService
import com.hkapps.academy.features.features_trainer.myclass.model.createClass.CreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.detailTraining.DetailTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.inviteParticipant.InviteParticipantTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listModule.ModulesCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listPartcipant.ParticipantsClassTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.listProject.ProjectsCreateClassResponse
import com.hkapps.academy.features.features_trainer.myclass.model.participantsTraining.ParticipantsTrainingTrainerResponse
import com.hkapps.academy.features.features_trainer.myclass.model.putTrainingDetailTrainer.UpdateTrainingTrainerResponse
import io.reactivex.Single
import javax.inject.Inject

class ClassTrainerDataSourceImpl @Inject constructor(private val service: ClassTrainerService) :
    ClassTrainerDataSource {

    override fun getListParticipantClass(
        name: String,
        projectCode: String,
        position: String,
        page: Int
    ): Single<ParticipantsClassTrainerResponse> {
        return service.getListParticipantClass(name, projectCode, position, page)
    }

    override fun getListModuleCreateClass(): Single<ModulesCreateClassResponse> {
        return service.getListModuleCreateClass()
    }

    override fun getListProjectCreateClass(projectName: String): Single<ProjectsCreateClassResponse> {
        return service.getListProjectCreateClass(projectName)
    }

    override fun createClassTrainer(
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
    ): Single<CreateClassResponse> {
        return service.createClassTrainer(trainerNuc, trainingName, participant, moduleId, assignOnlineTest, category, quota, projectCode, trainingDate, trainingStart, trainingEnd, region, jenisKelas, trainingLocationCode, locationDescription, alternateLocation, appName, appLink)
    }

    override fun inviteParticipant(
        userNuc: ArrayList<String>,
        trainingId: Int
    ): Single<InviteParticipantTrainerResponse> {
        return service.inviteParticipant(userNuc, trainingId)
    }

    override fun getDetailTraining(
        trainingId: Int,
        region: String
    ): Single<DetailTrainingTrainerResponse> {
        return service.getDetailTraining(trainingId, region)
    }

    override fun getParticipantsTraining(
        trainingId: Int,
        page: Int,
        size: Int
    ): Single<ParticipantsTrainingTrainerResponse> {
        return service.getParticipantsTraining(trainingId, page, size)
    }

    override fun updateAttendanceParticipant(
        participantId: Int,
        statusAttendance: String,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse> {
        return service.updateAttendanceParticipant(participantId, statusAttendance, userNuc)
    }

    override fun updateEndClassTraining(
        trainingId: Int,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse> {
        return service.updateEndClassTraining(trainingId, userNuc)
    }

    override fun updateScoreTraining(
        participantId: Int,
        nilai: Int,
        komentar: String,
        userNuc: String
    ): Single<UpdateTrainingTrainerResponse> {
        return service.updateScoreTraining(participantId, nilai, komentar, userNuc)
    }

}