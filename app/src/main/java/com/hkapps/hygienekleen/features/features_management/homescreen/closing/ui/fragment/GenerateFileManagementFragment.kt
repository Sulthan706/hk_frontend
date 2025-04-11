package com.hkapps.hygienekleen.features.features_management.homescreen.closing.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.hkapps.hygienekleen.R
import com.hkapps.hygienekleen.databinding.FragmentGenerateFileManagementBinding
import com.hkapps.hygienekleen.features.features_management.homescreen.closing.viewmodel.ClosingManagementViewModel
import com.hkapps.hygienekleen.pref.CarefastOperationPref
import com.hkapps.hygienekleen.pref.CarefastOperationPrefConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class GenerateFileManagementFragment : Fragment() {


    private lateinit var binding : FragmentGenerateFileManagementBinding

    private val closingManagementViewModel by lazy {
        ViewModelProvider(this)[ClosingManagementViewModel::class.java]
    }

    private val userId =
        CarefastOperationPref.loadInt(CarefastOperationPrefConst.USER_ID, 0)

    private val projectId =
        CarefastOperationPref.loadString(CarefastOperationPrefConst.PROJECT_ID_PROJECT_MANAGEMENT, "")

    private val userName = CarefastOperationPref.loadString(CarefastOperationPrefConst.USER_NAME, "")

    private var isGenerated = false

    private var closingId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentGenerateFileManagementBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binding.shimmerClosingArea.startShimmerAnimation()
        Handler().postDelayed({
            loadData(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_YESTERDAY, false))
        },1000)
    }

    private fun loadData(isYesterday : Boolean){
        if(isYesterday){
            closingManagementViewModel.getDetailDailyTarget(projectId,getYesterdayDate())
        }else{
            closingManagementViewModel.getDetailDailyTarget(projectId,getTwoYesterdayDate())
        }
        closingManagementViewModel.detailDailyTargetModel.observe(viewLifecycleOwner){ response ->
            if(response.code == 200){
                closingId = response.data.idClosing ?: 0
                isGenerated = response.data.fileGenerated
                binding.shimmerClosingArea.stopShimmerAnimation()
                binding.shimmerClosingArea.visibility = View.GONE
                binding.linearContent.visibility = View.VISIBLE
                binding.tvDate.text = response.data.closedAt
                binding.tvUser.text = response.data.closedBy
                val totalSettlement = "${response.data.totalTarget} Pekerjaan"
                binding.tvTotalSettlement.text = totalSettlement
                binding.tvTotalDiversion.text = if(response.data.totalDiverted != 0) response.data.totalDiverted.toString() else "0"
                binding.tvCountFinish.text = createSpannable("${response.data.targetsRealization}/${response.data.totalTarget}", "#FF6347")
                val totalFinishPercent = "${response.data.percentTargetsRealization}%"
                binding.tvPercentFinish.text = totalFinishPercent
            }else{
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createSpannable(text: String, firstPartColor: String): SpannableString {
        val separatorIndex = text.indexOf("/")
        val firstPart = text.substring(0, separatorIndex)
        val secondPart = text.substring(separatorIndex + 1)
        val displayFirstPart = if (firstPart == "0") "-" else firstPart
        val formattedText = "$displayFirstPart/$secondPart"

        val spannableString = SpannableString(formattedText)
        val firstColor = ForegroundColorSpan(Color.parseColor(firstPartColor))
        spannableString.setSpan(firstColor, 0, displayFirstPart.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val blackColor = ForegroundColorSpan(Color.BLACK)
        spannableString.setSpan(blackColor, displayFirstPart.length, formattedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun getTwoYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -2)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }

    private fun initView(){
        binding.btnClosing.setOnClickListener {
            val loading = activity?.findViewById<LinearLayout>(R.id.loading_generate_file)
            loading?.visibility = View.VISIBLE
            Handler().postDelayed({
//               generateFile(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_YESTERDAY, false))
                if(closingId != 0){
                    fetchData(CarefastOperationPref.loadBoolean(CarefastOperationPrefConst.IS_YESTERDAY, false))
                }else{
                    requireActivity().recreate()
                    Toast.makeText(requireContext(), "terjadi kesalahan, silahkan coba lagi", Toast.LENGTH_SHORT).show()
                }
            },1000)

        }
    }

    private fun generateFile(isYesterday: Boolean){
        val viewPager =  activity?.findViewById<ViewPager2>(R.id.view_pager_closing)
        val loading = activity?.findViewById<LinearLayout>(R.id.loading_generate_file)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val currentDate = Date()
        val date = dateFormat.format(currentDate)
        if(isGenerated){
            loading?.visibility = View.GONE
            viewPager?.currentItem = 1

        }else{
            closingManagementViewModel.generateFileClosingManagement(projectId,if(isYesterday) getYesterdayDate() else getTwoYesterdayDate(),userId)
            closingManagementViewModel.generateFileModel.observe(viewLifecycleOwner){
                if(it.code == 200 || isGenerated){
                    loading?.visibility = View.GONE
                    viewPager?.currentItem = 1
                }else{
                    loading?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Gagal generate file", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun fetchData(isYesterday: Boolean) {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager_closing)
        val loading = activity?.findViewById<LinearLayout>(R.id.loading_generate_file)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val urlString =
                    "https://ops.hk-indonesia.id/hksite/Detailperiodic/dataDetailPeriodikDailyTest/$projectId/${if(isYesterday) getYesterdayDate() else getTwoYesterdayDate()}/$closingId"
                Log.d("TESTED", urlString)


                val client = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.MINUTES)
                    .readTimeout(15, TimeUnit.MINUTES)
                    .writeTimeout(15, TimeUnit.MINUTES)
                    .sslSocketFactory(createUnsafeSslSocketFactory(), createTrustAllCertsManager())
                    .hostnameVerifier { _, _ -> true }
                    .build()

                val request = Request.Builder()
                    .url(urlString)
                    .post(RequestBody.create(null, ""))
                    .build()


                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string() ?: ""
                    val responseJson = JSONObject(responseBody)
                    val code = responseJson.getInt("code")
                    val message = responseJson.getString("message")

                    withContext(Dispatchers.Main) {
                        Log.d("API_RESPONSE", responseJson.toString())
                        if (code == 200) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            loading?.visibility = View.GONE
                            viewPager?.currentItem = 1
                        } else {
                            Toast.makeText(requireContext(), "Gagal generate file, silahkan coba lagi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("API_ERROR", "Response Code: ${response.code} ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("API_EXCEPTION", e.message.toString())
            }
        }
    }

    private fun createUnsafeSslSocketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        return sslContext.socketFactory
    }

    private fun createTrustAllCertsManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
    }

//    private fun fetchData(isYesterday: Boolean) {
//        val viewPager =  activity?.findViewById<ViewPager2>(R.id.view_pager_closing)
//        val loading = activity?.findViewById<LinearLayout>(R.id.loading_generate_file)
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val urlString =
//                    "https://ops.hk-indonesia.id/hksite/Detailperiodic/dataDetailPeriodikDailyTest/$projectId/${if(isYesterday) getYesterdayDate() else getTwoYesterdayDate()}/$closingId"
//                val url = URL(urlString)
//
//
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "POST"
//                connection.connectTimeout = 150000
//                connection.readTimeout = 150000
//
//                val responseCode = connection.responseCode
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//
//                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
//                    val result = StringBuilder()
//                    var line: String?
//
//                    while (reader.readLine().also { line = it } != null) {
//                        result.append(line)
//                    }
//                    reader.close()
//
//                    val responseJson = JSONObject(result.toString())
//                    val code = responseJson.getInt("code")
//                    val message = responseJson.getString("message")
//                    withContext(Dispatchers.Main) {
//                        Log.d("API_RESPONSE", responseJson.toString())
//                        if(code.toInt() == 200){
//                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//                            loading?.visibility = View.GONE
//                            viewPager?.currentItem = 1
//                        }else{
//                            Toast.makeText(requireContext(), "Gagal generate file, silahkan coba lagi", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Log.e("API_ERROR", "Response Code: $responseCode")
//                }
//                connection.disconnect()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(requireContext(), "Gagal generate file, silahkan coba lagi", Toast.LENGTH_SHORT).show()
//                Log.e("API_EXCEPTION", e.message.toString())
//            }
//        }
//    }

}