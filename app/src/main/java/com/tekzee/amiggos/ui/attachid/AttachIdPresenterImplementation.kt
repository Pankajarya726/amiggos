package com.tekzee.amiggos.ui.attachid

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.attachid.model.MyIdResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AttachIdPresenterImplementation(private var mainView: AttachIdActivityPresenter.AttachIdMainView,context: Context):AttachIdActivityPresenter.AttachIdPresenterMain {

    private var context: Context? = null
    private var disposable:Disposable? = null


    override fun doCallAttachIdApi(
        file: MultipartBody.Part?,
        userid: RequestBody,
        action: RequestBody,
        flag_save_or_delet: String,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallAttachIdApi(file,userid,action,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: AttachIdResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onAttachIdSuccess(responseData,flag_save_or_delet)
                            } else {
                                mainView.onAttachIdFailure(responseData.message.toString(),flag_save_or_delet)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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



    override fun getMyId(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doGetMyId(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: MyIdResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onMyIdSucess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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

    override fun OnStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}