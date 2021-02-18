package com.tekzee.amiggos.ui.invitationlist

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.InvitationListFragmentBinding
import com.tekzee.amiggos.ui.invitationlist.adapter.InvitationAdapter
import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class ViewGuestListActivity : BaseActivity(), KodeinAware, InvitationListEvent {
    private val globalList = ArrayList<InvitationListResponse.Data>()
    private var binding: InvitationListFragmentBinding? = null
    private lateinit var adapter: InvitationAdapter
    override val kodein: Kodein by closestKodein()

    private var languageData: LanguageData? = null
    val prefs: SharedPreference by instance()
    val factory: InvitationListViewModelFactory by instance<InvitationListViewModelFactory>()

    companion object {
        fun newInstance() = ViewGuestListActivity()
    }

    private lateinit var viewModel: InvitationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.invitation_list_fragment)
        viewModel = ViewModelProvider(this, factory).get(InvitationViewModel::class.java)
        languageData = prefs.getLanguageData(ConstantLib.LANGUAGE_DATA)
        binding!!.viewmodel = viewModel
        viewModel.event = this
        binding!!.headertitle.text = languageData!!.guest
        callInvitaionListApi()

    }

    override fun validateError(message: String) {
        Errortoast(message)
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    private fun callInvitaionListApi() {
        Coroutines.main {
            viewModel.CallInvitationList(intent.getStringExtra(ConstantLib.BOOKING_ID))
        }
    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerviewSettings.visibility = View.GONE
    }

    override fun onLoaded(response: InvitationListResponse) {
        setupAdapter(response)
        observeList()
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
        binding!!.recyclerviewSettings.visibility = View.GONE
        Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        Errortoast(message)
    }

    private fun observeList() {
        viewModel.staffList.observe(this, Observer { listitem ->
            globalList.clear()
            globalList.addAll(listitem)
            val newlist = ArrayList<InvitationListResponse.Data>()
            newlist.addAll(listitem)
            adapter.submitList(newlist)
            binding!!.progressCircular.visibility = View.GONE
            binding!!.recyclerviewSettings.visibility = View.VISIBLE
        })
    }

    private fun setupAdapter(response: InvitationListResponse) {
        adapter = InvitationAdapter(response)
        binding!!.recyclerviewSettings.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.recyclerviewSettings.adapter = adapter
    }


    override fun onBackButtonPressed() {
        onBackPressed()
    }

}