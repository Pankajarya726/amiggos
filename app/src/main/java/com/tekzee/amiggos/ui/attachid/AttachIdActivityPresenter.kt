package com.tekzee.amiggos.ui.attachid

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.attachid.model.MyIdResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AttachIdActivityPresenter {

    interface AttachIdPresenterMain{
        fun doCallAttachIdApi(
            file: MultipartBody.Part?,
            valueInt: RequestBody,
            date: RequestBody,
            flag_save_or_delet: String,
            createHeaders: HashMap<String, String?>
        )

        fun getMyId(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun OnStop()
    }

    interface AttachIdMainView: BaseMainView {
        fun onAttachIdSuccess(responseData: AttachIdResponse?, flag_save_or_delet: String)
        fun onMyIdSucess(responseData: MyIdResponse?)
        fun onAttachIdFailure(toString: String, flag_save_or_delet: String)
    }
}