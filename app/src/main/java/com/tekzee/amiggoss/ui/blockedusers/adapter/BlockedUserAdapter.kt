package com.tekzee.amiggoss.ui.blockedusers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.ui.blockedusers.UnblockListener
import com.tekzee.amiggoss.ui.blockedusers.model.BlockedUserResponse
import kotlinx.android.synthetic.main.blocked_friend_list.view.*
import java.util.ArrayList

class BlockedUserAdapter(
    private val items: ArrayList<BlockedUserResponse.Data.BlockedUser>,
    private val languageData: LanguageData?,
    private val listener: UnblockListener
): RecyclerView.Adapter<BlockedUserAdapter.FriendListViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.blocked_friend_list,parent,false)
        return FriendListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {

        holder.bindingdata.txt_name.text = items[position].name
        Glide.with(context!!).load(items[position].profile).into(holder.bindingdata.img_flag)
        holder.bindingdata.txt_unblock.text = languageData!!.PUNBLOCK
        holder.bindingdata.txt_unblock.setOnClickListener{
             listener.onUnblockClicked(items[position])
        }
        holder.bindingdata.mainlayout.setOnClickListener {
            listener.onItemClicked(items[position])
        }

    }

    inner class FriendListViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


