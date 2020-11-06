package com.tekzee.amiggos.network


import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggos.stripe.model.ClientSecretResponse
import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.attachid.model.MyIdResponse
import com.tekzee.amiggos.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.amiggos.ui.blockedusers.model.UnBlockFriendResponse
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import com.tekzee.amiggos.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggos.ui.calendarview.model.TimeSlotResponse
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageBookResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageResponse
import com.tekzee.amiggos.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggos.ui.guestlist.model.GuestListResponse
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.NearbyMeCountResponse
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.memories.venuefragment.model.VenueTaggedResponse
import com.tekzee.amiggos.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggos.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import com.tekzee.amiggos.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.amiggos.ui.ourmemories.model.InviteFriendResponse
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponseV2
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.ui.signup.login_new.model.ALoginResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.ui.stripepayment.model.DeleteCardResponse
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import com.tekzee.amiggos.util.NetworkConnectionInterceptor
import com.tekzee.amiggos.ui.addusers.model.AddUserResponse
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import com.tekzee.amiggos.ui.homescreen_new.model.BadgeCountResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.BookingPaymentResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.SetDefaultCardResponse
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

const val FIRST_PAGE = 0

interface ApiService {


    @POST("guest/getMenu")
    suspend fun doCallMenuApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<MenuResponse>


    @POST("partner/user_status")
    suspend fun doChangeUserStaus(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CommonResponse>


    @POST("partner/staff_detail")
    suspend fun doStaffByIdApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CommonMenuResponse>


    @POST("guest/save_booking")
    suspend fun createBooking(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CreateBookingResponse>


    @POST("guest/get_featured_product")
    suspend fun newdoGetFeaturedBrands(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<MemorieResponse>


    @POST("guest/getMyStories_v1")
    suspend fun doGetMemorie(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<MemorieResponse>


    @POST("guest/userFreindsList_chat")
    suspend fun doAddUserApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<AddUserResponse>

    @POST("partner/approved_tagged_memory")
    suspend fun docallAcceptDeclineApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CommonResponse>


    @POST("delete_memory")
    suspend fun docallDeleteApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CommonResponse>


    @POST("guest/tagging_search_customer")
    suspend fun doGetTaggingApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<TaggingResponse>

    @Multipart
    @POST("guest/create_memory")
    fun doCreateMemorieApi(
        @Part file: MultipartBody.Part?,
        @Part("userid") userid: RequestBody,
        @Part("type") type: RequestBody,
        @Part("venue_id") venue_id: RequestBody,
        @Part("tagged_array") taggedArray: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Single<ResponseBody>


//    userid,story,freind_ids,our_story_id,tagged_array
//    @Multipart
//    @POST("guest/createOurStory")
//    fun doCreateOurMemorieApi(
//    @Part file: MultipartBody.Part?,
//    @Part("userid") userid: RequestBody,
//    @Part("freind_ids") friendIds:RequestBody,
//    @Part("our_story_id") our_story_id: RequestBody: RequestBody,
//    @Part("tagged_array") taggedArray: HashMap<String, String?>
//    ): Single<ResponseBody>


    @Multipart
    @POST("guest/createOurStory")
    fun doCreateOurMemorieApi(
        @Part file: MultipartBody.Part,
        @Part("userid") userid: RequestBody,
        @Part("freind_ids") friendIds: RequestBody,
        @Part("our_story_id") our_story_id: RequestBody,
        @Part("tagged_array") taggedArray: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Single<ResponseBody>


//
//    @Multipart
//    @POST("guest/createOurStory")
//    fun doUploadFileOurStoryToServer(
//        @Part file: MultipartBody.Part?,
//        @Part("userid") valueInt: RequestBody,
//        @Part("friend_ids") friend_ids: RequestBody,
//        @Part("our_story_id") our_story_id: RequestBody,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>>


    //==========================================================================================
    //This api is changed from validateAppVersion to validateAppVersion_V1 by sumit sir on 2/12/2019
    @POST("guest/validateAppVersion")
    fun doValidateAppVersionApi(
        @Body input: JsonObject,
        @HeaderMap headers: HashMap<String, String?>
    ): Observable<Response<ValidateAppVersionResponse>>


    @GET("guest/getLanguageConstant")
    fun doLanguageConstantApi(@HeaderMap headers: HashMap<String, String?>): Observable<Response<JsonObject>>


    @GET("auth/language")
    fun doCallLanguageApi(
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LanguageResponse>>


    @POST("guest/login")
    fun doLoginApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>>


    @POST("guest/updateFirebaseId")
    fun doUpdateFirebaseApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>>


    @POST("user/turningUp")
    fun callTurningUpApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<TurningUpResponse>>


    @POST("guest/getVenueDetails")
    fun callGetVenueDetails(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueDetails>>


    @POST("guest/create_favoriteVenue")
    fun callLikeUnlikeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/getProfile_v1")
    fun callGetProfile(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GetUserProfileResponse>>


    @POST("guest/removeOtherImage")
    fun deletePhotoApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/set_defaultprofile")
    fun updateProfileImage(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


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


    @POST("guest/getSettingDetails")
    fun doCallSettingsApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<SettingsResponse>>


    @POST("guest/login")
    fun doCallLoginApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ALoginResponse>>


    @POST("guest/forgotPassword")
    fun callForgetPasswordApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("get_state")
    fun doCallStateApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<StateResponse>>


    @POST("guest/register")
    fun doCallSignupApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UserData>>


    @POST("guest/updateProfile")
    fun doUpdateUserInformation(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UserData>>


    @POST("get_city")
    fun doCallCityApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CityResponse>>


    @POST("guest/unblockFriend")
    fun docallunblockusers(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<UnBlockFriendResponse>>


    @POST("guest/blockList")
    fun doCallBlockedUser(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BlockedUserResponse>>


    @Multipart
    @POST("guest/updateProfile")
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


    @Multipart
    @POST("guest/addImage")
    fun doUpdateImage(
        @Part filesMultipart: Array<MultipartBody.Part?>,
        @Part("userid") userid: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>

    @Multipart
    @POST("guest/addImage")
    fun doUpdateSingleImage(
        @Part filesMultipart: MultipartBody.Part?,
        @Part("userid") userid: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


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


    @POST("guest/updateSettingValue")
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

//    @POST("user/getMyStories")
//    fun doGetMyStories(
//        @Body input: JsonObject,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<GetMyStoriesResponse>>


    @POST("guest/getMyStories_v1")
    fun doCallGetOurMemories(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>>


    @POST("guest/get_tagedVenue_from_memory")
    fun callTaggedVenueApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueTaggedResponse>>


    @POST("guest/getbookings")
    fun docallGetBookings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ABookingResponse>>


    @POST("guest/get_featured_product")
    fun doCallFeaturedProductFromMemory(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>>


    @POST("guest/getOurStories_v1")
    fun docallGetOurMyMemories(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>>


    @POST("user/getAllActiveUserList_V1")
    fun doCallOnlineFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<OnlineFriendResponse>>

    @POST("guest/getAllNotification")
    fun doCallNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<ANotificationResponse>>

    @POST("user/getNearByUser")
    fun getNearByUser(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<SearchFriendResponse>>

    @POST("guest/getNearByUser")
    fun getNearByUserv2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<NearByV2Response>>

    @POST("user/userFreindsList")
    fun doCallRealFriendApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendResponse>>


    @POST("guest/userFreindsList")
    fun doCallRealFriendApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>>


    @POST("guest/freindRealFreind")
    fun doCallRealFriendAmiggosApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>>


    @POST("user/get_favorite_venue")
    fun doCallFavoriteVenueV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>>

    @POST("guest/get_Freinffavorite_venue")
    fun doCallFriendsFavoriteVenueV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>>

    @POST("guest/dashboard")
    fun docallHomeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<HomeResponse>>

    @POST("guest/batchcount_customer")
    fun doCallBadgeApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BadgeCountResponse>>

    @POST("guest/mylifestyle_v1")
    fun docallMyLifestyleApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyLifestyleResponse>>


    @POST("guest/mylifestyleCategory")
    fun docallMyLifestyleSubcategoryApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyLifestyleSubcategoryResponse>>

    @POST("guest/setMylifestyle")
    fun docallSaveMyLifestyleSubcategoryApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


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


    @POST("payment/creatStripCustomer")
    fun saveCard(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("payment/getCard")
    fun getCardList(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CardListResponse>>


    @POST("payment/deleteCard")
    fun deleteCardApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<DeleteCardResponse>>

    @POST("guest/createBookingPayment")
    fun createBookingPayment(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingPaymentResponse>>


    @POST("payment/updateCard")
    fun setDefaultCard(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<SetDefaultCardResponse>>


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

     @POST("user/getallAmiggos_invited_V1")
    fun getallAmiggos_invited(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendListResponse>>

    @POST("guest/getBookingDetails")
    fun dogetBookingDetails(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingDetailsNewResponse>>

    @POST("guest/sendPartyInvite")
    fun doInviteFriend(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>




    @POST("guest/getFriendsForPartyInvite")
    fun doCallGetFriends(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<InviteFriendResponse>>


    @POST("user/getSetting")
    fun doCallGetSettings(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyPreferenceResponse>>


    @POST("user/userPartyInvites")
    fun doCallPartyInviteApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingInvitationResponse>>


    @POST("user/getRequestList")
    fun doCallInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponse>>


    @POST("guest/getRequestList")
    fun doCallInvitationApiV2(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponseV2>>


    @POST("guest/userPartyInvites")
    fun doCallBookingInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingInvitationResponse>>


//    @POST("user/acceptFriendRequest")
//    fun doAcceptInvitationApi(
//        @Body input: JsonObject,
//        @HeaderMap createHeaders: HashMap<String, String?>
//    ): Observable<Response<CommonResponse>>


    @POST("guest/acceptFriendRequest")
    fun doAcceptInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/acceptInvitation")
    fun doAcceptBookingInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/rejectInvitation")
    fun doRejectBookingInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/sendFriendRequest")
    fun doSendFriendRequest(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/unFriendUser")
    fun callunFriend(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/unblockFriend")
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


    @POST("guest/blockFriend")
    fun callBlock(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/unFriendUser")
    fun callReport(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/declineFriendRequest")
    fun doRejectInvitationApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("user/getGuestList")
    fun doCallGuestListApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<GuestListResponse>>



    @POST("guest/get_timeSlot")
    fun getTimeSlot(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<TimeSlotResponse>>


    @POST("user/getFreindProfile")
    fun doCallGetFriendProfileApi(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendProfileResponse>>


    @POST("guest/getFriendsProfile_v1")
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


    @POST("guest/acceptInvitation")
    fun doCallJoinPartyInvites(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>


    @POST("guest/rejectInvitation")
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

    @POST("guest/get_photoid")
    fun doGetMyId(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<MyIdResponse>>

    @POST("user/getNearByUserCount")
    fun getNearByUserCount(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<NearbyMeCountResponse>>

    @POST("guest/updateLatLangUser")
    fun doUploadUserLocation(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>>

    @Multipart
    @POST("guest/my_photoid")
    fun doCallAttachIdApi(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @Part("action_type") date: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>


    @Multipart
    @POST("user/createMyStory")
    fun doUploadFileToServer(
        @Part file: MultipartBody.Part?,
        @Part("userid") valueInt: RequestBody,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>>


    @POST("partner/chat_setting")
    suspend fun sendNotification(
        @Body input: JsonObject,
        @HeaderMap createHeaders: HashMap<String, String?>
    ): Response<CommonResponse>


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


    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ApiService {

            val gson = GsonBuilder().setLenient().create()
            val clientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(networkConnectionInterceptor)
                    .addInterceptor(loggingInterceptor)
            }

            val okHttpClient: OkHttpClient = clientBuilder
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build()

            return Retrofit.Builder().baseUrl(ConstantLib.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }
}