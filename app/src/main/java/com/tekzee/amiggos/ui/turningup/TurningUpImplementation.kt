//package com.tekzee.amiggos.ui.turningup
//
//import android.content.Context
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
//import com.tekzee.amiggos.network.ApiClient
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//
//class TurningUpImplementation(private var mainView: TurningupPresenter.TurningUpMainView,context: Context): TurningupPresenter.TurningUpMainPreseneter {
//
//
//
//    var context: Context? = context
//    private var disposable: Disposable? =null
//
//
//
//    override fun callTurningUpApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ) {
//        mainView.showProgressbar()
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.callTurningUpApi(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    mainView.hideProgressbar()
//                    val responseCode = response.code()
//                    when (responseCode) {
//                        200 -> {
//                            val responseData: TurningUpResponse? = response.body()
//                            if (responseData!!.status) {
//                                mainView.onTurningUpSuccess(responseData)
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
//    override fun onStop() {
//        if (disposable != null) {
//            disposable!!.dispose()
//        }
//    }
//
//}