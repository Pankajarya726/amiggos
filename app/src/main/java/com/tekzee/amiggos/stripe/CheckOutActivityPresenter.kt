package com.tekzee.amiggos.stripe

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.base.BaseMainView

class CheckOutActivityPresenter {

    interface CheckOutActivityPresenterMain {
//        fun getPaymentIntentClientSecret(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>
//        )

        fun updatePaymentStatus(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun callPaymentByCardId(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun getCardList(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun chargeUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun onStop()
    }

    interface CheckOutActivityPresenterMainView : BaseMainView {
//        fun onPaymentIntentclientSuccess(responseData: ClientSecretResponse?)
//        fun onPaymentIntentclientFailure(responseData: String)
        fun onUpdatePaymentStatus(responseData: CommonResponse)
        fun onUpdatePaymentFailure(message: String)
        fun onCardListSuccess(
            cards: List<CardListResponse.Data.Card>,
            customerStripId: String
        )
        fun onChargeSuccess(responseData: APaymentSuccessResponse?);
        fun onChargeFailure(responseData: String);

    }
}