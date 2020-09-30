package com.tekzee.amiggos.ui.realfriends.invitations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.realfriends.invitations.interfaces.InvitationInterfaces
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponseV2
import kotlinx.android.synthetic.main.single_invitation.view.*


class InvitationAdapter(
    private val items: ArrayList<InvitationResponseV2.Data.FreindRequest>,
    private val languageData: LanguageData?,
    private val invitationInterface: InvitationInterfaces
): RecyclerView.Adapter<InvitationAdapter.PartyInvitesViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInvitesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_invitation,parent,false)

        return PartyInvitesViewHolder(view)
    }


    override fun onBindViewHolder(holder: PartyInvitesViewHolder, position: Int) {
        holder.bindingdata.txt_name_invitation.text = items[position].name
        holder.bindingdata.txt_accept.text = languageData!!.paccept
        holder.bindingdata.txt_reject.text = languageData.preject
        Glide.with(context!!).load(items[position].profile).placeholder(R.drawable.user).into(holder.bindingdata.profile_image)

        holder.bindingdata.txt_accept.setOnClickListener{
            invitationInterface.onItemClicked(items[position],1)
        }

        holder.bindingdata.txt_reject.setOnClickListener{
            invitationInterface.onItemClicked(items[position],2)
        }

        holder.itemView.setOnClickListener{
            invitationInterface.onItemClicked(items[position],3)
        }

    }

    inner class PartyInvitesViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)

    override fun getItemCount(): Int {
        return items.size
    }


}


