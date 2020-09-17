package com.tekzee.amiggoss.ui.guestlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.guestlist.interfaces.GuestListInterface
import com.tekzee.amiggoss.ui.guestlist.model.GuestListData
import kotlinx.android.synthetic.main.single_guest_list.view.*


class GuestListAdapter(
    private val items: ArrayList<GuestListData>,
    private val guestListInterface: GuestListInterface
) : RecyclerView.Adapter<GuestListAdapter.PartyInvitesViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInvitesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_guest_list, parent, false)
        return PartyInvitesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PartyInvitesViewHolder, position: Int) {


        holder.bindingdata.g_txt_name.text = items[position].name
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.user)
            .into(holder.bindingdata.g_user_image)

    }

    inner class PartyInvitesViewHolder(val bindingdata: View) : RecyclerView.ViewHolder(bindingdata)


}


