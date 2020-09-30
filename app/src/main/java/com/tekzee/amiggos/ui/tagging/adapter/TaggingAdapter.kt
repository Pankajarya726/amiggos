package com.tekzee.amiggosvenueapp.ui.tagging.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.SingleTaggingListBinding
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse


class TaggingAdapter(private val listener: TaggingClickListener) :
    ListAdapter<TaggingResponse.Data.Search, TaggingAdapter.TaggingViewHolder>(
        TaggingDiffutil()
    ) {


    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaggingViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleTaggingListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_tagging_list, parent, false
        )
        return TaggingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaggingViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context)
            holder.itemView.setOnClickListener {
                listener.onItemClicked(position,listItem)
            }
           
        }
    }

    class TaggingViewHolder(val listitembinding: SingleTaggingListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: TaggingResponse.Data.Search?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.image).placeholder(R.drawable.header_logo).into(listitembinding.profileImage)
            listitembinding.taggingitems = listItem
            listitembinding.executePendingBindings()
        }
    }


}