package com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload

import android.annotation.SuppressLint
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
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.ourmemories.OurMemoriesActivity
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.adapter.OurMemoriesUploadAdapter
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListData
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class OurMemoriesUpload: BaseFragment(), OurMemoriesUploadPresenter.OurMemoriesUploadMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{
    private val mLoadingData = OurFriendListData(loadingStatus = true)
    lateinit var binding: RealFriendFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var ourMemoriesPresenterImplementation: OurMemoriesPresenterUploadImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<OurFriendListData>()
    private var adapter: OurMemoriesUploadAdapter? = null

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
        ourMemoriesPresenterImplementation = OurMemoriesPresenterUploadImplementation(this,activity!!)
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
        input.addProperty("our_story_id", OurMemoriesActivity.ourMemoryId)
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


    override fun onOurMemoriesSuccess(responseData: OurFriendListResponse?) {
        realFriendPageNo++
        mydataList = responseData!!.data as ArrayList<OurFriendListData>


        if (mydataList.size > 0) {
            for (i in 0 until mydataList.size) {
                val userid = mydataList.get(i).userid
                if (OurMemoriesActivity.selectUserIds.contains(userid)) {
                    mydataList.get(i).is_checked = true
                }
            }

        }
        setupRecyclerRealFriend()
    }

    private fun setupRecyclerRealFriend() {
        val onlineFriendRecyclerview: RecyclerView = activity!!.findViewById(R.id.real_friend_fragment)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = OurMemoriesUploadAdapter(
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


    override fun onOurMemoriesInfiniteSuccess(responseData: OurFriendListResponse?) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }



    override fun onOurMemoriesFailure(message: String) {
        adapter?.setLoadingStatus(false)
        if(mydataList.size>0)
        mydataList.removeAt(mydataList.size - 1)
        adapter?.notifyDataSetChanged()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOurMemoriesApi(true, "")
    }


    override fun itemClickCallback(position: Int) {
        mydataList[position].is_checked = !mydataList[position].is_checked

        if (mydataList[position].is_checked){

            if(!OurMemoriesActivity.selectUserIds.contains(mydataList[position].userid)){
                OurMemoriesActivity.selectUserIds.add(mydataList[position].userid)
            }

        }else{
            if(OurMemoriesActivity.selectUserIds.contains(mydataList[position].userid)){
                OurMemoriesActivity.selectUserIds.remove(mydataList[position].userid)
            }
        }



        adapter!!.notifyItemChanged(position)
//        Utility.addSelectedId(mydataList[position].userid,mydataList[position].is_checked)
//        adapter!!.notifyItemChanged(position)
    }

}