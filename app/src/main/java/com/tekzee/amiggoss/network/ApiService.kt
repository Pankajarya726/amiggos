package com.tekzee.amiggoss.network


import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggoss.stripe.model.ClientSecretResponse
import com.tekzee.amiggoss.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggoss.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggoss.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.amiggoss.ui.blockedusers.model.UnBlockFriendResponse
import com.tekzee.amiggoss.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.amiggoss.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import com.tekzee.amiggoss.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggoss.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggoss.ui.choosepackage.model.PackageBookResponse
import com.tekzee.amiggoss.ui.choosepackage.model.PackageResponse
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggoss.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.amiggoss.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggoss.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggoss.ui.guestlist.model.GuestListResponse
import com.tekzee.amiggoss.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggoss.ui.home.model.DashboardReponse
import com.tekzee.amiggoss.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggoss.ui.home.model.NearbyMeCountResponse
import com.tekzee.amiggoss.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggoss.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggoss.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggoss.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.amiggoss.ui.login.model.LoginResponse
import com.tekzee.amiggoss.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggoss.ui.memories.mymemories.model.OurMemoriesWithoutProductsResponse
import com.tekzee.amiggoss.ui.memories.ourmemories.model.AMyMemorieResponse
import com.tekzee.amiggoss.ui.memories.ourmemories.model.FeaturedBrandProductResponse
import com.tekzee.amiggoss.ui.memories.venuefragment.model.VenueTaggedResponse
import com.tekzee.amiggoss.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggoss.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggoss.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggoss.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggoss.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggoss.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import com.tekzee.amiggoss.ui.notification.model.NotificationResponse
import com.tekzee.amiggoss.ui.notification.model.StorieResponse
import com.tekzee.amiggoss.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggoss.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggoss.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.amiggoss.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggoss.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.amiggoss.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.amiggoss.ui.realfriends.invitations.model.InvitationResponseV2
import com.tekzee.amiggoss.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.amiggoss.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggoss.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggoss.ui.referalcode.model.VenueResponse
import com.tekzee.amiggoss.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.amiggoss.ui.settings.model.SettingsResponse
import com.tekzee.amiggoss.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggoss.ui.signup.login_new.model.ALoginResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.UserData
import com.tekzee.amiggoss.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggoss.ui.stripepayment.model.DeleteCardResponse
import com.tekzee.amiggoss.ui.turningup.model.TurningUpResponse
import com.tekzee.amiggoss.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    //This api is changed from validateAppVersion to validateAppVersion_V1 by sumit sir on 2/12/2019
    @POST("auth/validateAppVersion_V1")
    fun doValidateAppVersionApi(
        @Body input: JsonObject,
        @HeaderMap headers: HashMap<String, String?>
    ): Observable<Response<ValidateAppVersionResponse>>


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


    @POST("user/getVenueDetails_V2")
    fun callGetVenueDetails(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ClubDetailResponse>>


    @POST("user/create_favoriteVenue_V2")
    fun callLikeUnlikeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/getUserProfile_V2")
    fun callGetProfile(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GetUserProfileResponse>>


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


    @POST("auth/login_V2")
    fun doCallLoginApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ALoginResponse>>


    @POST("auth/forgotPassworduser_V2")
    fun callForgetPasswordApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("auth/get_state")
    fun doCallStateApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<StateResponse>>


    @POST("auth/register_V2")
    fun doCallSignupApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UserData>>


    @POST("auth/get_city")
    fun doCallCityApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CityResponse>>


    @POST("user/unblockFriend_V2")
    fun docallunblockusers(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UnBlockFriendResponse>>


    @POST("user/blockList_V2")
    fun doCallBlockedUser(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BlockedUserResponse>>


    @Multipart
    @POST("user/updateProfile_V2")
    fun doCallUpdateprofile(
        @Part filesMultipart: MultipartBody.Part?,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("date_of_birth") date_of_birth: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("phone_number") phone_number: RequestBody,
        @Part("userid") userid: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateProfileResponse>>


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


    @POST("user/getMyStories_V2")
    fun doCallGetOurMemories(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AMyMemorieResponse>>


    @POST("user/get_tagedVenue_from_memory_V2")
    fun callTaggedVenueApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueTaggedResponse>>


    @POST("user/getBookingList_V2")
    fun docallGetBookings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ABookingResponse>>


    @POST("user/get_featuredProduct_from_memory_V2")
    fun doCallFeaturedProductFromMemory(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FeaturedBrandProductResponse>>


    @POST("user/getOurStories_withoutFeaturedProduct_V2")
    fun docallGetMyMemories(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OurMemoriesWithoutProductsResponse>>


    @POST("user/getAllActiveUserList_V1")
    fun doCallOnlineFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OnlineFriendResponse>>

    @POST("user/getAllNotification_V2")
    fun doCallNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ANotificationResponse>>

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


    @POST("user/freindRealFreind_V2")
    fun doCallRealFriendAmiggosApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>>


    @POST("user/get_favorite_venue")
    fun doCallFavoriteVenueV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>>

    @POST("user/get_Freinffavorite_venue")
    fun doCallFriendsFavoriteVenueV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>>

    @POST("user/dashboard_club_V3")
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

    @POST("user/get_venueType")
    fun doCallVenueTypeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AVenueTypeResponse>>

    @POST("user/get_musicType")
    fun doCallMusicTypeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AMusicTypeResponse>>

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



    @POST("user/creatStripCustomer")
    fun saveCard(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/getCard")
    fun getCardList(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CardListResponse>>


    @POST("user/deleteCard")
    fun deleteCardApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<DeleteCardResponse>>


    @POST("user/getPakageList_V1")
    fun doCallPackageApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<PackageResponse>>

    @POST("user/bookPackage_V2")
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


    @POST("user/userPartyInvites_V2")
    fun doCallBookingInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingInvitationResponse>>


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


    @POST("user/acceptPartyInvitaion_V2")
    fun doAcceptBookingInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/rejectPartyInvitaion_V2")
    fun doRejectBookingInvitationApi(
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


    @POST("user/unblockFriend_V2")
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


    @POST("user/getFreindProfile")
    fun doCallGetFriendProfileApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendProfileResponse>>


    @POST("user/getFreindProfile_V2")
    fun doCallGetFriendProfileApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GetFriendProfileDetailsResponse>>


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


    @POST("user/acceptPartyInvitaion_V2")
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
    ): Observable<Response<com.tekzee.amiggoss.ui.home.model.VenueResponse>>

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

    @POST("user/create_stripepayment")
    fun getPaymentIntentClientSecret(
        @Body apiParamMap: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ClientSecretResponse>>

    @POST("user/stripePayment")
    fun chargeUserApi(
        @Body apiParamMap: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<APaymentSuccessResponse>>

    @POST("user/create_stripeSavedCard")
    fun callPaymentByCardId(
        @Body apiParamMap: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<APaymentSuccessResponse>>

 @POST("user/update_payment_status")
    fun updatePaymentStatus(
        @Body apiParamMap: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>

}