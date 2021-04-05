package com.tekzee.amiggos.ui.ourmemories

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.ourmemories.model.GetFriendForInviteAfterCreateMemoryResponse

class InviteFriendPresenter {

    interface InviteFriendPresenterMain{
        fun onStop()
        fun doCallGetFriends(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )

    }

    interface InviteFriendPresenterMainView : BaseMainView {
        fun onOurMemoriesSuccess(responseData: GetFriendForInviteAfterCreateMemoryResponse?)

        fun onOurMemoriesSuccessInfinite(responseData: GetFriendForInviteAfterCreateMemoryResponse?)
        fun onOurMemoriesFailure(message: String)
    }

}