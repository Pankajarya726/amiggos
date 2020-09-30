package com.tekzee.amiggos.ui.chatnew.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.chatnew.ChatActivity
import com.tekzee.amiggos.ui.chatnew.model.ChatMessage
import kotlinx.android.synthetic.main.row_chat_left.view.*
import kotlinx.android.synthetic.main.row_chat_right.view.*


class ChatNewAdapter(
    private val listener: ChatActivity,
    private val prefs: SharedPreference
) :
    ListAdapter<ChatMessage, ChatNewAdapter.ChatViewHolder>(
        ChatNewDiffutil()
    ) {
    val ref = FirebaseDatabase.getInstance().reference

    companion object {
        val MSG_TYPE_LEFT = 0
        val MSG_TYPE_RIGHT = 1
    }

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == MSG_TYPE_LEFT) {
            val view = inflater.inflate(R.layout.row_chat_left, parent, false)
            return ChatViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.row_chat_right, parent, false)
            return ChatViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var type: Int? = null
        getItem(position).let { listItem ->
            val senderid = listItem.sender
            if (senderid.equals(FirebaseAuth.getInstance().currentUser!!.uid, true)) {
                type = MSG_TYPE_RIGHT
            } else {
                type = MSG_TYPE_LEFT
            }
        }
        return type!!
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position).let { listItem ->
            if (holder.itemViewType == MSG_TYPE_LEFT) {
                holder.bindingdata.show_message.text = listItem.msg
                holder.bindingdata.txt_receiver_name.text = listItem.friendName
                Glide.with(context!!).load(listItem.friendImage)
                    .placeholder(R.drawable.header_logo).into(holder.bindingdata.img_receiver)

            } else {
                holder.bindingdata.show_message_right.text = listItem.msg
                holder.bindingdata.txt_sender_name.text = listItem.SenderName
                Glide.with(context!!)
                    .load(prefs.getValueString(ConstantLib.PROFILE_IMAGE))
                    .placeholder(R.drawable.header_logo).into(holder.bindingdata.img_sender)
                holder.bindingdata.show_message_right.setOnLongClickListener {
                    listener.onItemClicked(position, listItem)
                    false
                }
            }
        }
    }

    inner class ChatViewHolder(val bindingdata: View) : RecyclerView.ViewHolder(bindingdata)


}