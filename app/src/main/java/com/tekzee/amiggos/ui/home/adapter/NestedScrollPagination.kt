package com.tekzee.amiggos.ui.home.adapter

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager

abstract class NestedScrollPagination protected constructor(private val layoutManager: LinearLayoutManager) :
    NestedScrollView.OnScrollChangeListener {


    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if(v!!.getChildAt(v.getChildCount() - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                scrollY > oldScrollY) {

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading() && !isLastPage()) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems()
                    }
                }
            }
        }
    }

    protected abstract fun loadMoreItems()

    public abstract fun getTotalPageCount(): Int

    public abstract fun isLastPage(): Boolean

    public abstract fun isLoading(): Boolean

}