package com.tekzee.amiggos.ui.invitefriendnew

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ActivityOurMemoriesBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.invitefriendnew.adapter.InviteFriendBookingAdapter
import com.tekzee.amiggos.ui.invitefriendnew.model.GetUserForInviteResponse
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Successtoast
import com.tekzee.amiggos.util.Utility
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class InviteFriendNewActivity : BaseActivity(),
    InviteFriendNewPresenter.InviteFriendPresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    InviteFriendBookingAdapter.InviteFriendClick {

    private lateinit var adapter: InviteFriendBookingAdapter
    private lateinit var binding: ActivityOurMemoriesBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var selectedIds: String = ""
    private var inviteFriendImplementation: InviteFriendNewImplementation? = null
    private var mydataList = ArrayList<GetUserForInviteResponse.Data.User>()
    private val mLoadingData = GetUserForInviteResponse.Data.User(loadingStatus = true)
    private var onlineFriendPageNo = 0

    companion object {
        var selectUserIds: HashSet<Int> = HashSet()
        var undoUserIds: HashSet<Int> = HashSet()
        var ourMemoryId: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_memories)
        Log.e("TAG--->",InviteFriendNewActivity::class.java.simpleName)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        inviteFriendImplementation = InviteFriendNewImplementation(this, this)
        setupLanguage()
        setupRecyclerView()
        setupclickListener()
        doCallGetFriends(false, "")

    }


    private fun setupRecyclerView() {
        val onlineFriendRecyclerview: RecyclerView = findViewById(R.id.real_friend_fragment)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = InviteFriendBookingAdapter(
            mContext = this,
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this,
            languageData
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter.setLoadingStatus(true)
    }

    private fun doCallGetFriends(requestDatFromServer: Boolean, searchvalue: String) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("page_no", onlineFriendPageNo)
        input.addProperty("search", searchvalue)
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        inviteFriendImplementation!!.doCallGetFriends(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )


    }


    @SuppressLint("CheckResult")
    private fun setupclickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }



//        RxTextView.textChanges(binding.searchfriend).filter { it.length > 2 }
//            .debounce(1000, TimeUnit.MILLISECONDS).subscribeOn(
//                Schedulers.io()
//            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            if (it.isEmpty()) {
//                onlineFriendPageNo = 0
//                mydataList.clear()
//                adapter.notifyDataSetChanged()
//                doCallGetFriends(false, it.toString())
//            } else {
//                onlineFriendPageNo = 0
//                mydataList.clear()
//                adapter.notifyDataSetChanged()
//                doCallGetFriends(false, it.toString())
//            }
//        }


        RxTextView.textChanges(binding.searchfriend).filter { it.length > 2 }.debounce(1000,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.e("TEsgin---->",it.toString())
            if (it.isEmpty()) {
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter.notifyDataSetChanged()
                doCallGetFriends(false, it.toString())
            } else {
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter.notifyDataSetChanged()
                doCallGetFriends(false, it.toString())
            }
        }



        binding.btnInviteFriend.setOnClickListener {
            callSendRequest(toCommaSeparated()!!)
        }

    }

    private fun callSendRequest(toCommaSeparated: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        input.addProperty("friend_id", toCommaSeparated)
        input.addProperty("unfriend_id", toUndoCommaSeparated())
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        inviteFriendImplementation!!.doCallInviteFriendApi(
            input,
            Utility.createHeaders(sharedPreference)
        )

    }


    private fun setupLanguage() {
        binding.txtTitle.text = languageData!!.invitefriend
        if (intent.getStringExtra(ConstantLib.FROM) == ConstantLib.FINALBASKET) {
            binding.txtSubtitle.text = intent.getStringExtra(ConstantLib.MESSAGE)
        } else {
            binding.txtSubtitle.text = languageData!!.invitefriendhint
        }


        binding.btnInviteFriend.text = languageData!!.klDone
        Glide.with(applicationContext)
            .load(sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
            .placeholder(R.drawable.noimage)
            .into(binding.profileImage)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    override fun onOurMemoriesSuccess(responseData: GetUserForInviteResponse?) {
        onlineFriendPageNo++
        mydataList =
            responseData!!.data.user as ArrayList<GetUserForInviteResponse.Data.User>
        setupRecyclerView()
//        for (item in mydataList){
//            if(item.isInvited ==1){
//                addToSelectedUserid(item.userid)
//            }
//        }
        Log.e("Selected userids--->","---"+toCommaSeparated())
        Log.e("undo userids","---"+toUndoCommaSeparated())
    }


    override fun onBackPressed() {
        if (intent.getStringExtra(ConstantLib.FROM) == ConstantLib.FINALBASKET) {
            val intent = Intent(applicationContext, AHomeScreen::class.java)
            startActivity(intent)
            selectUserIds.clear()
            undoUserIds.clear()
            finishAffinity()
        } else {
            super.onBackPressed()
        }


    }


    override fun onOurMemoriesSuccessInfinite(responseData: GetUserForInviteResponse?) {

        onlineFriendPageNo++
        adapter.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data.user)
        adapter.notifyDataSetChanged()
//        for (item in responseData.data.user){
//            if(item.isInvited ==1){
//                addToSelectedUserid(item.userid)
//            }
//        }
    }


    override fun onOurMemoriesFailure(message: String) {

        if (mydataList.size > 0) {
            adapter.setLoadingStatus(false)
            mydataList.removeAt(mydataList.size - 1)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onFriendInviteSuccess(message: String) {

        Successtoast(message)
        val intent = Intent(applicationContext, AHomeScreen::class.java)
        startActivity(intent)
        selectUserIds.clear()
        undoUserIds.clear()
        finishAffinity()
    }

    override fun onFriendInviteFailure(message: String) {
        Errortoast(message)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    override fun onDestroy() {
        super.onDestroy()
        selectUserIds.clear()
        undoUserIds.clear()
    }


    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter.notifyDataSetChanged()
        doCallGetFriends(true, "")
    }


    override fun itemClickCallback(
        position: Int,
        realFreind: GetUserForInviteResponse.Data.User,
        type: Int
    ) {

        if(realFreind.isInvited==1){
            if (type == 1) {
                undoUserIds.add(realFreind.userid)
            } else {
                undoUserIds.remove(realFreind.userid)

            }
        }
        if (type == 1) {
            selectUserIds.remove(realFreind.userid)
        } else {
            if(realFreind.isInvited == 0)
            addToSelectedUserid(realFreind.userid)
        }
        adapter.notifyItemChanged(position)

        Log.e("Selected userids--->","---"+toCommaSeparated())
        Log.e("undo userids","---"+toUndoCommaSeparated())

    }


    fun addToSelectedUserid(userid: Int) {
        selectUserIds.add(userid)
    }

    fun toCommaSeparated(): String? {
        var result = ""
        if (selectUserIds.size > 0) {
            val sb = StringBuilder()
            for (s in selectUserIds) {
                sb.append(s).append(",")
            }
            result = sb.deleteCharAt(sb.length - 1).toString()
        }
        return result
    }
    fun toUndoCommaSeparated(): String? {
        var result = ""
        if (undoUserIds.size > 0) {
            val sb = StringBuilder()
            for (s in undoUserIds) {
                sb.append(s).append(",")
            }
            result = sb.deleteCharAt(sb.length - 1).toString()
        }
        return result
    }
}
