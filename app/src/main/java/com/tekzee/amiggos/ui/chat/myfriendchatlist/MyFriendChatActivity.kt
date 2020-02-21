package com.tekzee.amiggos.ui.chat.myfriendchatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.MyFriendChatListBinding
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.chat.MessageActivity
import com.tekzee.amiggos.ui.chat.model.Message
import com.tekzee.amiggos.ui.chat.myfriendchatlist.adapter.MyFriendChatAdapter
import com.tekzee.amiggos.ui.chat.myfriendchatlist.model.CustomUser
import com.tekzee.amiggos.ui.chat.myfriendchatlist.model.MyFriendChatModel
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib

class MyFriendChatActivity : BaseActivity() {

    private lateinit var binding: MyFriendChatListBinding
    private var languageData: LanguageData? = null
    private var sharedPreferences: SharedPreference? = null

    private lateinit var adapter: MyFriendChatAdapter
    var myfriendChatList = ArrayList<MyFriendChatModel>()
    val listOfAllUsers = ArrayList<User>()
    val listOfSenders = ArrayList<String>()
    val userList: ArrayList<CustomUser>? = null
    var mUsers: ArrayList<User>?= null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.my_friend_chat_list)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()

    }

    override fun onResume() {
        super.onResume()
        getAllConversation()
    }

    private fun getAllConversation() {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("conversation").child(FirebaseAuth.getInstance().currentUser!!.uid)
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for(conversationId in snapshot.children){
                        getSenderListFromConversationId(conversationId.key)
                    }
                }
            })
    }


    private fun getSenderListFromConversationId(conversationId: String?) {

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("message").child(conversationId!!)
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageData: Message? = snapshot.getValue(Message::class.java)
                    if (messageData!=null) {

//                        if(messageData.sender.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
//                            if(!listOfSenders.contains(messageData.receiver))
//                            listOfSenders.add(messageData.receiver)
//                            getAllLastConversationBetweenSenderAndReceiver(messageData.receiver)
//                        }
//
//                        if(messageData.receiver.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
//                            if(!listOfSenders.contains(messageData.sender))
//                            listOfSenders.add(messageData.sender)
//                            getAllLastConversationBetweenSenderAndReceiver(messageData.sender)
//                        }

//                        Logger.d("listof senders ---->"+listOfSenders.size.toString())

                        if (!listOfSenders.contains(messageData.sender) && !messageData.sender.equals(FirebaseAuth.getInstance().uid.toString())){
                            listOfSenders.add(messageData.sender)
                            getAllLastConversationBetweenSenderAndReceiver(messageData.sender)
                        }
                    }
                }
            })
    }

    private fun getAllLastConversationBetweenSenderAndReceiver(sender: String) {
        val messageBetweenSenderAndReceiver = ArrayList<Message>()
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("message")
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    myfriendChatList.clear()
                    messageBetweenSenderAndReceiver.clear()

                    for(messages in snapshot.children){
//                        Logger.d("messages---->"+messages.toString())
                        val data = messages.value as HashMap<String,Any>
                        val messageData = Message(
                            data["isSeen"]!!.toString().toBoolean(),
                            data["msg"].toString(), data["receiver"].toString(), data["sender"].toString(),
                            data["timeSpam"]!!.toString().toLong())
                        if (messageData!=null) {
                            if (messageData.sender.equals(sender)&& messageData.receiver.equals(FirebaseAuth.getInstance().uid.toString()) || messageData.sender.equals(FirebaseAuth.getInstance().uid.toString())&& messageData.receiver.equals(sender) ){
                                messageBetweenSenderAndReceiver.add(messageData)
                            }
                        }

                    }

                    val sortedMessageList: List<Message> = messageBetweenSenderAndReceiver.sortedWith(compareBy (Message::timeSpam)).reversed()
                    Logger.d("sortedMessageList :"+ sortedMessageList)
                    if(sortedMessageList[0].receiver.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
                        getUserInfoFromReceiver(sortedMessageList[0].sender,sortedMessageList[0].timeSpam,sortedMessageList[0].msg,getUnreadCount(sortedMessageList),myfriendChatList)
                    }else{
                        getUserInfoFromReceiver(sortedMessageList[0].receiver,sortedMessageList[0].timeSpam,sortedMessageList[0].msg,getUnreadCount(sortedMessageList),myfriendChatList)
                    }

                }

                private fun getUnreadCount(
                    sortedMessageList: List<Message>
                ): String {
                    var count: Int = 0;
                    for(item in sortedMessageList){

                        if(!item.isSeen){
                            if(!item.sender.equals(FirebaseAuth.getInstance().currentUser!!.uid))
                            count++
                        }
                    }
                    return count.toString()
                }

            })
    }

    private fun getUserInfoFromReceiver(
        receiver: String,
        timeSpam: Long,
        msg: String,
        unreadCount: String,
        myfriendChatList: ArrayList<MyFriendChatModel>
    ) {

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("users").child(receiver)
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)

                    myfriendChatList.add(
                                MyFriendChatModel(
                                    user!!.amiggosID,
                                    user.deviceToken,
                                    user.email,
                                    user.fcmToken,
                                    user.image,
                                    user.name,
                                    timeSpam.toString(),
                                    msg,
                                    unreadCount
                                )
                            )

                    Logger.d("myfriendChatList"+myfriendChatList.toString())

                    val tempData: List<MyFriendChatModel> = myfriendChatList.sortedWith(compareBy (MyFriendChatModel::time)).reversed()


                    binding.friendRecyclerview.setHasFixedSize(true)
                    binding.friendRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
                    adapter = MyFriendChatAdapter(tempData, object : MyFriendChatListListener {
                        override fun onMyFriendChatListClicked(myFriendChatModel: MyFriendChatModel) {

                            val intentActivity =
                                Intent(applicationContext, MessageActivity::class.java)
                            intentActivity.putExtra(ConstantLib.FRIEND_ID, myFriendChatModel.amiggosID.toString())
                            intentActivity.putExtra(ConstantLib.FRIENDNAME, myFriendChatModel.name)
                            intentActivity.putExtra(
                                ConstantLib.FRIENDIMAGE,
                                myFriendChatModel.image
                            )
                            startActivity(intentActivity)
                        }

                    })
                    binding.friendRecyclerview.adapter = adapter
                }

            })
    }



    private fun getAllUsers() {

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Database error", databaseError.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (user: DataSnapshot in snapshot.children) {
                    val userData: User? = user.getValue(User::class.java)
                    listOfAllUsers.add(userData!!)
                }

            }
        })

    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klBtnChatTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}