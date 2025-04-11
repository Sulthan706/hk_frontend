package com.hkapps.hygienekleen.features.auth.login.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.ActivityLoginBinding
import com.hkapps.hygienekleen.features.auth.forgotpass.ui.activity.ForgotPassActivity
import com.hkapps.hygienekleen.features.auth.login.ui.fragment.LoginFailedDialogFragment
import com.hkapps.hygienekleen.features.auth.login.ui.fragment.NotVerifiedDialogFragment
import com.hkapps.hygienekleen.features.auth.login.viewmodel.LoginViewModel
import com.hkapps.hygienekleen.features.auth.register.ui.activity.DaftarNucNikActivity
import com.hkapps.hygienekleen.features.features_client.home.ui.activity.HomeClientActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.activity.HomeManagementActivity
import com.hkapps.hygienekleen.features.features_management.homescreen.home.ui.bodClient.HomeBodClientActivity
import com.hkapps.hygienekleen.features.features_vendor.homescreen.home.ui.new_.activity.HomeVendorActivity
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import com.hkapps.hygienekleen.utils.CommonUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.realpacific.clickshrinkeffect.applyClickShrink


class LoginActivity : AppCompatActivity(), NotVerifiedDialogFragment.NotVerifiedDialogListener {
    private lateinit var binding: ActivityLoginBinding
    private var email = ""
    private var phone = ""
    private var isEmail = false
    private var isPhone = false
    private var loadingDialog: Dialog? = null
    private var buildPhone: String = "${Build.MANUFACTURER} ${Build.MODEL}"

    private var adsId: String = ""
    private var userId: String = ""
    private var phoneValid: Boolean = false

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tvForgotPass.applyClickShrink()
        binding.tvDaftar.applyClickShrink()

//        if (binding.etPassHome.editText?.text == null) {
//            binding.etPassHome.error = null
//        }

//        binding.etPassHome.addOnEditTextAttachedListener {
//            binding.etPassHome.error = null
//        }

        binding.btnLogin.setOnClickListener {
            showLoading(getString(R.string.loading_string2))
            validation()
        }

//        binding.etPassHome.editText?.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                validation()
//                true
//            } else {
//                false
//            }
//        }

        binding.tvForgotPass.setOnClickListener {
            val i = Intent(this, ForgotPassActivity::class.java)
            startActivity(i)
        }

        binding.tvDaftar.setOnClickListener {
            val intent = Intent(this, DaftarNucNikActivity::class.java)
            startActivity(intent)
        }

//        //get ads id
//        // Run the code in a background thread to avoid blocking the main UI thread
//        Thread(Runnable {
//            try {
//                val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)
//                val advertisingId = advertisingIdInfo.id
//
//                // Log or use the advertisingId as needed
//                Log.d("Advertising ID", advertisingId)
//                adsId = advertisingId
//
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            } catch (e: GooglePlayServicesNotAvailableException) {
//                e.printStackTrace()
//            } catch (e: GooglePlayServicesRepairableException) {
//                e.printStackTrace()
//            }
//        }).start()


        setObserver()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private fun validation() {
        val username = binding.etUsernameHome.editText?.text.toString()
        val password = binding.etPassHome.editText?.text.toString()

        loginViewModel.validate(username, password)
    }


    private fun login() {
        email = binding.etUsernameHome.editText?.text.toString()
        val password = binding.etPassHome.editText?.text.toString()

//        if (email.equals("client@client.com") && password.equals("client")){
//            val u = Intent(this, HomeClientActivity::class.java)
//            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
//            CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGIN_AS, "CLIENT")
//            startActivity(u)
//            finishAffinity()
//        }else{
        loginViewModel.login(email, password)
//        }
        Log.d("Login", "email & pass $email $password")
    }

    private fun notVerifiedDialog(errorCode: String) {
        val dialogNotVerified = NotVerifiedDialogFragment()
        val bundle = Bundle()
        bundle.putString("errorCode", errorCode)
        dialogNotVerified.arguments = bundle
        dialogNotVerified.setListener(this)
        dialogNotVerified.show(supportFragmentManager, "NotVerifiedDialogFragment")
    }

    private fun loginFailedDialog() {
        val dialogLoginFailed = LoginFailedDialogFragment()
        dialogLoginFailed.show(supportFragmentManager, "LoginFailedDialogFragment")
    }

    private fun setObserverClient() {
        Log.d(
            "TAG", "login client"
        )
        loginViewModel.getLoginClientModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_TOKEN,
                    it.data.token
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_REFRESH_TOKEN,
                    it.data.refreshToken
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_EMAIL,
                    it.data.email
                )
                CarefastOperationPref.saveInt(
                    CarefastOperationPrefConst.USER_ID,
                    it.data.clientId
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NAME,
                    it.data.clientName
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.CLIENT_PROJECT_CODE,
                    it.data.projectCode
                )
                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGIN_AS, "CLIENT")
                CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "CLIENT")

                fcmNotif("Client")
                val i = Intent(this, HomeClientActivity::class.java)
                startActivity(i)
                finishAffinity()
            } else {
                hideLoading()
                when (it.errorCode) {
                    "1" -> notVerifiedDialog("1")
                    "2" -> notVerifiedDialog("2")
                    "9" -> loginManagement()
                }
            }
        })
    }

    private fun setObserverManagement() {
        Log.d(
            "TAG", "login client"
        )
        loginViewModel.getLoginManagementModel().observe(this, Observer {
            if (it.code == 200) {
                hideLoading()
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_TOKEN,
                    it.data.token
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_REFRESH_TOKEN,
                    it.data.refreshToken
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_EMAIL,
                    it.data.adminMasterEmail
                )
                CarefastOperationPref.saveInt(
                    CarefastOperationPrefConst.USER_ID,
                    it.data.adminMasterId
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NAME,
                    it.data.adminMasterName
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_NUC,
                    it.data.adminMasterNUC
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_LEVEL_POSITION,
                    it.data.levelJabatan
                )
                CarefastOperationPref.saveString(
                    CarefastOperationPrefConst.USER_POSITION,
                    it.data.adminMasterJabatan
                )
                CarefastOperationPref.saveInt(
                    CarefastOperationPrefConst.MANAGEMENT_POSITION_LEVEL,
                    it.data.levelPosition
                )

                CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
                CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGIN_AS, "MANAGEMENT")
                CarefastOperationPref.saveInt(CarefastOperationPrefConst.ID_CABANG, it.data.idCabang)
                Log.d(
                    "LoginActivity",
                    "setObserverManagement: level jabatan ${it.data.levelJabatan}"
                )

//                fcmNotif("Management")
                if (it.data.levelJabatan == "CLIENT") {
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "MANAGEMENT_CLIENT")
                    val i = Intent(this, HomeBodClientActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                } else {
                    when (it.data.levelJabatan) {
                        "BSM" -> {
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_ID_PROJECT_MANAGEMENT, it.data.branchCode)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT, it.data.branchName)
                            CarefastOperationPref.saveInt(CarefastOperationPrefConst.BRANCH_NAME_PROJECT_MANAGEMENT_TOTAL_PROJECT, it.data.totalProject)
                            CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.IS_VP, true)
                            CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "BSM")
                        }
                        "BOD","CEO" -> CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "BOD")
                        "FM","GM","OM" -> CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "MANAGEMENT")
                        else -> CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "unknown management")
                    }

                    CarefastOperationPref.saveString(CarefastOperationPrefConst.LOGIN_MANAGEMENT_NUC,it.data.adminMasterNUC)
                    val i = Intent(this, HomeManagementActivity::class.java)
                    startActivity(i)
                    finishAffinity()

                }
            } else {
                hideLoading()
//                Toast.makeText(this,"Email atau Password anda salah.",Toast.LENGTH_SHORT).show()

//                binding.etUsernameHome.error = "Email atau Password anda salah"
                binding.etPassHome.error = "Email atau Password anda salah"
                binding.etPassHome.editText?.addTextChangedListener {
                    binding.etPassHome.error = null
                }

            }
        })
    }

    private fun setObserver() {
        Log.d(
            "TAG", "login vendor"
        )
        //login 1 akun
//        loginViewModel.validateImeiViewModel().observe(this, Observer {
//            if (it.code == 200) {
//                when(it.message){
//                    "Data Cocok" -> {
//                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
//                        startActivity(Intent(this,HomeVendorActivity::class.java))
//                        finishAffinity()
//                    }
//                    "Insert Berhasil" -> {
//                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
//                        startActivity(Intent(this,HomeVendorActivity::class.java))
//                        finishAffinity()
//                    }
//                    "Data Tidak Cocok" -> {
//                        CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, false)
//                        popupImeiNotValid()
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
//            }
//        })

        loginViewModel.getLoginModel().observe(this, Observer {
            if (it.code == 200) {
                if (it.data.isActive == "Y") {
                    hideLoading()
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_TOKEN,
                        it.data.token
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_REFRESH_TOKEN,
                        it.data.refreshToken
                    )
                    CarefastOperationPref.saveString(
                        CarefastOperationPrefConst.USER_EMAIL,
                        it.data.email
                    )
                    CarefastOperationPref.saveInt(
                        CarefastOperationPrefConst.USER_ID,
                        it.data.idEmployeeProject
                    )
                    CarefastOperationPref.saveString(CarefastOperationPrefConst.USER_ROLE, "EMPLOYEE")

                    userId = it.data.idEmployeeProject.toString()
                    CarefastOperationPref.saveBoolean(CarefastOperationPrefConst.LOGIN_STATUS, true)
                    loginViewModel.validateImei(
                        it.data.idEmployeeProject.toString(),
                        adsId,
                        buildPhone
                    )
                    fcmNotif("Operator")

                    val i = Intent(this, HomeVendorActivity::class.java)
                    startActivity(i)


//                    Toast.makeText(this, "anda login", Toast.LENGTH_SHORT).show()

                    finishAffinity()
                } else {
                    Toast.makeText(
                        this,
                        "Maaf, Anda sudah tidak terdaftar sebagai karyawan Carefast",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                hideLoading()
                when (it.errorCode) {
                    "1" -> notVerifiedDialog("1")
                    "2" -> notVerifiedDialog("2")
                    "9" -> loginClient()
                    "10" -> {
                        binding.etPassHome.error =
                            "Maaf, Anda sudah tidak terdaftar sebagai karyawan Carefast"
                        binding.etPassHome.editText?.addTextChangedListener {
                            binding.etPassHome.error = null
                        }
                    }
                }
            }
        })

        loginViewModel.getIsValidEmail().observe(this, Observer {
            if (!it) {
                hideLoading()
                binding.etUsernameHome.error = getString(R.string.email_error)
            } else {
                binding.etUsernameHome.error = null
            }
        })

        loginViewModel.getIsValidPasswordLength().observe(this, Observer {
            if (!it) {
                binding.etPassHome.error = getString(R.string.password_error)
            } else {
                binding.etPassHome.error = null
            }
        })

//        loginViewModel.getIsValidPhoneLength().observe(this, Observer {
//            if (!it) {
//                binding.etUsernameHome.error = getString(R.string.no_wa_error)
//            } else {
//                binding.etUsernameHome.error = null
//            }
//        })

        loginViewModel.getIsValidEmailPassword().observe(this, Observer {
            if (it) {
                login()
//                loginViewModel.isLoading?.observe(this, Observer<Boolean?> { isLoading ->
//                    if (isLoading != null) {
//                        if (isLoading) {
//                            Log.d(
//                                "Hide", "true"
//                            )
//                        } else {
//                            Log.d(
//                                "Hide", "false"
//                            )
//                        }
//                    }else{
//                        //nothing
//                    }
//                })
            } else {
                //nothing
            }
        })

        loginViewModel.getIsEmail().observe(this, Observer {
            isEmail = it
        })

        loginViewModel.getIsPhone().observe(this, Observer {
            isPhone = it
        })

    }

    private fun popupImeiNotValid() {
        val view = View.inflate(this, R.layout.dialog_imei_notvalid, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        val btnBack = dialog.findViewById<RelativeLayout>(R.id.btnBackGreetingSore)
        btnBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun loginClient() {
        email = binding.etUsernameHome.editText?.text.toString()
        val password = binding.etPassHome.editText?.text.toString()
        loginViewModel.loginClient(email, password)
        setObserverClient()
    }

    private fun loginManagement() {
        email = binding.etUsernameHome.editText?.text.toString()
        val password = binding.etPassHome.editText?.text.toString()
        loginViewModel.loginManagement(email, password)
        setObserverManagement()
    }

    override fun onVerify() {
        if (isEmail) {
            email = binding.etUsernameHome.editText?.text.toString()
        } else if (isPhone) {
            phone = binding.etUsernameHome.editText?.text.toString()
        }
//        AppNavigation.openRegisterAccountVerification(this, phone, email)
        finish()
    }

    private fun showLoading(loadingText: String) {
        loadingDialog = CommonUtils.showLoadingCancelableDialog(this, loadingText)
    }

    private fun hideLoading() {
        loadingDialog?.cancel()
    }

    //Notif subscribtion
    fun fcmNotif(topic: String) {
        Log.d("FCM", "fcm")
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener { task ->
            var msg = ""
            msg = if (!task.isSuccessful) {
                "failed"
            } else {
                "success"
            }
            Log.d("FCM", msg)
            //                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}