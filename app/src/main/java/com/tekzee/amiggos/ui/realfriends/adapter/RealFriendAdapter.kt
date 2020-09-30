package com.tekzee.amiggos.ui.realfriends.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.chatnew.ChatActivity
import com.tekzee.amiggos.ui.message.MessageActivity
import com.tekzee.amiggos.ui.message.model.MyFriendChatModel
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import kotlinx.android.synthetic.main.single_online_friend.view.profile_image
import kotlinx.android.synthetic.main.single_real_friend.view.*


class RealFriendAdapter(
    val mContext: Context, mRecyclerView: RecyclerView, val mLayoutManager: LinearLayoutManager,
    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, var mDataList: ArrayList<RealFriendV2Response.Data.RealFreind>, val mItemClickCallback: RealFriend
)
    : RecyclerView.Adapter<RealFriendAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null

    init {
        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(mContext, mRecyclerView, mLayoutManager, mRecyclerViewAdapterCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_real_friend, parent, false))
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
            Glide.with(itemView.context).load(mDataList[adapterPosition].profile).placeholder(R.drawable.user).into(itemView.profile_image)
            itemView.txt_name_real_friend.text = mDataList[adapterPosition].name

            itemView.setOnClickListener {
                mItemClickCallback.let {
                    mItemClickCallback.itemClickCallback(adapterPosition)
                }
            }

            if(mDataList[adapterPosition].isMyFriendBlocked){
                itemView.imageView8.visibility = View.GONE
            }else{
                itemView.imageView8.visibility = View.VISIBLE
            }

            itemView.imageView8.setOnClickListener {
//                val intentActivity = Intent(mContext, MessageActivity::class.java)
//                intentActivity.putExtra(ConstantLib.FRIEND_ID,mDataList[adapterPosition].userid.toString())
//                intentActivity.putExtra(ConstantLib.FRIENDNAME,mDataList[adapterPosition].name)
//                intentActivity.putExtra(ConstantLib.FRIENDIMAGE,mDataList[adapterPosition].profile)
//                mContext.startActivity(intentActivity)

                val myFriendChatModel = MyFriendChatModel()
                myFriendChatModel.name = mDataList[adapterPosition].name
                myFriendChatModel.image = mDataList[adapterPosition].profile
                val intent = Intent(mContext, ChatActivity::class.java)
                intent.putExtra(ConstantLib.FRIEND_ID,mDataList[adapterPosition].userid.toString())
                intent.putExtra(ConstantLib.CHAT_DATA,myFriendChatModel)
                mContext.startActivity(intent)


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