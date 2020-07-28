package com.tekzee.amiggos.ui.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.*
import com.tekzee.amiggos.databinding.MessageActivityBinding
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.network.APINotificationService
import com.tekzee.amiggos.network.Client
import com.tekzee.amiggos.ui.chat.adapter.ChatAdapter
import com.tekzee.amiggos.ui.chat.interfaces.ReceiverIdInterface
import com.tekzee.amiggos.ui.chat.model.Message

import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageActivity : BaseActivity() {
    private var valueEventSeenListener: ValueEventListener? = null
    private var messageListener: ValueEventListener? = null
    private var listListener: ValueEventListener? = null
    private var sharedPreferences: SharedPreference? = null
    private var databaseReferenceMessage: DatabaseReference? = null
    private var databaseReference: DatabaseReference? = null
    private var adapter: ChatAdapter? = null
    var chatListArray: ArrayList<Message>? = null
    private lateinit var binding: MessageActivityBinding
    private var myFirebaseUserid: String? = null
    private var listSize: Int = 0
    private var mApiService: APINotificationService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.message_activity)
        myFirebaseUserid = FirebaseAuth.getInstance().currentUser!!.uid
        sharedPreferences = SharedPreference(this)
        databaseReferenceMessage = FirebaseDatabase.getInstance().reference.child("message")

        setUpClickListener()
        setUpViewData()

        mApiService = Client.getClient("https://fcm.googleapis.com/")
            .create(APINotificationService::class.java)


        Log.e("Message FriendId:", "" + intent.getStringExtra(ConstantLib.FRIEND_ID))

        ChatHelper.getReceiverFirebaseId(intent.getStringExtra(ConstantLib.FRIEND_ID),
            object : ReceiverIdInterface {
                override fun getReceiverId(
                    receiverId: String,
                    user: User
                ) {
                    setMessageStatusToSeen(myFirebaseUserid!!, receiverId)
                }
            })



        ChatHelper.getReceiverFirebaseId(intent.getStringExtra(ConstantLib.FRIEND_ID),
            object : ReceiverIdInterface {
                override fun getReceiverId(
                    receiverId: String,
                    user: User
                ) {
                    getAllMessageBetweenSenderAndReceiver(myFirebaseUserid!!, receiverId)
                }
            })


    }

    override fun validateError(message: String) {

    }


    fun getAllMessageBetweenSenderAndReceiver(
        senderid: String,
        receiverId: String
    ) {
        chatListArray = ArrayList()
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("conversation").child(senderid)

        messageListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chatListArray!!.clear()
                listSize = 0
                for (conversationIds in dataSnapshot.children) {
                    listSize++
                    listListener = databaseReferenceMessage!!.child(conversationIds.key!!)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(dataSnapshot: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.getValue(Message::class.java) != null) {
                                    val message: Message? =
                                        dataSnapshot.getValue(Message::class.java)
                                    if (senderid.equals(
                                            message!!.sender,
                                            true
                                        ) && receiverId.equals(
                                            message.receiver,
                                            true
                                        )
                                    ) {
                                        if(!chatListArray!!.contains(message))
                                        chatListArray!!.add(message)
                                    }

                                    if(senderid.equals(
                                        message.receiver,
                                        true
                                    ) && receiverId.equals(message.sender, true)){
                                        if(!chatListArray!!.contains(message))
                                        chatListArray!!.add(message)
                                    }
                                    Log.d("chatListArray size :",""+chatListArray!!.size)
                                    setupRecyclerview()
                                }
                            }
                        })

                }


            }
        })

    }

    fun setMessageSeen(key: String?) {
        val map = HashMap<String, Any>()
        map["isSeen"] = true
        databaseReferenceMessage!!.child(key!!).updateChildren(map)
    }

    private fun setupRecyclerview() {
        binding.recyclerViewData.setHasFixedSize(true)
        val mlayoutManager = LinearLayoutManager(this)
        mlayoutManager.stackFromEnd = true
        binding.recyclerViewData.layoutManager = mlayoutManager
        adapter = ChatAdapter(
            chatListArray!!,
            sharedPreferences!!.getValueString(ConstantLib.PROFILE_IMAGE),
            sharedPreferences!!.getValueString(ConstantLib.USER_NAME),
            intent.getStringExtra(ConstantLib.FRIENDNAME)!!,
            intent.getStringExtra(ConstantLib.FRIENDIMAGE)!!
        )
        binding.recyclerViewData.adapter = adapter
    }

    private fun setUpViewData() {
        binding.toolbarTitle.text = intent.getStringExtra(ConstantLib.FRIENDNAME)
    }

    private fun setUpClickListener() {
        binding.ivsend.setOnClickListener {
            if (binding.etChat.text.trim().isEmpty()) {
                Toast.makeText(applicationContext, "Please enter message", Toast.LENGTH_LONG).show()
            } else {
                ChatHelper.getReceiverFirebaseId(intent.getStringExtra(ConstantLib.FRIEND_ID)!!,
                    object : ReceiverIdInterface {
                        override fun getReceiverId(
                            receiverId: String,
                            user: User
                        ) {
                            if (binding.etChat.text.trim().toString().isNotEmpty()) {

                                ChatHelper.sendMessage(
                                    myFirebaseUserid!!,
                                    receiverId,
                                    binding.etChat.text.trim().toString(),
                                    false,
                                    System.currentTimeMillis()
                                )
                                sendNotification(user, binding.etChat.text.trim().toString())
                                binding.etChat.setText("")
                            }

                        }
                    })
            }

        }


        binding.toolbarIconNavigation.setOnClickListener {
            onBackPressed()
        }
    }

    private fun sendNotification(
        user: User,
        message: String
    ) {
        val notification = Notification(
            "New Message",
            message,
            user.name,
            "coin.aiff"

        )

        val data = Data(
            user.deviceToken,
            R.mipmap.ic_launcher,
            "7007",
            user.name,
            message,
            user.deviceToken,
            "100",
            myFirebaseUserid
        )

        val notificationData = NotificationData(notification, data, user.fcmToken)

        mApiService!!.sendNotification(notificationData)
            .enqueue(object : Callback<MyResponse> {
                override fun onResponse(
                    call: Call<MyResponse>,
                    response: Response<MyResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()!!.success !== 1) {

                        }
                    }
                }

                override fun onFailure(
                    call: Call<MyResponse>,
                    t: Throwable
                ) {
                }
            })
    }

    override fun onPause() {
        super.onPause()
        if (listListener != null)
            databaseReferenceMessage!!.removeEventListener(listListener!!)


        if (messageListener != null)
            databaseReference!!.removeEventListener(messageListener!!)


        if (valueEventSeenListener != null)
            databaseReferenceMessage!!.removeEventListener(valueEventSeenListener!!)

    }

    override fun onDestroy() {
        super.onDestroy()

    }


    fun setMessageStatusToSeen(myFirebaseUserId: String, receiverId: String) {

        valueEventSeenListener =
            databaseReferenceMessage!!.addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (items in snapshot.children) {
                        val message = items.getValue(Message::class.java)
                        if (message!!.sender.equals(receiverId) && message.receiver.equals(
                                myFirebaseUserId
                            )
                        ) {
                            val map = HashMap<String, Any>()
                            map["isSeen"] = true
                            databaseReferenceMessage!!.child(items.key!!).updateChildren(map)
                        }
                    }
                }
            })
    }
}


