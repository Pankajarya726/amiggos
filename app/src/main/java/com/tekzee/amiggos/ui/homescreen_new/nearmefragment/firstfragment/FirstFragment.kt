package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.nicolettilu.hiddensearchwithrecyclerview.HiddenSearchWithRecyclerView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.adapter.FirstFragmentAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.util.*
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class FirstFragment : BaseFragment(), FirstFragmentPresenter.FirstFragmentPresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, FirstFragmentAdapter.HomeItemClick {

    private var searchView: HiddenSearchWithRecyclerView? = null
    private var refreshView: SwipeRefreshLayout? = null
    private val mLoadingData = NearByV2Response.Data.NearestFreind(loadingStatus = true)
    private var sharedPreference: SharedPreference? = null
    private var lastLocation: LatLng? = null
    private var onlineFriendPageNo = 0
    private var firstFragmentPresenterImplementation: FirstFragmentPresenterImplementation? = null
    private var mydataList = ArrayList<NearByV2Response.Data.NearestFreind>()
    private var adapter: FirstFragmentAdapter? = null
    private var isFragmentVisible = false;

    companion object {
        private val firstFragment: FirstFragment? = null


        fun newInstance(): FirstFragment{
            if(firstFragment == null){
             return FirstFragment()
            }
            return firstFragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLocationUpdate()
    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
    }

    override fun onResume() {
        super.onResume()

    }


    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(activity!!, object :
            SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callOnlineFriendApi(false, "",isFragmentVisible)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.first_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        firstFragmentPresenterImplementation =
            FirstFragmentPresenterImplementation(this, activity!!)
        setupViews(view)
        setupRecyclerFirstFragment(view)
        return view
    }


    private fun setupRecyclerFirstFragment(view: View) {
        val onlineFriendRecyclerview: RecyclerView =
            view.findViewById(R.id.first_fragment_recyclierview)
        val layoutManager = GridLayoutManager(activity!!, 2, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = FirstFragmentAdapter(
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


    private fun callOnlineFriendApi(
        requestDatFromServer: Boolean,
        searchvalue: String,
        fragmentVisible: Boolean
    ) {
        hideKeyboard()
        InitGeoLocationUpdate.stopLocationUpdate(activity!!)
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", onlineFriendPageNo)
        input.addProperty("latitude", lastLocation!!.latitude)
        input.addProperty("longitude", lastLocation!!.longitude)
        firstFragmentPresenterImplementation!!.getNearByUser(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer,
            fragmentVisible
        )

    }

    override fun onOnlineFriendSuccess(responseData: List<NearByV2Response.Data.NearestFreind>) {
        onlineFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onOnlineFriendInfiniteSuccess(responseData: List<NearByV2Response.Data.NearestFreind>) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onOnlineFriendFailure(responseData: String) {
        if (mydataList.size > 0) {
            adapter?.setLoadingStatus(false)
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("CheckResult")
    private fun setupViews(view: View?) {
        searchView = view!!.findViewById<HiddenSearchWithRecyclerView>(R.id.hidden_search_with_recycler)


        RxSearchObservable.fromView(searchView!!.searchBarSearchView)
            .debounce(500,TimeUnit.MILLISECONDS)
            .filter(Predicate {
                t ->
                t.isNotEmpty()
            }).distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String>() {
                t ->
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                searchView!!.searchBarSearchView.clearFocus()
                callOnlineFriendApi(false, t.toString(), isFragmentVisible)
            })




//        searchView!!.searchBarSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(value: String?): Boolean {
//                Log.d("onQueryTextSubmit --->",value.toString())
//                return true
//            }
//
//            override fun onQueryTextChange(value: String?): Boolean {
//                Log.d("onQueryTextChange --->",value.toString())
//                return true
//            }
//
//        })
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOnlineFriendApi(true, "", isFragmentVisible)

    }

    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, AProfileDetails::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
        intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].profile)
        intent.putExtra(ConstantLib.NAME, mydataList[position].name)
        intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
        intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
        intent.putExtra("from", "GroupFriendActivity")
        startActivity(intent)
    }

    override fun storieClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onStop() {
        super.onStop()
        firstFragmentPresenterImplementation!!.onStop()
    }
}