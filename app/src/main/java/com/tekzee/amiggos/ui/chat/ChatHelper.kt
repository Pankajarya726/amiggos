package com.tekzee.amiggos.ui.chat

import android.util.Log
import com.google.firebase.database.*
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.chat.interfaces.ChatListInterface
import com.tekzee.amiggos.ui.chat.interfaces.ReceiverIdInterface
import com.tekzee.amiggos.ui.chat.model.Message

class ChatHelper {


    companion object {
        var chatListArray:ArrayList<Message> = ArrayList()
        private var databaseReference: DatabaseReference? = null
        private var databaseReferenceMessage: DatabaseReference? = null

        fun setMessageStatusToSeen(senderId:String,receiverId:String){

            databaseReference = FirebaseDatabase.getInstance().reference.child("message")
            databaseReference!!.addValueEventListener(object : ValueEventListener{

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (items in snapshot.children){
                        val message = items.getValue(Message::class.java)
                        if(message!!.sender.equals(receiverId) && message.receiver.equals(senderId)){
                            val map = HashMap<String,Any>()
                            map["isSeen"] = true
                            databaseReference!!.child(items.key!!).updateChildren(map)
                        }
                    }
                }
            })
        }



        fun sendMessage(
            sender: String,
            receiver: String,
            message: String,
            isseen: Boolean,
            timestamp: Long
        ) {
            databaseReference = FirebaseDatabase.getInstance().reference.child("message")
            val mmessage = Message(isseen, message, receiver, sender, timestamp)

            val map = HashMap<String,Any>()
            map["isSeen"] = isseen
            map["msg"] = message
            map["receiver"] = receiver
            map["sender"] = sender
            map["timeSpam"] = timestamp
            val pushKey = databaseReference!!.push().key
            databaseReference!!.child(pushKey!!).setValue(map)
            setConversation(sender, pushKey, receiver)
        }

        fun getReceiverFirebaseId(
            amiggosId: String,
            listener: ReceiverIdInterface
        ) {

            databaseReference = FirebaseDatabase.getInstance().reference.child("users")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (items in dataSnapshot.children) {
                        val user: User? = items.getValue(User::class.java)
                        if (user!!.amiggosID.equals(amiggosId, true)) {
                            listener.getReceiverId(items.key!!,user!!)
                            break
                        }
                        Logger.d("User data: " + user.toString())
                    }
                }
            })

        }

        fun setConversation(
            senderId: String,
            conversationId: String,
            receiver: String
        ) {
            databaseReference = FirebaseDatabase.getInstance().reference.child("conversation")
            databaseReference!!.child(senderId).child(conversationId).setValue(1)

            databaseReference!!.child(receiver).child(conversationId).setValue(1)
        }


        fun getAllMessageBetweenSenderAndReceiver(
            senderid: String,
            receiverid: String,
            listener: ChatListInterface
        ) {
            chatListArray = ArrayList()
            databaseReference =
                FirebaseDatabase.getInstance().reference.child("conversation").child(senderid)
            databaseReferenceMessage = FirebaseDatabase.getInstance().reference.child("message")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (conversationIds in dataSnapshot.children) {
                        Log.d("Conversation id : ", conversationIds.key)
                        databaseReferenceMessage!!.child(conversationIds.key!!)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(dataSnapshot: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val message: Message? =
                                        dataSnapshot.getValue(Message::class.java)
                                    listener.getChatList(message!!)
                                }
                            })
                    }
                }
            })
        }

    }


    fun getAllUserMessagedMe(myId: String){
        var listOfUsers: ArrayList<User> = ArrayList()
        databaseReference = FirebaseDatabase.getInstance().reference.child("message")
        databaseReference!!.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (items in snapshot.children){
//                    if(myId.equals())
//                    val user = items.getValue(User::class.java)

                }
            }
        })


    }





}