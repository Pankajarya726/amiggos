package com.tekzee.amiggos.ui.notification

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.NotificationActivityBinding
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.friendprofile.model.UserFriendData
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.notification.adapter.NotificationAdapter
import com.tekzee.amiggos.ui.notification.model.NotificationData
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.storieview.StorieViewActivity
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView


class NotificationActivity : BaseActivity(), NotificationPresenter.NotificationMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, NotificationAdapter.HomeItemClick {
    var mDataList: ArrayList<StoriesData> = ArrayList()
    private var position: Int? = null
    private lateinit var binding: NotificationActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var notificationPresenterImplementation: NotificationPresenterImplementation? = null
    private var mydataList = ArrayList<NotificationData>()
    private var adapter: NotificationAdapter? = null
    private var pageNo = 0
    private val mLoadingData = NotificationData(loadingStatus = true)
    var notificationRecyclerView: RecyclerView? = null
    var p: Paint = Paint()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.notification_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        notificationPresenterImplementation = NotificationPresenterImplementation(this, this)
        callGetNotificationApi(false)
        setupToolBar()
        setupRecyclerView()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.clearall.setOnClickListener {
            clearAllNotificationConfirmation()
        }
    }

    private fun callClearAllNotificationApi(toString: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("notification_id", toString)
        notificationPresenterImplementation!!.doCallClearNotification(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callGetNotificationApi(requestDatFromServer: Boolean) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("page_no", pageNo)
        notificationPresenterImplementation!!.doCallGetNotification(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klNotificationTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecyclerView() {
        notificationRecyclerView = findViewById(R.id.notification_recyclerview)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        notificationRecyclerView!!.layoutManager = layoutManager
        adapter = NotificationAdapter(
            mContext = this,
            mRecyclerView = notificationRecyclerView!!,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        notificationRecyclerView!!.adapter = adapter
        adapter?.setLoadingStatus(true)

    }


    override fun onGetNotificationSuccess(responseData: NotificationResponse?) {
        pageNo++
        mydataList = responseData!!.data as ArrayList<NotificationData>
        setupRecyclerView()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onNotificationInfiniteSuccess(responseData: NotificationResponse?) {
        pageNo++
        mydataList.removeAt(mydataList.size - 1)
        adapter?.notifyDataSetChanged()
        adapter?.setLoadingStatus(true)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }

    override fun onNotificationFailure(responseData: String) {
        adapter?.setLoadingStatus(false)
        mydataList.removeAt(mydataList.size - 1)
        adapter?.notifyDataSetChanged()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callGetNotificationApi(true)
    }

    override fun itemClickCallback(position: Int) {
        val gson = Gson()
        val userFriendData: UserFriendData =
            gson.fromJson(mydataList.get(position).data, UserFriendData::class.java)
        when (mydataList.get(position).notificationKey) {

            1 -> {
//                gotoDocRejected(userFriendData.userid.toString())
                Logger.d("document verified, will not go anywhere")
            }
            2 -> {
                //friend_id (s) , user_id (r)
                gotToFriendProfile(userFriendData.userid.toString())
            }
            3 -> {
                gotToFriendProfile(userFriendData.friendId.toString())
            }
            4 -> {
                callStorieJoinViewApi(userFriendData.our_story_id.toString(), mydataList[position].id.toString())
            }
            5 -> {
                gotoSendPartyInvitation()
            }
            6 -> {
                gotoDocRejected(userFriendData.userid.toString())
            }
            7 -> {
                callStorieJoinViewApi(userFriendData.our_story_id.toString(), mydataList[position].id.toString())
            }
            8 -> {
                gotoSendPartyInvitation()
            }
            9 -> {
                //nothing to be implemented
            }
            10 -> {
                gotoBookingScreen()
            }
        }
    }

    private fun callStorieJoinViewApi(our_story_id: String, notificationid: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("our_story_id", our_story_id)
        notificationPresenterImplementation!!.doCallStorieViewApi(
            notificationid,
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun gotoBookingScreen() {

    }


    private fun gotoDocRejected(userid: String) {
        val intent = Intent(applicationContext, AttachIdActivity::class.java)
        intent.putExtra(ConstantLib.FROM, ConstantLib.NOTIFICAION)
        intent.putExtra(ConstantLib.USER_DATA, userid)
        startActivity(intent)
    }

    private fun gotoSendPartyInvitation() {

        val intent = Intent(applicationContext, PartyDetailsActivity::class.java)
        startActivity(intent)

    }

    private fun gotToFriendProfile(friendId: String) {
        val intent = Intent(applicationContext, FriendProfile::class.java)
        intent.putExtra("from","NotificationActivity")
        intent.putExtra(ConstantLib.FRIEND_ID, friendId)
        startActivity(intent)
    }

    override fun onClearNotificationSuccess(commonResponse: CommonResponse) {
        pageNo = 0
        mydataList.clear()
        adapter!!.notifyDataSetChanged()
        callGetNotificationApi(false)
    }


    fun clearAllNotificationConfirmation() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Are you sure you want to clear all notification"
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            callClearAllNotificationApi("")
        }
        pDialog.show()
    }


    fun deleteNotificationConfirmation() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Are you sure you want to delete notification"
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            callClearAllNotificationApi(mydataList.get(position!!).id.toString())
        }
        pDialog.show()
    }


    override fun onItemLongClickListener(pos: Int) {
        position = pos
        deleteNotificationConfirmation()
    }


    fun gotoStorieView(
        mDataList: ArrayList<StoriesData>,
        notificationId: String
    ) {
        val intent =Intent(applicationContext, StorieViewActivity::class.java)
        intent.putExtra(ConstantLib.CONTENT, this.mDataList[0])
        intent.putExtra(ConstantLib.PROFILE_IMAGE, this.mDataList[0].imageUrl)
        intent.putExtra(ConstantLib.FROM,"NOTIFICATION")
        intent.putExtra(ConstantLib.NOTIFICAION_ID,notificationId)
        intent.putExtra(ConstantLib.USER_ID, this.mDataList[0].userid.toString())
        intent.putExtra(ConstantLib.USER_NAME, this.mDataList[0].name)
        startActivity(intent)
    }

    override fun onStorieSuccess(
        responseData: StorieResponse,
        notificationId: String
    ) {
        mDataList = responseData.data as ArrayList<StoriesData>
        gotoStorieView(mDataList,notificationId)
    }


}