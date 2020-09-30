package com.tekzee.amiggos.ui.mylifestyle

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MyLifestylePresenterImplementation(
    private var mainView: MyLifestylePresenter.MyLifestylePresenterMainView,
    context: Context
) : MyLifestylePresenter.MyLifestylePresenterMain {
    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if(mainView!=null)
                mainView.hideProgressbar()
        }
    }

    override fun docallMyLifestyleApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
            disposable = ApiClient.instance.docallMyLifestyleApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MyLifestyleResponse> ->

                    when (response.code()) {
                        200 -> {
                            val responseData: MyLifestyleResponse = response.body()!!
                                if (responseData.status) {
                                    mainView.onMyLifeStyleSuccess(responseData)
                                }else
                                {
                                    mainView.onMyLifestyleFailure(responseData.message)
                                }

                        }
                        404 -> {
                                Utility.showLogoutPopup(context!!,"your Session has been expired,please logout")
                        }
                    }
                    mainView.hideProgressbar()
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMyLifestyleFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}