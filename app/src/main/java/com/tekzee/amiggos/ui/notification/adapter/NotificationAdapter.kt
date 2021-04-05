//package com.tekzee.amiggos.ui.notification.adapter
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.notification.model.NotificationData
//import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
//import kotlinx.android.synthetic.main.single_notification.view.*
//
//
//class NotificationAdapter(
//    val mContext: Context, mRecyclerView: RecyclerView, val mLayoutManager: LinearLayoutManager,
//    mRecyclerViewAdapterCallback: InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, var mDataList: ArrayList<NotificationData>, val mItemClickCallback: HomeItemClick?)
//    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){
//
//    private val holderLoading: Int = 0
//    private val holderRow: Int = 1
//    private var mInfiniteScrollRecyclerView: InfiniteScrollRecyclerView? = null
//
//    init {
//        mInfiniteScrollRecyclerView = InfiniteScrollRecyclerView(mContext, mRecyclerView, mLayoutManager, mRecyclerViewAdapterCallback)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        if (viewType == holderRow) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_notification, parent, false))
//        }
//        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.infinite_loading_progress_bar_layout, parent, false))
//    }
//
//    override fun getItemCount(): Int = mDataList.size
//
//    override fun getItemViewType(position: Int): Int = when (mDataList[position].loadingStatus) {
//        false -> holderRow
//        else -> {
//            holderLoading
//        }
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (holder.itemViewType == holderRow) {
//            holder.bind()
//        }
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        fun bind() {
//
//            itemView.notification_title.text = mDataList[adapterPosition].subject
//            itemView.notification_description.text = mDataList[adapterPosition].message
//
//            itemView.setOnClickListener {
//                mItemClickCallback?.let {
//                    mItemClickCallback.itemClickCallback(adapterPosition)
//                }
//            }
//
//            itemView.setOnLongClickListener(object: View.OnLongClickListener{
//                override fun onLongClick(p0: View?): Boolean {
//                    mItemClickCallback!!.onItemLongClickListener(adapterPosition)
//                    return true
//                }
//            })
//        }
//    }
//
//    interface HomeItemClick {
//        fun itemClickCallback(
//            position: Int
//        )
//        fun onItemLongClickListener(position: Int)
//    }
//
//    fun setLoadingStatus(status: Boolean) {
//        mInfiniteScrollRecyclerView!!.setLoadingStatus(status)
//    }
//
//    fun removeItem(position: Int) {
//        mDataList.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, mDataList.size);
//    }
//
//
//    fun removeAt(position: Int) {
//        mDataList.removeAt(position)
//        notifyItemRemoved(position)
//    }
//}