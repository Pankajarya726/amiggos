package com.tekzee.amiggos.ui.mymemories.fragment.ourmemories

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
import com.tekzee.amiggos.databinding.RealFriendFragmentBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.adapter.OurMemoriesAdapter
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model.OurMemoriesData
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class OurMemories: BaseFragment(), OurMemoriesPresenter.OurMemoriesMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{


    private val mLoadingData = OurMemoriesData(loadingStatus = true)
    lateinit var binding: RealFriendFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var ourMemoriesPresenterImplementation: OurMemoriesPresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<OurMemoriesData>()
    private var adapter: OurMemoriesAdapter? = null

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
        ourMemoriesPresenterImplementation = OurMemoriesPresenterImplementation(this,activity!!)
        setupViewData()
        callOurMemoriesApi(false, "")

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            realFriendPageNo = 0
            if(it.isEmpty()){
                callOurMemoriesApi(false, "")
            }else{
                callOurMemoriesApi(false, it.toString())
            }
        }
        return myView
    }


    private fun setupViewData() {
        binding.edtSearch.hint = languageData!!.klSearchTitle
    }

    private fun callOurMemoriesApi(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", realFriendPageNo)
        ourMemoriesPresenterImplementation!!.doCallOurMemoriesApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }

    override fun validateError(message: String) {


    }


    override fun onOurMemoriesSuccess(responseData: OurMemoriesResponse?) {
        realFriendPageNo++
        mydataList = responseData!!.data as ArrayList<OurMemoriesData>
        setupRecyclerRealFriend()
    }

    private fun setupRecyclerRealFriend() {
        val onlineFriendRecyclerview: RecyclerView = activity!!.findViewById(R.id.real_friend_fragment)
        val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = OurMemoriesAdapter(
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


    override fun onOurMemoriesInfiniteSuccess(responseData: OurMemoriesResponse?) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }



    override fun onOurMemoriesFailure(message: String) {
//        adapter?.setLoadingStatus(false)
//        mydataList.removeAt(mydataList.size - 1)
//        adapter?.notifyDataSetChanged()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOurMemoriesApi(true, "")
    }


    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
        startActivity(intent)
    }

}