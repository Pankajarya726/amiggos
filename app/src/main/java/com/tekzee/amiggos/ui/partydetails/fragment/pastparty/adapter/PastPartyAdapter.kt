package com.tekzee.amiggos.ui.partydetails.fragment.pastparty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.interfaces.PastPartyInterface
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyData
import kotlinx.android.synthetic.main.single_past_party.view.*


class PastPartyAdapter(
    private val items: ArrayList<PastPartyData>,
    private val languageData: LanguageData?,
    private  val pastPartyInterface: PastPartyInterface
): RecyclerView.Adapter<PastPartyAdapter.PartyInvitesViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyInvitesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = inflater.inflate(R.layout.single_past_party,parent,false)
        return PartyInvitesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PartyInvitesViewHolder, position: Int) {
        holder.bindingdata.p_txt_name.text = items[position].clubName
        holder.bindingdata.p_txt_date.text = items[position].partyDate
        holder.bindingdata.p_txt_time.text = items[position].startTime
        holder.bindingdata.p_txt_list.text = languageData!!.klGuestList
        Glide.with(context!!).load(items[position].venueHomeImage).placeholder(R.drawable.noimage).into(holder.bindingdata.p_user_image)
        holder.bindingdata.p_txt_list.setOnClickListener{
            pastPartyInterface.onItemClicked(items[position],1)
        }

    }

    inner class PartyInvitesViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}


