//package com.tekzee.amiggos.ui.memories.ourmemories
//
//import android.content.Context
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.network.ApiClient
//import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//
//class OurMemoriePresenterImplementation(
//    private var mainView: OurMemoriePresenter.OurMemoriePresenterMainView,
//    context: Context
//) : OurMemoriePresenter.OurMemoriePresenterrMain {
//
//
//    var context: Context? = context
//    private var disposable: Disposable? = null
//
//
//    override fun onStop() {
//        if (disposable != null) {
//            disposable!!.dispose()
//            if (mainView != null)
//                mainView.hideProgressbar()
//        }
//    }
//
//
//    override fun callGetOurMemories(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//     ) {
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.doCallGetOurMemories(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                   // mainView.hideProgressbar()
//                    when (response.code()) {
//                        200 -> {
//                            val responseData: MemorieResponse? = response.body()
//                            if (responseData!!.status) {
//                                if(responseData.data.memoriesList.isNotEmpty()){
//                                    mainView.onOurMemorieSuccess(responseData.data.memoriesList)
//                                }else{
//                                    mainView.onOurMemorieFailure(responseData.message)
//                                }
//
//                            } else {
//                                mainView.onOurMemorieFailure(responseData.message)
//                            }
//                        }
//                    }
//                }, { error ->
//                    //                    mainView.hideProgressbar()
//                    mainView.onOurMemorieFailure(error.message.toString())
//                })
//        } else {
//            //mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }
//
//
//    override fun doCallFeaturedProductFromMemory(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//     ) {
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.doCallFeaturedProductFromMemory(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                   // mainView.hideProgressbar()
//                    when (response.code()) {
//                        200 -> {
//                            val responseData: MemorieResponse? = response.body()
//                            if (responseData!!.status) {
//                                if(responseData.data.memoriesList.isNotEmpty()){
//                                    mainView.onFeaturedBrandSuccess(responseData.data.memoriesList)
//                                }else{
//                                    mainView.onFeaturedBrandFailure(responseData.message)
//                                }
//
//                            } else {
//                                mainView.onFeaturedBrandFailure(responseData.message)
//                            }
//                        }
//                    }
//                }, { error ->
//                    //                    mainView.hideProgressbar()
//                    mainView.onFeaturedBrandFailure(error.message.toString())
//                })
//        } else {
//            //mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }
//
//}