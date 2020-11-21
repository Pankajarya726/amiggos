package com.tekzee.amiggos.ui.stripepayment.paymentactivity

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.BookingPaymentResponse

class PaymentActivityPresenter {

    interface APaymentMethodPresenterMain{
        fun onStop()

        fun getCardList(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun createBookingPayment(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun setDefaultCard(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface APaymentMethodPresenterMainView: BaseMainView {
        fun onCardListSuccess(
            cards: List<CardListResponse.Data.Card>,
            customerStripId: String
        )
        fun onCardListFailure(
            message: CardListResponse?
        )
        fun onBookingSuccess(message: BookingPaymentResponse?)
        fun onBookingFailure(message: String)
        fun onSetDefaultCardSuccess(message: String)
        fun onSetDefaultCardFailure(message: String)

    }
}