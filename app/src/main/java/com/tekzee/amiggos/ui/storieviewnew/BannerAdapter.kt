package com.tekzee.amiggos.ui.storieviewnew

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.BannerRowDataBinding
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse


import com.tekzee.amiggosvenueapp.ui.storieview.BannerDiffutil


class BannerAdapter(private val listener: BannerClickListener) :
    ListAdapter<MemorieResponse.Data.Memories.Memory.Tagged, BannerAdapter.BannerViewHolder>(
        BannerDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: BannerRowDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.banner_row, parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context)
            holder.itemView.setOnClickListener {
                listener.onItemClicked(position,listItem)
            }
           
        }
    }

    class BannerViewHolder(val listitembinding: BannerRowDataBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: MemorieResponse.Data.Memories.Memory.Tagged?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.banner).placeholder(R.drawable.noimage).into(listitembinding.ivCard)
            listitembinding.bannerRowResponse = listItem
            listitembinding.executePendingBindings()
        }
    }


}