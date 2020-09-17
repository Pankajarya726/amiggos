package com.tekzee.amiggoss.ui.bookingqrcode

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.mallortaxi.base.BaseMainView

class BookingQrCodePresenter {

    interface BookingQrCodeMainPresenter{
        fun onStop()
        fun doGetBookingQrCode(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface BookingQrCodeMainView: BaseMainView{
        fun onGetBookingQrCodeSuccess(responseData: BookinQrCodeResponse?)
    }
}