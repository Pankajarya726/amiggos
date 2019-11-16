package com.tekzee.amiggos.ui.mymemories.fragment.memories

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MemoriesPresenterImplementation(
    private var mainView: MemoriesPresenter.MemoriesMainView,
    context: Context
) : MemoriesPresenter.MemoriesMainPresenter {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun docallMemoriesApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallMemoriesApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: MyMemoriesResponse? = response.body()
                            if (responseData!!.data.size>0) {
                                if(requestDatFromServer){
                                    mainView.onMemoriesInfiniteSuccess(responseData)
                                }else{
                                    mainView.onMemoriesSuccess(responseData)
                                }
                            } else {
                                mainView.onMemoriesFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMemoriesFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}