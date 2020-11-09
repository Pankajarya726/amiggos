package com.tekzee.amiggos.ui.memories.mymemoriesold

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class OurMemorieFragmentImplementation(
    private var mainView: OurMemorieFragmentPresenter.MyMemoriePresenterMainView,
    context: Context
) : OurMemorieFragmentPresenter.MyMemoriePresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if (mainView != null)
                mainView.hideProgressbar()
        }
    }


    override fun callGetOuryMemories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
     ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallGetOurMyMemories(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: MemorieResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onMyMemorieSuccess(responseData.data.memoriesList)
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