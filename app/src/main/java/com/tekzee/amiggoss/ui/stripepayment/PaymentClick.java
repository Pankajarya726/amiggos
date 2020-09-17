package com.tekzee.amiggoss.ui.stripepayment;

import com.tekzee.amiggoss.ui.stripepayment.model.CardListResponse;

public interface PaymentClick {
    void onRowClick(CardListResponse.Data.Card model);
    void onDeleteCard(CardListResponse.Data.Card model);
}