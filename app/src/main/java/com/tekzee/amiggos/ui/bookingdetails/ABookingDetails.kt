package com.tekzee.amiggos.ui.bookingdetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ABookingDetailsBinding
import com.tekzee.amiggos.ui.bookingdetails.model.ABookingDetailAdapter
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggos.ui.friendinviteconfirmation.FriendInviteConfirmation
import com.tekzee.amiggos.ui.friendlist.FriendInviteListener
import com.tekzee.amiggos.ui.friendlist.model.FriendListData
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.ui.profiledetails.PosterOverlayView
import com.tekzee.amiggos.util.RxSearchObservable
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.hiddensearchrecyclerview.utils.HiddenSearchWithRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.referal_activity.*
import java.util.concurrent.TimeUnit

class ABookingDetails : BaseActivity(), ABookingDetailsPresenter.ABookingDetailsPresenterMainView {
    private var imageBuilder: StfalconImageViewer<String>? =null
    private var searchView: HiddenSearchWithRecyclerView? = null
    private var dataFromIntent: ABookingResponse.Data.BookingData? = null
    private lateinit var adapter: ABookingDetailAdapter
    private var data = ArrayList<FriendListData>()
    var binding: ABookingDetailsBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aBookingDetailPresenterImplementation: ABookingDetailPresenterImplementation? = null
    private var searchItem:String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_booking_details)
        dataFromIntent =
            intent.getSerializableExtra(ConstantLib.BOOKING_DATA) as ABookingResponse.Data.BookingData
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aBookingDetailPresenterImplementation = ABookingDetailPresenterImplementation(this, this)
        setupUi()
        setupClickListener()
        setupRecyclerView()
        callFriendList(searchItem)


    }

    private fun setupRecyclerView() {
        binding!!.nearfriendrecycler.setHasFixedSize(true)
        binding!!.nearfriendrecycler.layoutManager = LinearLayoutManager(this)
        adapter = ABookingDetailAdapter(data, languageData, object : FriendInviteListener {
            override fun onFrienInviteClicked(friendListData: FriendListData) {
                if (friendListData.isInvited == 0) {
                    callInviteFriend(friendListData.userid)
                }
            }

        })
        binding!!.nearfriendrecycler.adapter = adapter
    }

    private fun callInviteFriend(userid: Int) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", dataFromIntent!!.id.toString())
        input.addProperty("friend_id", userid.toString())
        aBookingDetailPresenterImplementation!!.doInviteFriend(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun callFriendList(name: String) {
        hideKeyboard()
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", dataFromIntent!!.id.toString())
        input.addProperty("name", searchItem)
        aBookingDetailPresenterImplementation!!.doGetFriendList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }


    @SuppressLint("CheckResult")
    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding!!.htabHeader.setOnClickListener {
            val image = arrayOf(dataFromIntent!!.venue_home_image)
            imageBuilder = StfalconImageViewer.Builder<String>(this,image){
                imageView, image ->  Glide.with(this).load(image).placeholder(R.drawable.noimage).into(imageView)
            }.withTransitionFrom(imageView)
                .withBackgroundColor(resources.getColor(R.color.black))
                .allowSwipeToDismiss(true)
                .withOverlayView(PosterOverlayView(this).apply {

                    onDeleteClick = {
                        imageBuilder!!.dismiss()
                    }


                }).show(true)
        }

        binding!!.txtDone.setOnClickListener {

            binding!!.bookingDetailLayout.visibility = View.VISIBLE
            binding!!.txtDone.visibility = View.GONE
            binding!!.txtInviteFriend.visibility = View.VISIBLE
            binding!!.recyclerviewlayout.visibility = View.GONE
            startActivity(Intent(this, FriendInviteConfirmation::class.java))
        }

        binding!!.txtInviteFriend.setOnClickListener {

            binding!!.bookingDetailLayout.visibility = View.GONE
            binding!!.txtDone.visibility = View.VISIBLE
            binding!!.txtInviteFriend.visibility = View.GONE
            binding!!.recyclerviewlayout.visibility = View.VISIBLE
        }




        searchView = findViewById<HiddenSearchWithRecyclerView>(R.id.hidden_search_with_recycler)
        RxSearchObservable.fromView(searchView!!.searchBarSearchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter(Predicate { t ->
                t.isNotEmpty()
            }).distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String>() { it ->
                data.clear()
                searchView!!.searchBarSearchView.clearFocus()
                adapter.notifyDataSetChanged()
                searchItem = it.toString()
                callFriendList(searchItem)
            })

    }

    private fun setupUi() {
        binding!!.bookingDetailLayout.visibility = View.VISIBLE
        binding!!.txtDone.visibility = View.GONE
        binding!!.txtInviteFriend.visibility = View.VISIBLE
        binding!!.recyclerviewlayout.visibility = View.GONE

        Glide.with(applicationContext).load(dataFromIntent!!.venue_home_image)
            .placeholder(R.drawable.noimage).into(binding!!.htabHeader)
         binding!!.txtName.text = dataFromIntent!!.name
        binding!!.txtLocation.text = dataFromIntent!!.booking_date
        binding!!.bookingid.text = "Booking Id : " + dataFromIntent!!.id.toString()
        binding!!.date.text = "Date : " + dataFromIntent!!.booking_date
        binding!!.starttime.text = "Start Time : " + dataFromIntent!!.booking_time
        binding!!.endtime.text = "End Time : " + dataFromIntent!!.booking_time
        binding!!.address.text = "Booking Code : " + dataFromIntent!!.booking_date
    }

    override fun onFriendListSuccess(responseData: FriendListResponse?) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()

    }

    override fun onFriendInviteSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendList("")
    }


    override fun validateError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }
}