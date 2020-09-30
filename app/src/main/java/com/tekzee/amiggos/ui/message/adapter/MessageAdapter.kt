package com.tekzee.amiggos.ui.message.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.SingleChatListBinding
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.ui.message.MessageClickListener
import com.tekzee.amiggos.ui.message.model.Message
import com.tekzee.amiggos.ui.message.model.MyConversation
import com.tekzee.amiggos.ui.message.model.User
import kotlinx.android.synthetic.main.single_chat_list.view.*
import java.lang.Exception


class MessageAdapter(
    private val listener: MessageClickListener,
    private val currenLoggedinUserId: String?,
    private val applicationContext: Context
) :
    ListAdapter<MyConversation, MessageAdapter.ChatListViewHolder>(
        MessageDiffutil()
    ) {

    private var userInfoListener: ValueEventListener?=null
    private var context: Context? = null
    val ref = FirebaseDatabase.getInstance().reference
    var currentUser = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        val binding: SingleChatListBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.single_chat_list, parent, false
        )
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        getItem(position).let { listItem ->
            Log.e("listitem--->", listItem.toString())
            holder.bind(listItem)
            holder.itemView.setOnClickListener {
                listener.onItemClicked(position, listItem)
            }
            holder.itemView.txt_timestamp.text =
                Utility.getDateTimeFromEpocLongOfSeconds(listItem.timestamp!!.toLong())

            if (context != null) {

                userInfoListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try{
                            val user = snapshot.getValue(User::class.java)
                            Glide.with(applicationContext).load(user!!.image)
                                .placeholder(R.drawable.header_logo)
                                .into(holder.itemView.g_user_image)
                            holder.itemView.g_txt_name.text = user.name
                            listItem.name = user.name
                            listItem.image = user!!.image
                        }catch (e: Exception){
                            e.printStackTrace()
                        }

                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
                if(currentUser.equals(listItem.senderid)){
                    ref.child(ConstantLib.USER+"/"+listItem.receiverid)
                        .addValueEventListener(userInfoListener!!)
                }else{
                    ref.child(ConstantLib.USER+"/"+listItem.senderid)
                        .addValueEventListener(userInfoListener!!)
                }

                var finalUserId = ""
                if (Integer.parseInt(listItem.recevieramiggosid!!) > Integer.parseInt(listItem.senderamiggosid!!)) {
                    finalUserId = listItem.senderamiggosid!! + "_" + listItem.recevieramiggosid!!
                } else {
                    finalUserId = listItem.recevieramiggosid!! + "_" + listItem.senderamiggosid!!
                }
//
//                Log.e("finaluser id---->",finalUserId)
//
                ref.child(ConstantLib.MESSAGE).orderByChild("roomid").equalTo(finalUserId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var count = 0
                            for (value in snapshot.children) {
                                val message = value.getValue(Message::class.java)
                                if (message!!.receiver.equals(
                                        FirebaseAuth.getInstance().currentUser!!.uid,
                                        true
                                    ) && !message.isSeen
                                ) {
                                    count++
                                }
                            }
                            if (count > 0) {
                                holder.itemView.txt_unread.visibility = View.VISIBLE
                                holder.itemView.txt_unread.text = count.toString()
                            } else {
                                holder.itemView.txt_unread.visibility = View.GONE
                                holder.itemView.txt_unread.text = count.toString()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                ref.child(ConstantLib.MESSAGE).orderByChild("roomid").equalTo(finalUserId).limitToLast(1).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.e("data-------->",snapshot.toString())
                        for(item in snapshot.children){
                            val messageData = item.getValue(Message::class.java)
                            holder.itemView.txt_message.setText(messageData!!.msg)
                        }
                    }

                    override fun onCancelled(eror: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })


            }


        }
    }


    class ChatListViewHolder(val listitembinding: SingleChatListBinding) :
        RecyclerView.ViewHolder(listitembinding.root) {
        fun bind(listItem: MyConversation?) {
            listitembinding.chatlistdata = listItem
            listitembinding.executePendingBindings()
        }
    }

    fun ondestroyListeners(){
        if(userInfoListener!=null)
        ref.child(ConstantLib.USER).orderByChild("amiggosID").removeEventListener(userInfoListener!!)
    }


}
