package com.tekzee.amiggos.ui.chat

import com.google.firebase.database.*
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.firebasemodel.User

class ChatHelper {


    companion object {
        private var databaseReference: DatabaseReference? = null

        fun sendMessage(
            sender: String,
            receiver: String,
            message: String,
            isseen: Boolean,
            timestamp: Long,
            reciverUser: User,
            currentUserAmigoId: String?
        ) {
            databaseReference = FirebaseDatabase.getInstance().reference.child(ConstantLib.MESSAGE)
            val map = HashMap<String, Any>()
            map["seen"] = isseen
            map["msg"] = message
            map["receiver"] = receiver
            map["sender"] = sender
            map["timestamp"] = timestamp

            var conversationId = ""
            if (Integer.parseInt(reciverUser.amiggosID!!) > Integer.parseInt(currentUserAmigoId!!)) {
                conversationId = currentUserAmigoId + "_" + reciverUser.amiggosID
            } else {
                conversationId = reciverUser.amiggosID + "_" + currentUserAmigoId
            }

            map["roomid"] = conversationId
            val pushKey = databaseReference!!.push().key
            map["message_id"] = pushKey.toString()
            databaseReference!!.child(pushKey!!).setValue(map)


            setConversation(
                sender,
                receiver,
                reciverUser,
                currentUserAmigoId,
                message,
                timestamp
            )
            setConversationReceiver(
                sender,
                receiver,
                reciverUser,
                currentUserAmigoId,
                message,
                timestamp
            )


        }

        fun getReceiverFirebaseId(
            amiggosId: String,
            listener: com.tekzee.amiggos.ui.chat.interfaces.ReceiverIdInterface
        ) {

            databaseReference = FirebaseDatabase.getInstance().reference.child("users")
            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (items in dataSnapshot.children) {
                        val user: User? = items.getValue(User::class.java)
                        if (user!!.amiggosID.equals(amiggosId, true)) {
                            listener.getReceiverId(items.key!!, user!!)
                            break
                        }
//                        Logger.d("User data: " + user.toString())
                    }
                }
            })

        }

        fun setConversationReceiver(
            senderId: String,
            receiver: String,
            reciverUser: User,
            currentUserAmigoId: String?,
            message: String,
            timestamp: Long
        ) {

            databaseReference =
                FirebaseDatabase.getInstance().reference.child(ConstantLib.CONVERSATION)
            var conversationId = "";
            if (Integer.parseInt(reciverUser.amiggosID!!) > Integer.parseInt(currentUserAmigoId!!)) {
                conversationId = reciverUser.amiggosID + "_" + currentUserAmigoId
            } else {
                conversationId = currentUserAmigoId + "_" + reciverUser.amiggosID
            }

            val map = HashMap<String, Any>()
            map["message"] = message
            map["conversationid"] = conversationId
            map["timestamp"] = timestamp
            map["senderid"] = receiver
            map["receiverid"] = senderId
            map["senderamiggosid"] = reciverUser.amiggosID!!
            map["recevieramiggosid"] = currentUserAmigoId
            databaseReference!!.child(conversationId).setValue(map)

        }


        fun setConversation(
            senderId: String,
            receiver: String,
            reciverUser: User,
            currentUserAmigoId: String?,
            message: String,
            timestamp: Long
        ) {
            databaseReference =
                FirebaseDatabase.getInstance().reference.child(ConstantLib.CONVERSATION)

            var conversationId = ""
            if (Integer.parseInt(reciverUser.amiggosID!!) > Integer.parseInt(currentUserAmigoId!!)) {
                conversationId = currentUserAmigoId + "_" + reciverUser.amiggosID
            } else {
                conversationId = reciverUser.amiggosID + "_" + currentUserAmigoId

            }

            val map = HashMap<String, Any>()
            map["message"] = message
            map["conversationid"] = conversationId
            map["timestamp"] = timestamp
            map["senderid"] = senderId
            map["receiverid"] = receiver
            map["senderamiggosid"] = currentUserAmigoId
            map["recevieramiggosid"] = reciverUser.amiggosID!!
            databaseReference!!.child(conversationId).setValue(map)
        }


    }

}