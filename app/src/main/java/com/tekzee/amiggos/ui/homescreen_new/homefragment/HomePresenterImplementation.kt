package com.tekzee.amiggos.ui.homescreen_new.homefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class HomePresenterImplementation(
    private var mainView: HomePresenter.HomeMainView,
    context: Context
) : HomePresenter.HomeMainPresenter {
    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if(mainView!=null)
                mainView.hideProgressbar()
        }
    }

    override fun doCallHomeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        languageData: LanguageData?,
        callFrom: Int
    ) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
            disposable = ApiClient.instance.doCallHomeApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<HomeApiResponse> ->
                    when (response.code()) {
                        200 -> {
                            val responseData: HomeApiResponse = response.body()!!
                                if (responseData.status) {
                                    mainView.onHomeApiSuccess(responseData)
                                }else
                                {
                                    mainView.onHomeApiFailure(responseData.message)
                                }

                        }
                        404 -> {
                                Utility.showLogoutPopup(context!!,"your Session has been expired,please logout")
                        }
                    }
                    mainView.hideProgressbar()
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onHomeApiFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun searchApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        languageData: LanguageData?
    ) {

        if (mainView.checkInternet()) {

            disposable = ApiClient.instance.doCallHomeApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<HomeApiResponse> ->
                    when (response.code()) {
                        200 -> {
                            val responseData: HomeApiResponse = response.body()!!
                                if (responseData.status) {

                                    mainView.onSearchApiSuccess(responseData)
                                }else
                                {
                                    mainView.onHomeApiFailure(responseData.message)
                                }

                        }
                        404 -> {
                                Utility.showLogoutPopup(context!!,"your Session has been expired,please logout")
                        }
                    }

                }, { error ->
                    mainView.onHomeApiFailure(error.message.toString())
                })
        } else {

            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}