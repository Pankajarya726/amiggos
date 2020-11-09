package com.tekzee.amiggos.ui.invitefriendnew

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.invitefriendnew.model.GetUserForInviteResponse

class InviteFriendNewPresenter {

    interface InviteFriendPresenterMain{
        fun onStop()
        fun doCallGetFriends(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )

        fun doCallInviteFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>

        )

    }

    interface InviteFriendPresenterMainView : BaseMainView {
        fun onOurMemoriesSuccess(responseData: GetUserForInviteResponse?)
        fun onOurMemoriesSuccessInfinite(responseData: GetUserForInviteResponse?)
        fun onOurMemoriesFailure(message: String)
        fun onFriendInviteSuccess(message: String)
        fun onFriendInviteFailure(message: String)
    }

}