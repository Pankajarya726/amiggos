package com.tekzee.amiggos.ui.bookingdetails.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.friendlist.FriendInviteListener
import com.tekzee.amiggos.ui.friendlist.model.FriendListData
import kotlinx.android.synthetic.main.single_booking_detail_list.view.*

class ABookingDetailAdapter(
    private val items: ArrayList<FriendListData>,
    private val languageData: LanguageData?,
    private val listener: FriendInviteListener
): RecyclerView.Adapter<ABookingDetailAdapter.FriendListViewHolder>() {

    private var context:Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_booking_detail_list,parent,false)
        return FriendListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendListViewHolder, position: Int) {
        Logger.d("name====>"+items[position].name)
        holder.bindingdata.txt_name.text = items[position].name
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.noimage).into(holder.bindingdata.img_flag)
        if(items[position].isInvited == 0){
            holder.bindingdata.txt_invite.setText(languageData!!.klMsgInvite)
        }else{
            holder.bindingdata.txt_invite.setText(languageData!!.klMsgInvited)
        }

        holder.bindingdata.txt_invite.setOnClickListener{
            listener.onFrienInviteClicked(items[position])
        }

    }

    inner class FriendListViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


