package com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.memories.mymemories.MemorieClickListener
import kotlinx.android.synthetic.main.single_memorie_list.view.*


class NewMemorieAdapter(private val listener: MemorieClickListener) :
    PagedListAdapter<MemorieResponse.Data.Memories, NewMemorieAdapter.MyViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_memorie_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { itemData->
            holder.bindPost(itemData)

            holder.itemView.img_layout.setOnClickListener {
                listener.onItemClicked(itemData)
            }
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_image = itemView.user_image
        val s_text_name = itemView.s_text_name

        fun bindPost(memorieitem: MemorieResponse.Data.Memories) {
            with(memorieitem) {
                s_text_name.text = memorieitem.name
                Glide.with(itemView.context)
                    .load(memorieitem.profile)
                    .placeholder(R.drawable.noimage)
                    .into(user_image)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<MemorieResponse.Data.Memories>() {
        override fun areItemsTheSame(
            oldItem: MemorieResponse.Data.Memories,
            newItem: MemorieResponse.Data.Memories
        ): Boolean {
            return oldItem.venueId == newItem.venueId
        }

        override fun areContentsTheSame(
            oldItem: MemorieResponse.Data.Memories,
            newItem: MemorieResponse.Data.Memories
        ): Boolean {
            return oldItem.venueId == newItem.venueId
        }

    }
}