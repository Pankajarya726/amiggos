package com.tekzee.amiggos.ui.menu.commonfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.SingleCommonStaffListBinding
import com.tekzee.amiggos.ui.menu.commonfragment.CommonClickListener
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import kotlinx.android.synthetic.main.single_common_staff_list.view.*


class CommonAdapter(private val listener: CommonClickListener) :
    ListAdapter<CommonMenuResponse.Data.Staff, CommonAdapter.CommonStaffViewHolder>(
        CommonDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonStaffViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleCommonStaffListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_common_staff_list, parent, false
        )
        return CommonStaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonStaffViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem, context)
            holder.itemView.s_switch.isChecked = listItem.isActive == 1
            holder.itemView.s_switch.setOnCheckedChangeListener { compoundButton, b ->
                listener.onItemClicked(position, b, listItem)
            }

            holder.itemView.imageView3.setOnClickListener {
                listener.onChatButtonClicked(position, listItem)
            }

        }
    }

    class CommonStaffViewHolder(val listitembinding: SingleCommonStaffListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: CommonMenuResponse.Data.Staff?,
            context: Context?
        ) {
            Glide.with(context!!).load(listItem!!.profileImage)
                .placeholder(R.drawable.header_logo).into(itemView.profile_image)
            listitembinding.commonstaffitem = listItem
            listitembinding.executePendingBindings()
        }
    }


}