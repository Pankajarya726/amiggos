package com.tekzee.amiggos.ui.finalbasket

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.impulsiveweb.galleryview.GalleryView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.custom.MenuAgeSheetFragment
import com.tekzee.amiggos.databinding.CommonFragmentBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.finalbasket.adapter.FinalBasketAdapter
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.SharedPreference
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class FinalBasketActivity : AppCompatActivity(), FinalBasketEvent, KodeinAware, FinalBasketClickListener {

    private lateinit var newlist: java.util.ArrayList<Menu>
    private var dataItem: MenuResponse.Data.Section? = null

    //    private lateinit var navController: NavController
    private lateinit var adapter: FinalBasketAdapter
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: FinalBasketViewModelFactory by instance<FinalBasketViewModelFactory>()
    private var binding: CommonFragmentBinding? = null
    private var category_id: String? = null
    private lateinit var viewModel: FinalBasketViewModel
    private var repository: ItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.final_cart)
        val itemDao = AmiggoRoomDatabase.getDatabase(this).itemDao()
        repository = ItemRepository(itemDao)

        if (dataItem != null) {
            setupAdapter()
            observeList()
        }
        viewModel = ViewModelProvider(this, factory).get(FinalBasketViewModel::class.java)
        viewModel.commonEvent = this
    }

    private fun setupAdapter() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.recyclerCommon.visibility = View.GONE
        adapter = FinalBasketAdapter(this, repository,prefs)
        binding!!.recyclerCommon.layoutManager = LinearLayoutManager(
            this,
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
            repository!!.getItemCount().observe(
                this,
                Observer {
                    Log.e("item data size--->", "" + it.size)
                    Log.e("item data--->", "" + it.toString())
                }
            )
        }

    }

    override fun onChatButtonClicked(position: Int, listItem: CommonMenuResponse.Data.Staff) {


    }

    override fun showAgeRestrictionPopup(view: View) {
        showCustomDialog(view)
    }

    override fun viewImage(listItem: Menu?) {
        if(listItem!!.menuImage.isNotEmpty()){
            val paths: ArrayList<String> = ArrayList()
            paths.add(listItem!!.menuImage)
            GalleryView.show(this, paths)
        }

    }

    fun showCustomDialog(view: View){
        val bottomSheetDialog: MenuAgeSheetFragment = MenuAgeSheetFragment.newInstance()
        bottomSheetDialog.show(supportFragmentManager, "finalbasket fragment")
    }
}
