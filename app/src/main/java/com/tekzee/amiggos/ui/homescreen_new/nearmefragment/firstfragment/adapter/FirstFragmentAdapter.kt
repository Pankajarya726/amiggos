package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_first_fragment.view.*


class FirstFragmentAdapter(
    val mContext: Context, mRecyclerView: RecyclerView, val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, var mDataList: ArrayList<NearByV2Response.Data.NearestFreind>, val mItemClickCallback: HomeItemClick?)
    : RecyclerView.Adapter<FirstFragmentAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(mContext, mRecyclerView, mLayoutManager, mRecyclerViewAdapterCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_first_fragment, parent, false))
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.infinite_loading_progress_bar_layout, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun getItemViewType(position: Int): Int = when (mDataList[position].loadingStatus) {
        false -> holderRow
        else -> {
            holderLoading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Logger.d(mDataList[adapterPosition].profile)
            Glide.with(itemView.context).load(mDataList[adapterPosition].profile).placeholder(R.drawable.noimage).into(itemView.img_user_firstfragment)
//            itemView.txt_name.text = mDataList[adapterPosition].name
//            if(mDataList[adapterPosition].isRelate ==4 && mDataList[adapterPosition].isRelate== 5){
//                itemView.add.visibility = View.VISIBLE
//            }else{
//                itemView.add.visibility = View.GONE
//            }
            if(mDataList[adapterPosition].isMyFriend){
                itemView.is_my_friend.visibility = View.VISIBLE
            }else{
                itemView.is_my_friend.visibility = View.GONE
            }

            itemView.img_layout.setOnClickListener {
                mItemClickCallback?.let {
                    mItemClickCallback.itemClickCallback(adapterPosition,itemView.img_user_firstfragment,mDataList[adapterPosition])
                }
            }
//            itemView.storie.setOnClickListener{
//                mItemClickCallback!!.storieClicked(adapterPosition)
//            }
        }
    }

    interface HomeItemClick {
        fun itemClickCallback(
            position: Int,
            imgUserFirstfragment: ImageView,
            nearestFreind: NearByV2Response.Data.NearestFreind
        )

        fun storieClicked(
            position: Int
        )
    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
    }
}