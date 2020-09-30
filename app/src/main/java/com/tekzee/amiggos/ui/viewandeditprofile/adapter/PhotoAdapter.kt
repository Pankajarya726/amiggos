package com.tekzee.amiggos.ui.viewandeditprofile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.PhotoSingleListBinding
import com.tekzee.amiggos.ui.viewandeditprofile.PhotoDeleteClickListener
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import kotlinx.android.synthetic.main.photo_single_list.view.*


class PhotoAdapter(
    private val listener: PhotoDeleteClickListener,
    private val response: List<GetUserProfileResponse.Data.OtherImage>
) :
    ListAdapter<GetUserProfileResponse.Data.OtherImage, PhotoAdapter.PromotersViewHolder>(
        PhotoAdapterDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotersViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: PhotoSingleListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.photo_single_list, parent, false
        )
        return PromotersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromotersViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context,response)
            holder.itemView.delete_photo.setOnClickListener {
                listener.OnPhotoDeleteClicked(position,listItem.id)
            }
            holder.itemView.setOnClickListener {
                listener.onUpdateProfile(listItem)
            }
        }
    }

    class PromotersViewHolder(val listitembinding: PhotoSingleListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(listItem: GetUserProfileResponse.Data.OtherImage, context: Context?, response: List<GetUserProfileResponse.Data.OtherImage>) {
            Glide.with(context!!).load(listItem.image).placeholder(R.drawable.header_logo).into(listitembinding.imgUserFirstfragment)
            listitembinding.executePendingBindings()
        }
    }


}