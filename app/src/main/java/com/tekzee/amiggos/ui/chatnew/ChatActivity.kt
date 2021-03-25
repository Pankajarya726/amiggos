package com.tekzee.amiggos.ui.chatnew

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import com.shreyaspatil.MaterialDialog.MaterialDialog
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ChatFragmentBinding
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.chatnew.adapter.ChatNewAdapter
import com.tekzee.amiggos.ui.chatnew.model.ChatMessage
import com.tekzee.amiggos.ui.message.model.MyFriendChatModel
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.util.hideKeyboard
import com.tekzee.amiggosvenueapp.ui.chat.ChatActivityListener
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ChatActivity : AppCompatActivity(), ChatEvent, KodeinAware, ChatActivityListener {
//    private lateinit var listener: BroadcastReceiver
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance()
    val prefs: SharedPreference by instance()
    val factory: ChatViewModelFactory by instance()
    private var binding: ChatFragmentBinding? = null
    private lateinit var viewModel: ChatViewModel

    private var chatdata: MyFriendChatModel? = null
    private lateinit var adapter: ChatNewAdapter
    private var friendId: String? = null
    private var myFirebaseUserid: String? = null


    companion object {
        var iamActiveOnChat = false
        var talkingUser:String = ""
        fun newInstance() = ChatActivity()
    }

    override fun onPause() {
        super.onPause()
        iamActiveOnChat = false
        talkingUser = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iamActiveOnChat = true
        binding = DataBindingUtil.setContentView(this, R.layout.chat_fragment)
        setUpClickListener()
        setupRecyclerview()
        viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        binding!!.chatviewmodel = viewModel
        viewModel.chatEvent = this
        myFirebaseUserid = FirebaseAuth.getInstance().currentUser!!.uid
        callGetChat()
//        listener = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                when (intent.action) {
//                    ConstantLib.CHAT_NOTIFICATION -> {
//                        val title = intent.getStringExtra("title")
//                        val body = intent.getStringExtra("body")
//                        Alerter.create(this@ChatActivity)
//                            .setTitle(title)
//                            .setText(body)
//                            .show()
//                    }
//                    else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
//                }
//
//            }
//        }
    }

    private fun callGetChat() {
        friendId = intent.getStringExtra(ConstantLib.FRIEND_ID)
        talkingUser = friendId.toString()
        chatdata = intent.getSerializableExtra(ConstantLib.CHAT_DATA) as MyFriendChatModel?
        binding!!.headertitle.text = chatdata!!.name

        var conversationId = ""
        if (prefs.getValueString(ConstantLib.UNIQUE_TIMESTAMP)!!.toLong() > friendId!!.toLong()) {
            conversationId = friendId + "_" + prefs.getValueString(ConstantLib.UNIQUE_TIMESTAMP).toString()
        } else {
            conversationId = prefs.getValueString(ConstantLib.UNIQUE_TIMESTAMP).toString() + "_" + friendId
        }
        Log.e("Chat for conversation id--->",conversationId)

        viewModel.doGetChatBetweenSenderAndReceiver(conversationId).observe(this, Observer {
            val listOfChat = ArrayList<ChatMessage>()
            for (item in it) {
                listOfChat.add(
                    ChatMessage(
                        item.isSeen,
                        item.msg,
                        item.receiver,
                        item.sender,
                        "",
                        prefs.getValueString(ConstantLib.NAME)!!,
                        chatdata!!.name!!,
                        chatdata!!.image!!,
                        item.message_id,
                        "",
                        item.timeSpam
                    )
                )
            }
            adapter.submitList(listOfChat)
            if (listOfChat.size > 0) {
                binding!!.recyclerviewChat.smoothScrollToPosition(listOfChat.size - 1)
                adapter.notifyDataSetChanged();
            }
            onLoaded()
        })
    }


    private fun setUpClickListener() {
        binding!!.ivsend.setOnClickListener {
            if (binding!!.etChat.text.trim().isEmpty()) {
                Errortoast(languageConstant.entermessage)
            } else {
                checkUserIsBlocked(friendId!!)
            }

        }

    }

    private fun checkUserIsBlocked(receiverId: String) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID).toString())
        input.addProperty("freind_id", receiverId)
//        input.addProperty("type", "1")
        viewModel.checkUserisBlocked(input)
    }

    private fun sendNotification(message: String, receiverId: String) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID).toString())
        input.addProperty("receiveramiggosid", receiverId)
        input.addProperty("type", "1")
        input.addProperty("message", message)
        viewModel.sendNotification(input)
    }


    fun onStarted() {
        binding!!.recyclerviewChat.visibility = View.GONE
        binding!!.progressCircular.visibility = View.VISIBLE
    }

    fun onLoaded() {
        binding!!.recyclerviewChat.visibility = View.VISIBLE
        binding!!.progressCircular.visibility = View.GONE

        ChatHelper.getReceiverFirebaseId(friendId!!,
            object : ReceiverIdInterface {
                override fun getReceiverId(
                    receiverId: String,
                    user: User
                ) {
                    viewModel.setMessageStatusToSeen(myFirebaseUserid!!, receiverId)
                }
            })
    }


    override fun onBackButtonPressed() {
        onBackPressed()
        hideKeyboard(binding!!.drawerIcon)
    }

    override fun isBlockedUserSuccess() {
        ChatHelper.getReceiverFirebaseId(friendId!!,
            object : ReceiverIdInterface {
                override fun getReceiverId(
                    receiverId: String,
                    reciverUser: User
                ) {
                    if (binding!!.etChat.text.trim().toString().isNotEmpty()) {

                        ChatHelper.sendMessage(
                            myFirebaseUserid!!,
                            receiverId,
                            binding!!.etChat.text.trim().toString(),
                            false,
                            System.currentTimeMillis(),
                            reciverUser,
                            prefs.getValueString(ConstantLib.UNIQUE_TIMESTAMP).toString()
                        )
                        sendNotification(
                            binding!!.etChat.text.trim().toString(),
                            friendId!!
                        )
                        binding!!.etChat.setText("")
                    }

                }
            })
    }

    override fun isBlockedUserFailure(message: String) {
        Errortoast(message)
    }

    override fun onFailure(message: String) {
        Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        Utility.logOut(applicationContext, message)
    }


    private fun setupRecyclerview() {
        binding!!.recyclerviewChat.setHasFixedSize(true)
        val mlayoutManager = LinearLayoutManager(this)
        mlayoutManager.stackFromEnd = true
        binding!!.recyclerviewChat.layoutManager = mlayoutManager
        adapter = ChatNewAdapter(this, prefs)
        binding!!.recyclerviewChat.adapter = adapter

    }

    override fun onItemClicked(position: Int, listItem: ChatMessage) {
        val mDialog = MaterialDialog.Builder(this)
            .setTitle(languageConstant.delete)
            .setMessage(languageConstant.deletemessage)
            .setCancelable(false)
            .setPositiveButton(
                languageConstant.delete
            ) { dialogInterface, which ->
                viewModel.deleteSelectedMessage(listItem.message_id)
                dialogInterface.dismiss()
            }
            .setNegativeButton(
                languageConstant.klCancel
            ) { dialogInterface, which -> dialogInterface.dismiss() }
            .build()
        mDialog.show()

    }

    override fun onDestroy() {
        viewModel.removeListener()
        super.onDestroy()
    }


}
