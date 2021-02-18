package com.tekzee.amiggos.ui.invitationlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse


class InvitationListUserDiffutil : DiffUtil.ItemCallback<InvitationListResponse.Data>() {
    override fun areItemsTheSame(oldItem: InvitationListResponse.Data, newItem: InvitationListResponse.Data): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: InvitationListResponse.Data, newItem: InvitationListResponse.Data): Boolean {
        return oldItem == newItem
    }


}