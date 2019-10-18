package com.tekzee.mallortaxi.network

import com.google.gson.JsonObject
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
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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


    @POST("user/getSettingDetails")
    fun doCallSettingsApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<SettingsResponse>>


    @POST("user/getHelpCenter")
    fun doCallHelpCenterApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<HelpCenterResponse>>


    @POST("user/updateSettingValue")
    fun doUpdateSettings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateSettingsResponse>>

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

    @POST("user/userFreindsList")
    fun doCallRealFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendResponse>>

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



    @POST("user/getRequestList")
    fun doCallInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponse>>



    @POST("user/acceptFriendRequest")
    fun doAcceptInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>




    @POST("user/sendFriendRequest")
    fun doSendFriendRequest(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>




    @POST("user/unFriendUser")
    fun callunFriend(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>





    @POST("user/unblockFriend")
    fun callUnBlock(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>









    @POST("user/clearNotification")
    fun doCallClearNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>






    @POST("user/blockFriend")
    fun callBlock(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>





    @POST("user/unFriendUser")
    fun callReport(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>




    @POST("user/declineFriendRequest")
    fun doRejectInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>



    @POST("user/getGuestList")
    fun doCallGuestListApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GuestListResponse>>



    @POST("user/getFriendsProfile")
    fun doCallGetFriendProfileApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendProfileResponse>>




    @POST("user/getAllNotification")
    fun doCallGetNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<NotificationResponse>>



    @POST("user/acceptPartyInvitaion")
    fun doCallJoinPartyInvites(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>



    @POST("user/rejectPartyInvitaion")
    fun doCallDeclinePartyInvites(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


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