package com.hkapps.academy.di

import com.hkapps.academy.features.authentication.di.AuthAcademyModule
import com.hkapps.academy.features.authentication.viewModel.AuthAcademyViewModel
import com.hkapps.academy.features.features_participants.classes.di.ClassParticipantModule
import com.hkapps.academy.features.features_participants.classes.viewmodel.ClassParticipantViewModel
import com.hkapps.academy.features.features_participants.homescreen.home.di.HomeParticipantModule
import com.hkapps.academy.features.features_participants.homescreen.home.viewmodel.HomeParticipantViewModel
import com.hkapps.academy.features.features_participants.training.di.TrainingParticipantModule
import com.hkapps.academy.features.features_participants.training.viewmodel.TrainingParticipantViewModel
import com.hkapps.academy.features.features_trainer.homescreen.home.di.HomeTrainerModule
import com.hkapps.academy.features.features_trainer.homescreen.home.viewmodel.HomeTrainerViewModel
import com.hkapps.academy.features.features_trainer.myclass.di.ClassTrainerModule
import com.hkapps.academy.features.features_trainer.myclass.viewModel.ClassTrainerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ClassTrainerModule::class,
    AuthAcademyModule::class,
    HomeParticipantModule::class,
    TrainingParticipantModule::class,
    ClassParticipantModule::class,
    HomeTrainerModule::class
])

interface AcademyOperationComponent {
    fun injectRepository(classTrainerViewModel: ClassTrainerViewModel)
    fun injectRepository(authAcademyViewModel: AuthAcademyViewModel)
    fun injectRepository(homeParticipantViewModel: HomeParticipantViewModel)
    fun injectRepository(trainingParticipantViewModel: TrainingParticipantViewModel)
    fun injectRepository(classParticipantViewModel: ClassParticipantViewModel)
    fun injectRepository(homeTrainerViewModel: HomeTrainerViewModel)
}