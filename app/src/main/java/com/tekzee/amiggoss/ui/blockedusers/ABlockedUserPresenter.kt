package com.tekzee.amiggoss.ui.blockedusers

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ABlockedUserPresenter  {

    interface ABlockedUserPresenterMain{
        fun docallgetblockusers(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun docallunblockusers(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


        fun onStop()
    }

    interface ABlockedUserPresenterMainView: BaseMainView{
        fun onblockusersuccess(responseData: List<BlockedUserResponse.Data.BlockedUser>)
        fun onblockuserfailure(responseData: String)
        fun onunblocksuccess(responseData: String)
        fun onunblockfailure(message: String)

    }
}