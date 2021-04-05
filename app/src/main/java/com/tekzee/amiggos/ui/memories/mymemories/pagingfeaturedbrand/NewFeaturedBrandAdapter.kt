package com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.memories.mymemories.FeaturedBrandClickListener
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import kotlinx.android.synthetic.main.single_featuredbrand_list.view.*


class NewFeaturedBrandAdapter(private val listener: FeaturedBrandClickListener): PagedListAdapter<MemorieResponse.Data.Memories, NewFeaturedBrandAdapter.MyViewHolder>(
    DiffUtilCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_featuredbrand_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        getItem(position)?.let { itemData->
            holder.bindPost(itemData,listener)


        }
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val img_user = itemView.img_user_firstfragment

        fun bindPost(
            featuredbranditems: MemorieResponse.Data.Memories,
            listener: FeaturedBrandClickListener
        ){
            Glide.with(itemView.context)
                .load(featuredbranditems.profile)
                .placeholder(R.drawable.noimage)
                .into(img_user)

            itemView.img_layout.setOnClickListener {
                listener.onItemClickedBrand(featuredbranditems,adapterPosition)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<MemorieResponse.Data.Memories>() {
        override fun areItemsTheSame(oldItem: MemorieResponse.Data.Memories, newItem: MemorieResponse.Data.Memories): Boolean {
            return oldItem.venueId == newItem.venueId
        }

        override fun areContentsTheSame(oldItem: MemorieResponse.Data.Memories, newItem: MemorieResponse.Data.Memories): Boolean {
            return oldItem.venueId == newItem.venueId
        }

    }
}