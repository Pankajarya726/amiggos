package com.tekzee.amiggoss.ui.guestlist

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.guestlist.model.GuestListResponse
import com.tekzee.mallortaxi.base.BaseMainView

class GuestListPresenter {
    interface GuestListMainPresenter{
        fun onStop()
        fun doCallGuestListApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface GuestListMainView: BaseMainView{

        fun onGuestListSuccess(responseData: GuestListResponse?)

    }
}