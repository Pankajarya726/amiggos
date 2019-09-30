package com.tekzee.mallortaxi.network

import com.facebook.common.Common
import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
import com.tekzee.mallortaxi.base.model.CommonResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ApiService {

    @POST("auth/validateAppVersion")
    fun doValidateAppVersionApi(@Body input: JsonObject, @HeaderMap headers: HashMap<String, String?>): Observable<Response<ValidateAppVersionResponse>>


    @GET("getLanguageConstant")
    fun doLanguageConstantApi(@HeaderMap headers: HashMap<String, String?>): Observable<Response<JsonObject>>


    @GET("auth/language")
    fun doCallLanguageApi(
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LanguageResponse>>


    @POST("auth/login")
    fun doLoginApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>>



    @POST("user/turningUp")
    fun callTurningUpApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<TurningUpResponse>>


    @POST("user/insertReferralCode")
    fun doCallReferalApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ReferalCodeResponse>>

    @POST("user/isVenueCheckUncheck")
    fun doCallCheckVenueApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueResponse>>


    @POST("user/age_group")
    fun doCallAgeGroupApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AgeGroupResponse>>

    @POST("user/getMyStories")
    fun doGetMyStories(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GetMyStoriesResponse>>


    @POST("user/getAllActiveUserList")
    fun doCallOnlineFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OnlineFriendResponse>>

    @POST("user/dashboard_map")
    fun doGetDashboardMapApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<DashboardReponse>>


    @POST("user/updateInviteFriendsCount")
    fun doUpdateFriendCount(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateFriendCountResponse>>


    @POST("user/getSetting")
    fun doCallGetSettings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyPreferenceResponse>>


    @POST("user/userPartyInvites")
    fun doCallPartyInviteApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PartyInvitesResponse>>


    @POST("user/userPastParties")
    fun docallPastPartyApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PastPartyResponse>>



    @POST("user/userUpcomingParties")
    fun doCallUpcomingPartyApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PastPartyResponse>>


    @POST("user/user_preference")
    fun docallSaveSettings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PreferenceSavedResponse>>

    @POST("user/dashboard_club")
    fun doGetVenueApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<com.tekzee.amiggos.ui.home.model.VenueResponse>>

    @Multipart
    @POST("user/save_photoid")
    fun doCallAttachIdApi(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @Part("dob") date: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>


}