package com.tekzee.amiggoss.ui.postmemories

import android.content.Context
import io.reactivex.disposables.Disposable

class PostMemoriesPresenterImplementation(private var mainView: PostMemoriesPresenter.PostMemoriesMainView,context: Context):PostMemoriesPresenter.PostMemoriesMainPresenter {

    var context: Context? = context
    private var disposable: Disposable? =null



    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}