package com.tekzee.amiggos.ui.mylifestylesubcategory

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse

class MyLifestyleSubcategoryPresenter {
    interface MyLifestylePresenterSubcategoryMain {
        fun onStop()
        fun docallMyLifestyleSubcategoryApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun docallSaveMyLifestyleSubcategoryApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyLifestyleSubcategoryPresenterMainView : BaseMainView {
        fun onMyLifeStyleSubcategorySuccess(responseData: MyLifestyleSubcategoryResponse)
        fun onMyLifestyleSubcategoryFailure(message: String)
        fun onSaveMyLifestyleSubcategoryFailure(message: String)
        fun onSaveMyLifeStyleSubcategorySuccess(message: String)
    }
}