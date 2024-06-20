package com.ccinc.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kotlinx.serialization.json.Json
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ccinc.design_system.TestTheme
import com.ccinc.ui.navigation.RootNavGraph
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
        )
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.splash
        }

        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.getConfigs(
            type = "MOBILE",
            tenantId = "tbc",
            apiKey = "onaphYtIonimItIONceLp",
            version = "v1"
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseData = response.body()!!.string()
                    Log.d("HelloMainActivity", "Response: $responseData")
                } else {
                    Log.e("HelloMainActivity", "Request failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("HelloMainActivity", "Network request failed", t)
            }
        })

        setContent {
            TestTheme {
                RootNavGraph(navController = rememberNavController())
//                CatalogRoute()
            }
        }
    }
}

object ApiClient {
    private const val BASE_URL = "https://poc.riskoffice.formica.ai/api/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }
        return retrofit!!
    }
}

interface ApiService {
    @GET("event-tracker/get-configs/{type}")
    fun getConfigs(
        @Path("type") type: String,
        @Header("tenant-id") tenantId: String,
        @Header("x-api-key") apiKey: String,
        @Header("version") version: String
    ): Call<ResponseBody>
}