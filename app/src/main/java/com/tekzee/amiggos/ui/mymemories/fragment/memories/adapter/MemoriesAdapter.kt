package com.tekzee.amiggos.ui.mymemories.fragment.memories.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.mymemories.fragment.memories.Memories
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_memories.view.*


class MemoriesAdapter(
    val mContext: Context,
    mRecyclerView: RecyclerView,
    val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    var mDataList: ArrayList<StoriesData>,
    val mItemClickCallback: Memories
) : RecyclerView.Adapter<MemoriesAdapter.ViewHolder>() {

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(
            mContext,
            mRecyclerView,
            mLayoutManager,
            mRecyclerViewAdapterCallback
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.single_memories,
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.infinite_loading_progress_bar_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mDataList.size

    override fun getItemViewType(position: Int): Int = when (mDataList[position].loadingStatus) {
        false -> holderRow
        else -> {
            holderLoading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.d(mDataList[position].name)
        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            Glide.with(itemView.context).load(mDataList[adapterPosition].imageUrl)
                .placeholder(R.drawable.noimage).into(itemView.profile_image)
            if (mDataList[adapterPosition].content.size > 0)
                Glide.with(itemView.context).load(mDataList[adapterPosition].content[0].url).placeholder(R.drawable.noimage).into(itemView.maincontraintlayout)
            itemView.txt_name.text = mDataList[adapterPosition].name

            if (adapterPosition != 0) {
                itemView.txt_time.visibility = View.VISIBLE
                if(mDataList[adapterPosition].content.isNotEmpty())
                itemView.txt_time.text = mDataList[adapterPosition].content[0].timeAgo
            } else {
                itemView.txt_time.visibility = View.GONE

            }


            itemView.setOnClickListener {
                mItemClickCallback?.let {
                    mItemClickCallback.itemClickCallback(adapterPosition)
                }
            }
        }
    }

    interface HomeItemClick {
        fun itemClickCallback(position: Int)
    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
    }
}