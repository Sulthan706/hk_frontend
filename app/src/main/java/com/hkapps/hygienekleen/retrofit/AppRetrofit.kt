package com.hkapps.hygienekleen.retrofit


import com.hkapps.hygienekleen.features.auth.register.data.service.DaftarService
import com.hkapps.hygienekleen.features.auth.forgotpass.data.service.ForgotPassService
import com.hkapps.hygienekleen.features.auth.login.data.service.LoginService
import com.hkapps.hygienekleen.features.features_client.complaint.data.service.ClientComplaintService
import com.hkapps.hygienekleen.features.features_client.dashboardProject.data.service.DashboardProjectService
import com.hkapps.hygienekleen.features.features_client.home.data.service.HomeClientService
import com.hkapps.hygienekleen.features.features_client.myteam.data.service.MyTeamClientService
import com.hkapps.hygienekleen.features.features_client.notifcation.data.service.NotifClientService
import com.hkapps.hygienekleen.features.features_client.overtime.data.service.OvertimeClientService
import com.hkapps.hygienekleen.features.features_client.report.data.service.ReportClientService
import com.hkapps.hygienekleen.features.features_client.setting.data.service.SettingClientService
import com.hkapps.hygienekleen.features.features_client.training.data.service.TrainingClientService
import com.hkapps.hygienekleen.features.features_client.visitreport.data.service.VisitReportService
import com.hkapps.hygienekleen.features.features_management.complaint.data.service.ComplaintManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.data.service.AttendanceManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.data.service.AbsentOprMgmntService
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.data.service.CftalkManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.home.data.service.HomeManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.data.service.AuditService
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.data.service.InspeksiService
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.data.service.OperationalManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.data.service.AttendanceTrackingService
import com.hkapps.hygienekleen.features.features_management.myteam.data.service.MyTeamManagementService
import com.hkapps.hygienekleen.features.features_management.project.data.service.ProjectManagementService
import com.hkapps.hygienekleen.features.features_management.report.data.service.ReportManagementService
import com.hkapps.hygienekleen.features.features_management.service.overtime.data.service.OvertimeManagementService
import com.hkapps.hygienekleen.features.features_management.service.permission.data.service.PermissionManagementService
import com.hkapps.hygienekleen.features.facerecog.data.service.FaceRecogService
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.data.service.MonthlyWorkClientService
import com.hkapps.hygienekleen.features.features_management.damagereport.data.service.DamageReportManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.data.service.ClosingManagementService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.data.service.HomeManagementUpdatedService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.data.service.HumanCapitalService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.data.service.ProjectService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.data.service.VisitReportManagementService
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.data.service.RoutineService
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.data.service.WeeklyProgressService
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.data.service.PeriodicManagementService
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.data.service.ScheduleManagementService
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.data.service.ReportHighService
import com.hkapps.hygienekleen.features.features_management.service.resign.data.service.ResignManagementService
import com.hkapps.hygienekleen.features.features_management.shareloc.data.service.ShareLocManagementService
import com.hkapps.hygienekleen.features.features_vendor.damagereport.data.service.DamageReportVendorService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.data.service.AttendanceFixService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.data.service.AttendanceService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.data.service.ChecklistService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.data.service.ClosingService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.data.service.DacService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.data.service.HomeService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.data.service.MonthlyWorkReportService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.data.service.PlottingService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.data.service.ReportService
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.data.service.ScheduleService
import com.hkapps.hygienekleen.features.features_vendor.myteam.data.service.TimkuService
import com.hkapps.hygienekleen.features.features_vendor.notifcation.data.service.NotifVendorService
import com.hkapps.hygienekleen.features.features_vendor.profile.data.service.ProfileService
import com.hkapps.hygienekleen.features.features_vendor.service.approval.data.service.ApprovalService
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.data.service.VendorComplaintService
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.data.service.VendorComplaintInternalService
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.data.service.MekariService
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.data.service.OvertimeService
import com.hkapps.hygienekleen.features.features_vendor.service.permission.data.service.PermissionService
import com.hkapps.hygienekleen.features.features_vendor.service.resign.data.service.ResignService
import com.hkapps.hygienekleen.features.grafik.data.service.ChartService
import com.hkapps.hygienekleen.features.splash.data.service.SplashService
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppRetrofit {
//    private const val BASE_URL = "http://54.251.83.205:3000"
    private const val BASE_URL = "http://43.157.201.56:2990"
//    private const val BASE_URL = "http://13.215.81.247:3001"

    private val retrofitClient: Retrofit.Builder by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

//        val tokenAuthenticator = TokenAuthenticator()

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val request: Request =
                    original.newBuilder()
                        .header(
                            "Authorization", "Bearer " +
                                    CarefastOperationPref.loadString(
                                        CarefastOperationPrefConst.USER_TOKEN,
                                        ""
                                    )
//                                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2NzQ0In0.JLrbQWuBY5zfbwvI_bLFFxZK_J5uJUFXVGU8MU0j6jmdCPmAEBGepGDqz_Kady2WR3ACFL5jvuf_vrkPxUYn7Q"

                        )
                        .method(original.method, original.body)
                        .build()
                chain.proceed(request)
            })
            .addInterceptor(logging)
//            .authenticator(tokenAuthenticator)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val scheduleService: ScheduleService by lazy {
        retrofitClient
            .build()
            .create(ScheduleService::class.java)
    }

    val plottingService: PlottingService by lazy {
        retrofitClient
            .build()
            .create(PlottingService::class.java)
    }

    val attendanceService: AttendanceService by lazy {
        retrofitClient
            .build()
            .create(AttendanceService::class.java)
    }

    val attendanceFixService: AttendanceFixService by lazy {
        retrofitClient
            .build()
            .create(AttendanceFixService::class.java)
    }

    val homeService: HomeService by lazy {
        retrofitClient
            .build()
            .create(HomeService::class.java)
    }

    val checklistService: ChecklistService by lazy {
        retrofitClient
            .build()
            .create(ChecklistService::class.java)
    }

    val loginService: LoginService by lazy {
        retrofitClient
            .build()
            .create(LoginService::class.java)
    }

    val splashService: SplashService by lazy {
        retrofitClient
            .build()
            .create(SplashService::class.java)
    }

    val daftarService: DaftarService by lazy {
        retrofitClient
            .build()
            .create(DaftarService::class.java)
    }

    val permissionService: PermissionService by lazy {
        retrofitClient
            .build()
            .create(PermissionService::class.java)
    }

    val faceRecogService: FaceRecogService by lazy {
        retrofitClient
            .build()
            .create(FaceRecogService::class.java)
    }

    val damageReportVendorService: DamageReportVendorService by lazy {
        retrofitClient
            .build()
            .create(DamageReportVendorService::class.java)
    }


    //KHUSUS FORGOT
    private val retrofitClientForgotPass: Retrofit.Builder by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val request: Request =
                    original.newBuilder()
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

    val forgotService: ForgotPassService by lazy {
        retrofitClientForgotPass
            .build()
            .create(ForgotPassService::class.java)
    }

    val timkuService: TimkuService by lazy {
        retrofitClient
            .build()
            .create(TimkuService::class.java)
    }

    val dacService: DacService by lazy {
        retrofitClient
            .build()
            .create(DacService::class.java)
    }

    val profileService: ProfileService by lazy {
        retrofitClient
            .build()
            .create(ProfileService::class.java)
    }

    val notifVendorService: NotifVendorService by lazy {
        retrofitClient
            .build()
            .create(NotifVendorService::class.java)
    }

    val overtimeService: OvertimeService by lazy {
        retrofitClient
            .build()
            .create(OvertimeService::class.java)
    }


    val vendorComplaintService: VendorComplaintService by lazy {
        retrofitClient
            .build()
            .create(VendorComplaintService::class.java)
    }

    val homeManagementService: HomeManagementService by lazy {
        retrofitClient
            .build()
            .create(HomeManagementService::class.java)
    }

    val vendorComplaintInternalService: VendorComplaintInternalService by lazy {
        retrofitClient
            .build()
            .create(VendorComplaintInternalService::class.java)
    }
    val mekariServie: MekariService by lazy {
        retrofitClient
            .build()
            .create(MekariService::class.java)
    }
    val approvalService: ApprovalService by lazy {
        retrofitClient
            .build()
            .create(ApprovalService::class.java)
    }
    val reportService: ReportService by lazy {
        retrofitClient
            .build()
            .create(ReportService::class.java)
    }
    val resignService: ResignService by lazy {
        retrofitClient
            .build()
            .create(ResignService::class.java)
    }


    //untuk apps client
    val homeClientService: HomeClientService by lazy {
        retrofitClient
            .build()
            .create(HomeClientService::class.java)
    }


    val clientComplaintService: ClientComplaintService by lazy {
        retrofitClient
            .build()
            .create(ClientComplaintService::class.java)
    }

    val NOTIF_CLIENT_SERVICE: NotifClientService by lazy {
        retrofitClient
            .build()
            .create(NotifClientService::class.java)
    }

    val overtimeClientService: OvertimeClientService by lazy {
        retrofitClient
            .build()
            .create(OvertimeClientService::class.java)
    }
    val settingClientService: SettingClientService by lazy {
        retrofitClient
            .build()
            .create(SettingClientService::class.java)
    }
    val myTeamClientService: MyTeamClientService by lazy {
        retrofitClient
            .build()
            .create(MyTeamClientService::class.java)
    }
    val reportClientService: ReportClientService by lazy {
        retrofitClient
            .build()
            .create(ReportClientService::class.java)
    }
    val dashboardProjectService: DashboardProjectService by lazy {
        retrofitClient
            .build()
            .create(DashboardProjectService::class.java)
    }
    val trainingClientService: TrainingClientService by lazy {
        retrofitClient
            .build()
            .create(TrainingClientService::class.java)
    }
    val visitReportService: VisitReportService by lazy {
        retrofitClient
            .build()
            .create(VisitReportService::class.java)
    }
    val monthlyWorkReportService: MonthlyWorkReportService by lazy {
        retrofitClient
            .build()
            .create(MonthlyWorkReportService::class.java)
    }
    val monthlWorkClientService: MonthlyWorkClientService by lazy {
        retrofitClient
            .build()
            .create(MonthlyWorkClientService::class.java)
    }


    // management
    val myTeamManagementService: MyTeamManagementService by lazy {
        retrofitClient
            .build()
            .create(MyTeamManagementService::class.java)
    }
    val complaintManagementService: ComplaintManagementService by lazy {
        retrofitClient
            .build()
            .create(ComplaintManagementService::class.java)
    }
    val projectManagementService: ProjectManagementService by lazy {
        retrofitClient
            .build()
            .create(ProjectManagementService::class.java)
    }
    val absentOprMgmntService: AbsentOprMgmntService by lazy {
        retrofitClient
            .build()
            .create(AbsentOprMgmntService::class.java)
    }
    val operationalManagementService: OperationalManagementService by lazy {
        retrofitClient
            .build()
            .create(OperationalManagementService::class.java)
    }
    val cftalkManagementService: CftalkManagementService by lazy {
        retrofitClient
            .build()
            .create(CftalkManagementService::class.java)
    }
    val attendanceManagementService: AttendanceManagementService by lazy {
        retrofitClient
            .build()
            .create(AttendanceManagementService::class.java)
    }
    val attendanceTrackingService: AttendanceTrackingService by lazy {
        retrofitClient
            .build()
            .create(AttendanceTrackingService::class.java)
    }
    val permissionManagementService: PermissionManagementService by lazy {
        retrofitClient
            .build()
            .create(PermissionManagementService::class.java)
    }
    val overtimeManagementService: OvertimeManagementService by lazy {
        retrofitClient
            .build()
            .create(OvertimeManagementService::class.java)
    }
    val inspeksiService: InspeksiService by lazy {
        retrofitClient
            .build()
            .create(InspeksiService::class.java)
    }
    val reportManagementService: ReportManagementService by lazy {
        retrofitClient
            .build()
            .create(ReportManagementService::class.java)
    }
    val auditService: AuditService by lazy {
        retrofitClient
            .build()
            .create(AuditService::class.java)
    }
    val periodicManagementService: PeriodicManagementService by lazy {
        retrofitClient
            .build()
            .create(PeriodicManagementService::class.java)
    }
    val shareLocManagementService: ShareLocManagementService by lazy {
        retrofitClient
            .build()
            .create(ShareLocManagementService::class.java)
    }
    val damageReportManagementService: DamageReportManagementService by lazy {
        retrofitClient
            .build()
            .create(DamageReportManagementService::class.java)
    }

    val resignManagementService: ResignManagementService by lazy {
        retrofitClient
            .build()
            .create(ResignManagementService::class.java)
    }

    val scheduleManagementService: ScheduleManagementService by lazy {
        retrofitClient
            .build()
            .create(ScheduleManagementService::class.java)
    }

    val routineService: RoutineService by lazy {
        retrofitClient
            .build()
            .create(RoutineService::class.java)
    }


    val reportHighService: ReportHighService by lazy {
        retrofitClient
            .build()
            .create(ReportHighService::class.java)
    }

    val weeklyProgressService: WeeklyProgressService by lazy {
        retrofitClient
            .build()
            .create(WeeklyProgressService::class.java)

    }

    val closingService: ClosingService by lazy {
        retrofitClient
            .build()
            .create(ClosingService::class.java)
    }

    val chartService: ChartService by lazy {
        retrofitClient
            .build()
            .create(ChartService::class.java)
    }

    val closingManagementService: ClosingManagementService by lazy {
        retrofitClient
            .build()
            .create(ClosingManagementService::class.java)
    }

    val homeManagementUpdatedService: HomeManagementUpdatedService by lazy {
        retrofitClient
            .build()
            .create(HomeManagementUpdatedService::class.java)
    }

    val visitReportManagementService: VisitReportManagementService by lazy {
        retrofitClient
            .build()
            .create(VisitReportManagementService::class.java)
    }

    val projectService: ProjectService by lazy {
        retrofitClient
            .build()
            .create(ProjectService::class.java)
    }

    val humanCapitalService: HumanCapitalService by lazy {
        retrofitClient
            .build()
            .create(HumanCapitalService::class.java)
    }

}
