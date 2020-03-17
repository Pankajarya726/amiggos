package com.tekzee.amiggos.stripe

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.stripe.model.ClientSecretResponse
import com.tekzee.mallortaxi.base.BaseMainView

class CheckOutActivityPresenter  {

    interface CheckOutActivityPresenterMain{
        fun getPaymentIntentClientSecret(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun updatePaymentStatus(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun onStop()
    }

    interface CheckOutActivityPresenterMainView: BaseMainView{
        fun onPaymentIntentclientSuccess(responseData: ClientSecretResponse?)
        fun onPaymentIntentclientFailure(responseData: String)
        fun onUpdatePaymentStatus(responseData: CommonResponse)
        fun onUpdatePaymentFailure(message: String)

    }
}