package com.tekzee.amiggos.base.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.message.model.Message
import com.tekzee.amiggos.ui.message.model.MyConversation
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatRepository(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : SafeApiRequest() {

    private var messageListener: ValueEventListener? = null
    private val unreadMessageList = MutableLiveData<List<Message>>()
    var currentUser = firebaseAuth.currentUser!!.uid


    private var myconversationMutableLiveDataList = MutableLiveData<List<MyConversation>>()
    fun getMyAllConversation(myId: String): MutableLiveData<List<MyConversation>> {
        val myconversationDataList = ArrayList<MyConversation>()
        val conversationReference = firebaseDatabase.reference
        conversationReference.child(ConstantLib.CONVERSATION).orderByChild("conversationid")
            .startAt(myId).endAt(myId + "\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    myconversationDataList.clear()
                    for (value in snapshot.children) {
                        val myconverationData = value.getValue(MyConversation::class.java)
                        myconversationDataList.add(myconverationData!!)
                    }
                    myconversationDataList.sortWith(Comparator { o1, o2 -> o2!!.timestamp!!.compareTo(o1!!.timestamp!!) })
                    myconversationMutableLiveDataList.value = myconversationDataList
                }

                override fun onCancelled(dataerror: DatabaseError) {

                }

            })
        return myconversationMutableLiveDataList
    }


    fun getAllUnreadChatCount(): MutableLiveData<List<Message>> {
        val listOfUnreadMessageCount = ArrayList<Message>()
        firebaseDatabase.reference.child(ConstantLib.MESSAGE).addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfUnreadMessageCount.clear()
                    for (messages in snapshot.children) {
                        val messageData = messages.getValue(Message::class.java)
                        if (messageData != null) {
                            if (messageData.receiver.equals(
                                    currentUser,
                                    true
                                ) && !messageData.isSeen
                            ) {
                                listOfUnreadMessageCount.add(
                                    messageData
                                )
                            }
                        }
                    }
                    unreadMessageList.value = listOfUnreadMessageCount
                }
            })

        return unreadMessageList
    }

    private val mutalbeChatbetweenUser = MutableLiveData<ArrayList<Message>>()
    fun getChatBetweenSenderAndReceiver(roomId: String): MutableLiveData<ArrayList<Message>> {
        val listOfmessages = ArrayList<Message>()
        firebaseDatabase.reference.child(ConstantLib.MESSAGE).orderByChild("roomid").equalTo(roomId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listOfmessages.clear()
                        for (value in dataSnapshot.children) {
                            val messageData = value.getValue(Message::class.java)
                            listOfmessages.add(messageData!!)
                        }
                        mutalbeChatbetweenUser.value = listOfmessages
                    }

                    override fun onCancelled(dataEror: DatabaseError) {

                    }

                })
        return mutalbeChatbetweenUser
    }



    fun deleteMessageId(messageId: String) {
        firebaseDatabase.reference.child(ConstantLib.MESSAGE).child(messageId).removeValue()
    }


    fun setMessageStatusToSeen(myFirebaseUserId: String, receiverId: String) {

        messageListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (items in snapshot.children) {
                    val message = items.getValue(Message::class.java)

                    if (message!!.sender.equals(receiverId) && message.receiver.equals(
                            currentUser
                        )
                    ) {
                        val map = HashMap<String, Any>()
                        map["seen"] = true
                        firebaseDatabase.reference.child(ConstantLib.MESSAGE).child(items.key!!)
                            .updateChildren(map)
                    }
                }
            }
        }

        firebaseDatabase.reference.child(ConstantLib.MESSAGE)
            .addListenerForSingleValueEvent(messageListener!!)


    }

    fun updateUseractiveTime() {
        firebaseDatabase.reference.child(ConstantLib.USER).child(currentUser).child("timestamp")
            .setValue(System.currentTimeMillis())
    }

    fun removeListener() {
        if(messageListener!=null)
        firebaseDatabase.reference.child(ConstantLib.MESSAGE).removeEventListener(messageListener!!)
    }




}