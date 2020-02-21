package com.tekzee.amiggos.ui.onlinefriends

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.OnlineFriendActivityBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendData
import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class OnlineFriendActivity : BaseActivity(), OnlineFriendPresenter.OnlineFriendMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, OnlineFriendAdapter.HomeItemClick {


    lateinit var binding: OnlineFriendActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var onlineFriendPresenterImplementation: OnlineFriendPresenterImplementation? = null


    private val mLoadingData = OnlineFriendData(loadingStatus = true)
    private var onlineFriendPageNo = 0
    private var mydataList = ArrayList<OnlineFriendData>()
    private var adapter: OnlineFriendAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.online_friend_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        onlineFriendPresenterImplementation = OnlineFriendPresenterImplementation(this, this)
        setupToolBar()
        callOnlineFriendApi(false, "")

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


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", onlineFriendPageNo)
        onlineFriendPresenterImplementation!!.doCallOnlineFriendApi(
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
        binding.title.text = languageData!!.klActiveUserNavTitle
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onOnlineFriendSuccess(responseData: OnlineFriendResponse?) {
        onlineFriendPageNo++
        mydataList = responseData!!.data as ArrayList<OnlineFriendData>
        setupRecyclerOnlineFriend()
    }

    private fun setupRecyclerOnlineFriend() {
        val onlineFriendRecyclerview: RecyclerView = findViewById(R.id.friend_recyclierview)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = OnlineFriendAdapter(
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

    override fun onOnlineFriendInfiniteSuccess(responseData: OnlineFriendResponse?) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }

    override fun onOnlineFriendFailure(responseData: String) {
        adapter?.setLoadingStatus(false)
        if(mydataList.size>0 && onlineFriendPageNo !=0){
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }else{
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
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
        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[adapterPosition].userid.toString())
        intent.putExtra("from","OnlineFriendActivity")
        startActivity(intent)
    }

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}