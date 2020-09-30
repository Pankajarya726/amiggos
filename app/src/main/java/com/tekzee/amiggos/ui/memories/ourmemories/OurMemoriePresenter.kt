//package com.tekzee.amiggos.ui.memories.ourmemories
//
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.base.BaseMainView
//import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
//
//class OurMemoriePresenter{
//
//    interface OurMemoriePresenterrMain{
//        fun onStop()
//
//        fun callGetOurMemories(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>
//        )
//
//        fun doCallFeaturedProductFromMemory(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>
//        )
//
//    }
//
//    interface OurMemoriePresenterMainView: BaseMainView {
//
//        fun onOurMemorieSuccess(myStoryList: List<MemorieResponse.Data.Memories>)
//        fun onFeaturedBrandSuccess(data: List<MemorieResponse.Data.Memories>)
//        fun onFeaturedBrandFailure(message: String)
//        fun onOurMemorieFailure(message: String)
//
//    }
//}