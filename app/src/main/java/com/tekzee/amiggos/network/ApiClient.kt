package com.tekzee.mallortaxi.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger.addLogAdapter
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggos.ui.guestlist.model.GuestListResponse
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggos.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationData
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
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

    fun doCallSettingsApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<SettingsResponse>> {
        return apiService.doCallSettingsApi(input,createHeaders)
    }

    fun doCallHelpCenterApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<HelpCenterResponse>> {
        return apiService.doCallHelpCenterApi(input,createHeaders)
    }

    fun doUpdateSettings(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateSettingsResponse>> {
        return apiService.doUpdateSettings(input,createHeaders)
    }

    fun doLoginApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>> {
        return apiService.doLoginApi(input, createHeaders)
    }

    fun callTurningUpApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<TurningUpResponse>> {
        return apiService.callTurningUpApi(input, createHeaders)
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
    ): Observable<Response<AgeGroupResponse>> {
        return apiService.doCallAgeGroupApi(input, createHeaders)
    }


    fun doCallOnlineFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<OnlineFriendResponse>> {
        return apiService.doCallOnlineFriendApi(input, createHeaders)
    }

    fun doCallRealFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendResponse>> {
        return apiService.doCallRealFriendApi(input, createHeaders)
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

    fun doUpdateFriendCount(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateFriendCountResponse>> {
        return apiService.doUpdateFriendCount(input, createHeaders)
    }

    fun doCallGetSettings(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyPreferenceResponse>> {
        return apiService.doCallGetSettings(input, createHeaders)
    }

    fun doCallPartyInviteApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PartyInvitesResponse>> {
        return apiService.doCallPartyInviteApi(input, createHeaders)
    }


    fun doCallInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponse>> {
        return apiService.doCallInvitationApi(input, createHeaders)
    }


    fun doAcceptInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doAcceptInvitationApi(input, createHeaders)
    }

    fun doSendFriendRequest(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doSendFriendRequest(input, createHeaders)
    }


    fun callunFriend(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callunFriend(input, createHeaders)
    }

    fun callUnBlock(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callUnBlock(input, createHeaders)
    }


    fun doCallClearNotification(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doCallClearNotification(input, createHeaders)
    }


    fun doMyBookingApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyBookingResponse>> {
        return apiService.doMyBookingApi(input, createHeaders)
    }


    fun doMyProfile(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyProfileResponse>> {
        return apiService.doMyProfile(input, createHeaders)
    }


    fun callBlock(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callBlock(input, createHeaders)
    }

    fun callReport(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callUnBlock(input, createHeaders)
    }


    fun doRejectInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doRejectInvitationApi(input, createHeaders)
    }


    fun doCallGuestListApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<GuestListResponse>> {
        return apiService.doCallGuestListApi(input, createHeaders)
    }

    fun doCallGetFriendProfileApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendProfileResponse>> {
        return apiService.doCallGetFriendProfileApi(input, createHeaders)
    }

    fun doCallGetNotification(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<NotificationResponse>> {
        return apiService.doCallGetNotification(input, createHeaders)
    }

    fun doCallJoinPartyInvites(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doCallJoinPartyInvites(input, createHeaders)
    }

    fun doCallDeclinePartyInvites(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doCallDeclinePartyInvites(input, createHeaders)
    }

    fun docallPastPartyApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PastPartyResponse>> {
        return apiService.docallPastPartyApi(input, createHeaders)
    }

    fun doCallUpcomingPartyApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PastPartyResponse>> {
        return apiService.doCallUpcomingPartyApi(input, createHeaders)
    }

    fun docallSaveSettings(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PreferenceSavedResponse>> {
        return apiService.docallSaveSettings(input, createHeaders)
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