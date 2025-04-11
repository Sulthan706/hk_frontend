package com.hkapps.hygienekleen.di

import com.hkapps.hygienekleen.features.auth.forgotpass.di.ForgotPassModule
import com.hkapps.hygienekleen.features.auth.forgotpass.viewmodel.ForgotPassViewModel
import com.hkapps.hygienekleen.features.auth.register.di.DaftarModule
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DataEmployeeViewModel
import com.hkapps.hygienekleen.features.auth.register.viewmodel.DaftarViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.di.ChecklistModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.checklist.viewmodel.ChecklistViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.di.HomeModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.ListProjectViewModel
import com.hkapps.hygienekleen.features.auth.login.di.LoginModule
import com.hkapps.hygienekleen.features.auth.login.viewmodel.LoginViewModel
import com.hkapps.hygienekleen.features.features_client.complaint.di.ClientComplaintModule
import com.hkapps.hygienekleen.features.features_client.complaint.viewmodel.ClientComplaintViewModel
import com.hkapps.hygienekleen.features.features_client.dashboardProject.di.DashboardProjectModule
import com.hkapps.hygienekleen.features.features_client.dashboardProject.viewmodel.DashboardProjectViewModel
import com.hkapps.hygienekleen.features.features_client.home.di.HomeClientModule
import com.hkapps.hygienekleen.features.features_client.home.viewmodel.HomeClientViewModel
import com.hkapps.hygienekleen.features.features_client.myteam.di.MyTeamClientModule
import com.hkapps.hygienekleen.features.features_client.myteam.viewmodel.MyTeamClientViewModel

import com.hkapps.hygienekleen.features.features_client.notifcation.di.NotifModule
import com.hkapps.hygienekleen.features.features_client.notifcation.viewmodel.NotifClientViewModel
import com.hkapps.hygienekleen.features.features_client.overtime.di.OvertimeClientModule
import com.hkapps.hygienekleen.features.features_client.overtime.viewmodel.OvertimeClientViewModel
import com.hkapps.hygienekleen.features.features_client.report.di.ReportClientModule
import com.hkapps.hygienekleen.features.features_client.report.viewmodel.ReportClientViewModel
import com.hkapps.hygienekleen.features.features_client.setting.di.SettingClientModule
import com.hkapps.hygienekleen.features.features_client.setting.viewmodel.SettingClientViewModel
import com.hkapps.hygienekleen.features.features_client.training.di.TrainingClientModule
import com.hkapps.hygienekleen.features.features_client.training.viewmodel.TrainingClientViewModel
import com.hkapps.hygienekleen.features.features_client.visitreport.di.VisitReportModule
import com.hkapps.hygienekleen.features.features_client.visitreport.viewmodel.VisitReportViewModel
import com.hkapps.hygienekleen.features.features_management.complaint.di.ComplaintManagementModule
import com.hkapps.hygienekleen.features.features_management.complaint.viewmodel.ComplaintManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.di.AttendanceManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOSM.viewModel.AttendanceManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.di.AbsentOprManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.attendanceOpr.viewModel.AbsentOprMgmntViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.di.CftalkManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.cftalk.viewmodel.CftalkManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.home.di.HomeManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.home.viewmodel.HomeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.di.AuditModule
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.audit.viewmodel.AuditViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.di.InspeksiModule
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.mainInspeksi.viewmodel.InspeksiViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.di.OperationalManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.OperationalManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.operational.viewmodel.RatingProfilesViewModel
import com.hkapps.hygienekleen.features.features_management.myteam.di.MyTeamManagementModule
import com.hkapps.hygienekleen.features.features_management.myteam.viewmodel.MyTeamManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.project.di.ProjectManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.di.AttendanceTrackingModule
import com.hkapps.hygienekleen.features.features_management.homescreen.visitTracking.viewModel.AttendanceTrackingViewModel
import com.hkapps.hygienekleen.features.features_management.project.viewmodel.ProjectManagementViewModel
import com.hkapps.hygienekleen.features.features_management.report.di.ReportManagementModule
import com.hkapps.hygienekleen.features.features_management.report.viewmodel.ReportManagementViewModel
import com.hkapps.hygienekleen.features.features_management.service.overtime.di.OvertimeManagementModule
import com.hkapps.hygienekleen.features.features_management.service.overtime.viewModel.OvertimeManagementViewModel
import com.hkapps.hygienekleen.features.features_management.service.permission.di.PermissionManagementModule
import com.hkapps.hygienekleen.features.features_management.service.permission.viewmodel.PermissionManagementViewModel
import com.hkapps.hygienekleen.features.facerecog.di.FaceRecogModule
import com.hkapps.hygienekleen.features.facerecog.viewmodel.FaceRecogViewModel
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.di.MonthlyWorkClientModule
import com.hkapps.hygienekleen.features.features_client.monthlyworkreportclient.viewmodel.MonthlyWorkClientViewModel
import com.hkapps.hygienekleen.features.features_management.damagereport.di.DamageReportManagementModule
import com.hkapps.hygienekleen.features.features_management.damagereport.viewmodel.DamageReportManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.di.ClosingManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.di.HomeManagementUpdatedModule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.homeUpdated.viewmodel.HomeManagementUpdatedViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.di.HumanCapitalModule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.humanCapital.viewmodel.HumanCapitalViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.di.ProjectModule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.project.viewmodel.ProjectViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.di.VisitReportManagementModule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.visitReport.viewModel.VisitReportManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.di.RoutineModule
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.rutin.viewmodel.RoutineViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.di.WeeklyProgressModule
import com.hkapps.hygienekleen.features.features_management.homescreen.inspeksi.weeklyprogress.viewmodel.WeeklyProgressViewModel
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.di.PeriodicManagementModule
import com.hkapps.hygienekleen.features.features_management.homescreen.monthlyworkreportmanagement.viewmodel.PeriodicManagementViewModel
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.di.ScheduleManagementModule
import com.hkapps.hygienekleen.features.features_management.homeScreenUpdated.schedule.viewModel.ScheduleManagementViewModel
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.di.ReportHighModule
import com.hkapps.hygienekleen.features.features_management.reportHighMngmnt.viewmodel.ReportHighViewModel
import com.hkapps.hygienekleen.features.features_management.service.resign.di.ResignManagementModule
import com.hkapps.hygienekleen.features.features_management.service.resign.viewmodel.ResignManagementViewModel
import com.hkapps.hygienekleen.features.features_management.shareloc.di.ShareLocManagementModule
import com.hkapps.hygienekleen.features.features_management.shareloc.viewmodel.ShareLocManagementViewModel
import com.hkapps.hygienekleen.features.features_vendor.damagereport.di.DamageReportVendorModule
import com.hkapps.hygienekleen.features.features_vendor.damagereport.viewmodel.DamageReportVendorViewModel

import com.hkapps.hygienekleen.features.features_vendor.notifcation.di.NotifVendorModule
import com.hkapps.hygienekleen.features.features_vendor.notifcation.viewmodel.NotifVendorViewModel

import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.di.AttendanceFixModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_fix.viewmodel.AttendanceFixViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.di.AttendanceModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.attendance_old.viewmodel.AttendanceViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.di.ClosingModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.closing.viewmodel.ClosingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.di.DacModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.dac.viewmodel.DacViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.HomeReportViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.viewmodel.PlottingViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.viewmodel.StatusAbsenViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.di.MonthlyWorkReportModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.monthlyworkreport.viewmodel.MonthlyWorkReportViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.plotting.di.PlottingModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.di.ReportModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.report.viewmodel.ReportViewModel
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.di.ScheduleModule
import com.hkapps.hygienekleen.features.features_vendor.homescreen.schedule.viewmodel.ScheduleViewModel
import com.hkapps.hygienekleen.features.features_vendor.myteam.di.TimkuModule
import com.hkapps.hygienekleen.features.features_vendor.myteam.viewmodel.ShiftTimkuViewModel
import com.hkapps.hygienekleen.features.features_vendor.profile.di.ProfileModule
import com.hkapps.hygienekleen.features.features_vendor.profile.viewmodel.ProfileViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.approval.di.ApprovalModule
import com.hkapps.hygienekleen.features.features_vendor.service.approval.viewmodel.ApprovalViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.di.VendorComplaintModule
import com.hkapps.hygienekleen.features.features_vendor.service.complaint.viewmodel.VendorComplaintViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.di.VendorComplaintInternalModule
import com.hkapps.hygienekleen.features.features_vendor.service.complaint_internal.viewmodel.VendorComplaintInternalViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.di.MekariModule
import com.hkapps.hygienekleen.features.features_vendor.service.mekari.viewmodel.MekariViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.di.OvertimeModule
import com.hkapps.hygienekleen.features.features_vendor.service.overtime.viewmodel.OvertimeViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.permission.di.PermissionModule
import com.hkapps.hygienekleen.features.features_vendor.service.permission.viewmodel.PermissionViewModel
import com.hkapps.hygienekleen.features.features_vendor.service.resign.di.ResignModule
import com.hkapps.hygienekleen.features.features_vendor.service.resign.viewmodel.ResignViewModel
import com.hkapps.hygienekleen.features.grafik.di.ChartModule
import com.hkapps.hygienekleen.features.grafik.viewmodel.ChartViewModel
import com.hkapps.hygienekleen.features.splash.di.SplashModule
import com.hkapps.hygienekleen.features.splash.viewmodel.SplashViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
//disini module nya
@Component(modules = [
    LoginModule::class,
    SplashModule::class,
    HomeModule::class,
    ChecklistModule::class,
    AttendanceModule::class,
    AttendanceFixModule::class,
    DacModule::class,
    TimkuModule::class,
    PlottingModule::class,
    ForgotPassModule::class,
    DaftarModule::class,
    ProfileModule::class,
    ClientComplaintModule::class,
    HomeClientModule::class,
    NotifModule::class,
    NotifVendorModule::class,
    ScheduleModule::class,
    PermissionModule::class,
    OvertimeModule::class,
    VendorComplaintModule::class,
    OvertimeClientModule::class,
    MyTeamManagementModule::class,
    SettingClientModule::class,
    HomeManagementModule::class,
    ComplaintManagementModule::class,
    MyTeamClientModule::class,
    VendorComplaintInternalModule::class,
    ReportClientModule::class,
    ProjectManagementModule::class,
    AbsentOprManagementModule::class,
    OperationalManagementModule::class,
    CftalkManagementModule::class,
    AttendanceManagementModule::class,
    AttendanceTrackingModule::class,
    PermissionManagementModule::class,
    OvertimeManagementModule::class,
    DashboardProjectModule::class,
    TrainingClientModule::class,
    VisitReportModule::class,
    InspeksiModule::class,
    ReportManagementModule::class,
    AuditModule::class,
    MekariModule::class,
    ApprovalModule::class,
    FaceRecogModule::class,
    MonthlyWorkReportModule::class,
    PeriodicManagementModule::class,
    ShareLocManagementModule::class,
    ReportModule::class,
    MonthlyWorkClientModule::class,
    DamageReportManagementModule::class,
    DamageReportVendorModule::class,
    ResignModule::class,
    ResignManagementModule::class,
    ScheduleManagementModule::class,
    RoutineModule::class,
    ReportHighModule::class,
    WeeklyProgressModule::class,
    ClosingModule::class,
    ChartModule ::class,
    ClosingManagementModule::class,
    HomeManagementUpdatedModule::class,
    VisitReportManagementModule::class,
    ProjectModule::class,
    HumanCapitalModule::class
])

interface AppComponent {
    //disini viewmodel nya
    fun injectRepository(loginViewModel: LoginViewModel)
    fun injectRepository(splashScreenViewModel: SplashViewModel)
    fun injectRepository(homeViewModel: HomeViewModel)
    fun injectRepository(listProjectViewModel: ListProjectViewModel)
    fun injectRepository(checklistViewModel: ChecklistViewModel)
    fun injectRepository(att: AttendanceViewModel)
    fun injectRepository(attFix: AttendanceFixViewModel)
    fun injectRepository(plotting: PlottingViewModel)
    fun injectRepository(forgotpass: ForgotPassViewModel)
    fun injectRepository(statusAbsenViewModel: StatusAbsenViewModel)
    fun injectRepository(employeeData: DataEmployeeViewModel)
    fun injectRepository(daftarViewModel: DaftarViewModel)
    fun injectRepository(shiftTimkuViewModel: ShiftTimkuViewModel)
    fun injectRepository(dacViewModel: DacViewModel)
    fun injectRepository(profileViewModel: ProfileViewModel)
    fun injectRepository(notifViewModel: NotifVendorViewModel)
    fun injectRepository(scheduleViewModel: ScheduleViewModel)
    fun injectRepository(permissions: PermissionViewModel)
    fun injectRepository(overtimeViewModel: OvertimeViewModel)
    fun injectRepository(vendorComplaintViewModel: VendorComplaintViewModel)
    fun injectRepository(vendorComplaintInternalViewModel: VendorComplaintInternalViewModel)
    fun injectRepository(mekariViewModel: MekariViewModel)
    fun injectRepository(approvalViewModel: ApprovalViewModel)
    fun injectRepository(monthlyWorkReportViewModel: MonthlyWorkReportViewModel)
    fun injectRepository(homeReportViewModel: HomeReportViewModel)
    fun injectRepository(reportViewModel: ReportViewModel)
    fun injectRepository(damageReportVendorViewModel: DamageReportVendorViewModel)
    fun injectRepository(resignViewModel: ResignViewModel)







    //disini viewmodel buat apps client
    fun injectRepository(homeClientViewModel: HomeClientViewModel)
    fun injectRepository(clientComplaintViewModel: ClientComplaintViewModel)
    fun injectRepository(notifClientViewModel: NotifClientViewModel)
    fun injectRepository(overtimeClientViewModel: OvertimeClientViewModel)
    fun injectRepository(settingClientViewModel: SettingClientViewModel)
    fun injectRepository(myTeamClientViewModel: MyTeamClientViewModel)
    fun injectRepository(reportClientViewModel: ReportClientViewModel)
    fun injectRepository(dashboardProjectViewModel: DashboardProjectViewModel)
    fun injectRepository(trainingClientViewModel: TrainingClientViewModel)
    fun injectRepository(visitReportViewModel: VisitReportViewModel)
    fun injectRepository(monthlyWorkClientViewModel: MonthlyWorkClientViewModel)

    fun injectRepository(closingViewModel : ClosingViewModel)

    fun injectRepository(chartViewModel: ChartViewModel)




    // management
    fun injectRepository(myTeamManagementViewModel: MyTeamManagementViewModel)
    fun injectRepository(homeManagentViewModel: HomeManagementViewModel)
    fun injectRepository(complaintManagementViewModel: ComplaintManagementViewModel)
    fun injectRepository(projectManagementViewModel: ProjectManagementViewModel)
    fun injectRepository(absentOprMgmntViewModel: AbsentOprMgmntViewModel)
    fun injectRepository(operationalManagementViewModel: OperationalManagementViewModel)
    fun injectRepository(cftalkManagementViewModel: CftalkManagementViewModel)
    fun injectRepository(ratingProfilesViewModel: RatingProfilesViewModel)
    fun injectRepository(attendanceManagementViewModel: AttendanceManagementViewModel)
    fun injectRepository(attendanceTrackingViewModel: AttendanceTrackingViewModel)
    fun injectRepository(permissionManagementViewModel: PermissionManagementViewModel)
    fun injectRepository(overtimeManagementViewModel: OvertimeManagementViewModel)
    fun injectRepository(inspeksiViewModel: InspeksiViewModel)
    fun injectRepository(reportManagementViewModel: ReportManagementViewModel)
    fun injectRepository(auditViewModel: AuditViewModel)
    fun injectRepository(faceRecogViewModel: FaceRecogViewModel)
    fun injectRepository(periodicManagementViewModel: PeriodicManagementViewModel)
    fun injectRepository(shareLocManagementViewModel: ShareLocManagementViewModel)
    fun injectRepository(damageReportManagementViewModel: DamageReportManagementViewModel)
    fun injectRepository(resignManagementViewModel: ResignManagementViewModel)
    fun injectRepository(scheduleManagementViewModel: ScheduleManagementViewModel)
    fun injectRepository(routineViewModel: RoutineViewModel)
    fun injectRepository(reportHighViewModel: ReportHighViewModel)

    fun injectRepository(weeklyProgressViewModel: WeeklyProgressViewModel)

    fun injectRepository(closingManagementViewModel: ClosingManagementViewModel)
    fun injectRepository(homeManagementUpdatedViewModel: HomeManagementUpdatedViewModel)
    fun injectRepository(visitReportManagementViewModel: VisitReportManagementViewModel)
    fun injectRepository(projectViewModel: ProjectViewModel)
    fun injectRepository(humanCapitalViewModel: HumanCapitalViewModel)
}