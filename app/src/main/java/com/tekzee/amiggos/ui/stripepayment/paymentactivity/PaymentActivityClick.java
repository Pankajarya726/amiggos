package com.tekzee.amiggos.ui.stripepayment.paymentactivity;

import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;

public interface PaymentActivityClick {
    void onRowClick(CardListResponse.Data.Card model, int position);

}