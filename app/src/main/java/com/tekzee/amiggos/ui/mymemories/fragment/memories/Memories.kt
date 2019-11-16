package com.tekzee.amiggos.ui.mymemories.fragment.memories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.MemoriesFragmentBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.mymemories.fragment.memories.adapter.MemoriesAdapter
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MemoriesData
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendData
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Memories: BaseFragment(), MemoriesPresenter.MemoriesMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{



    private val mLoadingData = MemoriesData(loadingStatus = true)
    lateinit var binding: MemoriesFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var memoriesPresenterImplementation: MemoriesPresenterImplementation? = null
    private var memoriesPageNo = 0
    private var mydataList = ArrayList<MemoriesData>()
    private var adapter: MemoriesAdapter? = null

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.memories_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        memoriesPresenterImplementation = MemoriesPresenterImplementation(this,activity!!)
        setupViewData()
        callMemoriesApi(false, "")

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            memoriesPageNo = 0
            if(it.isEmpty()){
                callMemoriesApi(false, "")
            }else{
                callMemoriesApi(false, it.toString())
            }
        }
        return myView
    }

    private fun setupViewData() {
        binding.edtSearch.hint = languageData!!.klSearchTitle
    }

    private fun callMemoriesApi(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("is_dashboard", "0")
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", memoriesPageNo)
        memoriesPresenterImplementation!!.docallMemoriesApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }

    override fun validateError(message: String) {


    }


    override fun onMemoriesSuccess(responseData: MyMemoriesResponse?) {
        memoriesPageNo++
        mydataList = responseData!!.data as ArrayList<MemoriesData>
        setupRecyclerRealFriend()
    }

    private fun setupRecyclerRealFriend() {
        val onlineFriendRecyclerview: RecyclerView = activity!!.findViewById(R.id.real_friend_fragment)
        val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = MemoriesAdapter(
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


    override fun onMemoriesInfiniteSuccess(responseData: MyMemoriesResponse?) {
        memoriesPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }


    override fun onMemoriesFailure(message: String) {
//        adapter?.setLoadingStatus(false)
//        mydataList.removeAt(mydataList.size - 1)
//        adapter?.notifyDataSetChanged()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callMemoriesApi(true, "")
    }


    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
        startActivity(intent)
    }

}