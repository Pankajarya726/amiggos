package com.tekzee.amiggos.ui.guestlist

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.guestlist.model.GuestListResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GuestListPresenterImplementation(private var mainView: GuestListPresenter.GuestListMainView, context: Context): GuestListPresenter.GuestListMainPresenter {


    private var context: Context? = null
    private var disposable: Disposable? = null






    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }



    override fun doCallGuestListApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallGuestListApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: GuestListResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onGuestListSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }

    }
}