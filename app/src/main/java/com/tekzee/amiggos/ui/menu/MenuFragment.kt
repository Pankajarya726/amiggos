package com.tekzee.amiggos.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.StaffFragmentBinding
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.menu.commonfragment.CommonFragment
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MenuFragment : Fragment(), MenuEvent, KodeinAware {

    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: MenuModelFactory by instance<MenuModelFactory>()

    companion object {
        fun newInstance() = MenuFragment()
    }

    private var binding: StaffFragmentBinding? = null
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.staff_fragment, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,factory).get(MenuViewModel::class.java)
        binding!!.staffviewmodel = viewModel
        viewModel.menuEvent = this
        viewModel.callStaffApi()
        binding!!.headertitle.text = languageConstant.invite
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.headertitle.text = languageConstant.klMyBooking
    }

    private fun setupAdapter(
        menuList: ArrayList<MenuResponse.Data.Staff>,
        staffViewPager: ViewPager,
        staffTabs: TabLayout
    ) {
        val fragmentManager = childFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        for (item in menuList) {
            adapter.addFragment(CommonFragment.newInstance(item.id), item.name)
        }
        staffViewPager.adapter = adapter
        staffTabs.setupWithViewPager(staffViewPager)
    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
    }

    override fun onLoaded(menuList: ArrayList<MenuResponse.Data.Staff>) {
        binding!!.progressCircular.visibility = View.GONE
        setupAdapter(menuList,binding!!.staffViewPager,binding!!.staffTabs)
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
        requireActivity().Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        requireActivity().Errortoast(message)
    }

    override fun onDrawerClicked() {

    }

}
