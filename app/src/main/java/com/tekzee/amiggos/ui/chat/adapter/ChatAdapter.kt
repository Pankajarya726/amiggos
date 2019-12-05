package com.tekzee.amiggos.ui.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tekzee.amiggos.R
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.chat.ChatHelper
import com.tekzee.amiggos.ui.chat.MessageActivity
import com.tekzee.amiggos.ui.chat.interfaces.FirebaseUserInterface
import com.tekzee.amiggos.ui.chat.interfaces.ReceiverIdInterface
import com.tekzee.amiggos.ui.chat.model.Message
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.mallortaxiclient.constant.ConstantLib
import kotlinx.android.synthetic.main.row_chat_left.view.*
import kotlinx.android.synthetic.main.row_chat_right.view.*

class ChatAdapter(
    private val items: ArrayList<Message>,
    var senderImage: String?,
    var SenderName: String?,
    var friendName: String,
    var friendImage: String
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object{
        val MSG_TYPE_LEFT = 0
        val MSG_TYPE_RIGHT = 1
    }

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        if(viewType == MSG_TYPE_LEFT){
            val view = inflater.inflate(R.layout.row_chat_left,parent,false)
            return ChatViewHolder(view)
        }else{
            val view = inflater.inflate(R.layout.row_chat_right,parent,false)
            return ChatViewHolder(view)
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        if(holder.itemViewType == MSG_TYPE_LEFT){
            holder.bindingdata.show_message.text = items[position].msg
            holder.bindingdata.txt_receiver_name.text = friendName
            Glide.with(context!!).load(friendImage).into(holder.bindingdata.img_receiver)


            holder.bindingdata.img_receiver.setOnClickListener{
                ChatHelper.getamiggosIdFromFirebaseId(items[position].sender,
                    object : FirebaseUserInterface {
                        override fun getFirebaseUserIdFromAmiggosId(firebaseuserid: String) {
                            val intent = Intent(context, FriendProfile::class.java)
                            intent.putExtra(ConstantLib.FRIEND_ID,firebaseuserid)
                            context!!.startActivity(intent)
                        }
                    })
            }

        }else{
            holder.bindingdata.show_message_right.text = items[position].msg
            holder.bindingdata.txt_sender_name.text = SenderName
            Glide.with(context!!).load(senderImage).into(holder.bindingdata.img_sender)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val senderid = items[position].sender
        if(senderid.equals(FirebaseAuth.getInstance().currentUser!!.uid,true)){
            return MSG_TYPE_RIGHT
        }else{
            return MSG_TYPE_LEFT
        }
    }

    inner class ChatViewHolder(val bindingdata: View): RecyclerView.ViewHolder(bindingdata)


}
