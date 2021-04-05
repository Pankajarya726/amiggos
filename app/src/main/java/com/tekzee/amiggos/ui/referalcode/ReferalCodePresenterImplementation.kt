//package com.tekzee.amiggos.ui.referalcode
//
//import android.content.Context
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.referalcode.model.ReferalCodeResponse
//import com.tekzee.amiggos.ui.referalcode.model.VenueResponse
//import com.tekzee.amiggos.network.ApiClient
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//
//class ReferalCodePresenterImplementation(private var mainView: ReferalCodePresenter.ReferalCodeMainView,context: Context) : ReferalCodePresenter.ReferalCodePresenterMain{
//
//    var context: Context? = context
//    private var disposable: Disposable? =null
//
//
//    override fun doCallReferalApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ) {
//        mainView.showProgressbar()
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.doCallReferalApi(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    mainView.hideProgressbar()
//                    val responseCode = response.code()
//                    when (responseCode) {
//                        200 -> {
//                            val responseData: ReferalCodeResponse? = response.body()
//                            if (responseData!!.status) {
//                                mainView.onCallReferalApiSuccess(responseData)
//                            } else {
//                                mainView.validateError(responseData.message)
//                            }
//                        }
//                    }
//                }, { error ->
//                    mainView.hideProgressbar()
//                    mainView.validateError(error.message.toString())
//                })
//        } else {
//            mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }
//
//
//    override fun doCallCheckVenueApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ) {
//        mainView.showProgressbar()
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.doCallCheckVenueApi(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    mainView.hideProgressbar()
//                    when (response.code()) {
//                        200 -> {
//                            val responseData: VenueResponse? = response.body()
//                            if (responseData!!.status) {
//                                mainView.onCheckVenueSuccess(responseData)
//                            } else {
//                                mainView.validateError(responseData.message.toString())
//                            }
//                        }
//                    }
//                }, { error ->
//                    mainView.hideProgressbar()
//                    mainView.validateError(error.message.toString())
//                })
//        } else {
//            mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }
//
//    override fun onStop() {
//        if (disposable != null) {
//            disposable!!.dispose()
//        }
//    }
//}