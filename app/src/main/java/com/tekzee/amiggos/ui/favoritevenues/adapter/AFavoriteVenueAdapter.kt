package com.tekzee.amiggos.ui.favoritevenues.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.favoritevenues.AFavoriteVenues
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_first_fragment.view.*


class AFavoriteVenueAdapter(
    val mContext: Context, mRecyclerView: RecyclerView, val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, var mDataList: ArrayList<FavoriteVenueResponse.Data.FavoriteVenue>, val mItemClickCallback: AFavoriteVenues
)
    : RecyclerView.Adapter<AFavoriteVenueAdapter.ViewHolder>(){

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
            Glide.with(itemView.context).load(mDataList[adapterPosition].image).placeholder(R.drawable.user).into(itemView.img_user_firstfragment)
            itemView.img_layout.setOnClickListener {
                mItemClickCallback.let {
                    mItemClickCallback.itemClickCallback(adapterPosition)
                }
            }

//            itemView.imageView8.setOnClickListener {
//                val intentActivity = Intent(mContext, MessageActivity::class.java)
//                intentActivity.putExtra(ConstantLib.FRIEND_ID,mDataList[adapterPosition].userid.toString())
//                intentActivity.putExtra(ConstantLib.FRIENDNAME,mDataList[adapterPosition].name)
//                intentActivity.putExtra(ConstantLib.FRIENDIMAGE,mDataList[adapterPosition].profile)
//                mContext.startActivity(intentActivity)
//            }
        }
    }

    interface HomeItemClick {
        fun itemClickCallback(position: Int)
    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
    }
}