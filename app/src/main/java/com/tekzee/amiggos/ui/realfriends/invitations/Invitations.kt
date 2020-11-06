package com.tekzee.amiggos.ui.realfriends.invitations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.InvitationsFragmentBinding
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.realfriends.invitations.adapter.InvitationAdapter
import com.tekzee.amiggos.ui.realfriends.invitations.interfaces.InvitationInterfaces
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponseV2
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.NearMeFragment
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggos.util.RxSearchObservable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Invitations : BaseFragment(), InvitationPresenter.InvitationMainView {


    private var pageNo: Int? =0
    private var searchName: String? =""
    lateinit var binding: InvitationsFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var invitationPresenterImplementation: InvitationPresenterImplementation? = null
    private lateinit var adapter: InvitationAdapter
    private val items: ArrayList<InvitationResponseV2.Data.FreindRequest> = ArrayList()
    private var isFragmentVisible = false

    companion object {
        private val invitation: Invitations? = null


        fun newInstance(): Invitations {
            if(invitation == null){
                return Invitations()
            }
            return invitation
        }
    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
    }

    private fun setupView() {
        binding.hiddenSearchWithRecycler.searchBarSearchView.queryHint = languageData!!.klSearchTitle

        RxSearchObservable.fromView(binding.hiddenSearchWithRecycler.searchBarSearchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter(Predicate { t ->
                t.isNotEmpty()
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String>() { t ->
                pageNo = 0
                items.clear()
                adapter!!.notifyDataSetChanged()
                searchName = t.toString()
                callInvitationApi()
            })

        val closeButton: View? = binding.hiddenSearchWithRecycler.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {

            binding.hiddenSearchWithRecycler.searchBarSearchView.clearFocus()
            binding.hiddenSearchWithRecycler.searchBarSearchView.isIconified = false
            binding.hiddenSearchWithRecycler.clearSearchview()
            pageNo = 0
            items.clear()
            adapter.notifyDataSetChanged()
            searchName = ""
            callInvitationApi()
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.d("Onresume---> Invitation")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.invitations_fragment, container, false)
        myView = binding.root
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        invitationPresenterImplementation = InvitationPresenterImplementation(this, requireContext())
        setupRecyclerView()
        setupView()
        callInvitationApi()
        setupRefreshLayout()
    }

    private fun setupRefreshLayout() {
        binding!!.refreshlayout.setOnRefreshListener {
            binding!!.refreshlayout.isRefreshing = false
            pageNo = 0
            items.clear()
            adapter.notifyDataSetChanged()
            callInvitationApi()
        }
    }



    private fun setupRecyclerView() {

        binding.invitationRecyclerview.setHasFixedSize(true)
        binding.invitationRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = InvitationAdapter(items, languageData, object : InvitationInterfaces {
            override fun onItemClicked(
                invitationData: InvitationResponseV2.Data.FreindRequest,
                type: Int
            ) {
                when (type) {
                    1 -> {
                        callAcceptApi(invitationData)
                    }
                    2 -> {
                        callRejectApi(invitationData)
                    }
                    3 -> {

                        if (Utility.checkProfileComplete(sharedPreference)) {
                            val intent = Intent(activity, AProfileDetails::class.java)
                            intent.putExtra(ConstantLib.FRIEND_ID, invitationData.userid.toString())
                            intent.putExtra(ConstantLib.IS_MY_FRIEND, invitationData.isMyFriend)
                            intent.putExtra(ConstantLib.IS_MY_FRIEND_BLOCKED, invitationData.isMyFriendBlocked)
                            intent.putExtra(ConstantLib.PROFILE_IMAGE,invitationData.profile)
                            intent.putExtra(ConstantLib.NAME, invitationData.name)
                            intent.putExtra(ConstantLib.ADDRESS, invitationData.address)
                            intent.putExtra(ConstantLib.REAL_FREIND_COUNT, invitationData.real_freind_count)
                            intent.putExtra("from", "Invitation")
                            startActivityForResult(intent,100)
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
                }
            }

        })
        binding.invitationRecyclerview.adapter = adapter

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode ==2 ){
            items.clear()
            adapter.notifyDataSetChanged()
            pageNo = 0
            callInvitationApi()
        }
    }

    private fun callRejectApi(invitationData: InvitationResponseV2.Data.FreindRequest) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", invitationData.userid)
        invitationPresenterImplementation!!.doRejectInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callAcceptApi(invitationData: InvitationResponseV2.Data.FreindRequest) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", invitationData.userid)
        invitationPresenterImplementation!!.doAcceptInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callInvitationApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("page_no", pageNo)
        input.addProperty("name",searchName)
        invitationPresenterImplementation!!.doCallInvitationApi(
            input,
            Utility.createHeaders(sharedPreference),
                    isFragmentVisible
        )
    }

    override fun validateError(message: String) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onInvitaionSuccess(responseData: InvitationResponseV2?) {
        NearMeFragment.setInvitationBadge(responseData!!.data.total_count)
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData.data.freindRequest)
        adapter.notifyDataSetChanged()
//        setupErrorVisibility()
    }


    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity, responseData!!.message, Toast.LENGTH_LONG).show()
        callInvitationApi()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity, responseData!!.message, Toast.LENGTH_LONG).show()
        callInvitationApi()
    }

    override fun onInvitationFailure(responseData: String) {
        items.clear()
        adapter.notifyDataSetChanged()
//        setupErrorVisibility()
    }


    override fun onStop() {
        super.onStop()
        invitationPresenterImplementation!!.onStop()
    }



}