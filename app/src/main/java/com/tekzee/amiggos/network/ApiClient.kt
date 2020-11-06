package com.tekzee.amiggos.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger.addLogAdapter
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
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import com.tekzee.amiggos.ui.homescreen_new.model.BadgeCountResponse
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
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.ui.stripepayment.model.DeleteCardResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.BookingPaymentResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.SetDefaultCardResponse
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class ApiClient {

    private val apiService: ApiService

    init {
        val gson = GsonBuilder().setLenient().create()
        val clientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
            addLogAdapter(AndroidLogAdapter())
        }

        val okHttpClient: OkHttpClient = clientBuilder
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build();


        val retrofit = Retrofit.Builder().baseUrl(ConstantLib.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        apiService = retrofit.create(ApiService::class.java)

    }

    companion object {
        //        private val BASE_URL = "http://amiggos.com/Amiggos_tek/api/"
//        private val BASE_URL = "http://dev.tekzee.in/Amiggos_new/api/"

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
        return apiService.doCallSettingsApi(input, createHeaders)
    }

    fun doCallLoginApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ALoginResponse>> {
        return apiService.doCallLoginApi(input, createHeaders)
    }

    fun callForgetPasswordApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callForgetPasswordApi(input, createHeaders)
    }


    fun doCallStateApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<StateResponse>> {
        return apiService.doCallStateApi(input, createHeaders)
    }


    fun doCallSignupApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UserData>> {
        return apiService.doCallSignupApi(input, createHeaders)
    }

    fun doUpdateUserInformation(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UserData>> {
        return apiService.doUpdateUserInformation(input, createHeaders)
    }

    fun doCallCityApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CityResponse>> {
        return apiService.doCallCityApi(input, createHeaders)
    }

    fun docallunblockusers(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UnBlockFriendResponse>> {
        return apiService.docallunblockusers(input, createHeaders)
    }

    fun doCallBlockedUser(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BlockedUserResponse>> {
        return apiService.doCallBlockedUser(input, createHeaders)
    }

    fun doCallUpdateProfileApi(
        fileMultipartBody: MultipartBody.Part?,
        firstnameRequestBody: RequestBody,
        lastnameRequestBody: RequestBody,
        dobRequestBody: RequestBody,
        cityIdRequestBody: RequestBody,
        stateIdRequestBody: RequestBody,
        phonenumberRequestBody: RequestBody,
        useridRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateProfileResponse>> {
        return apiService.doCallUpdateprofile(
            fileMultipartBody,
            firstnameRequestBody,
            lastnameRequestBody,
            dobRequestBody,
            cityIdRequestBody,
            stateIdRequestBody,
            phonenumberRequestBody,
            useridRequestBody,
            createHeaders
        )
    }


    fun doUpdateImage(
        fileMultipartBody: Array<MultipartBody.Part?>,
        useridRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doUpdateImage(
            fileMultipartBody,
            useridRequestBody,
            createHeaders
        )
    }

    fun doUpdateSingleImage(
        fileMultipartBody: MultipartBody.Part?,
        useridRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doUpdateSingleImage(
            fileMultipartBody,
            useridRequestBody,
            createHeaders
        )
    }


    private fun getFilesMultipart(params: HashMap<String?, File?>?): MultipartBody.Part? {
        val requestFile =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), params?.get("image")!!)
        return MultipartBody.Part.createFormData(
            "image",
            "image_" + System.currentTimeMillis() + ".jpg",
            requestFile
        )
    }


    fun docallViewFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<StorieViewResponse>> {
        return apiService.docallViewFriendApi(input, createHeaders)
    }

    fun doCallHelpCenterApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<HelpCenterResponse>> {
        return apiService.doCallHelpCenterApi(input, createHeaders)
    }

    fun doUpdateSettings(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<UpdateSettingsResponse>> {
        return apiService.doUpdateSettings(input, createHeaders)
    }

    fun doLoginApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>> {
        return apiService.doLoginApi(input, createHeaders)
    }

    fun doUpdateFirebaseApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<LoginResponse>> {
        return apiService.doUpdateFirebaseApi(input, createHeaders)
    }

    fun callTurningUpApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<TurningUpResponse>> {
        return apiService.callTurningUpApi(input, createHeaders)
    }

    fun callGetVenueDetails(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueDetails>> {
        return apiService.callGetVenueDetails(input, createHeaders)
    }

    fun callLikeUnlikeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callLikeUnlikeApi(input, createHeaders)
    }

    fun callGetProfile(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<GetUserProfileResponse>> {
        return apiService.callGetProfile(input, createHeaders)
    }
    fun deletePhotoApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.deletePhotoApi(input, createHeaders)
    }

    fun updateProfileImage(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.updateProfileImage(input, createHeaders)
    }

    fun doCallDeleteStorie(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doCallDeleteStorie(input, createHeaders)
    }

    fun doAcceptOurStoryInvite(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doAcceptOurStoryInvite(input, createHeaders)
    }

    fun callVenueDetailsApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueDetailResponse>> {
        return apiService.callVenueDetailsApi(input, createHeaders)
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
    fun doCallNotification(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ANotificationResponse>> {
        return apiService.doCallNotification(input, createHeaders)
    }

    fun getNearByUser(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<SearchFriendResponse>> {
        return apiService.getNearByUser(input, createHeaders)
    }

    fun getNearByUserv2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<NearByV2Response>> {
        return apiService.getNearByUserv2(input, createHeaders)
    }

    fun doCallRealFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendResponse>> {
        return apiService.doCallRealFriendApi(input, createHeaders)
    }

    fun doCallRealFriendApiV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>> {
        return apiService.doCallRealFriendApiV2(input, createHeaders)
    }

    fun doCallRealFriendAmiggosApiV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<RealFriendV2Response>> {
        return apiService.doCallRealFriendAmiggosApiV2(input, createHeaders)
    }


    fun doCallFavoriteVenueV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>> {
        return apiService.doCallFavoriteVenueV2(input, createHeaders)
    }


    fun doCallFriendsFavoriteVenueV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<FavoriteVenueResponse>> {
        return apiService.doCallFriendsFavoriteVenueV2(input, createHeaders)
    }


    fun doCallHomeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<HomeResponse>> {
        return apiService.docallHomeApi(input, createHeaders)
    }
    fun doCallBadgeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BadgeCountResponse>> {
        return apiService.doCallBadgeApi(input, createHeaders)
    }

    fun docallMyLifestyleApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyLifestyleResponse>> {
        return apiService.docallMyLifestyleApi(input, createHeaders)
    }

    fun docallMyLifestyleSubcategoryApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyLifestyleSubcategoryResponse>> {
        return apiService.docallMyLifestyleSubcategoryApi(input, createHeaders)
    }

    fun docallSaveMyLifestyleSubcategoryApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.docallSaveMyLifestyleSubcategoryApi(input, createHeaders)
    }


    fun doCallOurMemoriesApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<OurMemoriesResponse>> {
        return apiService.doCallOurMemoriesApi(input, createHeaders)
    }

    fun doCallOurMemoriesUploadApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<OurFriendListResponse>> {
        return apiService.doCallOurMemoriesUploadApi(input, createHeaders)
    }
    fun doCallVenueTypeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<AVenueTypeResponse>> {
        return apiService.doCallVenueTypeApi(input, createHeaders)
    }

    fun doCallMusicTypeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<AMusicTypeResponse>> {
        return apiService.doCallMusicTypeApi(input, createHeaders)
    }

    fun doCallNearBy(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<OurFriendListResponse>> {
        return apiService.doCallNearBy(input, createHeaders)
    }

    fun docallMemoriesApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyMemoriesResponse>> {
        return apiService.docallMemoriesApi(input, createHeaders)
    }
//
//    fun doGetMyStories(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<GetMyStoriesResponse>> {
//        return apiService.doGetMyStories(input, createHeaders)
//    }

    fun doCallGetOurMemories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>> {
        return apiService.doCallGetOurMemories(input, createHeaders)
    }


    fun callTaggedVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<VenueTaggedResponse>> {
        return apiService.callTaggedVenueApi(input, createHeaders)
    }


    fun docallGetBookings(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ABookingResponse>> {
        return apiService.docallGetBookings(input, createHeaders)
    }

    fun doCallFeaturedProductFromMemory(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>> {
        return apiService.doCallFeaturedProductFromMemory(input, createHeaders)
    }

    fun docallGetOurMyMemories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MemorieResponse>> {
        return apiService.docallGetOurMyMemories(input, createHeaders)
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

    fun doGetBookingQrCode(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BookinQrCodeResponse>> {
        return apiService.doGetBookingQrCode(input, createHeaders)
    }

    fun doCallWeekApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ChooseWeekResponse>> {
        return apiService.doCallWeekApi(input, createHeaders)
    }

    fun saveCard(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.saveCard(input, createHeaders)
    }


    fun getCardList(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CardListResponse>> {
        return apiService.getCardList(input, createHeaders)
    }

    fun deleteCardApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<DeleteCardResponse>> {
        return apiService.deleteCardApi(input, createHeaders)
    }
    fun createBookingPayment(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingPaymentResponse>> {
        return apiService.createBookingPayment(input, createHeaders)
    }
    fun setDefaultCard(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<SetDefaultCardResponse>> {
        return apiService.setDefaultCard(input, createHeaders)
    }

    fun doCallPackageApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PackageResponse>> {
        return apiService.doCallPackageApi(input, createHeaders)
    }

    fun doBookPackage(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<PackageBookResponse>> {
        return apiService.doBookPackage(input, createHeaders)
    }



    fun getallAmiggos_invited(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendListResponse>> {
        return apiService.getallAmiggos_invited(input, createHeaders)
    }

    fun dogetBookingDetails(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingDetailsNewResponse>> {
        return apiService.dogetBookingDetails(input, createHeaders)
    }

    fun doInviteFriend(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doInviteFriend(input, createHeaders)
    }

    fun doCallGetFriends(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<InviteFriendResponse>> {
        return apiService.doCallGetFriends(input, createHeaders)
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
    ): Observable<Response<BookingInvitationResponse>> {
        return apiService.doCallPartyInviteApi(input, createHeaders)
    }


    fun doCallInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponse>> {
        return apiService.doCallInvitationApi(input, createHeaders)
    }


    fun doCallInvitationApiV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<InvitationResponseV2>> {
        return apiService.doCallInvitationApiV2(input, createHeaders)
    }


    fun doCallBookingInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<BookingInvitationResponse>> {
        return apiService.doCallBookingInvitationApi(input, createHeaders)
    }


    fun doAcceptInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doAcceptInvitationApi(input, createHeaders)
    }

    fun doAcceptBookingInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doAcceptBookingInvitationApi(input, createHeaders)
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

    fun doGetMyStoryByUserId(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<OurMemoriesResponse>> {
        return apiService.doGetMyStoryByUserId(input, createHeaders)
    }


    fun callBlock(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.callBlock(input, createHeaders)
    }



    fun doRejectInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doRejectInvitationApi(input, createHeaders)
    }

    fun doRejectBookingInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doRejectBookingInvitationApi(input, createHeaders)
    }


    fun doCallGuestListApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<GuestListResponse>> {
        return apiService.doCallGuestListApi(input, createHeaders)
    }


    fun getTimeSlot(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<TimeSlotResponse>> {
        return apiService.getTimeSlot(input, createHeaders)
    }

    fun doCallGetFriendProfileApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<FriendProfileResponse>> {
        return apiService.doCallGetFriendProfileApi(input, createHeaders)
    }

    fun doCallGetFriendProfileApiV2(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<GetFriendProfileDetailsResponse>> {
        return apiService.doCallGetFriendProfileApiV2(input, createHeaders)
    }

    fun doCallGetNotification(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<NotificationResponse>> {
        return apiService.doCallGetNotification(input, createHeaders)
    }


    fun doCallStorieViewApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<StorieResponse>> {
        return apiService.doCallStorieViewApi(input, createHeaders)
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

    fun doGetMyId(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<MyIdResponse>> {
        return apiService.doGetMyId(input, createHeaders)
    }

    fun getNearByUserCount(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<NearbyMeCountResponse>> {
        return apiService.getNearByUserCount(input, createHeaders)
    }

    fun doCallAttachIdApi(
        file: MultipartBody.Part?,
        userid: RequestBody,
        action: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>> {
        return apiService.doCallAttachIdApi(file, userid, action, createHeaders)
    }

    fun UploadFileToServer(
        file: MultipartBody.Part?,
        valueInt: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<AttachIdResponse>> {
        return apiService.doUploadFileToServer(file, valueInt, createHeaders)
    }

//
//    fun doUploadFileOurStoryToServer(
//        file: MultipartBody.Part?,
//        valueInt: RequestBody,
//        friend_ids: RequestBody,
//        our_story_id: RequestBody,
//        createHeaders: HashMap<String, String?>
//    ): Observable<Response<AttachIdResponse>> {
//        return apiService.doUploadFileOurStoryToServer(
//            file,
//            valueInt,
//            friend_ids,
//            our_story_id,
//            createHeaders
//        )
//    }


    fun doUploadUserLocation(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.doUploadUserLocation(input, createHeaders)
    }



    fun getPaymentIntentClientSecret(
        apiParamMap: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<ClientSecretResponse>> {
        return apiService.getPaymentIntentClientSecret(apiParamMap,createHeaders)
    }
    fun chargeUserApi(
        apiParamMap: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<APaymentSuccessResponse>> {
        return apiService.chargeUserApi(apiParamMap,createHeaders)
    }

    fun callPaymentByCardId(
        apiParamMap: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<APaymentSuccessResponse>> {
        return apiService.callPaymentByCardId(apiParamMap,createHeaders)
    }


    fun updatePaymentStatus(
        apiParamMap: JsonObject,
        createHeaders: HashMap<String, String?>
    ): Observable<Response<CommonResponse>> {
        return apiService.updatePaymentStatus(apiParamMap,createHeaders)
    }


}