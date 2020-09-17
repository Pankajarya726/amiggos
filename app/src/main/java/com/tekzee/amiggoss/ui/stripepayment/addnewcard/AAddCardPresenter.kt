package com.tekzee.amiggoss.ui.stripepayment.addnewcard

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
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