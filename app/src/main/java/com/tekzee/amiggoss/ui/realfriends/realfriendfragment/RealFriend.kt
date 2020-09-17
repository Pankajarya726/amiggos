package com.tekzee.amiggoss.ui.realfriends.realfriendfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.RealFriendFragmentBinding
import com.tekzee.amiggoss.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggoss.ui.profiledetails.AProfileDetails
import com.tekzee.amiggoss.ui.realfriends.adapter.RealFriendAdapter
import com.tekzee.amiggoss.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RealFriend: BaseFragment(), RealFriendPresenter.RealFriendMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{



    private val mLoadingData = RealFriendV2Response.Data.RealFreind(loadingStatus = true)
    lateinit var binding: RealFriendFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var realFriendPresenterImplementation: RealFriendPresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<RealFriendV2Response.Data.RealFreind>()
    private var adapter: RealFriendAdapter? = null
    private lateinit var onlineFriendRecyclerview: RecyclerView
    private var isFragmentVisible= false


    companion object {
        private val realfriend: RealFriend? = null


        fun newInstance(): RealFriend {
            if(realfriend == null){
                return RealFriend()
            }
            return realfriend
        }
    }



    override fun onResume() {
        super.onResume()
        Logger.d("Onresume---> realfriend")
    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode ==2 ){
            realFriendPageNo = 0
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
            callRealFriendApi(false, "", isFragmentVisible)
        }
    }


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.real_friend_fragment,container,false)
        myView = binding.root
        return myView

    }


    private fun setupClickListener() {
        binding.error.errorLayout.setOnClickListener {
            callRealFriendApi(false, "", isFragmentVisible)
        }
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreference = SharedPreference(context!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation = RealFriendPresenterImplementation(this,context!!)
        onlineFriendRecyclerview = myView!!.findViewById(R.id.real_friend_fragment)
        setupRecyclerRealFriend()
        setupClickListener()

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            realFriendPageNo = 0
            if(it.isEmpty()){
                callRealFriendApi(false, "",isFragmentVisible)
            }else{
                callRealFriendApi(false, it.toString(),isFragmentVisible)
            }
        }
        callRealFriendApi(false, "", isFragmentVisible)
    }

    private fun callRealFriendApi(
        requestDatFromServer: Boolean,
        searchvalue: String,
        fragmentVisible: Boolean
    ) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", realFriendPageNo)
        realFriendPresenterImplementation!!.doCallRealFriendApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer,
            fragmentVisible
        )

    }

    override fun validateError(message: String) {

        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }


    override fun onRealFriendSuccess(responseData: List<RealFriendV2Response.Data.RealFreind>) {
        realFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
        setupErrorVisibility()
    }

    private fun setupRecyclerRealFriend() {

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


    override fun onRealFriendInfiniteSuccess(responseData: List<RealFriendV2Response.Data.RealFreind>) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
        setupErrorVisibility()
    }


    override fun onRealFriendFailure(message: String) {
        adapter?.setLoadingStatus(false)
        if(mydataList.size>0 && realFriendPageNo > 0){
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }else{
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
        }

        setupErrorVisibility()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callRealFriendApi(true, "", isFragmentVisible)
    }


    override fun itemClickCallback(position: Int) {
//        val intent = Intent(activity, FriendProfile::class.java)
//        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
//        intent.putExtra("from","RealFriend")
//        //intent.putExtra(ConstantLib.OURSTORYID,mydataList[position].isRelateOurMemory.ourStoryId.toString())
//        startActivity(intent)


        val intent = Intent(activity, AProfileDetails::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
        intent.putExtra(ConstantLib.IS_MY_FRIEND, mydataList[position].isMyFriend)
        intent.putExtra(ConstantLib.IS_MY_FRIEND_BLOCKED, mydataList[position].isMyFriendBlocked)
        intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].profile)
        intent.putExtra(ConstantLib.NAME, mydataList[position].name)
        intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
        intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
        intent.putExtra("from", "RealFriend")
        startActivityForResult(intent,100)
    }


    override fun onStop() {
        super.onStop()
        realFriendPresenterImplementation!!.onStop()
    }



    fun setupErrorVisibility(){
        if(mydataList.size == 0){
            binding.error.errorLayout.visibility = View.VISIBLE
            binding.mainlayout.visibility = View.GONE
        }else{
            binding.mainlayout.visibility = View.VISIBLE
            binding.error.errorLayout.visibility = View.GONE
        }
    }

}