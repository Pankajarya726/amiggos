package com.tekzee.amiggos.ui.realamiggos

import android.annotation.SuppressLint
import android.content.Intent
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
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.realamiggos.adapter.RealAmiggosAdapter
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.RealAmiggosFragmentBinding
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RealAmiggos(var friendId: String?, var aProfileDetails: AProfileDetails) : BaseFragment(), RealAmiggosPresenter.RealAmiggosPresenterMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,RealAmiggosAdapter.HomeItemClick{



    private val mLoadingData = RealFriendV2Response.Data.RealFreind(loadingStatus = true)
    lateinit var binding: RealAmiggosFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var realFriendPresenterImplementation: RealAmiggosPresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<RealFriendV2Response.Data.RealFreind>()
    private var adapter: RealAmiggosAdapter? = null
    private lateinit var onlineFriendRecyclerview: RecyclerView



    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.real_amiggos_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation = RealAmiggosPresenterImplementation(this,requireContext())
        onlineFriendRecyclerview = myView!!.findViewById(R.id.real_friend_fragment)
        setupRecyclerRealFriend()

        binding.edtSearch.setOnFocusChangeListener { view, b ->
             if(view.isFocused){
                 aProfileDetails.setupScrolling()
             }

        }
        binding.edtSearch.setOnClickListener {
                 aProfileDetails.setupScrolling()
        }

        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            realFriendPageNo = 0
            if(it.isEmpty()){
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callRealFriendApi(false, "")
            }else{
                mydataList.clear()
                adapter!!.notifyDataSetChanged()

                callRealFriendApi(false, it.toString())
            }
        }


        return myView

    }

    fun removeFocus(){
        binding!!.mainlayout.requestFocus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callRealFriendApi(false, "")
    }

    private fun callRealFriendApi(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid",sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("freind_id",friendId)
        input.addProperty("name", searchvalue)
        input.addProperty("page_no", realFriendPageNo)
        realFriendPresenterImplementation!!.doCallRealFriendApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }

    override fun validateError(message: String) {

        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }


    override fun onRealFriendSuccess(responseData: List<RealFriendV2Response.Data.RealFreind>) {
        realFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    private fun setupRecyclerRealFriend() {

        val layoutManager = GridLayoutManager(activity,2, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = RealAmiggosAdapter(
            mContext = requireActivity(),
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
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callRealFriendApi(true, "")
    }


    override fun itemClickCallback(position: Int) {


        if (Utility.checkProfileComplete(sharedPreference)) {
            val intent = Intent(activity, AProfileDetails::class.java)
            intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
            intent.putExtra(ConstantLib.PROFILE_IMAGE,mydataList[position].profile)
            intent.putExtra(ConstantLib.IS_MY_FRIEND, mydataList[position].isMyFriend)
            intent.putExtra(ConstantLib.IS_MY_FRIEND_BLOCKED, mydataList[position].isMyFriendBlocked)
            intent.putExtra(ConstantLib.NAME, mydataList[position].name)
            intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
            intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
            intent.putExtra("from", "Invitation")
            startActivity(intent)
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