package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.interfaces.PartyInviteInterface
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import kotlinx.android.synthetic.main.single_party_invite.view.*
import kotlinx.android.synthetic.main.single_party_invite_new.view.*


class PartyInvitesNewAdapter(
    private val items: ArrayList<BookingInvitationResponse.Data.BookingDetail>,
    private val languageData: LanguageData?,
    private val partyinterface: PartyInviteInterface
): RecyclerView.Adapter<PartyInvitesNewAdapter.PartyInvitesViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInvitesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_party_invite_new,parent,false)
        return PartyInvitesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PartyInvitesViewHolder, position: Int) {
        holder.bindingdata.i_txt_name.text = items[position].name
        holder.bindingdata.txt_accept.text = languageData!!.acceptPartyInvite
        holder.bindingdata.txt_reject.text = languageData.rejectPartyInvite
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.noimage).into(holder.bindingdata.img_eye)

        holder.bindingdata.txt_accept.setOnClickListener{
            partyinterface.onItemClicked(items[position],1)   //1- Join,2-Decline
        }

        holder.bindingdata.txt_reject.setOnClickListener{
            partyinterface.onItemClicked(items[position],2)
        }

        holder.bindingdata.img_eye.setOnClickListener{
            partyinterface.onItemClicked(items[position],3)
        }

    }

    inner class PartyInvitesViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


