package com.tekzee.amiggos.ui.invitefriendnew.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.invitefriendnew.InviteFriendNewActivity
import com.tekzee.amiggos.ui.ourmemories.model.InviteFriendResponse
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_invite_friend.view.*


class InviteFriendBookingAdapter(
    val mContext: Context,
    mRecyclerView: RecyclerView,
    val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    var mDataList: ArrayList<InviteFriendResponse.Data.RealFreind>,
    val mItemClickCallback: InviteFriendClick?,
    val languageData: LanguageData?
)
    : RecyclerView.Adapter<InviteFriendBookingAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(mContext, mRecyclerView, mLayoutManager, mRecyclerViewAdapterCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_invite_friend, parent, false))
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
        Logger.d(mDataList[position].name)
        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Glide.with(itemView.context).load(mDataList[adapterPosition].profile).placeholder(R.drawable.user).into(itemView.img_flag)
            itemView.txt_name.text = mDataList[adapterPosition].name
            if(InviteFriendNewActivity.selectUserIds.contains(mDataList[adapterPosition].userid)){
                itemView.txt_invite.text = languageData!!.undo
            }else{
                itemView.txt_invite.text = languageData!!.invite
            }
            itemView.setOnClickListener {
                mItemClickCallback?.let {
                    if (itemView.txt_invite.text.equals(languageData.invite)){
                        mItemClickCallback.itemClickCallback(adapterPosition,mDataList[adapterPosition],0)
                    }else{
                        mItemClickCallback.itemClickCallback(adapterPosition,mDataList[adapterPosition],1)
                    }

                }
            }

        }
    }

    interface InviteFriendClick {
        fun itemClickCallback(
            position: Int,
            realFreind: InviteFriendResponse.Data.RealFreind,
            i: Int
        )

    }

    fun setLoadingStatus(status: Boolean) {
        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
    }
}