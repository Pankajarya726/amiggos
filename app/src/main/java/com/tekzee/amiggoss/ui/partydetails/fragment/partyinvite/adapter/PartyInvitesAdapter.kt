package com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.interfaces.PartyInviteInterface
import com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.model.PartyInvitesData
import kotlinx.android.synthetic.main.single_party_invite.view.*


class PartyInvitesAdapter(
    private val items: ArrayList<PartyInvitesData>,
    private val languageData: LanguageData?,
    private  val partyinterface: PartyInviteInterface
): RecyclerView.Adapter<PartyInvitesAdapter.PartyInvitesViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInvitesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_party_invite,parent,false)
        return PartyInvitesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PartyInvitesViewHolder, position: Int) {
        holder.bindingdata.i_txt_name.text = items[position].name
        holder.bindingdata.i_txt_clubname.text = items[position].clubName
        holder.bindingdata.i_txt_date.text = items[position].partyDate
        holder.bindingdata.i_txt_time.text = items[position].startTime
        holder.bindingdata.i_txt_address.text = items[position].clubAddress
        holder.bindingdata.btn_join.text = languageData!!.klJoin
        holder.bindingdata.btn_decline.text = languageData.klDecline
        holder.bindingdata.i_txt_list.text = languageData.klGuestList
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.user).into(holder.bindingdata.user_image)

        holder.bindingdata.btn_join.setOnClickListener{
            partyinterface.onItemClicked(items[position],1)   //1- Join,2-Decline
        }

        holder.bindingdata.btn_decline.setOnClickListener{
            partyinterface.onItemClicked(items[position],2)
        }
        holder.bindingdata.i_txt_list.setOnClickListener{
            partyinterface.onItemClicked(items[position],3)
        }

    }

    inner class PartyInvitesViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


