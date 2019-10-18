package com.tekzee.amiggos.ui.notification

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.NotificationActivityBinding
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.friendprofile.model.UserFriendData
import com.tekzee.amiggos.ui.notification.adapter.NotificationAdapter
import com.tekzee.amiggos.ui.notification.model.NotificationData
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class NotificationActivity: BaseActivity(), NotificationPresenter.NotificationMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, NotificationAdapter.HomeItemClick {


    private lateinit var binding: NotificationActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var notificationPresenterImplementation: NotificationPresenterImplementation? = null
    private var mydataList = ArrayList<NotificationData>()
    private var adapter: NotificationAdapter? = null
    private var pageNo = 0
    private val mLoadingData = NotificationData(loadingStatus = true)
    var notificationRecyclerView: RecyclerView? = null
    var p:Paint  = Paint()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.notification_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        notificationPresenterImplementation = NotificationPresenterImplementation(this,this)
        callGetNotificationApi(false)
        setupToolBar()
        setupRecyclerView()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.clearall.setOnClickListener{
           clearAllNotificationConfirmation()
        }
    }

    private fun callClearAllNotificationApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("notification_id  ", "")
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
        notificationRecyclerView= findViewById(R.id.notification_recyclerview)
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

        enableSwipe()
    }

    private fun enableSwipe() {

        var simpleCallback: SimpleCallback = object : SimpleCallback(0, ItemTouchHelper.LEFT){


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               var position = viewHolder.adapterPosition
                if(direction == ItemTouchHelper.LEFT){
                    var deleteData = mydataList.get(position)
                    var deletePosition = position
                    adapter!!.removeItem(position)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    val itemView : View = viewHolder.itemView
                    val height =itemView.getBottom().toFloat() - itemView.getTop().toFloat()
                    val width = height/3
                    if(dX >0){
                        p.setColor(Color.parseColor("#388E3C"));
                        val background:RectF  = RectF(itemView.getLeft().toFloat(), itemView.getTop().toFloat(), dX,itemView.getBottom().toFloat());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.plus_21);
                        val icon_dest:RectF = RectF(itemView.getLeft().toFloat() + width ,itemView.getTop().toFloat() + width,itemView.getLeft().toFloat()+ 2*width,itemView.getBottom().toFloat() - width)
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        }

        var itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(notificationRecyclerView)
    }

    override fun onGetNotificationSuccess(responseData: NotificationResponse?) {
        pageNo++
        mydataList = responseData!!.data as ArrayList<NotificationData>
        setupRecyclerView()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onNotificationInfiniteSuccess(responseData: NotificationResponse?) {
        pageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
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
        val userFriendData: UserFriendData = gson.fromJson(mydataList.get(position).data,UserFriendData::class.java)
        when(mydataList.get(position).notificationKey){

            1->{
                gotoDocRejected(userFriendData.userid.toString())
            }
            2->{

                gotToFriendProfile(userFriendData.friendId.toString())
            }
            3->{
                gotToFriendProfile(userFriendData.friendId.toString())
            }
            4 ->{
                //yet to be implemented
            }
            5->{
                gotoSendPartyInvitation()
            }
            6->{
                gotoDocRejected(userFriendData.userid.toString())
            }
            7->{
                //yet to be implemented
            }
            8->{
                gotoSendPartyInvitation()
            }
            9->{
             //nothing to be implemented
            }
            10->{
                gotoBookingScreen()
            }
        }
    }

    private fun gotoBookingScreen() {

    }


    private fun gotoDocRejected(userid: String) {
        val intent = Intent(applicationContext,AttachIdActivity::class.java)
        intent.putExtra(ConstantLib.FROM,ConstantLib.NOTIFICAION)
        intent.putExtra(ConstantLib.USER_DATA,userid)
        startActivity(intent)
    }

    private fun gotoSendPartyInvitation() {

            val intent = Intent(applicationContext,PartyDetailsActivity::class.java)
            startActivity(intent)

    }

    private fun gotToFriendProfile(friendId: String) {
        val intent  = Intent(applicationContext,FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,friendId)
        startActivity(intent)
    }

    override fun onClearNotificationSuccess(commonResponse: CommonResponse) {
        pageNo = 0
        mydataList.clear()
        adapter!!.notifyDataSetChanged()
        callGetNotificationApi(false)
    }


    fun clearAllNotificationConfirmation(){
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Are you sure you want to clear all notification"
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            callClearAllNotificationApi()
        }
        pDialog.show()
    }

}