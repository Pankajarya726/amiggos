package com.tekzee.amiggos.ui.chooselanguage

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.LanguageActivityBinding
import com.tekzee.amiggos.ui.chooselanguage.adapter.LanguageAdapter
import com.tekzee.amiggos.ui.chooselanguage.interfaces.LanguageClicked
import com.tekzee.amiggos.ui.chooselanguage.model.Language
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

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
        setupClickListener()
    }

    private fun setupClickListener() {

        binding.btnSave.setOnClickListener{


            if(languageCode == null){
                   Toast.makeText(applicationContext,"Please select language",Toast.LENGTH_LONG).show()
            }else{
                sharedPreferences!!.save(ConstantLib.LANGUAGE_CODE,languageCode!!)
                chooseLanguagePresenterImplementation!!.doLanguageConstantApi(Utility.createHeaders(sharedPreferences))
            }

        }
    }

    private fun setupViewName() {
        binding.txtLanguageTitle.text = languageData!!.klChooseLanguage
        binding.btnSave.text = languageData!!.klSAVE
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klLanguage
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
            override fun onLanguageClicked(position: Int, selectedData: Language) {
                data[position].isChecked = true
                languageAdapter!!.notifyItemChanged(position)
                sharedPreferences!!.save(ConstantLib.LANGUAGE_NAME,selectedData.name)
                languageCode = selectedData.iso2_code
                for (i in 0 until data.size)
                {
                    if(i!=position)
                    {
                        data[i].isChecked = false
                        languageAdapter!!.notifyItemChanged(i)
                    }
                }

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
        data.addAll(responseData.data.language)
        languageAdapter!!.notifyDataSetChanged()
    }




    override fun onLanguageConstantResponse(response: JsonObject) {
        sharedPreferences!!.save(ConstantLib.LANGUAGE_DATA,response.toString())
        val intent = Intent(this, MainSplashActivity::class.java)
        startActivity(intent)
        onBackPressed()
    }


}