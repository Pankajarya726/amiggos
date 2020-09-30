package com.tekzee.amiggos.ui.bookingqrcode

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.amiggos.base.BaseMainView

class BookingQrCodePresenter {

    interface BookingQrCodeMainPresenter{
        fun onStop()
        fun doGetBookingQrCode(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface BookingQrCodeMainView: BaseMainView {
        fun onGetBookingQrCodeSuccess(responseData: BookinQrCodeResponse?)
    }
}