package com.tekzee.amiggos.ui.stripepayment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.base.BaseMainView

class APaymentMethodPresenter {

    interface APaymentMethodPresenterMain{
        fun onStop()

        fun getCardList(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun deleteCardApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface APaymentMethodPresenterMainView: BaseMainView {
        fun onCardListSuccess(
            cards: List<CardListResponse.Data.Card>,
            customerStripId: String
        )
        fun onCardDeleteSuccess(message: String)

    }
}