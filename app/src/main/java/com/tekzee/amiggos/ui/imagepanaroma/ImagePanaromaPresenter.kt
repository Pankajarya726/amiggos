package com.tekzee.amiggos.ui.imagepanaroma

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ImagePanaromaPresenter {

    interface ImagePanaromaMainPresenter{
        fun callVenueDetailsApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }

    interface ImagePanaromaMainView: BaseMainView{
        fun onVenueDetailsSuccess(responseData: VenueDetailResponse?)
    }
}