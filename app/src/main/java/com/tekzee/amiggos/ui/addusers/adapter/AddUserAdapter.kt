package com.tekzee.amiggos.ui.addusers.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.SingleUserListBinding
import com.tekzee.amiggosvenueapp.ui.addusers.AddUserClickListener
import com.tekzee.amiggosvenueapp.ui.addusers.adapter.AddUserDiffutil
import com.tekzee.amiggosvenueapp.ui.addusers.model.AddUserResponse


class AddUserAdapter(
    private val listener: AddUserClickListener,
    private val response: AddUserResponse
) :
    ListAdapter<AddUserResponse.Data.Staff, AddUserAdapter.AddUserViewHolder>(
        AddUserDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddUserViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleUserListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_user_list, parent, false
        )
        return AddUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddUserViewHolder, position: Int) {
        getItem(position).let { listItem ->
            Log.e("Listitem adduser ---->",listItem.toString())
            holder.bind(listItem,context,response)
            holder.itemView.setOnClickListener {
                listener.onItemClicked(position,listItem)
            }

        }
    }

    class AddUserViewHolder(val listitembinding: SingleUserListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: AddUserResponse.Data.Staff?,
            context: Context?,
            response: AddUserResponse
        ) {
            Glide.with(context!!).load(listItem!!.profileImage).placeholder(R.drawable.noimage).into(listitembinding.profileImage)
            listitembinding.staff = listItem
            listitembinding.executePendingBindings()
        }
    }




}