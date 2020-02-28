package com.tekzee.mallortaxi.network


import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageBookResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageResponse
import com.tekzee.amiggos.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggos.ui.guestlist.model.GuestListResponse
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.NearbyMeCountResponse
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggos.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponseV2
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.Multipart



interface ApiService {

    //This api is changed from validateAppVersion to validateAppVersion_V1 by sumit sir on 2/12/2019
    @POST("auth/validateAppVersion_V1")
    fun doValidateAppVersionApi(@Body input: JsonObject, @HeaderMap headers: HashMap<String, String?>): Observable<Response<ValidateAppVersionResponse>>


    @GET("getLanguageConstant")
    fun doLanguageConstantApi(@HeaderMap headers: HashMap<String, String?>): Observable<Response<JsonObject>>


    @GET("auth/language")
    fun doCallLanguageApi(
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LanguageResponse>>


    @POST("auth/login_V1")
    fun doLoginApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>>


    @POST("user/updateUserFirebaseId")
    fun doUpdateFirebaseApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>>



    @POST("user/turningUp")
    fun callTurningUpApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<TurningUpResponse>>


    @POST("user/removeMyStoryAndOurStory")
    fun doCallDeleteStorie(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/acceptOurStoryInvite")
    fun doAcceptOurStoryInvite(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/getVenueDetails")
    fun callVenueDetailsApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueDetailResponse>>


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


    @POST("user/getMemoryViewedUserList")
    fun docallViewFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<StorieViewResponse>>


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


    @POST("user/getAllActiveUserList_V1")
    fun doCallOnlineFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OnlineFriendResponse>>

    @POST("user/getNearByUser")
    fun getNearByUser(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<SearchFriendResponse>>

    @POST("user/getNearByUser_V2")
    fun getNearByUserv2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<NearByV2Response>>

    @POST("user/userFreindsList")
    fun doCallRealFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendResponse>>


    @POST("user/userFreindsList_V2")
    fun doCallRealFriendApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>>


    @POST("user/get_favorite_venue")
    fun doCallFavoriteVenueV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>>

    @POST("user/dashboard_club_V2")
    fun docallHomeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<HomeApiResponse>>


    @POST("user/getOurStories")
    fun doCallOurMemoriesApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OurMemoriesResponse>>

    @POST("user/userFreindsList")
    fun doCallOurMemoriesUploadApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OurFriendListResponse>>

    @POST("user/getNearByUser")
    fun doCallNearBy(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OurFriendListResponse>>

    @POST("user/getMyStories")
    fun docallMemoriesApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyMemoriesResponse>>

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


    @POST("user/getBookingQrCode")
    fun doGetBookingQrCode(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookinQrCodeResponse>>


    @POST("user/getClubCalendar")
    fun doCallWeekApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ChooseWeekResponse>>


    @POST("user/getPakageList_V1")
    fun doCallPackageApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PackageResponse>>

    @POST("user/bookPackage")
    fun doBookPackage(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PackageBookResponse>>

    @POST("user/getFriendsForPartyInvite")
    fun doGetFriendList(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendListResponse>>

    @POST("user/getallAmiggos_invited_V1")
    fun getallAmiggos_invited(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendListResponse>>

    @POST("user/sendPartyInvite")
    fun doInviteFriend(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


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




    @POST("user/getRequestList_V2")
    fun doCallInvitationApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponseV2>>



//    @POST("user/acceptFriendRequest")
//    fun doAcceptInvitationApi(
//        @Body input: JsonObject,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>>




    @POST("user/acceptFriendRequest_V2")
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


    @POST("user/getBookingList")
    fun doMyBookingApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyBookingResponse>>


    @POST("user/getUserProfile")
    fun doMyProfile(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyProfileResponse>>


    @POST("user/getMyStoryByUserId")
    fun doGetMyStoryByUserId(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OurMemoriesResponse>>


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


    @POST("user/declineFriendRequest_V2")
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




    @POST("user/getOurStoryById")
    fun doCallStorieViewApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<StorieResponse>>



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


    //This api is changed from dashboard_club to dashboard_club_V1 by sumit sir on 2/12/2019
    @POST("user/dashboard_club_V1")
    fun doGetVenueApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<com.tekzee.amiggos.ui.home.model.VenueResponse>>

    @POST("user/getNearByUserCount")
    fun getNearByUserCount(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<NearbyMeCountResponse>>

    @POST("user/updateLatLangUser")
    fun doUploadUserLocation(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>

    @Multipart
    @POST("user/save_photoid")
    fun doCallAttachIdApi(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @Part("dob") date: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>


    @Multipart
    @POST("user/createMyStory")
    fun doUploadFileToServer(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>


    @Multipart
    @POST("user/createOurStory")
    fun doUploadFileOurStoryToServer(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @Part("friend_ids") friend_ids: RequestBody,
        @Part("our_story_id") our_story_id: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>



    @POST("user/updateLatLangUser")

    fun sendNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>

}