package com.tekzee.amiggos.ui.attachid

import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.mallortaxi.base.BaseMainView
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AttachIdActivityPresenter {

    interface AttachIdPresenterMain{
        fun doCallAttachIdApi(
            file: MultipartBody.Part,
            valueInt: RequestBody,
            date: RequestBody,
            createHeaders: HashMap<String, String?>
        )
        fun OnStop()
    }

    interface AttachIdMainView: BaseMainView{
        fun onAttachIdSuccess(responseData: AttachIdResponse?)
    }
}