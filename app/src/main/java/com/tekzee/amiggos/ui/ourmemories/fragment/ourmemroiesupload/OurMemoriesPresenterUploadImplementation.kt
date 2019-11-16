package com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OurMemoriesPresenterUploadImplementation(
    private var mainView: OurMemoriesUploadPresenter.OurMemoriesUploadMainView,
    context: Context
) : OurMemoriesUploadPresenter.OurMemoriesUploadMainPresenter {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doCallOurMemoriesApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallOurMemoriesUploadApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: OurFriendListResponse? = response.body()
                            if (responseData!!.data.isNotEmpty()) {
                                if(requestDatFromServer){
                                    mainView.onOurMemoriesInfiniteSuccess(responseData)
                                }else{
                                    mainView.onOurMemoriesSuccess(responseData)
                                }
                            } else {
                                mainView.onOurMemoriesFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onOurMemoriesFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}