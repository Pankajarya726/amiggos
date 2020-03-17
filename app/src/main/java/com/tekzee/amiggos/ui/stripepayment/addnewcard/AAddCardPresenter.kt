package com.tekzee.amiggos.ui.stripepayment.addnewcard

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.mallortaxi.base.BaseMainView

class AAddCardPresenter {

    interface AAddCardPresenterMain{
        fun onStop()

        fun getCardList(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun saveCard(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AAddCardPresenterMainView: BaseMainView{
        fun onChooseWeekSuccess(responseData: ChooseWeekResponse?)
        fun onSaveCardSuccess(responseData: CommonResponse?)
        fun onSaveCardFailure(message: String)
    }
}