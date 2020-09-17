package com.tekzee.amiggoss.ui.memories.mymemories

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.memories.mymemories.model.OurMemoriesWithoutProductsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MyMemorieImplementation(
    private var mainView: MyMemoriePresenter.MyMemoriePresenterMainView,
    context: Context
) : MyMemoriePresenter.MyMemoriePresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if (mainView != null)
                mainView.hideProgressbar()
        }
    }


    override fun callGetMyMemories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
     ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallGetMyMemories(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: OurMemoriesWithoutProductsResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onMyMemorieSuccess(responseData.data.ourStory)
                            } else {
                                mainView.onMyMemorieFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMyMemorieFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

}