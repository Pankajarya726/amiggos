package com.tekzee.amiggos.ui.mylifestylesubcategory

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MyLifestyleSubcategoryPresenterImplementation(
    private var mainView: MyLifestyleSubcategoryPresenter.MyLifestyleSubcategoryPresenterMainView,
    context: Context
) : MyLifestyleSubcategoryPresenter.MyLifestylePresenterSubcategoryMain {
    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if(mainView!=null)
                mainView.hideProgressbar()
        }
    }

    override fun docallMyLifestyleSubcategoryApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
            disposable = ApiClient.instance.docallMyLifestyleSubcategoryApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<MyLifestyleSubcategoryResponse> ->

                    when (response.code()) {
                        200 -> {
                            val responseData: MyLifestyleSubcategoryResponse = response.body()!!
                                if (responseData.status) {
                                    mainView.onMyLifeStyleSubcategorySuccess(responseData)
                                }else
                                {
                                    mainView.onMyLifestyleSubcategoryFailure(responseData.message)
                                }

                        }
                        404 -> {
                                Utility.showLogoutPopup(context!!,"your Session has been expired,please logout")
                        }
                    }
                    mainView.hideProgressbar()
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMyLifestyleSubcategoryFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun docallSaveMyLifestyleSubcategoryApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        if (mainView.checkInternet()) {
            mainView.showProgressbar()
            disposable = ApiClient.instance.docallSaveMyLifestyleSubcategoryApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<CommonResponse> ->

                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse = response.body()!!
                                if (responseData.status) {
                                    mainView.onSaveMyLifeStyleSubcategorySuccess(responseData.message)
                                }else
                                {
                                    mainView.onSaveMyLifestyleSubcategoryFailure(responseData.message)
                                }

                        }
                        404 -> {
                                Utility.showLogoutPopup(context!!,"your Session has been expired,please logout")
                        }
                    }
                    mainView.hideProgressbar()
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMyLifestyleSubcategoryFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}