package com.tekzee.amiggos.ui.addusers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.AddUserActivityBinding
import com.tekzee.amiggos.ui.chatnew.ChatActivity
import com.tekzee.amiggos.ui.message.model.MyFriendChatModel
import com.tekzee.amiggos.util.*
import com.tekzee.amiggos.ui.addusers.adapter.AddUserAdapter
import com.tekzee.amiggos.ui.addusers.model.AddUserResponse
import io.reactivex.Observable
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class AddUserFragment : AppCompatActivity(), KodeinAware, AddUserEvent, AddUserClickListener {
    private val globalList = ArrayList<AddUserResponse.Data.Staff>()
    private var binding: AddUserActivityBinding? = null
    private lateinit var adapter: AddUserAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance()
    val prefs: SharedPreference by instance()
    val factory: AddUserViewModelFactory by instance()


    private lateinit var viewModel: AddUserViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.add_user_activity)
        viewModel = ViewModelProvider(this, factory).get(AddUserViewModel::class.java)
        binding!!.viewmodel = viewModel
        viewModel.event = this
        binding!!.headertitle.text = languageConstant.newmember
        callPromotersApi()
        setupClicklistener()
    }


    @SuppressLint("CheckResult")
    private fun setupClicklistener() {
        val obs: Observable<String> =
            RxTextView.textChanges(binding!!.searchUser).debounce(300, TimeUnit.MILLISECONDS)
                .map { charSequence -> charSequence.toString() }
        obs.subscribe { string ->
            if (string.isNotEmpty()) {
                filter(string)
            } else {
                runOnUiThread {
                    if (globalList.isNotEmpty())
                        adapter.submitList(globalList)
                }
            }
        }
    }

    private fun filter(text: String) {
        val filterdNames: ArrayList<AddUserResponse.Data.Staff> = ArrayList()
        for (s in globalList) {
            val searchString = s.firstName + " " + s.lastName
            if (searchString.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }
        //calling a method of the adapter class and passing the filtered list
        adapter.submitList(filterdNames)
    }


    private fun callPromotersApi() {
        Coroutines.main {
            viewModel.CallGetUsers()
        }
    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerviewSettings.visibility = View.GONE
    }

    override fun onLoaded(response: AddUserResponse) {
        setupAdapter(response)
        observeList()
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
        binding!!.recyclerviewSettings.visibility = View.GONE
        Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        Errortoast(message)
//        Utility.logoutUser(prefs, this)
    }

    private fun observeList() {
        viewModel.staffList.observe(this, Observer { listitem ->
            globalList.clear()
            globalList.addAll(listitem)
            val newlist = ArrayList<AddUserResponse.Data.Staff>()
            newlist.addAll(listitem)
            adapter.submitList(newlist)
            binding!!.progressCircular.visibility = View.GONE
            binding!!.recyclerviewSettings.visibility = View.VISIBLE
        })
    }

    private fun setupAdapter(response: AddUserResponse) {
        adapter = AddUserAdapter(this, response)
        binding!!.recyclerviewSettings.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.recyclerviewSettings.adapter = adapter
    }


    override fun onBackButtonPressed() {
        onBackPressed()
        hideKeyboard(binding!!.drawerIcon)
    }

    override fun onItemClicked(position: Int, listItem: AddUserResponse.Data.Staff) {
        val myFriendChatModel = MyFriendChatModel()
        myFriendChatModel.amiggosID = prefs.getValueInt(ConstantLib.USER_ID).toString()
        myFriendChatModel.name = listItem.firstName + " " + listItem.lastName
        myFriendChatModel.image = listItem.profileImage
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID, listItem.id.toString())
        intent.putExtra(ConstantLib.CHAT_DATA, myFriendChatModel)
        startActivity(intent)
    }


}