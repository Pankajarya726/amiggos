package com.tekzee.amiggoss.ui.bookings_new.bookinginvitation

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookingPresenterImplementation(private var mainView: BookingInvitationPresenter.BookingInvitationMainView, context: Context):BookingInvitationPresenter.BookingInvitationMainPresenter {





    private var context: Context? = null
    private var disposable: Disposable? = null

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            mainView.hideProgressbar()
        }
    }

    override fun doCallBookingInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        fragmentVisible: Boolean
    ) {

        if(fragmentVisible)
        mainView.showProgressbar()

        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallBookingInvitationApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: BookingInvitationResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onInvitaionSuccess(responseData)
                            } else {
                                mainView.onInvitationFailure(responseData.message)
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

    override fun doAcceptBookingInvitationApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doAcceptBookingInvitationApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onAcceptInvitation(responseData)
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

    override fun doRejectBookingInvitationApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doRejectBookingInvitationApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onRejectInvitation(responseData)
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