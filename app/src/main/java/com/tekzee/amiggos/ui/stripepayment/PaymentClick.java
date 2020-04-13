package com.tekzee.amiggos.ui.stripepayment;

import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse;

public interface PaymentClick {
    void onRowClick(CardListResponse.Data.Card model);
    void onDeleteCard(CardListResponse.Data.Card model);
}