package com.hkapps.academy.retrofit

import com.hkapps.academy.features.authentication.data.service.AuthAcademyService
import com.hkapps.academy.features.features_participants.classes.data.service.ClassParticipantService
import com.hkapps.academy.features.features_participants.homescreen.home.data.service.HomeParticipantService
import com.hkapps.academy.features.features_participants.training.data.service.TrainingParticipantService
import com.hkapps.academy.features.features_trainer.homescreen.home.data.service.HomeTrainerService
import com.hkapps.academy.features.features_trainer.myclass.data.service.ClassTrainerService
import com.hkapps.academy.pref.AcademyOperationPref
import com.hkapps.academy.pref.AcademyOperationPrefConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AcademyRetrofit {

    private const val BASE_URL = "http://54.251.83.205:3001"

    private val retrofitClient: Retrofit.Builder by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header(
                        "Authorization", "Bearer " +
                                AcademyOperationPref.loadString(
                                    AcademyOperationPrefConst.ACADEMY_USER_TOKEN,
                                    "")
                    )
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val authAcademyService: AuthAcademyService by lazy {
        retrofitClient
            .build()
            .create(AuthAcademyService::class.java)
    }

    val homeParticipantService: HomeParticipantService by lazy {
        retrofitClient
            .build()
            .create(HomeParticipantService::class.java)
    }

    val trainingParticipantService: TrainingParticipantService by lazy {
        retrofitClient
            .build()
            .create(TrainingParticipantService::class.java)
    }

    val classParticipantService: ClassParticipantService by lazy {
        retrofitClient
            .build()
            .create(ClassParticipantService::class.java)
    }

    val homeTrainerService: HomeTrainerService by lazy {
        retrofitClient
            .build()
            .create(HomeTrainerService::class.java)
    }

    val classTrainerService: ClassTrainerService by lazy {
        retrofitClient
            .build()
            .create(ClassTrainerService::class.java)
    }


}