package com.tekzee.amiggos.ui.menu.commonfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.CommonFragmentBinding
import com.tekzee.amiggos.ui.menu.commonfragment.adapter.CommonAdapter
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import com.tekzee.amiggos.util.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CommonFragment() : Fragment(), CommonEvent, KodeinAware, CommonClickListener {

    companion object {
        fun newInstance(id: Int): CommonFragment {
            val fragment = CommonFragment()
            val args = Bundle()
            args.putString("id", id.toString())
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var navController: NavController
    private lateinit var adapter: CommonAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: CommonFragmentViewModelFactory by instance<CommonFragmentViewModelFactory>()
    private var binding: CommonFragmentBinding? = null
    private var category_id: String? = null
    private lateinit var viewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category_id = arguments?.getString("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_fragment, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       viewModel = ViewModelProvider(this, factory).get(CommonViewModel::class.java)
        viewModel.commonEvent = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        callStaffByIdApi()
    }

    private fun callStaffByIdApi() {
        Coroutines.main {
            viewModel.callStaffByIdApi(category_id)
        }
    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerCommon.visibility = View.GONE
    }

    override fun onLoaded() {
        setupAdapter()
        observeList()
    }

    override fun onChangeStatusStarted() {
        requireActivity().showProgressBar()
    }

    private fun setupAdapter() {
        adapter = CommonAdapter(this)
        binding!!.recyclerCommon.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.recyclerCommon.adapter = adapter
    }

    private fun observeList() {
        viewModel.commonlist.observe(this, Observer { listitem ->
            val newlist = ArrayList<CommonMenuResponse.Data.Staff>()
            newlist.addAll(listitem)
            adapter.submitList(newlist)
            binding!!.progressCircular.visibility = View.GONE
            binding!!.recyclerCommon.visibility = View.VISIBLE
        })
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
        binding!!.recyclerCommon.visibility = View.GONE
        requireActivity().Errortoast(message)
        requireActivity().hideProgressBar()
    }

    override fun onStatusUpdated(message: String) {
        requireActivity().hideProgressBar()
        requireActivity().Successtoast(message)
    }

    override fun sessionExpired(message: String) {
        requireActivity().hideProgressBar()
        requireActivity().Errortoast(message)

    }

    override fun onItemClicked(
        position: Int,
        status: Boolean,
        listItem: CommonMenuResponse.Data.Staff
    ) {
        viewModel.callChangeUserStatus(listItem, status)
    }

    override fun onChatButtonClicked(position: Int, listItem: CommonMenuResponse.Data.Staff) {


    }

}
