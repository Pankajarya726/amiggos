package com.tekzee.amiggos.ui.realfriends.realfriendfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.RealFriendFragmentBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.realfriends.adapter.RealFriendAdapter
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendData
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RealFriend: BaseFragment(), RealFriendPresenter.RealFriendMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{



    private val mLoadingData = RealFriendData(loadingStatus = true)
    lateinit var binding: RealFriendFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var realFriendPresenterImplementation: RealFriendPresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<RealFriendData>()
    private var adapter: RealFriendAdapter? = null

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.real_friend_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation = RealFriendPresenterImplementation(this,activity!!)

        callRealFriendApi(false, "")

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            realFriendPageNo = 0
            if(it.isEmpty()){
                callRealFriendApi(false, "")
            }else{
                callRealFriendApi(false, it.toString())
            }
        }
        return myView
    }

    private fun callRealFriendApi(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", realFriendPageNo)
        realFriendPresenterImplementation!!.doCallRealFriendApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }

    override fun validateError(message: String) {


    }


    override fun onRealFriendSuccess(responseData: RealFriendResponse?) {
        realFriendPageNo++
        mydataList = responseData!!.data as ArrayList<RealFriendData>
        setupRecyclerRealFriend()
    }

    private fun setupRecyclerRealFriend() {
        val onlineFriendRecyclerview: RecyclerView = activity!!.findViewById(R.id.real_friend_fragment)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = RealFriendAdapter(
            mContext = activity!!,
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)
    }


    override fun onRealFriendInfiniteSuccess(responseData: RealFriendResponse?) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }


    override fun onRealFriendFailure(message: String) {
        adapter?.setLoadingStatus(false)
        if(mydataList.size>0 && realFriendPageNo !=0){
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
        callRealFriendApi(true, "")
    }


    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
        intent.putExtra(ConstantLib.OURSTORYID,mydataList[position].isRelateOurMemory.ourStoryId.toString())
        startActivity(intent)
    }

}