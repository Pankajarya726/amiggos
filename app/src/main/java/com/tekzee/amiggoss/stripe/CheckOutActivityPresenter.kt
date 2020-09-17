package com.tekzee.amiggoss.stripe

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggoss.ui.stripepayment.model.CardListResponse
import com.tekzee.mallortaxi.base.BaseMainView

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