package com.tekzee.amiggos.ui.memories.ourmemories.adapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.StorieClickListener
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.storieview.StorieViewActivity
import com.tekzee.mallortaxiclient.constant.ConstantLib
import kotlinx.android.synthetic.main.infinite_loading_progress_bar_layout.view.*
import kotlinx.android.synthetic.main.single_list_stories.view.*



class MemorieAdapter(
    var mDataList: ArrayList<StoriesData>?,
    var listener: StorieClickListener
)
    : RecyclerView.Adapter<MemorieAdapter.ViewHolder>(){

    private val holderLoading: Int = 0
    private val holderRow: Int = 1
    private var isLoadingAdded = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == holderRow) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_list_stories, parent, false))
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.infinite_loading_progress_bar, parent, false))
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
            Logger.d("Inside holderRow")
        }else{
            Logger.d("Inside Progressbar")
            holder.bindProgressBar()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {

            Glide.with(itemView.context).load(mDataList!![adapterPosition].imageUrl).placeholder(R.drawable.blackbg).into(itemView.user_image)

            itemView.s_text_name.text = mDataList!![adapterPosition].name

            itemView.user_image.setOnClickListener{
                if(adapterPosition == 0 && mDataList!![adapterPosition].content.isEmpty()){
                    listener.onStorieClick(mDataList!![adapterPosition])
                }else{
                    val intent =Intent(itemView.context, StorieViewActivity::class.java)
                    intent.putExtra(ConstantLib.CONTENT,mDataList!![adapterPosition])
                    intent.putExtra(ConstantLib.PROFILE_IMAGE,mDataList!![adapterPosition].imageUrl)
                    intent.putExtra(ConstantLib.USER_ID,mDataList!![adapterPosition].userid.toString())
                    intent.putExtra(ConstantLib.USER_NAME,mDataList!![adapterPosition].name)
                    itemView.context.startActivity(intent)
                }

            }
        }

        fun bindProgressBar() {
            itemView.progress_bar.visibility = View.VISIBLE
        }
    }



    fun add(r: StoriesData) {
        r.loadingStatus = true
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