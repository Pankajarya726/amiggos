package com.tekzee.amiggos.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MessageFragmentBinding
import com.tekzee.amiggos.ui.chatnew.ChatActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.message.adapter.MessageAdapter
import com.tekzee.amiggos.ui.message.model.MyConversation
import com.tekzee.amiggos.ui.message.model.MyFriendChatModel
import com.tekzee.amiggos.ui.addusers.AddUserFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class MessageActivity : BaseActivity(), MessageEvent, KodeinAware,
    MessageClickListener {

    private lateinit var adapter: MessageAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance()
    val prefs: SharedPreference by instance()
    val factory: MessagesViewModelFactory by instance()

    companion object {
        fun newInstance() = MessageActivity()
    }

    private var binding: MessageFragmentBinding? = null
    private lateinit var viewModel: MessageViewModel

 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.message_fragment)
        setupAdapter()
        viewModel = ViewModelProvider(this, factory).get(MessageViewModel::class.java)
        binding!!.messageviewmodel = viewModel
        viewModel.messageEvent = this
        binding!!.headertitle.text = languageConstant.messages
        getAllConversation()
        setupclickListener()
        viewModel.updateUserTime()
    }

    private fun setupclickListener() {
        binding!!.addchat.setOnClickListener {
            val intent = Intent(applicationContext, AddUserFragment::class.java)
            startActivity(intent)
        }
    }


    private fun getAllConversation() {
        onStarted()
        viewModel.getAllMyConversation(
            prefs.getValueInt(ConstantLib.USER_ID).toString()
        ).observe(this,
            Observer {
                val newlist = ArrayList<MyConversation>()
                newlist.addAll(it)
                adapter.submitList(newlist)
                onLoaded()
            })
    }

    private fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.messageRecyclerview.visibility = View.GONE
    }

    private fun onLoaded() {
        binding!!.progressCircular.visibility = View.GONE
        binding!!.messageRecyclerview.visibility = View.VISIBLE

    }

    private fun setupAdapter() {
        adapter = MessageAdapter(this,prefs.getValueInt(ConstantLib.USER_ID).toString(),applicationContext)
        binding!!.messageRecyclerview.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.messageRecyclerview.adapter = adapter
    }


    override fun onDrawerClicked() {
       onBackPressed()
    }

    override fun onAddChatClicked() {
//        if(prefs.getValueString(ConstantLib.TYPE).equals("3")){
//            findNavController().navigate(R.id.addUserFragment)
//        }else{
//            findNavController().navigate(R.id.addUserActivity)
//        }

    }

    override fun onItemClicked(
        position: Int,
        listItem: MyConversation
    ) {

        val myFriendChatModel = MyFriendChatModel()
//        myFriendChatModel.amiggosID = id.toString()
        myFriendChatModel.name = listItem.name
        myFriendChatModel.image = listItem.image
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,listItem.conversationid!!.split("_")[1].toString())
        intent.putExtra(ConstantLib.CHAT_DATA,myFriendChatModel)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.ondestroyListeners()
    }

    override fun validateError(message: String) {

    }

}
