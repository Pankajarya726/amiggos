package com.tekzee.amiggos.ui.mypreferences

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.MyPreferencesBinding
import com.tekzee.amiggos.ui.mypreferences.adapter.MyExpandableAdapter
import com.tekzee.amiggos.ui.mypreferences.interfaces.MyPreferenceClicked
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceData
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.ParentData
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.thoughtbot.expandablerecyclerview.listeners.GroupExpandCollapseListener
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class MyPreferences : BaseActivity(), MyPreferencesPresenter.MyPreferencesMainView {

    lateinit var binding : MyPreferencesBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var adapter: MyExpandableAdapter? = null
    private var data = ArrayList<MyPreferenceData>()
    val listdata = ArrayList<ParentData>()
    private var myPreferencesPresenterImplementation: MyPreferencesPresenterImplementation? = null


    var musicSelected = ""
    var venueSelected = ""
    var ageGroupSelected = ""
    var ageFillterFrom = ""
    var ageFillterTill = ""
    var distanceFillterFrom = ""
    var distanceFillterTill = ""
    var languageSelected = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.my_preferences)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        myPreferencesPresenterImplementation = MyPreferencesPresenterImplementation(this,this)
        setupToolBar()
        callGetSettings()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.txtSave.setOnClickListener{
            getSelectedMusic()
            callSaveSettings()
        }

        binding.txtCancel.setOnClickListener{
            finish()
        }
    }

    private fun callSaveSettings() {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("venue_type", if(venueSelected.isNotEmpty()){venueSelected.substring(0, venueSelected.length-1)}else{""})
        input.addProperty("age_group", ageGroupSelected)
        input.addProperty("age_filter_from", ageFillterFrom)
        input.addProperty("age_filter_till", ageFillterTill)
        input.addProperty("distance_filter_from", distanceFillterFrom)
        input.addProperty("distance_filter_to", distanceFillterTill)
        input.addProperty("language_id", languageSelected)
        input.addProperty("music", if(musicSelected.isNotEmpty()){musicSelected.substring(0, musicSelected.length-1)}else{""})



        myPreferencesPresenterImplementation!!.docallSaveSettings(
            input,
            Utility.createHeaders(sharedPreference)
        )


    }

    private fun getSelectedMusic(): String? {

        for(listItem in listdata){

            when {
                listItem.title.split("--")[0].equals("Music",true) -> {
                    for(musicData in listItem.items){
                        if(musicData.isSet == "1"){
                            musicSelected = musicSelected + musicData.id +","
                        }
                    }
                    Logger.d("musicSelected--->"+if(musicSelected.length>0){musicSelected.substring(0, musicSelected.length-1)}else{"NA"})
                }
                listItem.title.split("--")[0].equals("Venue Type",true) -> {
                    for(venueData in listItem.items){
                        if(venueData.isSet == "1"){
                            venueSelected = venueSelected + venueData.id +","
                        }
                    }
                    Logger.d("venueSelected--->"+if(venueSelected.length>0){venueSelected.substring(0, venueSelected.length-1)}else{"NA"})
                }
                listItem.title.split("--")[0].equals("Age Group",true) -> {
                    for(ageGroupData in listItem.items){
                        if(ageGroupData.isSet == "1"){
                            ageGroupSelected = "1"
                        }else{
                            ageGroupSelected = "0"
                        }
                    }
                    Logger.d("ageGroupData--->"+ageGroupSelected)
                }
                listItem.title.split("--")[0].equals("Choose Age",true) -> {
                    for(chooseAgeData in listItem.items){
                        ageFillterFrom = chooseAgeData.filter_from
                        ageFillterTill = chooseAgeData.filter_to
                    }
                    Logger.d("chooseAgeData--->"+ageFillterFrom+"====="+ageFillterTill)
                }
                listItem.title.split("--")[0].equals("Distance",true) -> {
                    for(distanceData in listItem.items){
                        distanceFillterFrom = distanceData.filter_from
                        distanceFillterTill = distanceData.filter_to
                    }
                    Logger.d("distanceData--->"+distanceFillterFrom+"====="+distanceFillterTill)
                }
                listItem.title.split("--")[0].equals("Language",true) -> {
                    for(languageData in listItem.items){
                        if(languageData.isSet == "1"){
                            languageSelected = languageData.id.toString()
                        }else{
                            languageSelected = "0"
                        }
                    }
                    Logger.d("languageSelected--->"+languageSelected)
                }
            }

        }
            return ""
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klSetting
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callGetSettings() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        myPreferencesPresenterImplementation!!.doCallGetSettings(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun setupRecyclerView(data: ArrayList<MyPreferenceData>) {
        binding.mypreferenceRecyclerview.setHasFixedSize(true)
        binding.mypreferenceRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = MyExpandableAdapter(getData(data),sharedPreference, object : MyPreferenceClicked {
            override fun onMyPreferenceClicked(position: Int, selectedData: MyPreferenceData) {

            }

            override fun lastPosition(position: Int) {

            }

        })

        binding.mypreferenceRecyclerview.adapter = adapter





        adapter!!.setOnGroupExpandCollapseListener(object : GroupExpandCollapseListener {
            override fun onGroupExpanded(group: ExpandableGroup<*>?) {
                Log.e("OnGroup Expanded","Exapanded")
            }
            override fun onGroupCollapsed(group: ExpandableGroup<*>?) {
                Log.e("OnGroup collapsed","collapsed")
            }
        })



    }


    fun getData(data: ArrayList<MyPreferenceData>): ArrayList<ParentData> {
        for (item: MyPreferenceData in data){
            listdata.add(ParentData(item.title+"--"+item.type+"--"+item.selectMax,item.values))
         }
        return listdata
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
        finish()
    }



    override fun onGetSettingSuccess(responseData: MyPreferenceResponse?) {
        data.clear()
        data.addAll(responseData!!.data)
        setupRecyclerView(data)
    }


    override fun onCallSaveSettingsSuccess(responseData: PreferenceSavedResponse?) {

        sharedPreference!!.save(ConstantLib.VENUE_TYPE_SELECTED,if(venueSelected.isNotEmpty()){venueSelected.substring(0, venueSelected.length-1)}else{""})
        sharedPreference!!.save(ConstantLib.MUSIC_SELETED,if(musicSelected.isNotEmpty()){musicSelected.substring(0, musicSelected.length-1)}else{""})
        sharedPreference!!.save(ConstantLib.AGE_GROUP_SELECTED,ageGroupSelected)
        sharedPreference!!.save(ConstantLib.AGE_FILTER_FROM_SELECTED,ageFillterFrom)
        sharedPreference!!.save(ConstantLib.AGE_FILTER_TILL_SELECTED,ageFillterTill)
        sharedPreference!!.save(ConstantLib.DISTANCE_FILTER_FROM_SELECTED,distanceFillterFrom)
        sharedPreference!!.save(ConstantLib.DISTANCE_FILTER_TO_SELECTED,distanceFillterTill)
        sharedPreference!!.save(ConstantLib.LANGUAGE_ID_SELECTED,languageSelected)



        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.titleText = responseData!!.message
        pDialog.setCancelable(false)
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            onBackPressed()
        }
        pDialog.show()
    }



}