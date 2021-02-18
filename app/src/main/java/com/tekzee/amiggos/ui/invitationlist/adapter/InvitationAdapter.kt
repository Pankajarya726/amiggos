package com.tekzee.amiggos.ui.invitationlist.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.SingleInvitationListBinding
import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import kotlinx.android.synthetic.main.single_common_staff_list.view.*


class InvitationAdapter(
    private val response: InvitationListResponse
) :
    ListAdapter<InvitationListResponse.Data, InvitationAdapter.AddUserViewHolder>(
        InvitationListUserDiffutil()
    ) {

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddUserViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleInvitationListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_invitation_list, parent, false
        )
        return AddUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddUserViewHolder, position: Int) {
        getItem(position).let { listItem ->
            holder.bind(listItem,context,response)
        }
    }

    class AddUserViewHolder(val listitembinding: SingleInvitationListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(
            listItem: InvitationListResponse.Data,
            context: Context?,
            response: InvitationListResponse
        ) {
//            GlideApp.with(context!!).load(listItem!!.profile).placeholder(R.drawable.image_placeholder).into(listitembinding.profileImage)
            Glide.with(context!!).load(listItem!!.profile)
                .placeholder(R.drawable.noimage).into(listitembinding.profileImage)
            if(listItem!!.isCheckin==1){
                listitembinding.imageView3.visibility = View.VISIBLE
            }else{
                listitembinding.imageView3.visibility = View.GONE
            }
            listitembinding.invitaionlist = listItem
            listitembinding.executePendingBindings()

            listitembinding.profileImage.setOnClickListener {
                val intent = Intent(context, AProfileDetails::class.java)
                intent.putExtra(ConstantLib.FRIEND_ID, listItem.userid.toString())
                intent.putExtra(ConstantLib.FROM, ConstantLib.GUEST)
                context.startActivity(intent)
            }
        }
    }




}