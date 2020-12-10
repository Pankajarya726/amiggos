package com.tekzee.amiggos.ui.realfriends.realfriendfragment

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
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.RealFriendFragmentBinding
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.NearMeFragment
import com.tekzee.amiggos.ui.realfriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.realfriends.adapter.RealFriendAdapter
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggos.util.RxSearchObservable
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RealFriend : BaseFragment(), RealFriendPresenter.RealFriendMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, OnlineFriendAdapter.HomeItemClick {


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
    private var isFragmentVisible = false

    companion object {
        private val realfriend: RealFriend? = null


        fun newInstance(): RealFriend {

            if (realfriend == null) {
                return RealFriend()
            }
            return realfriend
        }
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 2) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.real_friend_fragment, container, false)
        myView = binding.root

        return myView

    }

    private fun setupView() {
        binding.hiddenSearchWithRecycler.searchBarSearchView.queryHint =
            languageData!!.klSearchTitle

        RxSearchObservable.fromView(binding.hiddenSearchWithRecycler.searchBarSearchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter(Predicate { t ->
                t.isNotEmpty()
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String>() { t ->
                realFriendPageNo = 0
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callRealFriendApi(false, t.toString(), isFragmentVisible)
            })

        val closeButton: View? =
            binding.hiddenSearchWithRecycler.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {

            binding.hiddenSearchWithRecycler.searchBarSearchView.clearFocus()
            binding.hiddenSearchWithRecycler.searchBarSearchView.isIconified = false
            binding.hiddenSearchWithRecycler.clearSearchview()
            realFriendPageNo = 0
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
            callRealFriendApi(false, "", isFragmentVisible)
        }
        setupRefreshLayout()
    }

    private fun setupRefreshLayout() {
        binding!!.refreshlayout.setOnRefreshListener {
            binding!!.refreshlayout.isRefreshing = false
            realFriendPageNo = 0
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
            callRealFriendApi(false, "", isFragmentVisible)
        }
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation =
            RealFriendPresenterImplementation(this, requireContext())
        onlineFriendRecyclerview = myView!!.findViewById(R.id.real_friend_recycler)
        setupRecyclerRealFriend()

        RxTextView.textChanges(binding.edtSearch).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
            realFriendPageNo = 0
            if (it.isEmpty()) {
                callRealFriendApi(false, "", isFragmentVisible)
            } else {
                callRealFriendApi(false, it.toString(), isFragmentVisible)
            }
        }
        callRealFriendApi(false, "", isFragmentVisible)
        setupView()

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
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }


    override fun onRealFriendSuccess(
        responseData: List<RealFriendV2Response.Data.RealFreind>,
        totalCount: Int,
        responseData1: RealFriendV2Response
    ) {
        //NearMeFragment.setRealFriendBadge(totalCount)
        realFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
        setupErrorVisibility(responseData1.message)
    }

    fun setupErrorVisibility(message: String) {
        if (mydataList.size == 0) {
            binding.errorLayout.visibility = View.VISIBLE
            binding.errortext.text = message
            onlineFriendRecyclerview.visibility = View.VISIBLE
        } else {
            onlineFriendRecyclerview.visibility = View.VISIBLE
            binding.errortext.text = ""
            binding.errorLayout.visibility = View.GONE
        }
    }


    private fun setupRecyclerRealFriend() {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = RealFriendAdapter(
            mContext = requireContext(),
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)
    }


    override fun onRealFriendInfiniteSuccess(
        responseData: List<RealFriendV2Response.Data.RealFreind>,
        responseData1: RealFriendV2Response
    ) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
        setupErrorVisibility(responseData1.message)
    }


    override fun onRealFriendFailure(message: String) {
        adapter?.setLoadingStatus(false)
        if (mydataList.size > 0 && realFriendPageNo > 0) {
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        } else {
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
        }

        setupErrorVisibility(message)
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callRealFriendApi(true, "", isFragmentVisible)
    }


    override fun itemClickCallback(position: Int) {
      if (Utility.checkProfileComplete(sharedPreference)) {
            val intent = Intent(activity, AProfileDetails::class.java)
            intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
            intent.putExtra(ConstantLib.IS_MY_FRIEND, mydataList[position].isMyFriend)
            intent.putExtra(
                ConstantLib.IS_MY_FRIEND_BLOCKED,
                mydataList[position].isMyFriendBlocked
            )
            intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].profile)
            intent.putExtra(ConstantLib.NAME, mydataList[position].name)
            intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
            intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
            intent.putExtra("from", "RealFriend")
            startActivityForResult(intent, 100)
        } else {

            val dialog: BottomDialogExtended =
                BottomDialogExtended.newInstance(
                    languageData!!.profilecompletedata,
                    arrayOf(languageData!!.yes)
                )
            dialog.show(childFragmentManager, "dialog")
            dialog.setListener { position ->
                val intent = Intent(requireContext(), AViewAndEditProfile::class.java)
                startActivity(intent)
            }
        }


    }


    override fun onStop() {
        super.onStop()
        realFriendPresenterImplementation!!.onStop()
    }


}