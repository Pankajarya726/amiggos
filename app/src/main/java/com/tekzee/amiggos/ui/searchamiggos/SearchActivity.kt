package com.tekzee.amiggos.ui.searchamiggos

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.OnlineFriendActivityBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.util.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.searchamiggos.adapter.SearchAdapter
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendData
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.SimpleCallback
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchActivity : BaseActivity(), SearchPresenter.SearchMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, SearchAdapter.HomeItemClick {


    private var lastLocation: LatLng? = null
    lateinit var binding: OnlineFriendActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var searchPresenterImplementation: SearchPresenterImplementation? = null
   


    private val mLoadingData = SearchFriendData(loadingStatus = true)
    private var onlineFriendPageNo = 0
    private var mydataList = ArrayList<SearchFriendData>()
    private var adapter: SearchAdapter? = null

    override fun onStart() {
        super.onStart()
        startLocationUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.online_friend_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        searchPresenterImplementation = SearchPresenterImplementation(this, this)
        setupToolBar()


        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            onlineFriendPageNo = 0
            if(it.isEmpty()){
                callOnlineFriendApi(false, "")
            }else{
                callOnlineFriendApi(false, it.toString())
            }
        }

    }

    private fun callOnlineFriendApi(requestDatFromServer: Boolean, searchvalue: String) {

        InitGeoLocationUpdate.stopLocationUpdate(this)
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("global_search", "1")
        input.addProperty("page_no", onlineFriendPageNo)
        input.addProperty("our_story_id", "")
        input.addProperty("latitude", lastLocation!!.latitude)
        input.addProperty("longitude", lastLocation!!.longitude)
        searchPresenterImplementation!!.getNearByUser(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }



    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(this, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                onlineFriendPageNo = 0
                callOnlineFriendApi(false, "")
            }
        })
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klAmiggos
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onOnlineFriendSuccess(responseData: SearchFriendResponse?) {
        onlineFriendPageNo++
        mydataList = responseData!!.data as ArrayList<SearchFriendData>
        setupRecyclerOnlineFriend()
    }

    private fun setupRecyclerOnlineFriend() {
        val onlineFriendRecyclerview: RecyclerView = findViewById(R.id.friend_recyclierview)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = SearchAdapter(
            mContext = this,
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)
    }

    override fun onOnlineFriendInfiniteSuccess(responseData: SearchFriendResponse?) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }

    override fun onOnlineFriendFailure(responseData: String) {
        if(mydataList.size>0){
        adapter?.setLoadingStatus(false)
        mydataList.removeAt(mydataList.size - 1)
        adapter?.notifyDataSetChanged()
        }
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOnlineFriendApi(true, "")
    }

    override fun itemClickCallback(
        adapterPosition: Int
    ) {
        val intent = Intent(applicationContext,FriendProfile::class.java)
        intent.putExtra("from","SearchActivity")
        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[adapterPosition].userid.toString())
        startActivity(intent)
    }

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onDestroy() {
        super.onDestroy()
        InitGeoLocationUpdate.stopLocationUpdate(this)
    }


}