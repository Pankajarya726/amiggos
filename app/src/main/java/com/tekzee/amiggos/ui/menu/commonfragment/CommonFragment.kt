package com.tekzee.amiggos.ui.menu.commonfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulsiveweb.galleryview.GalleryView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.custom.AlertAgeFragment
import com.tekzee.amiggos.custom.MenuAgeSheetFragment
import com.tekzee.amiggos.databinding.CommonFragmentBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.menu.commonfragment.adapter.CommonAdapter
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class CommonFragment() : Fragment(), CommonEvent, KodeinAware, CommonClickListener {

    companion object {
        fun newInstance(id: Int, item: MenuResponse.Data.Section): CommonFragment {
            val fragment = CommonFragment()
            val args = Bundle()
            args.putString("id", id.toString())
            args.putSerializable("data", item)
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var newlist: java.util.ArrayList<Menu>
    private var dataItem: MenuResponse.Data.Section? = null

    //    private lateinit var navController: NavController
    private lateinit var adapter: CommonAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: CommonFragmentViewModelFactory by instance<CommonFragmentViewModelFactory>()
    private var binding: CommonFragmentBinding? = null
    private var category_id: String? = null
    private lateinit var viewModel: CommonViewModel
    private var repository: ItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category_id = arguments?.getString("id")
        dataItem = arguments?.getSerializable("data") as MenuResponse.Data.Section
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_fragment, container, false)
        val itemDao = AmiggoRoomDatabase.getDatabase(requireContext()).itemDao()
        repository = ItemRepository(itemDao)

        if (dataItem != null) {
            setupAdapter()
            observeList()
        }

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(CommonViewModel::class.java)
        viewModel.commonEvent = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun setupAdapter() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerCommon.visibility = View.GONE
        adapter = CommonAdapter(this, repository,prefs)
        binding!!.recyclerCommon.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding!!.recyclerCommon.adapter = adapter
    }

    private fun observeList() {
        newlist = ArrayList<Menu>()
        newlist.addAll(dataItem!!.menu)
        adapter.submitList(newlist)
        binding!!.progressCircular.visibility = View.GONE
        binding!!.recyclerCommon.visibility = View.VISIBLE

    }


    override fun onItemClicked(position: Int, listItem: Menu, quantity: String) {
        Coroutines.main {
            val dataExist = repository!!.checkItemExists(listItem.id.toString())
            if (dataExist == null) {
                repository!!.insert(listItem)
            } else {
                repository!!.updateCount(quantity, listItem.id.toString());
            }

            if(quantity.equals("0")){
                repository!!.deleteitem(listItem.id)
            }
        }

    }

    override fun onChatButtonClicked(position: Int, listItem: CommonMenuResponse.Data.Staff) {


    }

    override fun showAgeRestrictionPopup(view: View) {
        showCustomDialog(view)
    }

    override fun showNotVerifiedMessage(isIdproofNotverifiedMessage: String) {
        requireActivity().Errortoast(isIdproofNotverifiedMessage)
    }

    override fun showAlertForAgeRestriction(message: String) {
        val bottomSheetDialog: AlertAgeFragment = AlertAgeFragment.newInstance(message)
        bottomSheetDialog.show(childFragmentManager, "AgeRestring Alert")
    }

    override fun viewImage(listItem: Menu?) {
        if(listItem!!.menuImage.isNotEmpty()){
            val paths: ArrayList<String> = ArrayList()
            paths.add(listItem!!.menuImage)
            GalleryView.show(requireContext(), paths)
        }

    }

    fun showCustomDialog(view: View){
        val bottomSheetDialog: MenuAgeSheetFragment = MenuAgeSheetFragment.newInstance()
        bottomSheetDialog.show(childFragmentManager, "AgeRestring fragment")
    }
}
