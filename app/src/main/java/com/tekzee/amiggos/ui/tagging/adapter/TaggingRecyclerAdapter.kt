package com.tekzee.amiggosvenueapp.ui.tagging.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.SingleTaggingRecyclerListBinding
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingRecyclerviewClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import kotlinx.android.synthetic.main.single_tagging_recycler_list.view.*


class TaggingRecyclerAdapter(private val listener: TaggingRecyclerviewClickListener) :
    ListAdapter<TaggingResponse.Data.Search, TaggingRecyclerAdapter.TaggingViewHolder>(
        TaggingDiffutil()
    ) {


    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaggingViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleTaggingRecyclerListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_tagging_recycler_list, parent, false
        )
        return TaggingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaggingViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context)
            holder.itemView.closeimage.setOnClickListener {
                listener.onItemCloseClicked(position,listItem)
            }
           
        }
    }

    class TaggingViewHolder(val listitembinding: SingleTaggingRecyclerListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: TaggingResponse.Data.Search?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.image).placeholder(R.drawable.noimage).into(listitembinding.profileImage)
            listitembinding.taggingitems = listItem
            listitembinding.profileImage.setOnClickListener {
                if(listItem.type == "3"){
                    val intent = Intent(context, AProfileDetails::class.java)
                    intent.putExtra(ConstantLib.FRIEND_ID, listItem.id.toString())
                    intent.putExtra(ConstantLib.FROM, ConstantLib.GUEST)
                    context.startActivity(intent)
                }
            }
            listitembinding.executePendingBindings()

        }
    }


}