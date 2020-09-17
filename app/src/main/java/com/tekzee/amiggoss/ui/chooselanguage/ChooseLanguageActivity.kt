package com.tekzee.amiggoss.ui.chooselanguage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.LanguageActivityBinding
import com.tekzee.amiggoss.ui.chooselanguage.adapter.LanguageAdapter
import com.tekzee.amiggoss.ui.chooselanguage.interfaces.LanguageClicked
import com.tekzee.amiggoss.ui.chooselanguage.model.Language
import com.tekzee.amiggoss.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib

class ChooseLanguageActivity: BaseActivity(),ChooseLanguagePresenter.ChooseLanguageMainView {

    private var languageAdapter: LanguageAdapter? = null
    private var chooseLanguagePresenterImplementation: ChooseLanguagePresenterImplementation? = null
    private var sharedPreferences: SharedPreference? = null
    lateinit var binding: LanguageActivityBinding
    private var languageData: LanguageData? = null
    private var data = ArrayList<Language>()
    private var languageCode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.language_activity)
        chooseLanguagePresenterImplementation = ChooseLanguagePresenterImplementation(this,this)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupViewName()
        callLanguageApi()
        setupRecyclerView()
//        setupClickListener()
    }

//    private fun setupClickListener() {
//
//        binding.btnSave.setOnClickListener{
//
//
//            if(languageCode == null){
//                   Toast.makeText(applicationContext,"Please select language",Toast.LENGTH_LONG).show()
//            }else{
//                sharedPreferences!!.save(ConstantLib.LANGUAGE_CODE,languageCode!!)
//                chooseLanguagePresenterImplementation!!.doLanguageConstantApi(Utility.createHeaders(sharedPreferences))
//            }
//
//        }
//    }

    private fun setupViewName() {
        binding.txtLanguageTitle.text = languageData!!.klLanguage
        binding.btnSave.text = languageData!!.klSAVE
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        binding.languageRecyclerview.setHasFixedSize(true)
        binding.languageRecyclerview.layoutManager = LinearLayoutManager(this)
        languageAdapter = LanguageAdapter(data,sharedPreferences, object : LanguageClicked {
            override fun onLanguageClicked(
                position: Int,
                switchclicked: Boolean,
                selectedData: Language
            ) {
                sharedPreferences!!.save(ConstantLib.LANGUAGE_CODE,selectedData.iso2_code)
                chooseLanguagePresenterImplementation!!.doLanguageConstantApi(Utility.createHeaders(sharedPreferences))
            }
        })
        binding.languageRecyclerview.adapter = languageAdapter
    }

    private fun callLanguageApi() {
       chooseLanguagePresenterImplementation!!.doCallLanguageApi(Utility.createHeaders(sharedPreferences))
    }



    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onLanguageSuccess(responseData: LanguageResponse) {
        data.clear()
        languageAdapter!!.notifyDataSetChanged()
        for(item in responseData.data.language){
            item.isChecked = item.iso2_code == sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_CODE)
            data.add(item)
        }
        languageAdapter!!.notifyDataSetChanged()
    }




    override fun onLanguageConstantResponse(response: JsonObject) {
        sharedPreferences!!.save(ConstantLib.LANGUAGE_DATA,response.toString())
        val intent = Intent(this, MainSplashActivity::class.java)
        startActivity(intent)
        onBackPressed()
    }


}