package com.tekzee.amiggos.ui.home.adapter
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
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_list_stories.view.*


class HomeMyStoriesAdapter(val mContext: Context, mRecyclerView: RecyclerView, val mLayoutManager: LinearLayoutManager,
                           mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, var mDataList: ArrayList<StoriesData>?, val mItemClickCallback: HomeItemClick?)
    : RecyclerView.Adapter<HomeMyStoriesAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(mContext, mRecyclerView, mLayoutManager, mRecyclerViewAdapterCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_list_stories, parent, false))
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.infinite_loading_progress_bar_layout, parent, false))
    }

    override fun getItemCount(): Int = mDataList!!.size

    override fun getItemViewType(position: Int): Int = when (mDataList!![position].loadingStatus) {
        false -> holderRow
        else -> {
            holderLoading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.d(mDataList!![position].name)
        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Glide.with(itemView.context).load(mDataList!![adapterPosition].imageUrl).into(itemView.user_image)
            itemView.s_text_name.text = mDataList!![adapterPosition].name

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