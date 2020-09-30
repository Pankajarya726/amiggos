package com.tekzee.amiggos.ui.blockedusers

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.amiggos.base.BaseMainView

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

    interface ABlockedUserPresenterMainView: BaseMainView {
        fun onblockusersuccess(responseData: List<BlockedUserResponse.Data.BlockedUser>)
        fun onblockuserfailure(responseData: String)
        fun onunblocksuccess(responseData: String)
        fun onunblockfailure(message: String)

    }
}