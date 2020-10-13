package com.tekzee.amiggos.ui.mylifestylesubcategory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MyLifestyleSubcategoryFragmentBinding
import com.tekzee.amiggos.ui.mylifestylesubcategory.adapter.MyLifestyleSubcategoryAdapter
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse
import com.tekzee.amiggos.ui.notification_new.ANotification
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility


class AMyLifestyleSubcategory : BaseActivity(), MyLifestyleSubcategoryPresenter.MyLifestyleSubcategoryPresenterMainView,
     MyLifestyleSubcategoryClickListener {

    private lateinit var newlist: java.util.ArrayList<MyLifestyleSubcategoryResponse.Data.Lifestyle>
    private lateinit var adapter: MyLifestyleSubcategoryAdapter
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: MyLifestyleSubcategoryFragmentBinding? = null
    private var presenterImplementation: MyLifestyleSubcategoryPresenterImplementation? = null
    private val SEPARATOR = ","
    private val removedIds = HashSet<String>()
    companion object {

        fun newInstance(): AMyLifestyleSubcategory {
            return AMyLifestyleSubcategory()
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.my_lifestyle_subcategory_fragment)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        presenterImplementation = MyLifestyleSubcategoryPresenterImplementation(this, this)
        setupLanguage()
        callMyLifestyle()
        setupAdapter()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding!!.btnBack.setOnClickListener{
            onBackPressed()
        }

        binding!!.notification.setOnClickListener{
            val intent = Intent(this, ANotification::class.java)
            startActivity(intent)
        }
        binding!!.save.setOnClickListener{
            val builder = StringBuilder()
            val removedIdbuilder = StringBuilder()
            for(item in newlist){
                if(item.selected==1 && item.isalreadyselected!=1){
                    builder.append(item.id);
                    builder.append(SEPARATOR);
                }
            }

            for(item in removedIds){
                removedIdbuilder.append(item);
                removedIdbuilder.append(SEPARATOR);
            }

            var listofids: String = builder.toString()
            if(listofids.isNotEmpty()){
                listofids = listofids.substring(0, listofids.length - SEPARATOR.length);
            }else{
                listofids = ""
            }



            var removedlistofids: String = removedIdbuilder.toString()
            if(removedlistofids.isNotEmpty()){
                removedlistofids = removedlistofids.substring(0, removedlistofids.length - SEPARATOR.length)
            }else{
                removedlistofids = ""
            }

            if(listofids.isEmpty() && removedlistofids.isEmpty()){
                finish()
            }else{
                callSaveMyLifestyleApi(listofids,removedlistofids)
            }


        }
    }

    private fun callSaveMyLifestyleApi(catid: String, removedlistofids: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("catid", catid)
        input.addProperty("remove_catid", removedlistofids)
        presenterImplementation!!.docallSaveMyLifestyleSubcategoryApi(
            input, Utility.createHeaders(
                sharedPreference
            )
        )
    }


    private fun setupLanguage() {
        binding!!.heading.text = intent.getStringExtra(ConstantLib.CATEGORY_NAME)
    }


    private fun callMyLifestyle() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("catid", intent.getStringExtra(ConstantLib.CATEGORY_ID))
        presenterImplementation!!.docallMyLifestyleSubcategoryApi(
            input, Utility.createHeaders(
                sharedPreference
            )
        )
    }



    private fun setupAdapter() {
        adapter = MyLifestyleSubcategoryAdapter(this, ArrayList())
        binding!!.mylifestyleRecyclerview.layoutManager = GridLayoutManager(
            this,
            3
        )
        binding!!.mylifestyleRecyclerview.adapter = adapter
    }


    override fun onMyLifeStyleSubcategorySuccess(responseData: MyLifestyleSubcategoryResponse) {
        newlist = ArrayList<MyLifestyleSubcategoryResponse.Data.Lifestyle>()
        for(item in responseData.data.lifestyle){
            if(item.selected ==1){
                item.isalreadyselected =1
            }else{
                item.isalreadyselected =0
            }
        }
        newlist.addAll(responseData.data.lifestyle)
        adapter.submitList(newlist)
    }

    override fun onMyLifestyleSubcategoryFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onSaveMyLifestyleSubcategoryFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onSaveMyLifeStyleSubcategorySuccess(message: String) {
        onBackPressed()
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenterImplementation!!.onStop()

    }

    override fun onItemClick(
        listitem: MyLifestyleSubcategoryResponse.Data.Lifestyle,
        position: Int
    ) {
            if(listitem.selected==0){
                listitem.selected =1
                removedIds.remove(listitem.id.toString())
            }else{
                if(listitem.isalreadyselected==1){
                    removedIds.add(listitem.id.toString())
                }
                listitem.selected =0
            }
        adapter.submitList(newlist)
        adapter.notifyItemChanged(position)
    }


}