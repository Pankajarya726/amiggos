package com.tekzee.amiggos.stripe;

import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;

public interface CheckoutPaymentClick {
    void onRowClick(CardListResponse.Data.Card model, int position);
}