package com.tekzee.mallortaxi.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger.addLogAdapter
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.mallortaxi.base.model.CommonResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    private val apiService: ApiService

    init {
        val gson = GsonBuilder().setLenient().create()
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            clientBuilder.addInterceptor(loggingInterceptor)
            addLogAdapter(AndroidLogAdapter())
        }

        val okHttpClient: OkHttpClient = clientBuilder
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();


        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        apiService = retrofit.create(ApiService::class.java)

    }

    companion object {
        private val BASE_URL = "http://dev.tekzee.in/Amiggos_new/api/"
        private var apiClient: ApiClient? = null
        /**
         * Gets my app client.
         *
         * @return the my app client
         */
        val instance: ApiClient
            get() {
                if (apiClient == null) {
                    apiClient = ApiClient()
                }
                return apiClient as ApiClient
            }
    }

    fun doValidateAppVersionApi(
        input: JsonObject,
        headers: HashMap<String, String?>
    ): Observable<Response<ValidateAppVersionResponse>> {
        return apiService.doValidateAppVersionApi(input, headers)
    }


    fun doLanguageConstantApi(
        headers: HashMap<String, String?>
    ): Observable<Response<JsonObject>> {
        return apiService.doLanguageConstantApi(headers)
    }

    fun doCallLanguageApi(
        createHeaders: HashMap<String, String?>
    ): Observable<Response<LanguageResponse>> {
        return apiService.doCallLanguageApi(createHeaders)
    }

    fun doLoginApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>> {
        return apiService.doLoginApi(input, createHeaders)
    }

    fun doCallReferalApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ReferalCodeResponse>> {
        return apiService.doCallReferalApi(input, createHeaders)
    }

    fun doCallCheckVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueResponse>> {
        return apiService.doCallCheckVenueApi(input, createHeaders)
    }

    fun doCallAgeGroupApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doCallAgeGroupApi(input, createHeaders)
    }


    fun doGetMyStories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<GetMyStoriesResponse>> {
        return apiService.doGetMyStories(input, createHeaders)
    }

    fun doGetDashboardMapApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<DashboardReponse>> {
        return apiService.doGetDashboardMapApi(input, createHeaders)
    }
    fun doGetVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<com.tekzee.amiggos.ui.home.model.VenueResponse>> {
        return apiService.doGetVenueApi(input, createHeaders)
    }

    fun doCallAttachIdApi(
        file: MultipartBody.Part?,
        valueInt: RequestBody,
        date: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>> {
        return apiService.doCallAttachIdApi(file, valueInt, date, createHeaders)
    }


}