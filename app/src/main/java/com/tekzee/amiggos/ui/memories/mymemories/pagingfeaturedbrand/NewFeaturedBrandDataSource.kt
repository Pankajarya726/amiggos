package com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.repository.MemorieFeaturedBrandRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.network.FIRST_PAGE
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NewFeaturedBrandDataSource(private val scope:CoroutineScope, private val repository: MemorieFeaturedBrandRepository, private val prefs: SharedPreference) :
    PageKeyedDataSource<Int, MemorieResponse.Data.Memories>() {

    private var page = FIRST_PAGE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MemorieResponse.Data.Memories>
    ) {
        scope.launch {
            try {
                val jsoninput = JsonObject()
                jsoninput.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("page_no",page)


                val response  = repository.doGetFeaturedBrands(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    callback.onResult(response.data.memoriesList,null,page+1)
                }else{
                    callback.onResult(listOf(),null,page+1)
                }
            }catch (e:Exception){
                Log.e("Post data source ","failed to fetch data")
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MemorieResponse.Data.Memories>
    ) {
        scope.launch {
            try {
                val jsoninput = JsonObject()

                jsoninput.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("page_no", params.key)

                val response  = repository.doGetFeaturedBrands(jsoninput,Utility.createHeaders(prefs))



                if(response.status){
                    callback.onResult(response.data.memoriesList,params.key+1)
                }else{
                    callback.onResult(listOf(),params.key+1)
                }
            }catch (e:Exception){
                Log.e("Post data source ","failed to fetch data")
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MemorieResponse.Data.Memories>
    ) {
        TODO("Not yet implemented")
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}