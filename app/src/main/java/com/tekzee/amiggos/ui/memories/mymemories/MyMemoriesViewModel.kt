package com.tekzee.amiggos.ui.memories.mymemories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.MemorieFeaturedBrandRepository
import com.tekzee.amiggos.ui.memories.mymemories.MemorieEvent
import com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand.NewFeaturedBrandDataSource
import com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand.NewMemorieDataSource
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.util.SharedPreference

class MyMemoriesViewModel(
    private val context: Context,
    private val frepository: MemorieFeaturedBrandRepository,
    private val languageConstant: LanguageData,
    private val prefs: SharedPreference
) : ViewModel() {

    var featuredBrandsLiveData: LiveData<PagedList<MemorieResponse.Data.Memories>>
    var memorieLiveData: LiveData<PagedList<MemorieResponse.Data.Memories>>
    var memorieEvent: MemorieEvent? = null

    init {
        val config = PagedList.Config.Builder().setPageSize(5).setPrefetchDistance(5).setEnablePlaceholders(true).build()
        featuredBrandsLiveData = initializePagedListBuilder(config).build()
        memorieLiveData = initializeMemoriePagedListBuilder(config).build()
    }

    fun getFeaturedBrandProducts(): LiveData<PagedList<MemorieResponse.Data.Memories>> =
        featuredBrandsLiveData

    fun getMemorieProducts(): LiveData<PagedList<MemorieResponse.Data.Memories>> = memorieLiveData

    private fun initializePagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, MemorieResponse.Data.Memories> {
        val newFeaturedBrandDataSourceFactory =
            object : DataSource.Factory<Int, MemorieResponse.Data.Memories>() {
                override fun create(): DataSource<Int, MemorieResponse.Data.Memories> {
                    return NewFeaturedBrandDataSource(viewModelScope, frepository, prefs)
                }
            }
        return LivePagedListBuilder<Int, MemorieResponse.Data.Memories>(
            newFeaturedBrandDataSourceFactory,
            config
        )
    }

    private fun initializeMemoriePagedListBuilder(config: PagedList.Config): LivePagedListBuilder<Int, MemorieResponse.Data.Memories> {
        val newMemorieDataSourceFactory =
            object : DataSource.Factory<Int, MemorieResponse.Data.Memories>() {
                override fun create(): DataSource<Int, MemorieResponse.Data.Memories> {
                    return NewMemorieDataSource(viewModelScope, frepository, prefs)
                }
            }
        return LivePagedListBuilder<Int, MemorieResponse.Data.Memories>(
            newMemorieDataSourceFactory,
            config
        )
    }


}
