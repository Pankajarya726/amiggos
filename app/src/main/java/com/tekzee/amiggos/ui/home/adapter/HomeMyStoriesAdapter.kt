package com.tekzee.amiggos.ui.home.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.StoriesData
import kotlinx.android.synthetic.main.single_list_stories.view.*

class HomeMyStoriesAdapter(var mDataList: ArrayList<StoriesData>?)
    : RecyclerView.Adapter<HomeMyStoriesAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var isLoadingAdded = false


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

        if (holder.itemViewType == holderRow) {
            holder.bind()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Glide.with(itemView.context).load(mDataList!![adapterPosition].imageUrl).placeholder(R.drawable.user).into(itemView.user_image)
            itemView.s_text_name.text = mDataList!![adapterPosition].name

//            itemView.setOnClickListener {
//                mItemClickCallback?.let {
//                    mItemClickCallback.itemClickCallback(adapterPosition)
//                }
//            }
        }
    }


    fun add(r: StoriesData) {
        mDataList!!.add(r)
        notifyItemInserted(mDataList!!.size - 1)
    }

    fun addAll(moveResults: List<StoriesData>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: StoriesData?) {
        val position = mDataList!!.indexOf(r)
        if (position > -1) {
            mDataList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(StoriesData())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = mDataList!!.size - 1
        val result = getItem(position)

        if (result != null) {
            mDataList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): StoriesData? {
        return mDataList!!.get(position)
    }


}