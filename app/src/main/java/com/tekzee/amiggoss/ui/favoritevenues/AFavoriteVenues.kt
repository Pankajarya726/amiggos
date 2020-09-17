package com.tekzee.amiggoss.ui.favoritevenues

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.ui.favoritevenues.adapter.AFavoriteVenueAdapter
import com.tekzee.amiggoss.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import com.tekzee.amiggoss.databinding.RealFavoriteFragmentBinding
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AFavoriteVenues(var friendId: String?) : BaseFragment(), AFavoriteVenuePresenter.AFavoriteVenuePresenterMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,AFavoriteVenueAdapter.HomeItemClick{



    private val mLoadingData = FavoriteVenueResponse.Data.FavoriteVenue(loadingStatus = true)
    lateinit var binding: RealFavoriteFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var realFriendPresenterImplementation: AFavoritePresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<FavoriteVenueResponse.Data.FavoriteVenue>()
    private var adapter: AFavoriteVenueAdapter? = null
    private lateinit var onlineFriendRecyclerview: RecyclerView



    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.real_favorite_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation = AFavoritePresenterImplementation(this,activity!!)
        onlineFriendRecyclerview = myView!!.findViewById(R.id.real_friend_fragment)
        setupRecyclerRealFriend()

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            realFriendPageNo = 0
            if(it.isEmpty()){
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callFavoriteVenue(false, "")
            }else{
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callFavoriteVenue(false, it.toString())
            }
        }


        return myView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFavoriteVenue(false, "")
    }

    private fun callFavoriteVenue(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("freind_id", friendId)
        realFriendPresenterImplementation!!.doCallFavoriteVenueApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }


    override fun validateError(message: String) {

        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }


    private fun setupRecyclerRealFriend() {

        val layoutManager = GridLayoutManager(activity,2, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = AFavoriteVenueAdapter(
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


    override fun onFavoriteVenueSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>) {
        realFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onFavoriteVenueInfiniteSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onFavoriteVenueFailure(message: String) {


        adapter?.setLoadingStatus(false)
        if(mydataList.size>0 && realFriendPageNo > 0){
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
        callFavoriteVenue(true, "")
    }


    override fun itemClickCallback(position: Int) {
//        val intent = Intent(activity, FriendProfile::class.java)
//        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
//        intent.putExtra("from","RealFriend")
//        //intent.putExtra(ConstantLib.OURSTORYID,mydataList[position].isRelateOurMemory.ourStoryId.toString())
//        startActivity(intent)
    }


    override fun onStop() {
        super.onStop()
        realFriendPresenterImplementation!!.onStop()
    }

}