package com.tekzee.amiggoss.stripe;

import com.tekzee.amiggoss.ui.stripepayment.model.CardListResponse;

public interface CheckoutPaymentClick {
    void onRowClick(CardListResponse.Data.Card model, int position);
}