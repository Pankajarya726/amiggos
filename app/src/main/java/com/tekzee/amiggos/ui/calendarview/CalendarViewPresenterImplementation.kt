package com.tekzee.amiggos.ui.calendarview

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.calendarview.model.TimeSlotResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CalendarViewPresenterImplementation(private var mainView: CalendarViewPresenter.CalendarMainView, context: Context): CalendarViewPresenter.CalendarViewMainPresenter {


    private var context: Context? = null
    private var disposable: Disposable? = null






    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }



    override fun callGetTimeSlot(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.getTimeSlot(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: TimeSlotResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onTimeSlotSuccess(responseData)
                            } else {
                                mainView.onTimeSlotFailure(responseData.message)
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